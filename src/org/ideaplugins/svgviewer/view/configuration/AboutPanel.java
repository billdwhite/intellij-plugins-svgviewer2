package org.ideaplugins.svgviewer.view.configuration;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Stack;

class AboutPanel
extends JPanel
implements HyperlinkListener {


    private static final Logger LOG = Logger.getInstance(AboutPanel.class.toString());
    private static final String URL_ABOUT = "/html/About.html";
    private static final String ICON_BACK = "/actions/back.png";
    private static final String SVGVIEWER_ABOUT_ACTION_GROUP = "SvgViewerAboutActionGroup";
    private static final String SVGVIEWER_ABOUT_ACTION_TOOLBAR = "SvgViewerAboutActionToolbar";
    private JLabel _status = new JLabel();
    private Stack _history = new Stack();
    private JEditorPane _pane = new JEditorPane();



    public AboutPanel() {
        super(new BorderLayout());
        buildGUI();
    }



    private void buildGUI() {
        this._pane.setEditable(false);
        this._pane.setEditorKit(new HTMLEditorKit());
        this._pane.addHyperlinkListener(this);

        DefaultActionGroup actionGroup = new DefaultActionGroup(AboutPanel.SVGVIEWER_ABOUT_ACTION_GROUP, false);
        actionGroup.add(new BackAction());
        actionGroup.add(Separator.getInstance());
        ActionToolbar toolBar = getToolBar(actionGroup);
        JPanel controlBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 0));
        controlBar.add(toolBar.getComponent());
        controlBar.add(this._status);
        add(controlBar, "North");

        add(new JBScrollPane(this._pane), "Center");
        try {
            this._status.setText(getClass().getResource(AboutPanel.URL_ABOUT).toString());
            this._pane.setPage(getClass().getResource(AboutPanel.URL_ABOUT));
        }
        catch (IOException ioe) {
            showIOException(ioe);
        }
    }



    private ActionToolbar getToolBar(DefaultActionGroup actionGroup) {
        return ActionManager.getInstance().createActionToolbar(AboutPanel.SVGVIEWER_ABOUT_ACTION_TOOLBAR, actionGroup, true);
    }



    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            JEditorPane pane = (JEditorPane) e.getSource();
            if ((e instanceof HTMLFrameHyperlinkEvent)) {
                HTMLDocument doc = (HTMLDocument) pane.getDocument();
                doc.processHTMLFrameHyperlinkEvent((HTMLFrameHyperlinkEvent) e);
            }
            else {
                try {
                    URL url = e.getURL();
                    this._status.setText(url.toString());
                    URL oldURL = pane.getPage();
                    pane.setPage(url);
                    pushUrl(oldURL);
                }
                catch (IOException ioe) {
                    showIOException(ioe);
                }
            }
        }
    }



    private void showIOException(IOException ioe) {
        this._status.setText(ioe.toString());
        LOG.info(ioe);
    }



    private void pushUrl(URL url) {
        if (url != null) {
            this._history.push(url);
        }
    }



    private URL popUrl() {
        URL url;
        if (!this._history.empty()) {
            url = (URL) this._history.pop();
        }
        else {
            url = null;
        }
        return url;
    }



    private class BackAction
    extends AnAction {


        private static final String BACK_TOOLTIP = "Back";
        private static final String BACK_DESCRIPTION = "Go back one page";



        public BackAction() {
            super(BackAction.BACK_TOOLTIP, BackAction.BACK_DESCRIPTION, IconLoader.getIcon(AboutPanel.ICON_BACK));
        }



        public void update(AnActionEvent event) {
            event.getPresentation().setEnabled(!AboutPanel.this._history.empty());
        }



        public void actionPerformed(AnActionEvent event) {
            try {
                URL url = AboutPanel.this.popUrl();
                if (url != null) {
                    AboutPanel.this._status.setText(url.toString());
                    AboutPanel.this._pane.setPage(url.toString());
                }
            }
            catch (IOException ioe) {
                AboutPanel.this.showIOException(ioe);
            }
        }
    }
}