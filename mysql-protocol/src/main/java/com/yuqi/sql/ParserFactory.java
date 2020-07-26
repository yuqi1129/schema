package com.yuqi.sql;

import com.google.common.collect.ImmutableList;
import com.yuqi.sql.rule.SlothRules;
import org.apache.calcite.avatica.util.Casing;
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
import org.apache.calcite.rel.rules.ProjectTableScanRule;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlOperatorTable;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.impl.SqlParserImpl;
import org.apache.calcite.sql.util.ChainedSqlOperatorTable;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql.validate.SqlValidatorUtil;

import java.util.Properties;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 10/7/20 19:50
 **/
public class ParserFactory {
    private SlothSchemaHolder holder = SlothSchemaHolder.INSTANCE;
    public static final CalciteCatalogReader CALCITE_CATALOG_READER = new CalciteCatalogReader(
            CalciteSchema.createRootSchema(false),
            ImmutableList.of(),
            new JavaTypeFactoryImpl(),
            new CalciteConnectionConfigImpl(new Properties())
    );


    public static SlothParser getParser(String sql) {
        final CalciteCatalogReader calciteCatalogReader = getCatalogReader();
        return new SlothParser(getSqlParser(sql), getOptPlanner(), calciteCatalogReader, createSqlValidator(calciteCatalogReader));
    }


    public static RelOptPlanner getOptPlanner() {
        final VolcanoPlanner volcanoPlanner = new VolcanoPlanner();
        volcanoPlanner.addRelTraitDef(ConventionTraitDef.INSTANCE);
        RelOptUtil.registerAbstractRules(volcanoPlanner);

        for (RelOptRule relOptRule : SlothRules.DEFAULT_RULES) {
            volcanoPlanner.addRule(relOptRule);
        }

        volcanoPlanner.addRule(ProjectTableScanRule.INSTANCE);
        volcanoPlanner.addRule(ProjectTableScanRule.INTERPRETER);

        return volcanoPlanner;
    }


    public static SqlParser getSqlParser(String sql) {
        final SqlParser.ConfigBuilder sqlBuilder = SqlParser.configBuilder()
                .setLex(Lex.MYSQL)
                .setQuotedCasing(Casing.UNCHANGED)
                .setUnquotedCasing(Casing.UNCHANGED)
                .setCaseSensitive(false)
                .setParserFactory(SqlParserImpl.FACTORY);


        SqlParser.Config config = sqlBuilder.build();

        return SqlParser.create(sql, config);
    }

    public static CalciteCatalogReader getCatalogReader() {
        CalciteSchema calciteSchema = CalciteSchema.createRootSchema(false);
        return new CalciteCatalogReader(
                calciteSchema,
                ImmutableList.of(),
                new JavaTypeFactoryImpl(),
                new CalciteConnectionConfigImpl(new Properties()));
    }

    public static SqlValidator createSqlValidator(CalciteCatalogReader calciteCatalogReader) {
        final SqlOperatorTable operatorTable1 = calciteCatalogReader.getConfig().fun(
                SqlOperatorTable.class,
                SqlStdOperatorTable.instance());


        final SqlOperatorTable operatorTable2 = ChainedSqlOperatorTable.of(operatorTable1, calciteCatalogReader);
        final RelDataTypeFactory factory = calciteCatalogReader.getTypeFactory();

        SqlConformanceEnum sqlConformanceEnum = SqlConformanceEnum.MYSQL_5;

        return SqlValidatorUtil.newValidator(operatorTable2, calciteCatalogReader, factory, sqlConformanceEnum);
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

            System.out.println("schema = " + k);
            v.getTableNames().forEach(System.out::println);
        });


        String sql = "create database db1";
        final SlothParser parser = ParserFactory.getParser(sql);

        try {
            SqlNode sqlNode = parser.getSqlNode(sql);
            System.out.println(sqlNode);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
