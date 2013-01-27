package org.ideaplugins.svgviewer.view.script;

import org.apache.batik.script.rhino.RhinoClassLoader;

import java.net.URL;

class MyRhinoClassLoader
extends RhinoClassLoader {


    public MyRhinoClassLoader(URL docURL, ClassLoader loader) {
        super(docURL, loader);
    }



    protected synchronized Class loadClass(String name, boolean resolve)
    throws ClassNotFoundException {
        Class clazz = findLoadedClass(name);
        if (clazz == null) {
            ClassLoader loader = RhinoClassLoader.class.getClassLoader();

            if (loader == null) {
                clazz = findSystemClass(name);
            }
            else {
                clazz = loader.loadClass(name);
            }
        }
        if (resolve) {
            resolveClass(clazz);
        }
        return clazz;
    }
}