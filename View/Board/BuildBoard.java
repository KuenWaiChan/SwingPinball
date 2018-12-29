package View.Board;

import Model.IGizmoModel;

import javax.swing.*;
import java.awt.*;

public class BuildBoard extends Board {

    public BuildBoard(JPanel parent, IGizmoModel model) {
        super(parent, model);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //Draw on the grid
        getDrawer().drawGrid(g,scaleX,scaleY,this.getHeight(),this.getWidth());
    }
}
