package com.yuqi.schema.common;

import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.prepare.PlannerImpl;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.dialect.MysqlSqlDialect;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 25/2/20 11:58
 **/
public class SqlTest {

    public static void main(String[] args) {

        try {
            final FrameworkConfig frameworkConfig = Frameworks.newConfigBuilder()
                    .defaultSchema(null)
                    .parserConfig(SqlParser.configBuilder().setUnquotedCasing(Casing.UNCHANGED).build())
                    .build();

            final PlannerImpl planner = new PlannerImpl(frameworkConfig);
            final SqlNode sqlNode = planner.parse("select count(*) from t");


            System.out.println(((SqlSelect) sqlNode).getSelectList().getList().get(0).toSqlString(MysqlSqlDialect.DEFAULT));

        } catch (Exception e) {
            e.printStackTrace();
        }





    }
}
