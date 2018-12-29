package View.GUIPanel;

import Model.IGizmoModel;
import View.MainView.GBallView;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public abstract class GBallGui extends JPanel {
    JPanel sideMenuArea;
    JDialog sideFrame = new JDialog();
    JScrollPane sideMenuScrollPane;
    JLabel tipMessage;

    public GBallGui() {
        setLayout(new BorderLayout());

        sideMenuScrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sideMenuArea = new JPanel();

        sideMenuScrollPane.setViewportView(sideMenuArea);
        sideMenuScrollPane.setPreferredSize(new Dimension(240, GBallView.START_HEIGHT));

        sideFrame.setContentPane(sideMenuScrollPane);
        sideFrame.setResizable(false);
        sideFrame.setFocusable(false);
        sideFrame.pack();
        sideFrame.setVisible(true);
        sideFrame.setTitle("Tools");

        setFocusable(true);
        JPanel messageArea;
        messageArea = new JPanel();
        tipMessage = new JLabel("Build Mode");

        messageArea.add(tipMessage);
        add(messageArea, BorderLayout.SOUTH);
    }

    public void collapseSideBar() {
        sideFrame.setVisible(false);
        sideFrame.setFocusable(false);
        setFocusable(false);
    }

    public void showSideBar() {
        sideFrame.setVisible(true);
        sideFrame.setFocusable(false);
    }

    public Border buttonBorderGenerator(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.black), title);
        border.setTitleJustification(TitledBorder.LEFT);
        return border;
    }

    public void addBoard(JPanel panel) {
        add(panel, BorderLayout.CENTER);
        panel.setBackground(Color.LIGHT_GRAY);
    }

    abstract JPanel sideMenuAreaSetup();

    public abstract void setModel(IGizmoModel interfaceCopy);

    public void display(String s) {
        tipMessage.setText(s);
    }

    public void stop() {
    }
}
