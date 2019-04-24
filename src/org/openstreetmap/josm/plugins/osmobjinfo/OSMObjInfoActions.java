package org.openstreetmap.josm.plugins.osmobjinfo;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;

import javax.swing.JOptionPane;
import org.openstreetmap.josm.gui.Notification;
import org.openstreetmap.josm.plugins.osmobjinfo.OSMObjInfotDialog.AllOsmObjInfo;

import static org.openstreetmap.josm.tools.I18n.tr;
import org.openstreetmap.josm.tools.OpenBrowser;

/**
 *
 * @author ruben
 */
public class OSMObjInfoActions {
    public static final String COPY = "Copy: {0}";
    public static final String OPEN_IN_BROWSER = "Open in browser {0}";

    public static void copyUser(String user) {
        if (!user.isEmpty()) {
            String linkUser = "http://www.openstreetmap.org/user/".concat(user);
            StringSelection selection = new StringSelection(linkUser);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            new Notification(tr(COPY, linkUser)).setIcon(JOptionPane.INFORMATION_MESSAGE).setDuration(Notification.TIME_SHORT).show();
        }
    }

    public static void openinBrowserUser(String user) {
        if (!user.isEmpty()) {
            String url = "http://www.openstreetmap.org/user/".concat(user);
            new Notification(tr(OPEN_IN_BROWSER, url)).setIcon(JOptionPane.INFORMATION_MESSAGE).setDuration(Notification.TIME_SHORT).show();
            OpenBrowser.displayUrl(url);
        }

    }

    public static void openinBrowserUserNeis(String user) {
        if (!user.isEmpty()) {
            String url = "http://hdyc.neis-one.org/?".concat(user);
            new Notification(tr(OPEN_IN_BROWSER, url)).setIcon(JOptionPane.INFORMATION_MESSAGE).setDuration(Notification.TIME_SHORT).show();
            OpenBrowser.displayUrl(url);
        }
    }

    static void openinBrowserUserOsmComments(String user) {
        if (!user.isEmpty()) {
            String url = "https://www.mapbox.com/osm-comments/#/changesets/?q=users:".concat(user);
            new Notification(tr(OPEN_IN_BROWSER, url)).setIcon(JOptionPane.INFORMATION_MESSAGE).setDuration(Notification.TIME_SHORT).show();
            OpenBrowser.displayUrl(url);
        }
    }

    public static void copyChangeset(String idChangeset) {
        if (!idChangeset.isEmpty()) {
            String linkchangeset = "https://www.openstreetmap.org/changeset/".concat(idChangeset);
            StringSelection selection = new StringSelection(linkchangeset);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            new Notification(tr(COPY, linkchangeset)).setIcon(JOptionPane.INFORMATION_MESSAGE).setDuration(Notification.TIME_SHORT).show();
        }
    }

    public static void openinBrowserChangeset(String idChangeset) {
        if (!idChangeset.isEmpty()) {
            String url = "https://www.openstreetmap.org/changeset/".concat(idChangeset);
            new Notification(tr(COPY, url)).setIcon(JOptionPane.INFORMATION_MESSAGE).setDuration(Notification.TIME_SHORT).show();
            OpenBrowser.displayUrl(url);
        }
    }

    public static void openinBrowserChangesetMap(String idChangeset) {
        if (!idChangeset.isEmpty()) {
            String url = "https://osmcha.mapbox.com/".concat(idChangeset);
            new Notification(tr(OPEN_IN_BROWSER, url)).setIcon(JOptionPane.INFORMATION_MESSAGE).setDuration(Notification.TIME_SHORT).show();
            OpenBrowser.displayUrl(url);
        }
    }

    public static void copyIdobj(List<AllOsmObjInfo> osmObjInfo) {
        if (osmObjInfo == null || osmObjInfo.isEmpty()) return;
        String linkobjid = "";
        StringBuilder builder = new StringBuilder();
        for (AllOsmObjInfo object : osmObjInfo) {
            builder.append("https://www.openstreetmap.org/")
                .append(object.typeObj).append("/").append(object.idObject);
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

    public static void openinBrowserIdobj(List<AllOsmObjInfo> osmObjInfo) {
        if (osmObjInfo.size() > 5 || osmObjInfo.isEmpty()) return;
        for (AllOsmObjInfo info : osmObjInfo) {
            if (info.typeObj != null && !info.idObject.isEmpty()) {
                String url = "https://www.openstreetmap.org/" + info.typeObj + "/" + info.idObject;
                new Notification(tr(OPEN_IN_BROWSER, url)).setIcon(JOptionPane.INFORMATION_MESSAGE).setDuration(Notification.TIME_SHORT).show();
                OpenBrowser.displayUrl(url);
            }
        }
    }

    public static void openinBrowserIdobjOsmDeepHistory(List<AllOsmObjInfo> osmObjInfo) {
        if (osmObjInfo.size() > 5 || osmObjInfo.isEmpty()) return;
        for (AllOsmObjInfo info : osmObjInfo) {
            if (info.typeObj != null && !info.idObject.isEmpty()) {
                String url = "http://osmlab.github.io/osm-deep-history/#/" + info.typeObj + "/" + info.idObject;
                new Notification(tr(OPEN_IN_BROWSER, url)).setIcon(JOptionPane.INFORMATION_MESSAGE).setDuration(Notification.TIME_SHORT).show();
                OpenBrowser.displayUrl(url);
            }
        }
    }

    public static void openinBrowserMapillary(String coords) {
        if (coords == null || coords.isEmpty()) return;
        String[] arrCoords = coords.split(",");
        String url = "https://www.mapillary.com/app/?lat=" + arrCoords[0] + "&lng=" + arrCoords[1] + "&z=20&focus=map&dateFrom=2017-01-01";
        new Notification(tr(OPEN_IN_BROWSER, url)).setIcon(JOptionPane.INFORMATION_MESSAGE).setDuration(Notification.TIME_SHORT).show();
        OpenBrowser.displayUrl(url);

    }

    public static void openinBrowserOpenstreetcam(String coords) {
        if (coords == null || coords.isEmpty()) return;
        String[] arrCoords = coords.split(",");
        String url = "http://openstreetcam.org/map/@" + arrCoords[0] + "," + arrCoords[1] + ",18z";
        new Notification(tr(OPEN_IN_BROWSER, url)).setIcon(JOptionPane.INFORMATION_MESSAGE).setDuration(Notification.TIME_SHORT).show();
        OpenBrowser.displayUrl(url);
    }

}
