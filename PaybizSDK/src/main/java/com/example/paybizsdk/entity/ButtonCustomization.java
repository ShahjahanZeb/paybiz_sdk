package com.example.paybizsdk.entity;

import com.example.paybizsdk.constants.ButtonType;

public class ButtonCustomization extends Customization{

    private String backgroundColor;

    private int cornerRadius;

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

}
