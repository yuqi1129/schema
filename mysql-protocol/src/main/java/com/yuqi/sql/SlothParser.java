package com.yuqi.sql;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.yuqi.sql.rule.SlothRules;
import com.yuqi.sql.trait.SlothConvention;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptMaterialization;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.plan.RelOptTable;
import org.apache.calcite.plan.RelTraitSet;
import org.apache.calcite.plan.SubstitutionVisitor;
import org.apache.calcite.plan.hep.HepPlanner;
import org.apache.calcite.plan.hep.HepProgramBuilder;
import org.apache.calcite.prepare.CalciteCatalogReader;
import org.apache.calcite.rel.RelCollations;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelRoot;
import org.apache.calcite.rel.metadata.ChainedRelMetadataProvider;
import org.apache.calcite.rel.metadata.DefaultRelMetadataProvider;
import org.apache.calcite.rel.metadata.RelMetadataProvider;
import org.apache.calcite.rel.rules.CoreRules;
import org.apache.calcite.rel.rules.materialize.MaterializedViewRules;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexBuilder;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql2rel.SqlToRelConverter;
import org.apache.calcite.sql2rel.StandardConvertletTable;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 10/7/20 19:50
 **/
public class SlothParser implements RelOptTable.ViewExpander {
    public static final Logger log = LoggerFactory.getLogger(SlothParser.class);
    private SqlParser sqlParser;
    private RelOptPlanner relOptPlanner;
    private SqlValidator sqlValidator;
    private CalciteCatalogReader calciteCatalogReader;
    private int expansionDepth;

    private SqlNode sqlNode;
    private RelNode relNode;
    private RelDataType relDataType;
    List<List<String>> fieldOrigins;

    public CalciteCatalogReader getCalciteCatalogReader() {
        return calciteCatalogReader;
    }

    public SlothParser(SqlParser sqlParser, RelOptPlanner relOptPlanner,
                       CalciteCatalogReader calciteCatalogReader, SqlValidator sqlValidator) {
        this.sqlParser = sqlParser;
        this.relOptPlanner = relOptPlanner;
        this.sqlValidator = sqlValidator;
        this.calciteCatalogReader = calciteCatalogReader;
    }

    private SqlToRelConverter getSqlToRelConverter() {
        final SqlToRelConverter.ConfigBuilder builder =
                SqlToRelConverter.configBuilder()
                        .withTrimUnusedFields(true);

        final RexBuilder rexBuilder = new RexBuilder(calciteCatalogReader.getTypeFactory());
        final RelOptCluster relOptCluster = RelOptCluster.create(relOptPlanner, rexBuilder);
        RelMetadataProvider planChain = ChainedRelMetadataProvider.of(ImmutableList.of(DefaultRelMetadataProvider.INSTANCE));
        relOptCluster.setMetadataProvider(planChain);

        return new SqlToRelConverter(this, sqlValidator, calciteCatalogReader,
                relOptCluster, StandardConvertletTable.INSTANCE, builder.build());
    }

    //db元数据存在mysql, 起动的时候再恢复
    public SqlNode getSqlNode(String sql) throws SqlParseException {
        return sqlParser.parseQuery(sql);
    }

    public SqlNode getSqlNode() throws SqlParseException {
        return sqlParser.parseStmt();
    }

    public RelNode getPlanWithoutCBO(String sql) throws SqlParseException {
        SqlNode sqlNode = sqlParser.parseQuery(sql);
        final SqlToRelConverter sqlToRelConverter = getSqlToRelConverter();
        RelRoot relRoot = sqlToRelConverter.convertQuery(sqlNode, true, true);

        relDataType = sqlValidator.getValidatedNodeType(sqlNode);
        fieldOrigins = sqlValidator.getFieldOrigins(sqlNode);

        //
        relRoot = trimUnusedFields(relRoot, sqlToRelConverter);
        relNode = sqlToRelConverter.flattenTypes(relRoot.rel, true);
        relNode = sqlToRelConverter.decorrelate(sqlNode, relNode);
        return relNode;
    }

    public RelNode getPlan(SqlNode sqlNode) {
        final SqlToRelConverter sqlToRelConverter = getSqlToRelConverter();
        RelRoot relRoot = sqlToRelConverter.convertQuery(sqlNode, true, true);

        relDataType = sqlValidator.getValidatedNodeType(sqlNode);
        fieldOrigins = sqlValidator.getFieldOrigins(sqlNode);

        //
        relRoot = trimUnusedFields(relRoot, sqlToRelConverter);
        relNode = sqlToRelConverter.flattenTypes(relRoot.rel, true);
        relNode = sqlToRelConverter.decorrelate(sqlNode, relNode);
        return optimize(relNode);
    }

    public RelNode getPlan(String sql) throws SqlParseException {
        final SqlNode sqlNode = sqlParser.parseQuery(sql);
        return getPlan(sqlNode);
    }

    protected RelRoot trimUnusedFields(RelRoot root, SqlToRelConverter converter) {
        final boolean ordered = !root.collation.getFieldCollations().isEmpty();
        final boolean dml = SqlKind.DML.contains(root.kind);
        return root.withRel(converter.trimUnusedFields(dml || ordered, root.rel));
    }

    @Override
    public RelRoot expandView(RelDataType rowType, String queryString, List<String> schemaPath, List<String> viewPath) {
        expansionDepth++;

        SqlParser parser = ParserFactory.getSqlParser(queryString);
        SqlNode sqlNode;
        try {
            sqlNode = parser.parseQuery();
        } catch (SqlParseException e) {
            throw new RuntimeException("parse failed", e);
        }

        // View may have different schema path than current connection.
        SqlToRelConverter sqlToRelConverter = getSqlToRelConverter();
        RelRoot root =
                sqlToRelConverter.convertQuery(sqlNode, true, false);

        --expansionDepth;
        return root;
    }


    private RelNode optimize(RelNode relNode) {

        //before cbo
       final HepPlanner hepPlannerBefore = buildHepPlannerBeforeCbo();
        hepPlannerBefore.setRoot(relNode);
        relNode = hepPlannerBefore.findBestExp();

        //cbo
        // Here: reqiredTraitSet is the RelTraitSet you want, basicly you should at
        // least assure the convention you want.
        RelTraitSet reqiredTraitSet = relNode.getTraitSet()
                .replace(SlothConvention.INSTANCE)
                //TODO FIX relcollation bug
                .plus(RelCollations.EMPTY)
                .simplify();
        RelNode relNode1 = relNode.getTraitSet().equals(reqiredTraitSet)
                ? relNode : relOptPlanner.changeTraits(relNode, reqiredTraitSet);

        relOptPlanner.setRoot(relNode1);
//        if (calciteCatalogReader.getRootSchema().getSubSchema("db", false) != null) {
//            addMateralizedRule(relOptPlanner);
//        }

        relNode = relOptPlanner.chooseDelegate().findBestExp();

        //after cbo
        final HepPlanner hepPlannerAfter = buildHepPlannerAfterCbo();
        hepPlannerAfter.setRoot(relNode);
        return hepPlannerAfter.findBestExp();
    }


    private HepPlanner buildHepPlannerForCalc() {
        HepProgramBuilder builder = new HepProgramBuilder();
        builder.addRuleInstance(CoreRules.PROJECT_CALC_MERGE);
        builder.addRuleInstance(CoreRules.PROJECT_TO_CALC);
        builder.addRuleInstance(CoreRules.FILTER_TO_CALC);
        builder.addRuleInstance(CoreRules.FILTER_CALC_MERGE);
        builder.addRuleInstance(CoreRules.CALC_MERGE);

        return new HepPlanner(builder.build());
    }


    private HepPlanner buildHepPlannerBeforeCbo() {
        HepProgramBuilder builder = new HepProgramBuilder();
        for (RelOptRule relOptRule : SlothRules.CONSTANT_REDUCTION_RULES) {
            builder.addRuleInstance(relOptRule);
        }

        for (RelOptRule relOptRule : SlothRules.BASE_RULES) {
            builder.addRuleInstance(relOptRule);
        }

        for (RelOptRule relOptRule : SlothRules.ABSTRACT_RELATIONAL_RULES) {
            builder.addRuleInstance(relOptRule);
        }

        for (RelOptRule relOptRule : SlothRules.ABSTRACT_RULES) {
            builder.addRuleInstance(relOptRule);
        }

        final HepPlanner hepPlanner = new HepPlanner(builder.build());

        //Why remove the join rule here
        hepPlanner.removeRule(CoreRules.JOIN_COMMUTE);
        hepPlanner.removeRule(CoreRules.JOIN_ASSOCIATE);
        return hepPlanner;
    }

    public HepPlanner buildHepPlannerAfterCbo() {
        HepProgramBuilder builder = new HepProgramBuilder();
        for (RelOptRule relOptRule : SlothRules.AFTER_CBO_RULES) {
            builder.addRuleInstance(relOptRule);
        }
        return new HepPlanner(builder.build());
    }

    private void addMateralizedRule(RelOptPlanner volcanoPlanner) {
        List<RelOptMaterialization> r = createMateralizedRule();

        volcanoPlanner.addRule(MaterializedViewRules.PROJECT_JOIN);
        volcanoPlanner.addRule(MaterializedViewRules.JOIN);
        volcanoPlanner.addRule(MaterializedViewRules.PROJECT_AGGREGATE);
        volcanoPlanner.addRule(MaterializedViewRules.AGGREGATE);
        volcanoPlanner.addRule(MaterializedViewRules.FILTER);
        volcanoPlanner.addRule(MaterializedViewRules.PROJECT_FILTER);
        volcanoPlanner.addRule(MaterializedViewRules.FILTER_SCAN);
        r.forEach(volcanoPlanner::addMaterialization);
    }

    private List<RelOptMaterialization> createMateralizedRule() {
        List<RelOptMaterialization> r = Lists.newArrayList();
        try {
            final RelNode view = getPlanWithoutCBO("select * from db.v1");
            // origin is the following one line
            // final RelNode sql = getPlanWithoutCBO("select * from db.t1 where id > 0");


            final RelNode sql = getPlanWithoutCBO("select count(t1.id), max(t2.name) from db.t1 inner join db.t2 on t1.id = t2.id where t1.id > 0");
            RelNode query = getPlanWithoutCBO("select id, count(*) from db.t1 group by id");
            HepPlanner hepPlanner = buildHepPlannerForCalc();
            hepPlanner.setRoot(query);
            query = hepPlanner.findBestExp();

//            RelNode target = getPlanWithoutCBO("select id, count(*) from db.t1 group by id");
            RelNode target = getPlanWithoutCBO("select id, name, count(*) from db.t1 group by id, name");
            hepPlanner = buildHepPlannerForCalc();
            hepPlanner.setRoot(target);
            target = hepPlanner.findBestExp();

            SubstitutionVisitor visitor = new SubstitutionVisitor(target, query);
            List<RelNode> rels = visitor.go(view);



            RelOptMaterialization rule = new RelOptMaterialization(view, sql, null,
                Lists.newArrayList("db", "v1"));
//
//            final RelNode view1 = getPlanWithoutCBO("select * from tpch.view1_tpch_lineitem");
//            final RelNode sql1 = getPlanWithoutCBO("select\n"
//                + "  l_returnflag,\n"
//                + "  l_linestatus,\n"
//                + "  sum(l_quantity) as sum_qty,\n"
//                + "  sum(l_extendedprice) as sum_base_price,\n"
//                + "  sum(l_extendedprice * (1 - l_discount)) as sum_disc_price,\n"
//                + "  sum(l_extendedprice * (1 - l_discount) * (1 + l_tax)) as sum_charge,\n"
//                + "  avg(l_quantity) as avg_qty,\n"
//                + "  avg(l_extendedprice) as avg_price,\n"
//                + "  avg(l_discount) as avg_disc,\n"
//                + "  count(*) as count_order\n"
//                + "from\n"
//                + "  tpch.tpch_lineitem\n"
//                + "where\n"
//                + "  l_shipdate <= date '1998-12-01'\n"
//                + "group by\n"
//                + "  l_returnflag,\n"
//                + "  l_linestatus\n"
//                + "order by\n"
//                + "  l_returnflag,\n"
//                + "  l_linestatus");
//            RelOptMaterialization rule1 = new RelOptMaterialization(view1, sql1, null,
//                Lists.newArrayList("tpch", "view1_tpch_lineitem"));


            final RelNode view1 = getPlanWithoutCBO("select * from tpch.view2");
            final RelNode sql1 = getPlanWithoutCBO("select\n"
                + "  c_custkey,\n"
                + "  c_name,\n"
                + "  c_acctbal,\n"
                + "  n_name,\n"
                + "  c_address,\n"
                + "  c_phone,\n"
                + "  c_comment\n"
                + "from\n"
                + "  tpch_customer,\n"
                + "  tpch_orders,\n"
                + "  tpch_lineitem,\n"
                + "  tpch_nation\n"
                + "where\n"
                + "  c_custkey = o_custkey\n"
                + "  and l_orderkey = o_orderkey\n"
                + "  and o_orderdate >= '1993-10-01'\n"
                + "  and l_returnflag = 'R'\n"
                + "  and c_nationkey = n_nationkey");

            RelOptMaterialization rule1 = new RelOptMaterialization(view1, sql1, null,
                Lists.newArrayList("tpch", "view2"));

            r.add(rule);
            r.add(rule1);
        } catch (SqlParseException e) {
            log.warn("{}", e.toString());
        }
        return r;
    }
}
