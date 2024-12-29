package com.orangeHRM.utilities;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {

    private final Map<String, String> scenarioContext;

    public ScenarioContext() {
        scenarioContext = new HashMap<>();
    }

    public void setContext(String key,String value) {
        scenarioContext.put(key, value);
    }

    public String getContext(String key) {
        return scenarioContext.get(key);
    }

    public Boolean isContains(String key) {
        return scenarioContext.containsKey(key);
    }
}
