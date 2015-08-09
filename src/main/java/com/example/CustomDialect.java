package com.example;

import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.JdbcMappingHint;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;
import org.seasar.doma.jdbc.type.JdbcTypes;
import org.seasar.doma.wrapper.StringWrapper;

import java.sql.SQLException;

/**
 * Created by kakusuke on 15/08/09.
 */
public class CustomDialect extends StandardDialect {
    @Override
    public JdbcMappingVisitor getJdbcMappingVisitor() {
        return jdbcMappingVisitor;
    }

    private JdbcMappingVisitor jdbcMappingVisitor = new PostgresDialect.PostgresJdbcMappingVisitor() {
        @Override
        public Void visitStringWrapper(StringWrapper wrapper, JdbcMappingFunction p, JdbcMappingHint q) throws SQLException {
            return p.apply(wrapper, new PortableObjectType<>(JdbcTypes.STRING));
//            return super.visitStringWrapper(wrapper, p, q);
        }
    };
}
