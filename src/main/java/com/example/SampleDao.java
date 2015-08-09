package com.example;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;

import java.util.List;

/**
 * Created by kakusuke on 15/08/09.
 */
@Dao
public interface SampleDao {
    @Select
    List<Sample> select();
    @Insert
    int insert(Sample sample);
}
