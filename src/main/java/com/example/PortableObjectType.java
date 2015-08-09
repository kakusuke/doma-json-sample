package com.example;

import org.seasar.doma.jdbc.type.AbstractJdbcType;
import org.seasar.doma.jdbc.type.JdbcType;

import java.sql.*;

public class PortableObjectType<T> extends AbstractJdbcType<T> {
    private final JdbcType<T> jdbcType;

    public PortableObjectType(JdbcType<T> jdbcType) {
        super(Types.OTHER);
        this.jdbcType = jdbcType;
    }

    @Override
    protected T doGetValue(ResultSet resultSet, int index) throws SQLException {
        return jdbcType.getValue(resultSet, index);
    }

    @Override
    protected void doSetValue(PreparedStatement preparedStatement, int index, T value) throws SQLException {
        preparedStatement.setObject(index, value, type);
    }

    @Override
    protected T doGetValue(CallableStatement callableStatement, int index) throws SQLException {
        return jdbcType.getValue(callableStatement, index);
    }

    @Override
    protected String doConvertToLogFormat(T value) {
        return jdbcType.convertToLogFormat(value);
    }
}
