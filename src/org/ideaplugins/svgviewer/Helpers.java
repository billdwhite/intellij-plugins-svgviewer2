package org.ideaplugins.svgviewer;

import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;

public class Helpers {


    public static final String ICON_TOOL_WINDOW = "/org/ideaplugins/svgviewer/resources/SvgViewerToolWindow13x13.png";
    private static final Icon DEFAULT_ICON = getDefaultIcon();



    public static Icon getIcon(String path) {
        URL url = Helpers.class.getResource(path);
        if (url == null) {
            try {
                url = new URL(path);
            }
            catch (MalformedURLException e) {
                return DEFAULT_ICON;
            }
        }

        Icon icon = new ImageIcon(url);
        if ((icon.getIconWidth() < 0) || (icon.getIconHeight() < 0)) {
            return DEFAULT_ICON;
        }

        return icon;
    }



    private static Icon getDefaultIcon() {
        BufferedImage bi = UIUtil.createImage(18, 18, 3);
        Graphics2D g2 = bi.createGraphics();
        g2.setBackground(Color.red);
        g2.clearRect(0, 0, bi.getWidth(), bi.getHeight());
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(2.0F));
        GeneralPath x = new GeneralPath();
        x.moveTo(0.0F, 0.0F);
        x.lineTo(bi.getWidth() - 1.0F, bi.getHeight() - 1.0F);
        x.moveTo(0.0F, bi.getHeight() - 1.0F);
        x.lineTo(bi.getWidth() - 1.0F, 0.0F);
        g2.draw(x);
        return new ImageIcon(bi);
    }



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