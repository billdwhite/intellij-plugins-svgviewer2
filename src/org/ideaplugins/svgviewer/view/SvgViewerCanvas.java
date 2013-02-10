package org.ideaplugins.svgviewer.view;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.util.XMLResourceDescriptor;
import org.ideaplugins.svgviewer.Helpers;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.io.IOException;

public class SvgViewerCanvas
extends JSVGCanvas {

    private static final Logger LOG = Logger.getInstance(SvgViewerCanvas.class.getName());
    private static final int BACKGROUND_LIGHT  = 1;
    private static final int BACKGROUND_MEDIUM = 2;
    private static final int BACKGROUND_DARK   = 3;
    private static final Color COLOR_LIGHT  = Color.decode("#F3F3F3");
    private static final Color COLOR_MEDIUM = Color.LIGHT_GRAY;
    private static final Color COLOR_DARK   = Color.DARK_GRAY;

    private VirtualFile _file;
    private ToolWindow _toolWindow;
    private SvgViewerPanel _viewer;
    private SvgImageDocument _svgImageDoc;
    private int _currentBackground;
    private Color _currentBackgroundColor;


    public SvgViewerCanvas(SvgViewerPanel viewer, UserAgent useragent) {
        super(useragent, true, true);
        this.toggleBackgroundColor();
        this._viewer = viewer;
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        this._svgImageDoc = new SvgImageDocument(new SAXSVGDocumentFactory(parser));
        addListeners();
    }


    private void addListeners() {
        SvgEventListener tracker = new SvgEventListener(this._viewer);
        addGVTTreeBuilderListener(tracker);
        addGVTTreeRendererListener(tracker);
        addUpdateManagerListener(tracker);
        addSVGDocumentLoaderListener(tracker);
        addSVGLoadEventDispatcherListener(tracker);
        addLinkActivationListener(tracker);
    }


    public void setToolWindow(ToolWindow toolWindow) {
        this._toolWindow = toolWindow;
    }


    public void setFile(VirtualFile file) {
        this._file = file;
    }



    public VirtualFile getFile() {
        return this._file;
    }


    public void updateTitle() {
        if ((getSVGDocument() == null) || (getSVGDocument().getTitle().trim().length() == 0)) {
            this._toolWindow.setTitle("[" + this._file.getName() + "]");
        }
        else {
            this._toolWindow.setTitle("[" + this._file.getName() + "] - " + getSVGDocument().getTitle());
        }
    }


    public void showSelectedFile() {
        if (this._toolWindow == null) {
            return;
        }
        this._viewer.clearLogMessages();

        if (("svg".equalsIgnoreCase(this._file.getExtension())) || ("svgz".equalsIgnoreCase(this._file.getExtension()))) {
            String url = Helpers.getRFC1738CompliantUrl(this._file.getUrl());
            setURI(url);
            updateTitle();
            computeRenderingTransform();
        }
        else {
            try {
                setSVGDocument(this._svgImageDoc.getDocument(this._file));
                updateTitle();
                computeRenderingTransform();
            }
            catch (IOException e) {
                this._viewer.setStatus(e.toString());
                setURI(null);
            }
        }
    }


    public void refresh() {
        setSVGDocument(getSVGDocument());
    }


    public void toggleBackgroundColor() {
        switch (this._currentBackground) {
            case BACKGROUND_LIGHT:
                this._currentBackground = BACKGROUND_MEDIUM;
                this._currentBackgroundColor = COLOR_MEDIUM;
                break;
            case BACKGROUND_MEDIUM:
                this._currentBackground = BACKGROUND_DARK;
                this._currentBackgroundColor = COLOR_DARK;
                break;
            case BACKGROUND_DARK:
            default:
                this._currentBackground = BACKGROUND_LIGHT;
                this._currentBackgroundColor = COLOR_LIGHT;
                break;
        }
        this.setBackground(this._currentBackgroundColor);
    }


    protected boolean computeRenderingTransform() {
        double x;
        double y;
        try {
            Rectangle paneSize = getBounds();
            if (paneSize.isEmpty()) {
                return false;
            }
            Dimension2D imageSize = getSVGDocumentSize();

            x = paneSize.getWidth() / imageSize.getWidth();
            y = paneSize.getHeight() / imageSize.getHeight();

            if ((x > 1.0D) && (y > 1.0D)) {
                x = 1.0D;
                y = 1.0D;
            }
            else {
                x = Math.min(x, y);
                y = Math.min(x, y);
            }
        }
        catch (NullPointerException npe) {
            x = 1.0D;
            y = 1.0D;
        }
        this.initialTransform = AffineTransform.getScaleInstance(x, y);
        setRenderingTransform(this.initialTransform);
        return true;
    }
}