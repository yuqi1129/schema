package com.yuqi.schema.mysql;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactoryImpl;
import org.apache.calcite.rel.type.RelDataTypeField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuqi
 * @mail yuqi4733@gmail.com
 * @description your description
 * @time 19/1/20 20:32
 **/
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class MySqlReaderImpl implements MysqlReader {

    @Setter
    @Getter
    private String sql;
    private final Connection connection;
    private final MysqlTable mysqlTable;

    @Override
    public Iterator<Object[]> readData() {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql);
            ResultSet r = preparedStatement.executeQuery();
            RelDataType relDataType = mysqlTable.getRelDataType();

            return getDataFromResult(r, relDataType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private Iterator<Object[]> getDataFromResult(ResultSet r, RelDataType colunmType) {
        final List<RelDataTypeField> f = colunmType.getFieldList();
        //RelDataTypeFactoryImpl.JavaType

        List<Class> typeClass = f.stream()
                .map(RelDataTypeField::getType)
                .map(a -> (RelDataTypeFactoryImpl.JavaType) a)
                .map(a -> a.getJavaClass())
                .collect(Collectors.toList());


        List<Object[]> result = Lists.newArrayList();
        int columnSize = typeClass.size();

        try {
            while (r.next()) {
                Object[] objects = new Object[columnSize];
                for (int i = 1; i <= columnSize; i++) {
                    //Object object = TypeUtil.ObjectToClassData(r, i, typeClass.get(i));
                    //objects[i - 1] = object;
                    objects[i - 1] = r.getObject(i);
                }

                result.add(objects);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result.iterator();
    }

}
