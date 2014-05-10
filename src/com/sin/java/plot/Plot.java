package com.sin.java.plot;

import java.util.ArrayList;
import java.util.List;

import com.sin.java.plot.model.DrawableObject;
import com.sin.java.plot.util.XLog;

/***
 * UI方法，提供图形绘制接口
 * 
 * @author RobinTang
 * 
 */

final public class Plot {
	private static List<PlotFrame> plotFrameStack = null;
	private static PlotFrame currenPlotFrame = null;
	private static int plotFrameConter = 1;
	private static boolean autoFigure = false; // 自动创建图形窗口

	/**
	 * 初始化图形窗口栈
	 */
	private static void initPlotFrameStack() {
		if (plotFrameStack == null) {
			plotFrameStack = new ArrayList<PlotFrame>();
		}
	}

	/**
	 * 初始化当前图形窗口
	 */
	private static void initCurrentPlotFrame() {
		if (currenPlotFrame == null) {
			figrue();
		}
	}

	// like matlab

	/**
	 * 开图形保存
	 */
	public static void hold_on() {
		initCurrentPlotFrame();
		currenPlotFrame.setHoldOn(true);
	}

	/**
	 * 关图形保持
	 */
	public static void hold_off() {
		initCurrentPlotFrame();
		currenPlotFrame.setHoldOn(false);
	}

	/**
	 * 开自动创建图形窗口
	 */
	public static void autofigure_on() {
		autoFigure = true;
	}

	/**
	 * 关自动创建图形窗口
	 */
	public static void autofigure_off() {
		autoFigure = false;
	}

	/**
	 * 打开图形窗口
	 */
	public static PlotFrame figrue() {
		return figrue(plotFrameConter);
	}

	public static PlotFrame figrue(String title) {
		initPlotFrameStack();
		currenPlotFrame = null;
		for (PlotFrame fm : plotFrameStack) {
			if (title.equals(fm.getTitle())) {
				currenPlotFrame = fm;
				break;
			}
		}
		if (currenPlotFrame == null)
			addPlotFrame(new PlotFrame(title));
		return currenPlotFrame;
	}

	public static PlotFrame figrue(int index) {
		return figrue("Figure " + index);
	}

	/**
	 * 绘图
	 */
	public static PlotFrame plot(DrawableObject drawableObject) {
		if (autoFigure)
			figrue();
		else
			initCurrentPlotFrame();
		currenPlotFrame.plot(drawableObject);
		Plot.suit();
		return currenPlotFrame;
	}

	public static PlotFrame plot(double[] y) {
		return plot(new DrawableObject(y));
	}

	public static PlotFrame plot(double[] x, double[] y) {
		return plot(new DrawableObject(x, y));
	}

	public static PlotFrame plot(double[][] pts) {
		return plot(new DrawableObject(pts[0], pts[1]));
	}

	public static PlotFrame plot(double[] x, String pams) {
		return plot(new DrawableObject(x, pams));
	}

	public static PlotFrame plot(double[] x, double[] y, String pams) {
		return plot(new DrawableObject(x, y, pams));
	}

	public static PlotFrame plot(double[][] pts, String pams) {
		return plot(new DrawableObject(pts[0], pts[1], pams));
	}

	/**
	 * 设置显示范围
	 */
	public static void axis(double sx, double ex, double sy, double ey) {
		initCurrentPlotFrame();
		currenPlotFrame.axis(sx, ex, sy, ey);
	}

	/**
	 * 最佳视野
	 */
	public static void suit() {
		initCurrentPlotFrame();
		currenPlotFrame.suit();
	}

	// internal
	public static void addPlotFrame(PlotFrame frame) {
		XLog.log("add plotframe %s", frame.getTitle());
		initPlotFrameStack();
		++plotFrameConter;
		plotFrameStack.add(frame);
		currenPlotFrame = frame;
	}

	public static boolean removePlotFrame(PlotFrame frame) {
		XLog.log("remove plotframe %s", frame.getTitle());
		plotFrameStack.remove(frame);
		return plotFrameStack.size() != 0;
	}
}
