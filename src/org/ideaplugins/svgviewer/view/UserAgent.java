package org.ideaplugins.svgviewer.view;

import com.intellij.openapi.diagnostic.Logger;
import org.apache.batik.swing.svg.SVGUserAgentGUIAdapter;

import javax.swing.*;
import java.util.Locale;

class UserAgent
extends SVGUserAgentGUIAdapter {


    private static final String USERAGENT_ERROR = "Error: ";
    private static final String USERAGENT_ALERT = "Alert: ";
    private static final String USERAGENT_PROMPT = "Prompt: ";
    private static final String USERAGENT_REPLY = "Reply: ";
    private static final String USERAGENT_CONFIRM = "Confirm: ";
    private static final String USERAGENT_OPEN_LINK = "Open link: ";
    private static final String USERAGENT_PARSER_CLASSNAME = "Parser: ";
    private static Logger _log;
    private SvgViewerPanel _viewer;



    public UserAgent(SvgViewerPanel parentComponent) {
        super(parentComponent);
        this._viewer = parentComponent;
        _log = Logger.getInstance(getClass().getName());
    }



    public void displayError(String message) {
        _log.info(message);
        this._viewer.addLogMessage(UserAgent.USERAGENT_ERROR + message);
    }



    public void displayError(Exception ex) {
        _log.info(ex);
        this._viewer.addLogMessage(UserAgent.USERAGENT_ERROR + ex.toString());
    }



    public void displayMessage(String message) {
        this._viewer.setStatus(message);
    }



    public void showAlert(String message) {
        this._viewer.setStatus(UserAgent.USERAGENT_ALERT + message);
        this._viewer.addLogMessage(UserAgent.USERAGENT_ALERT + message);
    }



    public String showPrompt(String message) {
        this._viewer.addLogMessage(UserAgent.USERAGENT_PROMPT + message);
        String reply = JOptionPane.showInputDialog(this.parentComponent, message, "Script Prompt", JOptionPane.QUESTION_MESSAGE);

        this._viewer.addLogMessage(UserAgent.USERAGENT_REPLY + reply);
        return reply;
    }



    public String showPrompt(String message, String defaultValue) {
        this._viewer.addLogMessage(UserAgent.USERAGENT_PROMPT + message + " [" + defaultValue + ']');
        String reply = (String) JOptionPane.showInputDialog(this.parentComponent,
                                                            message,
                                                            "Script Prompt",
                                                            JOptionPane.QUESTION_MESSAGE,
                                                            null,
                                                            null,
                                                            defaultValue);

        this._viewer.addLogMessage(UserAgent.USERAGENT_REPLY + reply);
        return reply;
    }



    public boolean showConfirm(String message) {
        this._viewer.addLogMessage(UserAgent.USERAGENT_CONFIRM + message);
        boolean reply = 0 == JOptionPane.showConfirmDialog(this.parentComponent,
                                                           message,
                                                           "Confirm",
                                                           JOptionPane.YES_NO_OPTION,
                                                           JOptionPane.QUESTION_MESSAGE);

        this._viewer.addLogMessage(UserAgent.USERAGENT_REPLY + Boolean.toString(reply));
        return reply;
    }



    public String getLanguages() {
        return Locale.getDefault().getLanguage();
    }



    public String getXMLParserClassName() {
        this._viewer.addLogMessage(UserAgent.USERAGENT_PARSER_CLASSNAME + super.getXMLParserClassName());
        return super.getXMLParserClassName();
    }



    public void openLink(String uri, boolean newc) {
        this._viewer.addLogMessage(UserAgent.USERAGENT_OPEN_LINK + uri + " (popup=" + newc + ')');
    }
}