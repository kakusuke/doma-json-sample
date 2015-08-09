package com.example;

import org.seasar.doma.Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kakusuke on 15/08/10.
 */
@Domain(valueType = String.class)
public class IntList extends JsonObject<List<Integer>> {
    public IntList() {
        super(new ArrayList<>());
    }
    public IntList(List<Integer> object) {
        super(object);
    }
    public IntList(String json) {
        super(json);
    }
}
