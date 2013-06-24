package org.ideaplugins.svgviewer.controller.application;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import org.ideaplugins.svgviewer.Helpers;
import org.ideaplugins.svgviewer.controller.project.SvgViewerProjectComponent;
import org.ideaplugins.svgviewer.view.configuration.ConfigurationPanel;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class Configuration
implements ApplicationComponent, JDOMExternalizable, Configurable {


    private static final String CONFIGURATION_COMPONENT_NAME = "Settings";
    public static final String PLUGIN_NAME = "SvgViewer";
    public boolean PLUGIN_ENABLED = true;
    private ConfigurationPanel _panel;



    public void initComponent() {
    }



    public void disposeComponent() {
    }



    @NotNull
    public String getComponentName() {
        return Configuration.PLUGIN_NAME + "." + Configuration.CONFIGURATION_COMPONENT_NAME;
    }



    public void readExternal(Element element)
    throws InvalidDataException {
        DefaultJDOMExternalizer.readExternal(this, element);
    }



    public void writeExternal(Element element)
    throws WriteExternalException {
        DefaultJDOMExternalizer.writeExternal(this, element);
    }



    public String getDisplayName() {
        return "SvgViewer";
    }



    public Icon getIcon() {
        return Helpers.ICON_TOOL_WINDOW;
    }



    public String getHelpTopic() {
        return null;
    }



    public JComponent createComponent() {
        this._panel = new ConfigurationPanel();
        return this._panel;
    }



    public boolean isModified() {
        return isPluginEnabledStateChanged();
    }



    private boolean isPluginEnabledStateChanged() {
        return this._panel.isPluginEnabled() ^ isPluginEnabled();
    }



    public void apply()
    throws ConfigurationException {
        if (isPluginEnabledStateChanged()) {
            enableToolWindows(this._panel.isPluginEnabled());
        }
        setPluginEnabled(this._panel.isPluginEnabled());
    }



    private static void enableToolWindows(boolean enableToolWindows) {
        Project[] projects = ProjectManager.getInstance().getOpenProjects();
        for (Project project : projects) {
            SvgViewerProjectComponent pc = SvgViewerProjectComponent.getInstance(project);
            if (enableToolWindows) {
                pc.initToolWindow();
            }
            else {
                pc.unregisterToolWindow();
            }
        }
    }



    public void reset() {
        this._panel.setPluginEnabled(isPluginEnabled());
    }



    public void disposeUIResources() {
        this._panel = null;
    }



    public boolean isPluginEnabled() {
        return this.PLUGIN_ENABLED;
    }



    private void setPluginEnabled(boolean enabled) {
        this.PLUGIN_ENABLED = enabled;
    }



    public static Configuration getInstance() {
        return ApplicationManager.getApplication().getComponent(Configuration.class);
    }
}