package View.MainView;

import Controller.RunListeners.KeyPressListener;

import java.io.FileNotFoundException;

public interface iGBallMainView {

	void toggleMode();
	boolean isBuildMode();

	void update();

	void clear();
	void display(String s);
	void reSetRun();

	void loadFile(String absolutePath) throws FileNotFoundException;

	void save(String absolutePath);

	void setKeyListener(KeyPressListener kpl);
}
