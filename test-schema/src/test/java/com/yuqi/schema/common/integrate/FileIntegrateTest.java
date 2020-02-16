package com.yuqi.schema.common.integrate;

import org.apache.calcite.avatica.InternalProperty;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import static com.yuqi.schema.common.constants.MetaConstants.META_MODEL;

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
                {"file/select/select1.sql", "file/select/select1.txt"},
                {"file/groupby/groupby1.sql", "file/groupby/groupby1.txt"}
        });
    }

    @Override
    public Properties getProperties() {
        final Properties info = new Properties();
        info.setProperty(META_MODEL,
                "/Users/yuqi/project/" + "schema/test-schema/src/main/resources/file_and_mysql.json");

        info.setProperty(InternalProperty.CASE_SENSITIVE.name(), "false");
        return info;
    }
}
