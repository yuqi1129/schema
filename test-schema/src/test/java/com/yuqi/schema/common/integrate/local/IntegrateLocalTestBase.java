package com.yuqi.schema.common.integrate.local;

import com.yuqi.schema.common.integrate.IntegrateTestBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 14/2/20 19:40
 **/
@RunWith(Parameterized.class)
@RequiredArgsConstructor
@Slf4j
public abstract class IntegrateLocalTestBase extends IntegrateTestBase {

    private final String inputFile;
    private final String resultFile;

    private List<String> inputSql;
    private List<String> results;

    @Before
    public void before() {
        //todo
        try (
                final InputStream inputSqlStream = IntegrateLocalTestBase.class.getClassLoader().getResourceAsStream(inputFile);
                final InputStream resultStream = IntegrateLocalTestBase.class.getClassLoader().getResourceAsStream(resultFile)) {

            inputSql = IOUtils.readLines(inputSqlStream, Charset.defaultCharset()).stream().filter(this::isEmptyLineOrComment).collect(Collectors.toList());
            results = IOUtils.readLines(resultStream, Charset.defaultCharset()).stream().filter(this::isEmptyLineOrComment).collect(Collectors.toList());
        } catch (Exception e) {
           log.error(e.toString());
           throw new RuntimeException(e);
        }

    }


    @After
    public void teardown() {
        if (null != calciteStatement) {
            try {
                calciteStatement.close();
            } catch (Exception e) {
                log.error(e.toString());
            }
        }
    }

    @Test
    public void work() {
        //do somethin
        runTest();
    }


    private void runTest() {
        final int length = inputSql.size();

        for (int i = 0; i < length; i++) {
            final List<List<String>> acutalResult = runSql(inputSql.get(i), calciteStatement);
            final List<List<String>> expectResult = createExpectResult(results.get(i));

            if (!compareResultIgnoreSequence(expectResult, acutalResult)) {
                String errorMessage = String.format(
                        "Error in Integrate test, detail:\n, File:'%s'\n SQL:'%s'\nExpected result:'%s'\n Actual Result:'%s'",
                        inputFile, inputSql.get(i), expectResult.toString(), acutalResult.toString());
                log.error(errorMessage);
                throw new RuntimeException(errorMessage);
            }
        }
    }

}
