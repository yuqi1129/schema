package com.yuqi.schema.common.integrate.singlesource;

import com.yuqi.schema.common.integrate.IntegrateTestBase;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;


/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 14/2/20 19:47
 **/
public class FileIntegrateTest extends IntegrateTestBase {

    public FileIntegrateTest(String inputFile, String resultFile) {
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
