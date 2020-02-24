package com.yuqi.schema.common.integrate.remote;

import com.yuqi.schema.common.integrate.IntegrateTestBase;
import com.yuqi.schema.common.integrate.local.IntegrateLocalTestBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 17/2/20 19:34
 **/
@RunWith(Parameterized.class)
@Slf4j
public abstract class IntegrateRemoteTestBase extends IntegrateTestBase {
    private List<String> sqls;
    private String filePath;
    protected Statement dbStatement;

    public IntegrateRemoteTestBase(String filePath) {
        this.filePath = filePath;
    }

    @Before
    public void init() {
        try (
                final InputStream inputSqlStream = IntegrateLocalTestBase.class.getClassLoader().getResourceAsStream(filePath)) {

            sqls = IOUtils.readLines(inputSqlStream, Charset.defaultCharset()).stream().filter(this::isEmptyLineOrComment).collect(Collectors.toList());

            dbStatement = getStatement();
        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    @After
    public void after() {
        try {
            if (calciteStatement != null && !calciteStatement.isClosed()) {
                calciteStatement.close();
            }

            if (dbStatement != null && !dbStatement.isClosed()) {
                calciteStatement.close();
            }
        } catch (Exception e) {
            //ignore
            log.error(e.getMessage());
        }
    }

    @Test
    public void test() {
        //to
        if (CollectionUtils.isEmpty(sqls)) {
            return;
        }

        sqls.forEach(sql -> {
            List<List<String>> actualResult = runSql(sql, calciteStatement);
            List<List<String >> expectedResult = runSql(sql, dbStatement);

            if(compareResultIgnoreSequence(expectedResult, actualResult)) {
                String errorMessage = String.format(
                        "Error in Integrate test, detail:\n, File:'%s'\n SQL:'%s'\nExpected result:'%s'\n Actual Result:'%s'",
                        filePath, sql, expectedResult.toString(), actualResult.toString());
                log.error(errorMessage);
                throw new RuntimeException(errorMessage);
            }
        });

    }

    public abstract Statement getStatement();

}
