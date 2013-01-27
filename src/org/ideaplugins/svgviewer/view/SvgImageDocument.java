package org.ideaplugins.svgviewer.view;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.ideaplugins.svgviewer.Helpers;
import org.w3c.dom.svg.SVGDocument;

import javax.swing.*;
import java.io.IOException;
import java.io.StringReader;

class SvgImageDocument {


    private static final Logger LOG = Logger.getInstance(SvgImageDocument.class.getName());
    private static final String SVG_PROLOG = "<?xml version='1.0' standalone='no'?>" +
                                             "<!DOCTYPE svg PUBLIC '-//W3C//DTD SVG 1.1//EN' 'http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd'>" +
                                             "<svg xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink' ";
    private static final String SVG_MIDDLE_BIT = "'><title>Image</title><g><image style='image-rendering:auto' x='0' y='0' ";
    private static final String SVG_EPILOG = "'/></g></svg>";
    private SAXSVGDocumentFactory _saxFactory;



    public SvgImageDocument(SAXSVGDocumentFactory factory) {
        this._saxFactory = factory;
    }



    private static void appendWidthAndHeight(StringBuffer sb, int width, int height) {
        sb.append(" width='");
        sb.append(width);
        sb.append("' height='");
        sb.append(height);
        sb.append("' ");
    }



    public SVGDocument getDocument(VirtualFile image)
    throws IOException {
        ImageIcon img = new ImageIcon(image.getPath());
        StringBuffer sb = new StringBuffer(512);
        sb.append(SvgImageDocument.SVG_PROLOG);
        appendWidthAndHeight(sb, img.getIconWidth(), img.getIconHeight());
        sb.append("viewbox='0 0 ");
        sb.append(img.getIconWidth() - 1);
        sb.append(" ");
        sb.append(img.getIconHeight() - 1);
        sb.append(SvgImageDocument.SVG_MIDDLE_BIT);
        appendWidthAndHeight(sb, img.getIconWidth(), img.getIconHeight());
        sb.append("xlink:href='");
        String url = Helpers.getRFC1738CompliantUrl(image.getUrl());

        sb.append(url);
        sb.append(SvgImageDocument.SVG_EPILOG);
        return this._saxFactory.createSVGDocument("http:internal", new StringReader(sb.toString()));
    }
}