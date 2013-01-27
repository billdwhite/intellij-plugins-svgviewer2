package org.ideaplugins.svgviewer.controller.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.ideaplugins.svgviewer.controller.project.SvgViewerProjectComponent;
import org.ideaplugins.svgviewer.view.SupportedExtensions;
import org.ideaplugins.svgviewer.view.SvgViewerPanel;

public class SVGViewerAction
extends AnAction {


    public void update(AnActionEvent event) {
        Presentation presentation = event.getPresentation();
        Project project = getProject(event);
        if (project == null) {
            hideThisAction(presentation);
            return;
        }
        ToolWindow toolWindow = getToolWindow(project);
        if (toolWindow == null) {
            hideThisAction(presentation);
            return;
        }
        VirtualFile file = getFile(event);
        if ((file == null) || (!SupportedExtensions.isSupported(file.getExtension()))) {
            hideThisAction(presentation);
            return;
        }
        presentation.setEnabled(toolWindow.isAvailable());
        presentation.setVisible(true);
    }



    private void hideThisAction(Presentation presentation) {
        presentation.setEnabled(false);
        presentation.setVisible(false);
    }



    private ToolWindow getToolWindow(Project project) {
        return ToolWindowManager.getInstance(project).getToolWindow("SvgViewer");
    }



    private Project getProject(AnActionEvent event) {
        return (Project) event.getDataContext().getData("project");
    }



    private VirtualFile getFile(AnActionEvent event) {
        return (VirtualFile) event.getDataContext().getData("virtualFile");
    }



    public void actionPerformed(AnActionEvent event) {
        Project project = getProject(event);
        SvgViewerPanel viewer = SvgViewerProjectComponent.getInstance(project).getViewerPanel();

        VirtualFile file = getFile(event);
        viewer.getCanvas().setFile(file);

        ToolWindow toolWindow = getToolWindow(project);
        toolWindow.activate(viewer);
    }
}