package com.yuqi.schema.common.integrate;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.avatica.InternalProperty;
import org.apache.commons.lang3.StringUtils;

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
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 17/2/20 19:49
 **/
@Slf4j
public abstract class IntegrateTestBase {
    protected Statement calciteStatement;

    public IntegrateTestBase() {
        getStatement();
    }

    protected boolean isEmptyLineOrComment(String line) {
        if (Objects.isNull(line)) {
            return false;
        }

        final String sql = line.trim();
        return !(StringUtils.isEmpty(line) || sql.startsWith("--") || sql.startsWith("#"));
    }

    private void getStatement() {
        try {
            if (calciteStatement != null && !calciteStatement.isClosed()) {
                return ;
            }

            Properties properties = getProperties();
            Connection connection =
                    DriverManager.getConnection("jdbc:calcite:", properties);

            calciteStatement = connection.createStatement();
        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }

    protected Properties getProperties() {
        final Properties info = new Properties();
        info.setProperty(META_MODEL,
                "/Users/yuqi/project/" + "schema/test-schema/src/test/resources/schema.json");

        info.setProperty(InternalProperty.CASE_SENSITIVE.name(), "false");
        return info;
    }

    protected boolean compareResultIgnoreSequence(List<List<String>> expect, List<List<String>> actual) {
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

    protected List<List<String>> runSql(String sql, Statement statement) {

        final List<List<String>> result = Lists.newArrayList();
        try {
            final ResultSet r = statement.executeQuery(sql);
            final int columnCount = r.getMetaData().getColumnCount();
            while (r.next()) {
                final List<String> value = Lists.newArrayListWithCapacity(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    //FIXME r.getString is not exact
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


    protected List<List<String>> createExpectResult(String origin) {
        return Arrays.stream(origin.split(";")).map(string ->
                Arrays.stream(string.split(","))
                        .map(String::trim).map(s -> {
                    if (s.equals("null")) {
                        return null;
                    }

                    return s;
                })
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

}
