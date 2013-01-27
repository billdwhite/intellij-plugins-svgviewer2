package org.ideaplugins.svgviewer.controller.project;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import org.ideaplugins.svgviewer.Helpers;
import org.ideaplugins.svgviewer.controller.application.Configuration;
import org.ideaplugins.svgviewer.view.SvgViewerPanel;
import org.jetbrains.annotations.NotNull;

public class SvgViewerProjectComponent
implements ProjectComponent {


    public static final String ID_TOOL_WINDOW = "SvgViewer";
    private Project _project;
    private FileListener _fileListener;
    private EditorListener _editorListener;
    private SvgViewerPanel _viewerPanel;
    private static final String PROJECT_COMPONENT_NAME = "ProjectComponent";



    public SvgViewerProjectComponent(Project project) {
        this._project = project;
    }



    @NotNull
    public String getComponentName() {
        return SvgViewerProjectComponent.ID_TOOL_WINDOW + "." + SvgViewerProjectComponent.PROJECT_COMPONENT_NAME;
    }



    public void initComponent() {
    }



    public void projectOpened() {
        if (Configuration.getInstance().isPluginEnabled()) {
            initToolWindow();
        }
    }



    public void projectClosed() {
        unregisterToolWindow();
    }



    public void disposeComponent() {
    }



    public void initToolWindow() {
        this._viewerPanel = new SvgViewerPanel();

        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(this._project);
        ToolWindow toolWindow = toolWindowManager.registerToolWindow(SvgViewerProjectComponent.ID_TOOL_WINDOW, getViewerPanel(), ToolWindowAnchor.RIGHT);

        toolWindow.setIcon(Helpers.getIcon(Helpers.ICON_TOOL_WINDOW));
        getViewerPanel().getCanvas().setToolWindow(toolWindow);

        this._editorListener = new EditorListener(getViewerPanel());
        FileEditorManager.getInstance(this._project).addFileEditorManagerListener(this._editorListener);

        this._fileListener = new FileListener(getViewerPanel());
        this._project.getProjectFile().getFileSystem().addVirtualFileListener(this._fileListener);
    }



    public void unregisterToolWindow() {
        if (this._editorListener != null) {
            FileEditorManager.getInstance(this._project).removeFileEditorManagerListener(this._editorListener);
        }
        if (this._fileListener != null) {
            this._project.getProjectFile().getFileSystem().removeVirtualFileListener(this._fileListener);
        }
        if (isToolWindowRegistered()) {
            ToolWindowManager.getInstance(this._project).unregisterToolWindow(SvgViewerProjectComponent.ID_TOOL_WINDOW);
        }
    }



    private boolean isToolWindowRegistered() {
        return ToolWindowManager.getInstance(this._project).getToolWindow(SvgViewerProjectComponent.ID_TOOL_WINDOW) != null;
    }



    public static SvgViewerProjectComponent getInstance(Project project) {
        return project.getComponent(SvgViewerProjectComponent.class);
    }



    public SvgViewerPanel getViewerPanel() {
        return this._viewerPanel;
    }
}