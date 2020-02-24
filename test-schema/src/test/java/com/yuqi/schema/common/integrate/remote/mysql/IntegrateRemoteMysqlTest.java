package com.yuqi.schema.common.integrate.remote.mysql;

import com.yuqi.schema.common.integrate.remote.IntegrateRemoteSqlTest;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 24/2/20 16:47
 **/
public class IntegrateRemoteMysqlTest extends IntegrateRemoteSqlTest {
    public IntegrateRemoteMysqlTest(String filePath, String connectionConf) {
        super(filePath, connectionConf);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"sql_and_result/remote/mysql/select/select1.sql", "sql_and_result/remote/mysql/mysql_config.json" }
        });
    }
}
