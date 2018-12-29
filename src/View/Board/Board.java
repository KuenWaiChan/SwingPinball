package View.Board;

import Controller.FrameListeners.ResizeListener;
import Model.IGizmoModel;
import Controller.DrawController.Drawer;
import Controller.DrawController.iDrawerView;
import View.MessageDisplay;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public abstract class Board extends JPanel implements iBoard {
    JPanel parentPanel;
    protected double scaleX, scaleY;
    private iDrawerView drawer;

    public Board(JPanel parent, IGizmoModel model) {
        parentPanel = parent;
        setBackground(Color.WHITE);//Charge of the light brigade
        this.addComponentListener(new ResizeListener(this));
        drawer = new Drawer(model);
    }

    /**
     * Sets the size of the frame to the best to fit the screen
     */
    public void setPrefScreenDimension() {
        Dimension maxSize = parentPanel.getSize();
        double maxWidth = maxSize.getWidth();
        double maxHeight = maxSize.getHeight();
        double maxLength = maxHeight;

        if (maxWidth < maxHeight) {
            maxLength = maxWidth;
        }

        int length = (int) maxLength;
        length = (length - (length % 20) + 1);

        setSize(new Dimension(length, length));

    }


    /**
     * alerts the user with the messages
     * @param messages != null, messages to be displayed
     */
    private void alert(List<Pair<Color, String>> messages) {
        String message = "VALENTINE : V01\t" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + "\n";
        for (Pair<Color, String> s : messages)
            message = message + "\n\t"+ s.getValue();

        new MessageDisplay(message);
    }

    @Override
    public synchronized void paint(Graphics g) {
        super.paint(g);
        scaleX = this.getWidth() / 20;
        scaleY = this.getHeight() / 20;
        drawer.draw(g, scaleX, scaleY); //Passes the graphics to the drawer to be drawn
        if (drawer.hasMessages()) {
            this.alert(drawer.getMessages());
            drawer.clearMessages();
        }
    }

    /**
     * Sets the current model to be updated from and to edit to
     * @param model != null
     */
    public void setModel(IGizmoModel model) {
        if(model != null)
        drawer = new Drawer(model);
    }

    public iDrawerView getDrawer() {
        return drawer;
    }

    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }


}
