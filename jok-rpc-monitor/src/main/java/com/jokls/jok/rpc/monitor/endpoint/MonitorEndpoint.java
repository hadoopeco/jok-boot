package com.jokls.jok.rpc.monitor.endpoint;

import java.util.LinkedHashMap;
import java.util.Map;

public class MonitorEndpoint {


    public Map<String, Object> invoke() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("consumers", "");
        result.put("provider" , "");

        return result;
    }
}
