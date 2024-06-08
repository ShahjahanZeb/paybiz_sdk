package com.example.paybizsdk.entity;

public class LabelCustomization extends Customization{

    private String headingTextColor;

    private String headingTextFontName;

    private int headingTextFontSize;

    public String getHeadingTextColor() {
        return headingTextColor;
    }

    public void setHeadingTextColor(String headingTextColor) {
        this.headingTextColor = headingTextColor;
    }

    public String getHeadingTextFontName() {
        return headingTextFontName;
    }

    public void setHeadingTextFontName(String headingTextFontName) {
        this.headingTextFontName = headingTextFontName;
    }

    public int getHeadingTextFontSize() {
        return headingTextFontSize;
    }

    public void setHeadingTextFontSize(int headingTextFontSize) {
        this.headingTextFontSize = headingTextFontSize;
    }
}
