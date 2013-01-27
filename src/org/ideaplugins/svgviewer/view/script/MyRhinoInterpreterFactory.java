package org.ideaplugins.svgviewer.view.script;

import org.apache.batik.script.Interpreter;
import org.apache.batik.script.InterpreterFactory;
import java.net.URL;

public class MyRhinoInterpreterFactory
implements InterpreterFactory {


    public Interpreter createInterpreter(URL documentURL) {
        return new MyRhinoInterpreter(documentURL);
    }



    @Override
    public String[] getMimeTypes() {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }



    @Override
    public Interpreter createInterpreter(URL url, boolean b) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}