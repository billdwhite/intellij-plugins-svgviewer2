package org.ideaplugins.svgviewer.view.configuration;

import org.ideaplugins.svgviewer.Helpers;

import javax.swing.*;
import java.awt.*;

public class ConfigurationPanel
extends JPanel {


    private static final String ENABLE_PLUGIN = "Enable SvgViewer Plugin";
    private static final String ENABLE_PLUGIN_TOOLTIP = "Enable/disable the SvgViewer tool window";
    private static final String ABOUT = "About";
    private JCheckBox _pluginEnabled;



    public ConfigurationPanel() {
        buildGUI();
    }



    private void buildGUI() {
        this._pluginEnabled = new JCheckBox(ConfigurationPanel.ENABLE_PLUGIN);
        this._pluginEnabled.setToolTipText(ConfigurationPanel.ENABLE_PLUGIN_TOOLTIP);

        setLayout(new BorderLayout());

        JPanel topPane = new JPanel();
        topPane.setLayout(new BoxLayout(topPane, BoxLayout.X_AXIS));
        topPane.setBorder(BorderFactory.createEtchedBorder());
        topPane.add(this._pluginEnabled);

        JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
        tabbedPane.setPreferredSize(new Dimension(400, 600));
        tabbedPane.insertTab(ConfigurationPanel.ABOUT, Helpers.ICON_TOOL_WINDOW, new AboutPanel(), null, 0);

        add(topPane, "North");
        add(tabbedPane, "Center");
    }



    public boolean isPluginEnabled() {
        return this._pluginEnabled.isSelected();
    }



    public void setPluginEnabled(boolean enabled) {
        this._pluginEnabled.setSelected(enabled);
    }
}