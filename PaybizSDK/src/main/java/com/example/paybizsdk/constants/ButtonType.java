package com.example.paybizsdk.constants;

public enum ButtonType {

    SUBMIT,
    CONTINUE,
    NEXT,
    CANCEL,
    RESEND;

    public static ButtonType fromString(String buttonType) throws IllegalArgumentException {
        for (ButtonType type : ButtonType.values()) {
            if (type.name().equalsIgnoreCase(buttonType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown button type: " + buttonType);
    }
}

