package com.yuqi.schema.common.integrate.meta;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.yuqi.schema.common.integrate.remote.IntegrateRemoteSqlTest;
import com.yuqi.schema.common.util.ResultSetUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.runners.Parameterized;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description This class is to test meta data of query between calcite and mysql!!!!
 * @time 24/2/20 16:33
 **/
@Slf4j
public class MetaIntegrateTest extends IntegrateRemoteSqlTest {

    public MetaIntegrateTest(String filePath, String mysqlConf) {
        super(filePath, mysqlConf);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"sql_and_result/remote/mysql/select/select1.sql", "sql_and_result/remote/mysql/mysql_config.json" }
        });
    }


    @Override
    protected List<List<String>> runSql(String sql, Statement statement) {

        final List<List<String>> result = Lists.newArrayList();
        try {
            final ResultSet r = statement.executeQuery(sql);
            final List<String> columnName = ResultSetUtils.getColumnNameFromResultSet(r);
            result.add(columnName);
        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException(Throwables.getStackTraceAsString(e));
        }

        return result;
    }
}
