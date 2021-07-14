package org.openstreetmap.josm.plugins.osmobjinfo;

import java.awt.GraphicsEnvironment;

import org.openstreetmap.josm.data.preferences.IntegerProperty;
import org.openstreetmap.josm.gui.MapFrame;
import org.openstreetmap.josm.plugins.Plugin;
import org.openstreetmap.josm.plugins.PluginInformation;

/**
 *
 * @author ruben
 */
public class OSMObjInfoPlugin extends Plugin {

    /** The maximum number of objects to allow to be used in group selections. */
    public static final IntegerProperty MAXIMUM_SELECTION = new IntegerProperty("osm-obj-info.maximum-selection", 5);

    protected static OSMObjInfotDialog objInfotDialog;

    public OSMObjInfoPlugin(PluginInformation info) {
        super(info);
    }

    @Override
    public void mapFrameInitialized(MapFrame oldFrame, MapFrame newFrame) {
        if (newFrame != null && !GraphicsEnvironment.isHeadless()) {
            newFrame.addToggleDialog(objInfotDialog = new OSMObjInfotDialog());
        }
    }
}
