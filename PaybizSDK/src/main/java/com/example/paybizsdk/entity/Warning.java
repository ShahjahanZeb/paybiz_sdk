package com.example.paybizsdk.entity;

import androidx.annotation.NonNull;

import com.example.paybizsdk.constants.WarningEnum;
import com.example.paybizsdk.constants.WarningSeverity;

public class Warning {

    public Warning() {
    }

    public Warning(WarningEnum warningEnum, String warningMessage, WarningSeverity warningSeverity) {
        this.warningEnum = warningEnum;
        this.warningMessage = warningMessage;
        this.warningSeverity = warningSeverity;
    }

    private WarningEnum warningEnum;

    private String warningMessage;

    private WarningSeverity warningSeverity;

    public WarningEnum getWarningEnum() {
        return warningEnum;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public WarningSeverity getWarningSeverity() {
        return warningSeverity;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getWarningEnum().toString();
    }
}
