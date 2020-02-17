package com.yuqi.schema.common.integrate.local.multisource;

import com.yuqi.schema.common.integrate.local.IntegrateLocalTestBase;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 17/2/20 10:45
 **/
public class FileAndMysqlJoinIntegrateLocalTest extends IntegrateLocalTestBase {
    public FileAndMysqlJoinIntegrateLocalTest(String inputFile, String resultFile) {
        super(inputFile, resultFile);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"sql_and_result/local/file/join/join1.sql", "sql_and_result/local/file/join/join1.txt"},
        });
    }
}
