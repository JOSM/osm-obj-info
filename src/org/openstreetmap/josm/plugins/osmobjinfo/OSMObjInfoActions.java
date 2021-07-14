package org.openstreetmap.josm.plugins.osmobjinfo;

import static org.openstreetmap.josm.tools.I18n.marktr;
import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.JOptionPane;

import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.Notification;
import org.openstreetmap.josm.io.remotecontrol.RemoteControl;
import org.openstreetmap.josm.io.remotecontrol.handler.LoadAndZoomHandler;
import org.openstreetmap.josm.plugins.osmobjinfo.OSMObjInfotDialog.AllOsmObjInfo;
import org.openstreetmap.josm.spi.preferences.Config;
import org.openstreetmap.josm.tools.OpenBrowser;

/**
 *
 * @author ruben
 */
public class OSMObjInfoActions {
    public static final String COPY = marktr("Copy: {0}");

    public static void copyUser(String user) {
        if (!user.isEmpty()) {
            String linkUser = Config.getUrls().getBaseUserUrl().concat("/").concat(user);
            StringSelection selection = new StringSelection(linkUser);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            new Notification(tr(COPY, linkUser)).setIcon(JOptionPane.INFORMATION_MESSAGE).setDuration(Notification.TIME_SHORT).show();
        }
    }

    public static void openinBrowserUser(String user) {
        if (!user.isEmpty()) {
            openInBrowser(Config.getUrls().getBaseUserUrl().concat("/").concat(user));
        }

    }

    public static void openinBrowserUserNeis(String user) {
        if (!user.isEmpty()) {
            openInBrowser("https://hdyc.neis-one.org/?".concat(user));
        }
    }

    static void openinBrowserUserOsmComments(String user) {
        if (!user.isEmpty()) {
            openInBrowser("https://resultmaps.neis-one.org/osm-discussion-comments?commented&user=".concat(user));
        }
    }

    public static void copyChangeset(String idChangeset) {
        if (!idChangeset.isEmpty()) {
            String linkchangeset = Config.getUrls().getBaseBrowseUrl().concat("/changeset/").concat(idChangeset);
            StringSelection selection = new StringSelection(linkchangeset);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            new Notification(tr(COPY, linkchangeset)).setIcon(JOptionPane.INFORMATION_MESSAGE).setDuration(Notification.TIME_SHORT).show();
        }
    }

    public static void openinBrowserChangeset(String idChangeset) {
        if (!idChangeset.isEmpty()) {
            openInBrowser(Config.getUrls().getBaseBrowseUrl().concat("/changeset/").concat(idChangeset));
        }
    }

    public static void openinBrowserChangesetMap(String idChangeset) {
        if (!idChangeset.isEmpty()) {
            openInBrowser("https://osmcha.org/".concat(idChangeset));
        }
    }

    public static void copyIdobj(List<AllOsmObjInfo> osmObjInfo) {
        if (osmObjInfo == null || osmObjInfo.isEmpty()) return;
        String linkobjid = "";
        StringBuilder builder = new StringBuilder();
        for (AllOsmObjInfo object : osmObjInfo) {
            builder.append(Config.getUrls().getBaseBrowseUrl()).append('/')
                .append(object.typeObj).append('/').append(object.idObject);
            if (osmObjInfo.indexOf(object) < osmObjInfo.size() - 2) {
                builder.append(", ");
            }
            if (osmObjInfo.indexOf(object) == osmObjInfo.size() - 2) {
                if (osmObjInfo.size() == 2) {
                    builder.append(" ");
                } else {
                    builder.append(", ");
                }
                builder.append(tr("and"));
                builder.append(" ");
            }
        }
        linkobjid = builder.toString();
        StringSelection selection = new StringSelection(linkobjid);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        new Notification(tr(COPY, linkobjid)).setIcon(JOptionPane.INFORMATION_MESSAGE).setDuration(Notification.TIME_SHORT).show();
    }

    public static void copyRemoteControl(List<AllOsmObjInfo> osmObjInfo) {
        if (osmObjInfo == null)
            return;
        StringBuilder builder = new StringBuilder();
        try {
            builder.append("http://");
            builder.append(RemoteControl.getInet4Address().getHostAddress());
            builder.append(":");
            builder.append(Config.getPref().getInt("remote.control.port", 8111));
        } catch (UnknownHostException e) {
            // Append the default remote control address (this gets thrown at RemoteControl#getInet4Address)
            builder.append("127.0.0.1:8111");
        }
        builder.append("/").append(LoadAndZoomHandler.command).append("?");
        if (!osmObjInfo.isEmpty())
            builder.append("select=");
        for (int i = 0; i < osmObjInfo.size(); i++) {
            AllOsmObjInfo object = osmObjInfo.get(i);
            builder.append(object.typeObj);
            builder.append(object.idObject);
            if (i < osmObjInfo.size() - 1)
                builder.append(',');
        }
        Bounds viewPortBound = MainApplication.getMap().mapView
                .getLatLonBounds(MainApplication.getMap().mapView.getVisibleRect());
        // We don't need to check that the length is > 0, as we have the remote control URL
        if (builder.charAt(builder.length() - 1) != '?') {
            builder.append('&');
        }
        builder.append("left=").append(viewPortBound.getMinLon()).append("&right=").append(viewPortBound.getMaxLon())
                .append("&top=").append(viewPortBound.getMaxLat()).append("&bottom=").append(viewPortBound.getMinLat());
        String remoteControl = builder.toString();
        StringSelection selection = new StringSelection(remoteControl);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        new Notification(tr(COPY, remoteControl)).setIcon(JOptionPane.INFORMATION_MESSAGE)
          .setDuration(Notification.TIME_SHORT).show();
    }

    public static void openinBrowserIdobj(List<AllOsmObjInfo> osmObjInfo) {
        if (osmObjInfo.size() > OSMObjInfoPlugin.MAXIMUM_SELECTION.get() || osmObjInfo.isEmpty())
            return;
        for (AllOsmObjInfo info : osmObjInfo) {
            if (info.typeObj != null && !info.idObject.isEmpty()) {
                openInBrowser(Config.getUrls().getBaseBrowseUrl().concat("/").concat(info.typeObj).concat("/").concat(info.idObject));
            }
        }
    }

    public static void openinBrowserIdobjOsmDeepHistory(List<AllOsmObjInfo> osmObjInfo) {
        if (osmObjInfo.size() > OSMObjInfoPlugin.MAXIMUM_SELECTION.get() || osmObjInfo.isEmpty())
            return;
        for (AllOsmObjInfo info : osmObjInfo) {
            if (info.typeObj != null && !info.idObject.isEmpty()) {
                openInBrowser("http://osmlab.github.io/osm-deep-history/#/" + info.typeObj + "/" + info.idObject);
            }
        }
    }

    public static void openinBrowserMapillary(String coords) {
        if (coords == null || coords.isEmpty()) return;
        String[] arrCoords = coords.split(",");
        openInBrowser("https://www.mapillary.com/app/?lat=" + arrCoords[0] + "&lng=" + arrCoords[1] + "&z=20&focus=map&dateFrom=2017-01-01");
    }

    public static void openinBrowserOpenstreetcam(String coords) {
        if (coords == null || coords.isEmpty()) return;
        String[] arrCoords = coords.split(",");
        openInBrowser("https://openstreetcam.org/map/@" + arrCoords[0] + "," + arrCoords[1] + ",18z");
    }

    public static void openInBrowserYandex(String coords) {
        if (coords == null || coords.isEmpty()) return;
        String[] arrCoords = coords.split(",");
        openInBrowser("https://yandex.com/maps/?l=stv,sta&ll=" + arrCoords[1] + "," + arrCoords[0] + "&z=18");
    }

    private static void openInBrowser(String url) {
        new Notification(tr("Open in browser {0}", url)).setIcon(JOptionPane.INFORMATION_MESSAGE).setDuration(Notification.TIME_SHORT).show();
        OpenBrowser.displayUrl(url);
    }
}
