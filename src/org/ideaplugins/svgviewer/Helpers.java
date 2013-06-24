package org.ideaplugins.svgviewer;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

public class Helpers {

    public static final Icon ICON_TOOL_WINDOW = IconLoader.getIcon("/org/ideaplugins/svgviewer/resources/SvgViewerToolWindow13x13.png");

    public static String getRFC1738CompliantUrl(String url) {
        if (!"file:///".equalsIgnoreCase(url.substring(0, 8))) {
            if ("file://".equalsIgnoreCase(url.substring(0, 7))) {
                if (url.charAt(8) == ':') {
                    url = "file:/" + url.substring(5);
                }
            }
        }
        return url;
    }
}