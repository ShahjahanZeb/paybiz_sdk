package com.example.paybizsdk.service;

import com.example.paybizsdk.constants.ButtonType;
import com.example.paybizsdk.entity.ButtonCustomization;
import com.example.paybizsdk.entity.LabelCustomization;
import com.example.paybizsdk.entity.TextBoxCustomization;
import com.example.paybizsdk.entity.ToolbarCustomization;
import com.example.paybizsdk.exceptions.InvalidInputException;

import java.util.HashMap;
import java.util.Map;

public class UiCustomization {

    private Map<ButtonType, ButtonCustomization> buttonCustomizationMap;
    private ToolbarCustomization toolbarCustomizationMap;
    private LabelCustomization labelCustomizationMap;
    private TextBoxCustomization textBoxCustomizationMap;

    public UiCustomization() {
        this.buttonCustomizationMap = new HashMap<>();
    }

    public void setButtonCustomization(ButtonCustomization buttonCustomization, ButtonType buttonType) throws InvalidInputException {
        ButtonCustomization newButtonCustomization = new ButtonCustomization();
        newButtonCustomization.setBackgroundColor(buttonCustomization.getBackgroundColor());
        newButtonCustomization.setCornerRadius(buttonCustomization.getCornerRadius());
        buttonCustomizationMap.put(buttonType, newButtonCustomization);
    }

    public void setToolBarCustomization(ToolbarCustomization toolBarCustomization) throws InvalidInputException {
        ToolbarCustomization newToolbarCustomization = new ToolbarCustomization();
        newToolbarCustomization.setBackgroundColor(toolBarCustomization.getBackgroundColor());
        newToolbarCustomization.setButtonText(toolBarCustomization.getButtonText());
        newToolbarCustomization.setHeaderText(toolBarCustomization.getHeaderText());
        this.toolbarCustomizationMap = newToolbarCustomization;
    }

    public void setLabelCustomization(LabelCustomization labelCustomization) throws InvalidInputException {
        LabelCustomization newLabelCustomization = new LabelCustomization();
        newLabelCustomization.setHeadingTextColor(labelCustomization.getHeadingTextColor());
        newLabelCustomization.setHeadingTextFontName(labelCustomization.getHeadingTextFontName());
        newLabelCustomization.setHeadingTextFontSize(labelCustomization.getHeadingTextFontSize());
        this.labelCustomizationMap = newLabelCustomization;
    }

    public void setTextBoxCustomization(TextBoxCustomization textBoxCustomization) throws InvalidInputException {
        TextBoxCustomization newTextBoxCustomization = new TextBoxCustomization();
        newTextBoxCustomization.setCornerRadius(textBoxCustomization.getCornerRadius());
        newTextBoxCustomization.setBorderColor(textBoxCustomization.getBorderColor());
        newTextBoxCustomization.setBorderWidth(textBoxCustomization.getBorderWidth());
        this.textBoxCustomizationMap = textBoxCustomization;
    }

    public ButtonCustomization getButtonCustomization(ButtonType buttonType) throws InvalidInputException {
        ButtonCustomization buttonCustomization = buttonCustomizationMap.get(buttonType);

        if (buttonCustomization == null) {
            throw new InvalidInputException("Button customization not found for button type: " + buttonType);
        }

        return buttonCustomization;
    }

    public ButtonCustomization getButtonCustomization(String buttonType) throws InvalidInputException {
        ButtonCustomization buttonCustomization = buttonCustomizationMap.get(ButtonType.fromString(buttonType));

        if (buttonCustomization == null) {
            throw new InvalidInputException("Button customization not found for button type: " + buttonType);
        }

        return buttonCustomization;
    }

    public ToolbarCustomization getToolbarCustomization() throws InvalidInputException {
        return this.toolbarCustomizationMap;
    }

    public LabelCustomization getLabelCustomization() throws InvalidInputException {
        return this.labelCustomizationMap;
    }

    public TextBoxCustomization getTextBoxCustomization() throws InvalidInputException {
        return this.textBoxCustomizationMap;
    }


}
