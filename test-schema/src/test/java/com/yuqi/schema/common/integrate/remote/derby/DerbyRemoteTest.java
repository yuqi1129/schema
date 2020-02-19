package com.yuqi.schema.common.integrate.remote.derby;

import com.yuqi.schema.common.integrate.local.IntegrateLocalTestBase;
import com.yuqi.schema.common.integrate.remote.IntegrateRemoteTestBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.runners.Parameterized;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuqi
 * @mail yuqi5@xiaomi.com
 * @description your description
 * @time 19/2/20 14:44
 **/
@Slf4j
public class DerbyRemoteTest extends IntegrateRemoteTestBase {
    private final String metaFile;

    public DerbyRemoteTest(String filePath, String metaFile) {
        super(filePath);
        this.metaFile = metaFile;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"sql_and_result/remote/derby/select/select1.sql", "sql_and_result/remote/derby/derby.sql"}
        });
    }

    @Override
    public void init() {
        super.init();
        try {

            final InputStream inputSqlStream = IntegrateLocalTestBase.class.getClassLoader().getResourceAsStream(metaFile);
            List<String> sqls = IOUtils.readLines(inputSqlStream, Charset.defaultCharset())
                    .stream()
                    .filter(this::isEmptyLineOrComment)
                    .collect(Collectors.toList());

            for (String sql : sqls) {
                dbStatement.execute(sql);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public Statement getStatement() {

        String url = "jdbc:derby:memory:xxx;create=true";
        Connection connection;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            connection = DriverManager.getConnection(url);
            return connection.createStatement();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
