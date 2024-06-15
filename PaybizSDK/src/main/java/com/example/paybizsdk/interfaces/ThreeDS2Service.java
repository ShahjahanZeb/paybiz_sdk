package com.example.paybizsdk.interfaces;

import com.example.paybizsdk.constants.UICustomizationType;
import com.example.paybizsdk.entity.Warning;
import com.example.paybizsdk.service.ConfigParameters;
import com.example.paybizsdk.service.UiCustomization;

import java.util.List;
import java.util.Map;

public interface ThreeDS2Service {

    public void initialize(android.content.Context applicationContext, ConfigParameters configParameters,
                          String locale, Map<UICustomizationType, UiCustomization> uiCustomizationMap);

    public Transaction createTransaction(String directoryServerID, String messageVersion);

    public void cleanup(android.content.Context applicationContext);

    public String getSDKVersion();

    public List<Warning> getWarnings();

}
