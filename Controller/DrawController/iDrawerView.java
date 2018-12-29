package Controller.DrawController;

import javafx.util.Pair;

import java.awt.*;
import java.util.List;

public interface iDrawerView {
    List<Pair<Color, String>> getMessages();
    Boolean hasMessages();
	void clearMessages();
	void draw(Graphics g, double scaleX, double scaleY);
	void drawGrid(Graphics g, double scaleX, double scaleY, int height, int width);
}
