package com.yuqi.schema.common.integrate.multisource;

import com.yuqi.schema.common.integrate.IntegrateTestBase;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 17/2/20 10:45
 **/
public class FileAndMysqlJoinIntegrateTest extends IntegrateTestBase {
    public FileAndMysqlJoinIntegrateTest(String inputFile, String resultFile) {
        super(inputFile, resultFile);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"sql_and_result/file/join/join1.sql", "sql_and_result/file/join/join1.txt"},
        });
    }
}
