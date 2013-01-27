package org.ideaplugins.svgviewer.view;

import java.util.HashSet;
import java.util.Set;

public class SupportedExtensions {


    private static Set<String> _fileTypes = new HashSet<String>();



    public static boolean isSupported(String extension) {
        return (extension != null) && (_fileTypes.contains(extension.toLowerCase()));
    }



    static {
        _fileTypes.add("svg");
        _fileTypes.add("svgz");
        _fileTypes.add("png");
        _fileTypes.add("jpg");
        _fileTypes.add("gif");
        _fileTypes.add("tif");
    }
}