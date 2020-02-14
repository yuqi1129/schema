package com.yuqi.schema.common.integrate;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.avatica.InternalProperty;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

import static com.yuqi.schema.common.constants.MetaConstants.META_MODEL;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 14/2/20 19:40
 **/
@RunWith(Parameterized.class)
@RequiredArgsConstructor
@Slf4j
public abstract class IntegrateTestBase {

    private final String inputFile;
    private final String resultFile;

    private List<String> inputSql;
    private List<String> results;
    private Statement statement;

    @Before
    public void before() {
        //todo
        try (
                final InputStream inputSqlStream = IntegrateTestBase.class.getClassLoader().getResourceAsStream(inputFile);
                final InputStream resultStream = IntegrateTestBase.class.getClassLoader().getResourceAsStream(resultFile)){

            inputSql = IOUtils.readLines(inputSqlStream, Charset.defaultCharset());
            results = IOUtils.readLines(resultStream, Charset.defaultCharset());
            statement = getStatement();
        } catch (Exception e) {
           log.error(e.toString());
           throw new RuntimeException(e);
        }

    }


    @After
    public void teardown() {
        if (null != statement) {
            try {
                statement.close();
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
            final List<List<String>> acutalResult = runSql(inputSql.get(i));
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

    private List<List<String>> runSql(String sql) {

        final List<List<String>> result = Lists.newArrayList();
        try {
            final ResultSet r = statement.executeQuery(sql);
            final int columnCount = r.getMetaData().getColumnCount();

            while (r.next()) {
                final List<String> value = Lists.newArrayListWithCapacity(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    value.add(r.getString(i));
                }
                result.add(value);
            }
        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException(Throwables.getStackTraceAsString(e));
        }

        return result;
    }


    private List<List<String>> createExpectResult(String origin) {
        return Arrays.stream(origin.split(";")).map(string ->
            Arrays.stream(string.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private boolean compareResultIgnoreSequence(List<List<String>> expect, List<List<String>> actual) {
        //sort
        expect.sort(Comparator.comparing(Object::toString));
        actual.sort(Comparator.comparing(Object::toString));

        final int expectLen = expect.size();
        final int acutalLen = actual.size();

        if (expectLen != acutalLen) {
            return false;
        }

        for (int i = 0; i < acutalLen; i++) {
            if (!Objects.equals(expect.get(i), actual.get(i))) {
                return false;
            }
        }

        return true;
    }

    private Statement getStatement() throws SQLException {
        Properties properties = getProperties();
        Connection connection =
                DriverManager.getConnection("jdbc:calcite:", properties);

        return connection.createStatement();
    }

    public abstract Properties getProperties();

}
