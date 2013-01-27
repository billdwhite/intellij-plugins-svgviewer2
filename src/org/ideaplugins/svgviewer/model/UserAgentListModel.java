package org.ideaplugins.svgviewer.model;

import javax.swing.*;

public class UserAgentListModel
extends DefaultListModel {


    public void addMessage(String message) {
        add(getSize(), message);
    }
}