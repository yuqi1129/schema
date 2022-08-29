package com.yuqi.sql;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.yuqi.sql.rule.SlothRules;
import com.yuqi.sql.rule.cbo.SlothPhysicalJoinChooseRule;
import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.config.CalciteConnectionConfigImpl;
import org.apache.calcite.config.Lex;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.jdbc.JavaTypeFactoryImpl;
import org.apache.calcite.plan.ConventionTraitDef;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.plan.RelOptUtil;
import org.apache.calcite.plan.volcano.VolcanoPlanner;
import org.apache.calcite.prepare.CalciteCatalogReader;
import org.apache.calcite.rel.RelCollationTraitDef;
import org.apache.calcite.rel.rules.CoreRules;
import org.apache.calcite.rel.rules.JoinPushThroughJoinRule;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.rex.RexUtil;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperatorTable;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.impl.SqlSchemaParserImpl;
import org.apache.calcite.sql.util.ChainedSqlOperatorTable;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql.validate.SqlValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Properties;

import static com.yuqi.sql.rule.SlothRules.BASE_RULES;
import static com.yuqi.sql.rule.SlothRules.CONSTANT_REDUCTION_RULES;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 10/7/20 19:50
 **/
public class ParserFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParserFactory.class);
    public static final CalciteCatalogReader CALCITE_CATALOG_READER = new CalciteCatalogReader(
            CalciteSchema.createRootSchema(false),
            ImmutableList.of(),
            new JavaTypeFactoryImpl(),
            new CalciteConnectionConfigImpl(new Properties())
    );


    public static SlothParser getParser(String sql, String currentDb) {
        CalciteCatalogReader calciteCatalogReader = getCatalogReader();

        if (Objects.nonNull(currentDb)) {
            calciteCatalogReader = calciteCatalogReader.withSchemaPath(Lists.newArrayList(currentDb));
        }
        return new SlothParser(getSqlParser(sql), getOptPlanner(),
                calciteCatalogReader, createSqlValidator(calciteCatalogReader));
    }

    public static SlothParser getParserWithCatalogReader(String sql, CalciteCatalogReader calciteCatalogReader) {
        return new SlothParser(getSqlParser(sql), getOptPlanner(),
                calciteCatalogReader, createSqlValidator(calciteCatalogReader));
    }


    public static RelOptPlanner getOptPlanner() {
        final VolcanoPlanner volcanoPlanner = new VolcanoPlanner();
        volcanoPlanner.addRelTraitDef(ConventionTraitDef.INSTANCE);
        volcanoPlanner.setExecutor(RexUtil.EXECUTOR);

        /**
         * See {VolcanoPlanner#getCost(
         * RelNode, RelMetadataQuery)}
         */
        volcanoPlanner.setNoneConventionHasInfiniteCost(false);

        registerRules(volcanoPlanner);

        for (RelOptRule relOptRule : SlothRules.CONVERTER_RULE) {
            volcanoPlanner.addRule(relOptRule);
        }

        volcanoPlanner.addRule(SlothPhysicalJoinChooseRule.INSTANCE);


        //默认情况下plan有convertion 这个traitset,只需要加下需要加的traitset即可
        volcanoPlanner.addRelTraitDef(RelCollationTraitDef.INSTANCE);
        return volcanoPlanner;
    }

    public static void registerRules(RelOptPlanner relOptPlanner) {
        for (RelOptRule relOptRule : CONSTANT_REDUCTION_RULES) {
            relOptPlanner.addRule(relOptRule);
        }

        RelOptUtil.registerAbstractRelationalRules(relOptPlanner);
        RelOptUtil.registerAbstractRules(relOptPlanner);

        for (RelOptRule relOptRule : BASE_RULES) {
            relOptPlanner.addRule(relOptRule);
        }

        relOptPlanner.addRule(CoreRules.PROJECT_TABLE_SCAN);
        relOptPlanner.addRule(CoreRules.PROJECT_INTERPRETER_TABLE_SCAN);
        relOptPlanner.addRule(CoreRules.FILTER_REDUCE_EXPRESSIONS);

        //TODO 尝试添加适配SlothConvention JoinToMultiJoinRule, 然后在
        /**
         * {@link org.apache.calcite.rel.rules.JoinToMultiJoinRule}
         * {@link org.apache.calcite.rel.rules.MultiJoinOptimizeBushyRule}
         * {@link org.apache.calcite.rel.rules.LoptOptimizeJoinRule}
         */
        relOptPlanner.addRule(CoreRules.JOIN_COMMUTE);
        relOptPlanner.addRule(CoreRules.JOIN_ASSOCIATE);

        relOptPlanner.addRule(CoreRules.JOIN_TO_MULTI_JOIN);

        //this two we can only need one
        relOptPlanner.addRule(CoreRules.MULTI_JOIN_OPTIMIZE_BUSHY);
        relOptPlanner.addRule(CoreRules.MULTI_JOIN_OPTIMIZE);

        relOptPlanner.addRule(JoinPushThroughJoinRule.LEFT);
        relOptPlanner.addRule(JoinPushThroughJoinRule.RIGHT);

        //relOptPlanner.addRule(SlothPhysicalJoinChooseRule.INSTANCE);

        //Currently when introduce with relcollation rule, SortRemoveRule has bug
        //sort remove this rule temporarily
        relOptPlanner.removeRule(CoreRules.SORT_REMOVE);
    }


    public static SqlParser getSqlParser(String sql) {
        final SqlParser.ConfigBuilder sqlBuilder = SqlParser.configBuilder()
                .setLex(Lex.MYSQL)
                .setQuoting(Quoting.BACK_TICK)
                .setQuotedCasing(Casing.UNCHANGED)
                .setUnquotedCasing(Casing.UNCHANGED)
                .setCaseSensitive(false)
                .setConformance(SqlConformanceEnum.MYSQL_5)
                .setParserFactory(SqlSchemaParserImpl.FACTORY);


        SqlParser.Config config = sqlBuilder.build();

        return SqlParser.create(sql, config);
    }

    public static CalciteCatalogReader getCatalogReader() {
        return CALCITE_CATALOG_READER;
    }

    public static SqlValidator createSqlValidator(CalciteCatalogReader calciteCatalogReader) {
        final SqlOperatorTable operatorTable1 = calciteCatalogReader.getConfig().fun(
                SqlOperatorTable.class,
                SqlStdOperatorTable.instance());


        final SqlOperatorTable operatorTable2 = ChainedSqlOperatorTable.of(operatorTable1, calciteCatalogReader);
        final RelDataTypeFactory factory = calciteCatalogReader.getTypeFactory();

        return SqlValidatorUtil.newValidator(operatorTable2, calciteCatalogReader, factory, SqlValidator.Config.DEFAULT);
    }

    public static void main(String[] args) {


        SlothSchema slothSchema = new SlothSchema("schema1");

        SlothTable slothTable1 = new SlothTable("table1");
        slothTable1.setSchema(slothSchema);
        slothSchema.addTable(slothTable1.getTableName(), slothTable1);

        SlothTable slothTable2 = new SlothTable("table2");
        slothTable2.setSchema(slothSchema);
        slothSchema.addTable(slothTable2.getTableName(), slothTable2);


        CALCITE_CATALOG_READER.getRootSchema().add("schema1", slothSchema);
        CALCITE_CATALOG_READER.getRootSchema().getSubSchemaMap().forEach((k, v) -> {
            final String schema = k;
            final CalciteSchema schemas = v;

            LOGGER.info("schema = " + k);
            v.getTableNames().forEach(System.out::println);
        });


        String sql = "create table `db1`.`t1` (id int, name double)";
        final SlothParser parser = ParserFactory.getParser(sql, null);

        try {
            SqlNode sqlNode = parser.getSqlNode();
            LOGGER.info(sqlNode.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
