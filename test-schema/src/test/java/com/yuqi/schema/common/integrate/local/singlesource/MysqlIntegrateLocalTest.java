package com.yuqi.schema.common.integrate.local.singlesource;

import com.yuqi.schema.common.integrate.local.IntegrateLocalTestBase;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 17/2/20 10:38
 **/
public class MysqlIntegrateLocalTest extends IntegrateLocalTestBase {
    public MysqlIntegrateLocalTest(String inputFile, String resultFile) {
        super(inputFile, resultFile);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"sql_and_result/local/file/select/select1.sql", "sql_and_result/local/file/select/select1.txt"},
                {"sql_and_result/local/file/groupby/groupby1.sql", "sql_and_result/local/file/groupby/groupby1.txt"}
        });
    }

    @Override
    public Properties getProperties() {
        return super.getProperties();
    }
}
