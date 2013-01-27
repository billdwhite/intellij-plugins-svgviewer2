package org.ideaplugins.svgviewer.controller.project;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.vfs.VirtualFile;
import org.ideaplugins.svgviewer.view.SupportedExtensions;
import org.ideaplugins.svgviewer.view.SvgViewerPanel;

class EditorListener
implements FileEditorManagerListener {


    private SvgViewerPanel _viewer;



    public EditorListener(SvgViewerPanel viewer) {
        this._viewer = viewer;
    }



    public void fileOpened(FileEditorManager fileEditorManager, VirtualFile virtualFile) {
    }



    public void fileClosed(FileEditorManager fileEditorManager, VirtualFile virtualFile) {
    }



    public void selectionChanged(FileEditorManagerEvent event) {
        if (event.getNewFile() == null) {
            return;
        }
        if (SupportedExtensions.isSupported(event.getNewFile().getExtension())) {
            this._viewer.getCanvas().setFile(event.getNewFile());
            this._viewer.getCanvas().showSelectedFile();
        }
    }
}