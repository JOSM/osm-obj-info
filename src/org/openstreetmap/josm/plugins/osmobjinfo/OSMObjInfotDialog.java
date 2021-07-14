package org.openstreetmap.josm.plugins.osmobjinfo;

import static org.openstreetmap.josm.gui.MainApplication.getLayerManager;
import static org.openstreetmap.josm.gui.MainApplication.getMap;
import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.openstreetmap.josm.data.UserIdentityManager;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.event.SelectionEventManager;
import org.openstreetmap.josm.gui.SideButton;
import org.openstreetmap.josm.gui.dialogs.ToggleDialog;
import org.openstreetmap.josm.gui.layer.NoteLayer;
import org.openstreetmap.josm.gui.util.GuiHelper;
import org.openstreetmap.josm.tools.ImageProvider;
import org.openstreetmap.josm.tools.ImageProvider.ImageSizes;
import org.openstreetmap.josm.tools.Shortcut;
import org.openstreetmap.josm.tools.date.DateUtils;

/**
 * @author ruben
 */
public class OSMObjInfotDialog extends ToggleDialog {

    protected JLabel lbUser;
    protected JLabel lbVersion;
    protected JLabel lbIdobj;
    protected JLabel lbRemoteControl;
    protected JLabel lbTimestamp;
    protected JLabel lbIdChangeset;
    protected JLabel lbLinkUser;
    protected JLabel lbLinnkIdobj;
    protected JLabel lbLinkMapillary;
    protected JLabel lbLinkOSMcamp;
    protected JLabel lbLinkYandex;
    protected JLabel lbLinkIdChangeset;
    protected JLabel lbCopyUser;
    protected JLabel lbCopyIdobj;
    protected JLabel lbCopyIdChangeset;

    protected JLabel lbNeisUser;
    protected JLabel lbChangesetMap;
    protected JLabel lbOsmDeepHistory;
    protected JLabel lbUserOsmComments;

    protected JLabel lbMapillary;
    protected JLabel lbOsmcamp;

    /**
     * A list that holds all selected OSM information
     */
    List<AllOsmObjInfo> allSelectedObjInfo;

    /**
     * A class that holds OSM object info
     *
     * @author Taylor Smock
     */
    public static class AllOsmObjInfo implements Serializable {
        /**
         * A "non" object to avoid NPEs
         */
        public static final AllOsmObjInfo NO_OSM_OBJECTS = new AllOsmObjInfo();
        String typeObj;
        String user = "";
        String version = "";
        String idObject = "";
        String timestamp = "";
        String idchangeset = "";
        String coordinates = "";
    }

    public OSMObjInfotDialog() {
        super(tr("OpenStreetMap obj info"),
          "iconosmobjid",
          tr("Open OpenStreetMap obj info window"),
          Shortcut
            .registerShortcut("osmObjInfo", tr("Toggle: {0}", tr("OpenStreetMap obj info")), KeyEvent.VK_I,
              Shortcut.ALT_CTRL_SHIFT), 90);

        allSelectedObjInfo = new ArrayList<>();
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        //user
        panel.add(new JLabel(tr("User")));
        panel.add(buildUser());
        //changeset
        panel.add(new JLabel(tr("Changeset")));
        panel.add(buildChangeset());
        //obj id
        panel.add(new JLabel(tr("Object Id")));
        panel.add(buildidObject());
        //version
        panel.add(new JLabel(tr("Version")));
        lbVersion = new JLabel();
        panel.add(lbVersion);
        //date
        panel.add(new JLabel(tr("Date")));
        lbTimestamp = new JLabel();
        panel.add(lbTimestamp);

        panel.add(new JLabel(tr("Images")));
        lbMapillary = new JLabel();
        lbOsmcamp = new JLabel();
        panel.add(streetLevelImages());

        createLayout(panel, false, Arrays.asList(new SideButton[] {}));
        SelectionEventManager.getInstance().addSelectionListener(event -> selection(event.getSelection()));

        getMap().mapView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                getInfoNotes(e);
            }
        });
    }

    private JPanel buildUser() {
        //OSM USER
        JPanel jpUser = new JPanel(new BorderLayout());

        lbUser = new JLabel();
        lbLinkUser = new JLabel(ImageProvider.get("dialogs", "link.png"));
        lbCopyUser = new JLabel(ImageProvider.get("dialogs", "copy.png"));
        lbNeisUser = new JLabel(ImageProvider.get("dialogs", "neisuser.png"));
        lbUserOsmComments = new JLabel(ImageProvider.get("dialogs", "userosmcomments.png"));

        lbUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbLinkUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbCopyUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbNeisUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbUserOsmComments.setCursor(new Cursor(Cursor.HAND_CURSOR));

        lbUser.setForeground(Color.BLUE);

        JPanel jpuseroptions = new JPanel(new GridLayout(1, 4, 5, 5));
        jpuseroptions.add(lbCopyUser);
        jpuseroptions.add(lbNeisUser);
        jpuseroptions.add(lbUserOsmComments);
        jpuseroptions.add(lbLinkUser);

        //add
        jpUser.add(lbUser, BorderLayout.LINE_START);
        jpUser.add(jpuseroptions, BorderLayout.LINE_END);
        //User Actions
        lbUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                OSMObjInfoFunctions.selectbyUser(lbUser.getText());
            }
        });

        lbLinkUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                OSMObjInfoActions.openinBrowserUser(lbUser.getText());
            }
        });

        lbCopyUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                OSMObjInfoActions.copyUser(lbUser.getText());
            }
        });

        lbNeisUser.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                OSMObjInfoActions.openinBrowserUserNeis(lbUser.getText());
            }
        });

        lbUserOsmComments.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                OSMObjInfoActions.openinBrowserUserOsmComments(lbUser.getText());
            }
        });
        return jpUser;
    }

    private JPanel buildChangeset() {

        //CHANGESET
        JPanel jpChangeset = new JPanel(new BorderLayout());
        //add
        lbIdChangeset = new JLabel();
        lbLinkIdChangeset = new JLabel(ImageProvider.get("dialogs", "link"));
        lbCopyIdChangeset = new JLabel(ImageProvider.get("dialogs", "copy"));
        lbChangesetMap = new JLabel(ImageProvider.get("dialogs", "changesetmap"));

        lbIdChangeset.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbLinkIdChangeset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbCopyIdChangeset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbChangesetMap.setCursor(new Cursor(Cursor.HAND_CURSOR));

        lbIdChangeset.setForeground(Color.BLUE);

        JPanel jpchOptions = new JPanel(new GridLayout(1, 3, 5, 5));

        jpchOptions.add(lbCopyIdChangeset);
        jpchOptions.add(lbChangesetMap);
        jpchOptions.add(lbLinkIdChangeset);

        //add
        jpChangeset.add(lbIdChangeset, BorderLayout.LINE_START);
        jpChangeset.add(jpchOptions, BorderLayout.LINE_END);

        //Changeset actions
        lbIdChangeset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int idchangeset = Integer.parseInt(lbIdChangeset.getText());
                OSMObjInfoFunctions.selectbyChangesetId(idchangeset);
            }
        });
        lbLinkIdChangeset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                OSMObjInfoActions.openinBrowserChangeset(lbIdChangeset.getText());
            }
        });
        lbCopyIdChangeset.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                OSMObjInfoActions.copyChangeset(lbIdChangeset.getText());
            }
        });
        lbChangesetMap.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                OSMObjInfoActions.openinBrowserChangesetMap(lbIdChangeset.getText());
            }
        });

        return jpChangeset;
    }

    private JPanel buildidObject() {

        //OBJ ID
        JPanel jpIdobj = new JPanel(new BorderLayout());

        lbIdobj = new JLabel();
        lbLinnkIdobj = new JLabel(ImageProvider.get("dialogs", "link"));
        lbCopyIdobj = new JLabel(ImageProvider.get("dialogs", "copy"));
        lbOsmDeepHistory = new JLabel(ImageProvider.get("dialogs", "deephistory"));
        lbRemoteControl = new JLabel(ImageProvider.get("dialogs", "remote", ImageSizes.SMALLICON));

        lbLinnkIdobj.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbCopyIdobj.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbOsmDeepHistory.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbRemoteControl.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel jpIdobjOptions = new JPanel(new GridLayout(1, 3, 5, 5));
        jpIdobjOptions.add(lbCopyIdobj);
        jpIdobjOptions.add(lbOsmDeepHistory);
        jpIdobjOptions.add(lbLinnkIdobj);
        jpIdobjOptions.add(lbRemoteControl);
        //add
        jpIdobj.add(lbIdobj, BorderLayout.LINE_START);
        jpIdobj.add(jpIdobjOptions, BorderLayout.LINE_END);

        //id obj actions
        lbLinnkIdobj.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                OSMObjInfoActions.openinBrowserIdobj(allSelectedObjInfo);
            }
        });
        lbCopyIdobj.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                OSMObjInfoActions.copyIdobj(allSelectedObjInfo);
            }
        });

        lbOsmDeepHistory.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                OSMObjInfoActions.openinBrowserIdobjOsmDeepHistory(allSelectedObjInfo);
            }
        });
        lbRemoteControl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                OSMObjInfoActions.copyRemoteControl(allSelectedObjInfo);
            }
        });
        return jpIdobj;
    }

    public void selection(Collection<? extends OsmPrimitive> selection) {
        allSelectedObjInfo = new ArrayList<>();
        if (selection.size() < OSMObjInfoPlugin.MAXIMUM_SELECTION.get()) {
            for (OsmPrimitive element : selection) {
                if (!element.isNew()) {
                    AllOsmObjInfo info = new AllOsmObjInfo();
                    allSelectedObjInfo.add(info);
                    info.typeObj = element.getType().toString();
                    try {
                        info.user = element.getUser().getName();
                        info.timestamp = DateUtils
                          .getDateTimeFormatter(FormatStyle.MEDIUM, FormatStyle.SHORT)
                          .format(element.getInstant());
                    } catch (NullPointerException e) {
                        info.user = UserIdentityManager.getInstance().getUserName();
                    }
                    info.idObject = String.valueOf(element.getId());
                    info.version = String.valueOf(element.getVersion());
                    info.idchangeset = String.valueOf(element.getChangesetId());

                    DecimalFormat df = new DecimalFormat("#.00000");
                    info.coordinates = df.format(element.getBBox().getCenter().lat()) + "," + df
                      .format(element.getBBox().getCenter().lon());
                }
            }
        }

        AllOsmObjInfo info = allSelectedObjInfo.stream().findFirst().orElse(AllOsmObjInfo.NO_OSM_OBJECTS);
        final String txtUser = info.user;
        final String txtVersion = info.version;
        final String txtIdobject = info.idObject;
        final String txtTimestamp = info.timestamp;
        final String txtIdChangeset = info.idchangeset;
        final String coordinates = info.coordinates;

        GuiHelper.runInEDT(() -> {
            lbUser.setText(txtUser);
            lbIdChangeset.setText(txtIdChangeset);
            lbIdobj.setText(txtIdobject);
            lbVersion.setText(txtVersion);
            lbTimestamp.setText(txtTimestamp);
            lbMapillary.setText(coordinates);

        });
    }

    public void getInfoNotes(MouseEvent e) {
        if (!getLayerManager().getLayersOfType(NoteLayer.class).isEmpty()) {
            NoteLayer noteLayer = getLayerManager().getLayersOfType(NoteLayer.class).get(0);
            noteLayer.mouseClicked(e);
            if (!noteLayer.getNoteData().getNotes().isEmpty()
              && noteLayer.getNoteData().getSelectedNote() != null) {
                allSelectedObjInfo = new ArrayList<>();
                AllOsmObjInfo info = new AllOsmObjInfo();
                allSelectedObjInfo.add(info);
                info.typeObj = "note";
                info.user = noteLayer.getNoteData().getSelectedNote().getFirstComment().getUser()
                  .getName();
                lbUser.setText(info.user);
                lbIdChangeset.setText("");
                if (noteLayer.getNoteData().getSelectedNote().getId() < 0) {
                    lbIdobj.setText("");
                } else {
                    info.idObject = Long
                      .toString(noteLayer.getNoteData().getSelectedNote().getId());
                    lbIdobj.setText(info.idObject);
                }
                lbVersion.setText("");
                info.timestamp = DateUtils.getDateTimeFormatter(FormatStyle.MEDIUM, FormatStyle.SHORT)
                  .format(noteLayer.getNoteData().getSelectedNote().getCreatedAt());
                lbTimestamp.setText(info.timestamp);
            }
        }
    }

    /**
     * Get the panel for street level imagery
     *
     * @return The panel for street level imagery
     */
    private JPanel streetLevelImages() {
        JPanel jpIMapillary = new JPanel(new BorderLayout());
        lbMapillary = new JLabel();
        lbLinkMapillary = new JLabel(ImageProvider.get("dialogs", "mapillary"));
        lbLinkOSMcamp = new JLabel(ImageProvider.get("dialogs", "openstreetcam"));
        lbLinkYandex = new JLabel(ImageProvider.get("dialogs", "yandex"));
        lbLinkMapillary.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbLinkOSMcamp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lbLinkYandex.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JPanel jpIMapillaryOptions = new JPanel(new GridLayout(1, 2, 5, 5));
        jpIMapillaryOptions.add(lbLinkMapillary);
        jpIMapillaryOptions.add(lbLinkOSMcamp);
        jpIMapillaryOptions.add(lbLinkYandex);
        //add
        jpIMapillary.add(lbMapillary, BorderLayout.LINE_START);
        jpIMapillary.add(jpIMapillaryOptions, BorderLayout.LINE_END);
        //id obj actions
        lbLinkMapillary.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                OSMObjInfoActions.openinBrowserMapillary(lbMapillary.getText());
            }
        });
        lbLinkOSMcamp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                OSMObjInfoActions.openinBrowserOpenstreetcam(lbMapillary.getText());
            }
        });
        lbLinkYandex.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                OSMObjInfoActions.openInBrowserYandex(lbMapillary.getText());
            }
        });
        return jpIMapillary;
    }
}
