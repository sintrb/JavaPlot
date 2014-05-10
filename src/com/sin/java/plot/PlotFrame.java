package com.sin.java.plot;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.sin.java.plot.model.DrawableObject;

/***
 * 绘图窗口，对应matlab的firgue
 * @author RobinTang
 *
 */
public class PlotFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造方法
	 */
	public PlotFrame() throws HeadlessException {
		super();
		this.commonInit();
	}

	public PlotFrame(GraphicsConfiguration gc) {
		super(gc);
		this.commonInit();
	}

	public PlotFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
		this.commonInit();
	}

	public PlotFrame(String title) throws HeadlessException {
		super(title);
		this.commonInit();
	}
	
	
	private PlotPanle plotPanle = null;
	// 公共初始化
	private void commonInit(){
		plotPanle = new PlotPanle();
		this.add(plotPanle);
		this.setSize(300, 200);
		this.setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(!Plot.removePlotFrame(PlotFrame.this)){
					System.exit(0);
				}
			}
		});
		this.setVisible(true);
	}

	/**
	 * 设置绘图保持
	 */
	public void setHoldOn(boolean holdOn) {
		this.plotPanle.setHoldOn(holdOn);
	}
	
	/**
	 * 绘图
	 */
	public void plot(DrawableObject drawableObject){
		this.plotPanle.plot(drawableObject);
	}
	
	/**
	 * 设置显示范围
	 */
	public void axis(double sx, double ex, double sy, double ey) {
		this.plotPanle.axis(sx, ex, sy, ey);
	}
	
	/**
	 * 最佳视野
	 */
	public void suit(){
		this.plotPanle.suit();
	}
}
