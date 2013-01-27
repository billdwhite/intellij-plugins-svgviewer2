package org.ideaplugins.svgviewer.view.script;

import org.apache.batik.script.rhino.RhinoInterpreter;
import java.net.URL;

class MyRhinoInterpreter
extends RhinoInterpreter {

    public MyRhinoInterpreter(URL documentURL) {
        super(documentURL);
        this.rhinoClassLoader = new MyRhinoClassLoader(documentURL, getClass().getClassLoader());
    }
}