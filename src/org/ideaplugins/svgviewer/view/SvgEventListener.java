package org.ideaplugins.svgviewer.view;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.UpdateManager;
import org.apache.batik.bridge.UpdateManagerEvent;
import org.apache.batik.bridge.UpdateManagerListener;
import org.apache.batik.script.InterpreterPool;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.gvt.GVTTreeRendererListener;
import org.apache.batik.swing.svg.*;
import org.ideaplugins.svgviewer.view.script.MyRhinoInterpreterFactory;

class SvgEventListener
implements SVGDocumentLoaderListener,
           GVTTreeBuilderListener,
           GVTTreeRendererListener,
           UpdateManagerListener,
           SVGLoadEventDispatcherListener,
           LinkActivationListener {


    private static final String STATUS_LOADING = "Loading...";
    private static final String STATUS_LOAD_CANCELLED = "Load cancelled.";
    private static final String STATUS_LOAD_FAILED = "Load failed.";
    private static final String STATUS_LOAD_COMPLETE = "Loaded ";
    private static final String STATUS_BUILDING = "Building GVT tree...";
    private static final String STATUS_BUILD_CANCELLED = "Build cancelled.";
    private static final String STATUS_BUILD_FAILED = "Build failed.";
    private static final String STATUS_BUILD_COMPLETE = "Build complete.";
    private static final String STATUS_PREPARING_TO_RENDER = "Preparing to render.";
    private static final String STATUS_RENDERING_STARTED = "Rendering started...";
    private static final String STATUS_RENDERING_CANCELLED = "Rendering cancelled.";
    private static final String STATUS_RENDERING_FAILED = "Rendering failed.";
    private static final String STATUS_RENDERING_COMPLETE = "Rendering complete.";
    private static final String STATUS_SCRIPT_LOADING = "Script loading...";
    private static final String STATUS_SCRIPT_LOADED = "Script loaded.";
    private static final String STATUS_SCRIPT_LOADING_CANCELLED = "Script loading cancelled.";
    private static final String STATUS_SCRIPT_LOADING_FAILED = "Script loading failed.";
    private static final String STATUS_ANIMATION_STARTED = "Animation started.";
    private static final String STATUS_ANIMATION_PAUSED = "Animation paused.";
    private static final String STATUS_ANIMATION_RESUMED = "Animation resumed.";
    private static final String STATUS_UPDATE_STARTED = "";
    private static final String STATUS_UPDATE_FAILED = "Update failed.";
    private static final String STATUS_ANIMATION_STOPPED = "Animation stopped.";
    private static final String STATUS_LINK_ACTIVATED = "Activating link. ";
    private SvgViewerPanel _viewer;



    public SvgEventListener(SvgViewerPanel viewer) {
        this._viewer = viewer;
    }



    private void enableRefreshButton(boolean enabled) {
        this._viewer.setRefreshEnabled(enabled);
    }



    private void enablePauseButton(boolean enabled) {
        this._viewer.setPauseEnabled(enabled);
    }



    private void enableStopButton(boolean enabled) {
        this._viewer.setStopEnabled(enabled);
    }


    private void enableBackgroundButton(boolean enabled) {
        this._viewer.setRefreshEnabled(enabled);
    }



    private void showMessage(String status) {
        this._viewer.setStatus(status);
        this._viewer.addLogMessage(status);
    }



    private void showMessage(String status, String logMessage) {
        this._viewer.setStatus(status);
        this._viewer.addLogMessage(logMessage);
    }



    public void documentLoadingStarted(SVGDocumentLoaderEvent e) {
        showMessage(SvgEventListener.STATUS_LOADING);
        enableRefreshButton(false);
        enablePauseButton(false);
        enableStopButton(true);
        enableBackgroundButton(false);
    }



    public void documentLoadingCancelled(SVGDocumentLoaderEvent e) {
        showMessage(SvgEventListener.STATUS_LOAD_CANCELLED);
        enableRefreshButton(true);
        enableStopButton(false);
        enableBackgroundButton(false);
    }



    public void documentLoadingFailed(SVGDocumentLoaderEvent e) {
        showMessage(SvgEventListener.STATUS_LOAD_FAILED);
        enableRefreshButton(true);
        enableStopButton(false);
        enableBackgroundButton(false);
    }



    public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {
        showMessage(SvgEventListener.STATUS_LOAD_COMPLETE, SvgEventListener.STATUS_LOAD_COMPLETE + e.getSVGDocument().getURL());
        this._viewer.getCanvas().updateTitle();
        enableBackgroundButton(true);
    }



    public void gvtBuildStarted(GVTTreeBuilderEvent e) {
        showMessage(SvgEventListener.STATUS_BUILDING);
        enableStopButton(true);
        enableBackgroundButton(false);
    }



    public void gvtBuildCancelled(GVTTreeBuilderEvent e) {
        showMessage(SvgEventListener.STATUS_BUILD_CANCELLED);
        enableRefreshButton(true);
        enableStopButton(false);
        enableBackgroundButton(false);
    }



    public void gvtBuildFailed(GVTTreeBuilderEvent e) {
        showMessage(SvgEventListener.STATUS_BUILD_FAILED);
        enableRefreshButton(true);
        enableStopButton(false);
        enableBackgroundButton(false);
    }



    public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
        showMessage(SvgEventListener.STATUS_BUILD_COMPLETE);
        SvgViewerCanvas canvas = this._viewer.getCanvas();
        if (canvas.isDynamic()) {
            UpdateManager updateManager = canvas.getUpdateManager();
            BridgeContext bridgeContext = updateManager.getBridgeContext();
            InterpreterPool pool = bridgeContext.getInterpreterPool();
            pool.putInterpreterFactory("text/ecmascript", new MyRhinoInterpreterFactory());
        }
    }



    public void gvtRenderingPrepare(GVTTreeRendererEvent e) {
        this._viewer.setStatus(SvgEventListener.STATUS_PREPARING_TO_RENDER);
        enableRefreshButton(true);
    }



    public void gvtRenderingStarted(GVTTreeRendererEvent e) {
        this._viewer.setStatus(SvgEventListener.STATUS_RENDERING_STARTED);
    }



    public void gvtRenderingCancelled(GVTTreeRendererEvent e) {
        this._viewer.setStatus(SvgEventListener.STATUS_RENDERING_CANCELLED);
        enableStopButton(false);
    }



    public void gvtRenderingFailed(GVTTreeRendererEvent e) {
        showMessage(SvgEventListener.STATUS_RENDERING_FAILED);
    }



    public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
        this._viewer.setStatus(SvgEventListener.STATUS_RENDERING_COMPLETE);
        if (!this._viewer.getCanvas().isDynamic()) {
            enableStopButton(false);
        }
    }



    public void svgLoadEventDispatchStarted(SVGLoadEventDispatcherEvent e) {
        showMessage(SvgEventListener.STATUS_SCRIPT_LOADING);
        enableStopButton(true);
    }



    public void svgLoadEventDispatchCompleted(SVGLoadEventDispatcherEvent e) {
        showMessage(SvgEventListener.STATUS_SCRIPT_LOADED);
        enableStopButton(false);
    }



    public void svgLoadEventDispatchCancelled(SVGLoadEventDispatcherEvent e) {
        showMessage(SvgEventListener.STATUS_SCRIPT_LOADING_CANCELLED);
        enableStopButton(false);
    }



    public void svgLoadEventDispatchFailed(SVGLoadEventDispatcherEvent e) {
        showMessage(SvgEventListener.STATUS_SCRIPT_LOADING_FAILED);
        enableStopButton(false);
    }



    public void managerStarted(UpdateManagerEvent e) {
        if (this._viewer.getCanvas().isDynamic()) {
            if (this._viewer.isPaused()) {
                this._viewer.getCanvas().suspendProcessing();
            }
            else {
                showMessage(SvgEventListener.STATUS_ANIMATION_STARTED);
                enablePauseButton(true);
                enableStopButton(true);
                enableBackgroundButton(false);
            }
        }
    }



    public void managerSuspended(UpdateManagerEvent e) {
        if (this._viewer.getCanvas().isDynamic()) {
            showMessage(SvgEventListener.STATUS_ANIMATION_PAUSED);
            enablePauseButton(true);
            enableStopButton(true);
            enableBackgroundButton(false);
        }
    }



    public void managerResumed(UpdateManagerEvent e) {
        if (this._viewer.getCanvas().isDynamic()) {
            showMessage(SvgEventListener.STATUS_ANIMATION_RESUMED);
            enablePauseButton(true);
            enableStopButton(true);
            enableBackgroundButton(false);
        }
    }



    public void updateStarted(UpdateManagerEvent e) {
        this._viewer.setStatus(SvgEventListener.STATUS_UPDATE_STARTED);
    }



    public void updateFailed(UpdateManagerEvent e) {
        showMessage(SvgEventListener.STATUS_UPDATE_FAILED);
        enablePauseButton(false);
        enableStopButton(false);
        enableBackgroundButton(false);
    }



    public void updateCompleted(UpdateManagerEvent e) {
    }



    public void managerStopped(UpdateManagerEvent e) {
        if (this._viewer.getCanvas().isDynamic()) {
            showMessage(SvgEventListener.STATUS_ANIMATION_STOPPED);
            enablePauseButton(false);
            enableStopButton(false);
            enableBackgroundButton(false);
        }
    }



    public void linkActivated(LinkActivationEvent e) {
        showMessage(SvgEventListener.STATUS_LINK_ACTIVATED, SvgEventListener.STATUS_LINK_ACTIVATED + e.getReferencedURI());
        this._viewer.getCanvas().setURI(e.getReferencedURI());
    }
}