package org.ideaplugins.svgviewer.controller.project;

import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileListener;
import org.ideaplugins.svgviewer.view.SvgViewerPanel;

class FileListener
implements VirtualFileListener {


    private SvgViewerPanel _viewer;



    public FileListener(SvgViewerPanel viewer) {
        this._viewer = viewer;
    }



    public void contentsChanged(VirtualFileEvent event) {
        if (event.getFile().equals(this._viewer.getCanvas().getFile())) {
            this._viewer.getCanvas().showSelectedFile();
        }
    }
}