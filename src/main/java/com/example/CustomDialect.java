package com.example;

import java.sql.SQLException;

import org.seasar.doma.jdbc.JdbcMappingFunction;
import org.seasar.doma.jdbc.JdbcMappingHint;
import org.seasar.doma.jdbc.JdbcMappingVisitor;
import org.seasar.doma.jdbc.dialect.PostgresDialect;
import org.seasar.doma.jdbc.dialect.StandardDialect;
import org.seasar.doma.jdbc.type.JdbcTypes;
import org.seasar.doma.wrapper.StringWrapper;

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
            if (q.getDomainClass().map(DateRange.class::isAssignableFrom).orElse(false)) {
              return p.apply(wrapper, new PortableObjectType<>(JdbcTypes.STRING));
            }
            if (q.getDomainClass().map(JsonObject.class::isAssignableFrom).orElse(false)) {
              return p.apply(wrapper, new PortableObjectType<>(JdbcTypes.STRING));
            }
            return p.apply(wrapper, JdbcTypes.STRING);
//            return super.visitStringWrapper(wrapper, p, q);
        }
    };
}
