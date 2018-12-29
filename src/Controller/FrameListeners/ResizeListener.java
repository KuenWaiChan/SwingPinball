package Controller.FrameListeners;

import View.Board.iBoard;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class ResizeListener implements ComponentListener {
	private iBoard board;

	public ResizeListener(iBoard board) {

		this.board = board;
	}

	@Override
	public void componentResized(ComponentEvent e) {
		board.setPrefScreenDimension();
		board.repaint();
	}

	@Override
	public void componentMoved(ComponentEvent e) {

	}

	@Override
	public void componentShown(ComponentEvent e) {

	}

	@Override
	public void componentHidden(ComponentEvent e) {

	}
}
