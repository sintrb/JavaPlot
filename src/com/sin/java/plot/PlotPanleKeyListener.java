package com.sin.java.plot;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlotPanleKeyListener implements KeyListener {
	private PlotPanle panle = null;
	private boolean leftDown = false;
	private boolean rightDown = false;
	private boolean upDown = false;
	private boolean downDown = false;
	private boolean ctrlDown = false;

	public PlotPanleKeyListener(PlotPanle panle) {
		super();
		this.panle = panle;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		boolean status = true;
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			leftDown = status;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			rightDown = status;
		if (e.getKeyCode() == KeyEvent.VK_UP)
			upDown = status;
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			downDown = status;
		if (e.getKeyCode() == KeyEvent.VK_CONTROL)
			ctrlDown = status;
		if(e.getKeyCode() == KeyEvent.VK_ALT){
			panle.setShowCurosr(true);
		}
		
		double sc = 0.1;
		double st = ctrlDown ? 0.01f : 0.05f;
		if (leftDown && rightDown) {
			if (ctrlDown)
				panle.zoom(1.0f - sc, 1.0f);
			else
				panle.zoom(1.0f + sc, 1.0f);
		} else if (leftDown) {
			panle.moveAxis(-st, 0);
		} else if (rightDown) {
			panle.moveAxis(st, 0);
		}
		if (upDown && downDown) {
			if (ctrlDown)
				panle.zoom(1.0f, 1.0f - sc);
			else
				panle.zoom(1.0f, 1.0f + sc);
		} else if (upDown) {
			panle.moveAxis(0, st);
		} else if (downDown) {
			panle.moveAxis(0, -st);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		boolean status = false;
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			leftDown = status;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			rightDown = status;
		if (e.getKeyCode() == KeyEvent.VK_UP)
			upDown = status;
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			downDown = status;
		if (e.getKeyCode() == KeyEvent.VK_CONTROL)
			ctrlDown = status;
		if(e.getKeyCode() == KeyEvent.VK_ALT){
			panle.setShowCurosr(false);
		}
	}

}
