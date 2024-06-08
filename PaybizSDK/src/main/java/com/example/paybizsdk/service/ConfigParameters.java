package com.example.paybizsdk.service;

import com.example.paybizsdk.exceptions.InvalidInputException;

import java.util.HashMap;
import java.util.Map;

public class ConfigParameters {

    private Map<String, Map<String, String>> groupConfigMap;

    public ConfigParameters() {
        this.groupConfigMap = new HashMap<>();
    }

    public void addParam(String group, String paramName, String paramValue) throws InvalidInputException {
        Map<String, String> configParameters;
        if(groupConfigMap.containsKey(group)){
            configParameters = groupConfigMap.get(group);
        }
        else {
            configParameters = new HashMap<>();
            groupConfigMap.put(group, configParameters);
        }
        configParameters.put(paramName, paramValue);
    }

    public String getParamValue(String group, String paramName) throws InvalidInputException {
        Map<String, String> configParameters;
        String paramValue = null;
        if(groupConfigMap.containsKey(group)){
            configParameters = groupConfigMap.get(group);
            if(configParameters.containsKey(paramName)){
                paramValue = configParameters.get(paramName);
            }
        }
        return paramValue;
    }

    public String removeValue(String group, String paramName) throws InvalidInputException {
        Map<String, String> configParameters;
        String paramValue = null;
        if(groupConfigMap.containsKey(group)){
            configParameters = groupConfigMap.get(group);
            if(configParameters.containsKey(paramName)){
                paramValue = configParameters.remove(paramName);
            }
        }
        return "Value Removed";
    }
}
