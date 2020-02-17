package com.yuqi.schema.common.integrate.singlesource;

import com.yuqi.schema.common.integrate.IntegrateTestBase;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 17/2/20 10:38
 **/
public class MysqlIntegrateTest extends IntegrateTestBase {
    public MysqlIntegrateTest(String inputFile, String resultFile) {
        super(inputFile, resultFile);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"sql_and_result/file/select/select1.sql", "sql_and_result/file/select/select1.txt"},
                {"sql_and_result/file/groupby/groupby1.sql", "sql_and_result/file/groupby/groupby1.txt"}
        });
    }

    @Override
    public Properties getProperties() {
        return super.getProperties();
    }
}
