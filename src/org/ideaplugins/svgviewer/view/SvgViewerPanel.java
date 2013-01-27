package org.ideaplugins.svgviewer.view;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import org.ideaplugins.svgviewer.model.UserAgentListModel;

import javax.swing.*;
import java.awt.*;

public class SvgViewerPanel
extends JPanel
implements Runnable {


    private static final String SVGVIEWER_ACTION_GROUP = "SvgViewerActionGroup";
    private static final String SVGVIEWER_ACTION_TOOLBAR = "SvgViewerActionToolbar";
    private static final String ICON_REFRESH = "/actions/sync.png";
    private static final String ICON_PAUSE = "/actions/pause.png";
    private static final String ICON_STOP = "/actions/suspend.png";
    private JLabel _status = new JLabel();
    private UserAgentListModel _model = new UserAgentListModel();
    private JBList _messages = new JBList(this._model);
    private SvgViewerCanvas _canvas;
    private boolean _refreshEnabled = false;
    private boolean _paused = false;
    private boolean _pauseEnabled = false;
    private boolean _stopEnabled = false;



    public SvgViewerPanel() {
        buildGUI();
    }



    private void buildGUI() {
        setLayout(new BorderLayout());
        UserAgent _userAgent = new UserAgent(this);
        this._canvas = new SvgViewerCanvas(this, _userAgent);
        this._messages.setBorder(BorderFactory.createEmptyBorder());

        DefaultActionGroup actionGroup = new DefaultActionGroup(SvgViewerPanel.SVGVIEWER_ACTION_GROUP, false);
        actionGroup.add(new RefreshAction());
        actionGroup.add(new PauseAction());
        actionGroup.add(new StopAction());
        actionGroup.add(Separator.getInstance());
        ActionToolbar toolBar = getToolBar(actionGroup);
        JPanel controlBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 0));
        controlBar.add(toolBar.getComponent());
        controlBar.add(this._status);
        add(controlBar, "North");

        Splitter splitPane = new Splitter(true, 0.1F);
        splitPane.setFirstComponent(new JBScrollPane(this._messages));
        splitPane.setSecondComponent(this._canvas);
        add(splitPane, "Center");
    }



    private ActionToolbar getToolBar(DefaultActionGroup actionGroup) {
        return ActionManager.getInstance().createActionToolbar(SvgViewerPanel.SVGVIEWER_ACTION_TOOLBAR, actionGroup, true);
    }



    public SvgViewerCanvas getCanvas() {
        return this._canvas;
    }



    public void setStatus(String message) {
        this._status.setText(message);
    }



    public void run() {
        this._canvas.showSelectedFile();
    }



    public void addLogMessage(String message) {
        this._model.addMessage(message);
        this._messages.ensureIndexIsVisible(this._model.getSize() - 1);
    }



    public void clearLogMessages() {
        this._model.clear();
    }



    public boolean isRefreshEnabled() {
        return this._refreshEnabled;
    }



    public void setRefreshEnabled(boolean refreshEnabled) {
        this._refreshEnabled = refreshEnabled;
    }



    public boolean isPaused() {
        return this._paused;
    }



    public void setPaused(boolean paused) {
        this._paused = paused;
    }



    public boolean isPauseEnabled() {
        return this._pauseEnabled;
    }



    public void setPauseEnabled(boolean pauseEnabled) {
        this._pauseEnabled = pauseEnabled;
    }



    public boolean isStopEnabled() {
        return this._stopEnabled;
    }



    public void setStopEnabled(boolean stopEnabled) {
        this._stopEnabled = stopEnabled;
    }



    private class StopAction
    extends AnAction {


        private static final String STOP_TOOLTIP = "Stop";
        private static final String STOP_DESCRIPTION = "Stop rendering this image";



        public void actionPerformed(AnActionEvent e) {
            SvgViewerPanel.this._canvas.stopProcessing();
        }



        public void update(AnActionEvent event) {
            event.getPresentation().setEnabled(SvgViewerPanel.this.isStopEnabled());
        }



        public StopAction() {
            super(StopAction.STOP_TOOLTIP, StopAction.STOP_DESCRIPTION, IconLoader.getIcon(SvgViewerPanel.ICON_STOP));
        }
    }



    private class PauseAction
    extends ToggleAction {


        private static final String PAUSE_TOOLTIP = "Pause";
        private static final String PAUSE_DESCRIPTION = "Pause or resume the rendering of this image";



        public boolean isSelected(AnActionEvent event) {
            return SvgViewerPanel.this.isPaused();
        }



        public void setSelected(AnActionEvent event, boolean isSelected) {
            SvgViewerPanel.this.setPaused(isSelected);
            if (SvgViewerPanel.this.isPaused()) {
                SvgViewerPanel.this._canvas.suspendProcessing();
            }
            else {
                SvgViewerPanel.this._canvas.resumeProcessing();
            }
        }



        public void update(AnActionEvent event) {
            event.getPresentation().setEnabled(SvgViewerPanel.this.isPauseEnabled());
        }



        public PauseAction() {
            super(PauseAction.PAUSE_TOOLTIP,
                  PauseAction.PAUSE_DESCRIPTION,
                  IconLoader.getIcon(SvgViewerPanel.ICON_PAUSE));
        }
    }



    private class RefreshAction
    extends AnAction {


        private static final String RELOAD_TOOL_TIP = "Reload";
        private static final String RELOAD_DESCRIPTION = "Reload this image";



        public void actionPerformed(AnActionEvent e) {
            SvgViewerPanel.this.clearLogMessages();
            SvgViewerPanel.this._canvas.refresh();
        }



        public void update(AnActionEvent event) {
            event.getPresentation().setEnabled(SvgViewerPanel.this.isRefreshEnabled());
        }



        public RefreshAction() {
            super(RefreshAction.RELOAD_TOOL_TIP, RefreshAction.RELOAD_DESCRIPTION, IconLoader.getIcon(SvgViewerPanel.ICON_REFRESH));
        }
    }
}