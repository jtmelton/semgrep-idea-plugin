package com.github.jtmelton.semgrepideaplugin.settings;

import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AppSettingsComponent {

    public static final String SEMGREP_BINARY_LOCATION = "com.github.jtmelton.semgrepideaplugin.BINARY_LOCATION";
    public static final String SEMGREP_RULES_LOCATION = "com.github.jtmelton.semgrepideaplugin.RULES_LOCATION";

    private final JPanel myMainPanel;
    private final JBTextField binaryPath = new JBTextField();
    private final JBTextField rulesPath = new JBTextField();

    public AppSettingsComponent() {
        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Binary / Run Script Location (fully qualified file path): "), binaryPath, 1, false)
                .addLabeledComponent(new JBLabel("Rules Location (fully qualified file path): "), rulesPath, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return binaryPath;
    }

    @NotNull
    public String getBinaryPathText() {
        return binaryPath.getText();
    }

    public void setBinaryPathText(@NotNull String newText) {
        binaryPath.setText(newText);
    }

    @NotNull
    public String getRulesPathText() {
        return rulesPath.getText();
    }

    public void setRulesPathText(@NotNull String newText) {
        rulesPath.setText(newText);
    }

}
