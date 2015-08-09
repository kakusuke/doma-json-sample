package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SimpleDataSource;
import org.seasar.doma.jdbc.dialect.Dialect;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by kakusuke on 15/08/09.
 */
public class JsonDomainMappingTest {
    private Config config = new Config() {
        @Override
        public DataSource getDataSource() {
            SimpleDataSource ds = new SimpleDataSource();
            ds.setUrl("jdbc:postgresql:test");
            ds.setUser("postgres");
            return ds;
        }

        @Override
        public Dialect getDialect() {
            return new CustomDialect();
        }
    };

    private SampleDao dao;
    private ObjectMapper mapper;
    @Before
    public void setUp() throws Exception {
        execute("DROP TABLE IF EXISTS sample");
        execute("CREATE TABLE sample (daterange daterange, json json, text text)");
        dao = new SampleDaoImpl(config);
        mapper = new ObjectMapper();
        mapper.registerModule(new JsonObjectModule());
    }

    @Test
    public void mappingTest() throws Exception {
        Sample sample = mapper.readValue("{\"dateRange\":\"[2014-01-01,2015-01-01)\",\"json\":[1,2,3],\"text\":\"text\"}", Sample.class);
        dao.insert(sample);

        String json = mapper.writeValueAsString(dao.select());
        assertEquals(json, "[{\"dateRange\":\"[2014-01-01,2015-01-01)\",\"json\":[1,2,3],\"text\":\"text\"}]");
    }

    private void execute(String sql) throws SQLException {
        config.getDataSource().getConnection().createStatement().execute(sql);
    }
}