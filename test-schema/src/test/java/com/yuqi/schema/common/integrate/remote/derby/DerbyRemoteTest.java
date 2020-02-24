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

import static com.yuqi.schema.common.constants.CommonConstant.DERBY_DRIVER;
import static com.yuqi.schema.common.constants.CommonConstant.DERBY_URL;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/2/20 14:44
 **/
@Slf4j
public class DerbyRemoteTest extends IntegrateRemoteTestBase {
    private final String metaFile;
    private Connection connection;

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
    public void after() {
        super.after();
        if (null != connection) {
            try {
                connection.close();
            } catch (Exception e) {
                log.error("close derby connection:" + e.getMessage());
            }
        }
    }

    @Override
    public void init() {
        super.init();
        try {

            //create schema and table in memory
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

        try {
            Class.forName(DERBY_DRIVER).newInstance();
            connection = DriverManager.getConnection(DERBY_URL);
            return connection.createStatement();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
