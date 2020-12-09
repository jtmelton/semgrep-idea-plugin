package com.github.jtmelton.semgrepideaplugin.settings;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class SemgrepSettingsConfigurable implements Configurable {

    private AppSettingsComponent mySettingsComponent;
    PropertiesComponent properties = PropertiesComponent.getInstance();

    // A default constructor with no arguments is required because this implementation
    // is registered as an applicationConfigurable EP

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Semgrep Settings";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new AppSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        boolean modified = !mySettingsComponent.getBinaryPathText().equals(getBinaryPathSetting());
        modified |= !mySettingsComponent.getRulesPathText().equals(getRulesPathSetting());
        return modified;
    }

    @Override
    public void apply() {
        setBinaryPathSetting(mySettingsComponent.getBinaryPathText());
        setRulesPathSetting(mySettingsComponent.getRulesPathText());
    }

    @Override
    public void reset() {
        mySettingsComponent.setBinaryPathText(getBinaryPathSetting());
        mySettingsComponent.setRulesPathText(getRulesPathSetting());
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }

    private String getBinaryPathSetting() {
        String path = "";

        if(properties.isValueSet(AppSettingsComponent.SEMGREP_BINARY_LOCATION)) {
            path = properties.getValue(AppSettingsComponent.SEMGREP_BINARY_LOCATION);
        }

        return path;
    }

    private void setBinaryPathSetting(String path) {
        properties.setValue(AppSettingsComponent.SEMGREP_BINARY_LOCATION, path);
    }

    private String getRulesPathSetting() {
        String path = "";

        if(properties.isValueSet(AppSettingsComponent.SEMGREP_RULES_LOCATION)) {
            path = properties.getValue(AppSettingsComponent.SEMGREP_RULES_LOCATION);
        }

        return path;
    }

    private void setRulesPathSetting(String path) {
        properties.setValue(AppSettingsComponent.SEMGREP_RULES_LOCATION, path);
    }
}
