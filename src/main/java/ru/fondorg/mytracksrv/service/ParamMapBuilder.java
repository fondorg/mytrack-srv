package ru.fondorg.mytracksrv.service;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;

public class ParamMapBuilder {
    private MultiValueMap<String, String> map;

    private ParamMapBuilder() {
        map = new LinkedMultiValueMap<>();
    }

    public static ParamMapBuilder newMap() {
        return new ParamMapBuilder();
    }


    public ParamMapBuilder add(String key, List<String> value) {
        map.put(key, value);
        return this;
    }

    public ParamMapBuilder add(String key, String value) {
        map.put(key, Collections.singletonList(value));
        return this;
    }

    public MultiValueMap<String, String> build() {
        return map;
    }


}
