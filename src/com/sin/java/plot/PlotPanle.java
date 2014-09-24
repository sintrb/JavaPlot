package com.sin.java.plot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import com.sin.java.plot.model.DrawableAttribute;
import com.sin.java.plot.model.DrawableObject;

/**
 * 绘图面板，对应matlab中的plot面板
 * 
 * @author RobinTang
 * 
 */
public class PlotPanle extends JPanel {
	private static final long serialVersionUID = 1L;
	private boolean holdOn = false;
	private List<DrawableObject> drawableObjectStack = null;
	private double axis_x = 0;
	private double axis_y = 0;
	private double axis_w = 15;
	private double axis_h = 15;
	private int w;
	private int h;

	private DrawableAttribute attribute = null;
	private int point_w = 0;

	// 信息属性
	public final static int doControl_x = 5;
	public final static int doControl_y = 5;
	public final static int doControl_w = 10;
	public final static int doControl_h = 10;

	public PlotPanle() {
		super();
		this.commonInit();
	}

	/**
	 * 公共初始化
	 */
	private void commonInit() {
		this.drawableObjectStack = Collections.synchronizedList(new ArrayList<DrawableObject>());
		PlotPanleMouseListener mlst = new PlotPanleMouseListener(this);
		this.addMouseListener(mlst);
		this.addMouseMotionListener(mlst);

		this.setEnabled(true);
		this.setFocusable(true);
		PlotPanleKeyListener klst = new PlotPanleKeyListener(this);
		this.addKeyListener(klst);
	}

	// 重载绘图
	@Override
	public void paint(Graphics g) {
		Dimension size = this.getSize();
		w = size.width;
		h = size.height;
		g.clearRect(0, 0, w, h);
		g.setColor(Color.white);
		g.fillRect(0, 0, w, h);
		for (int i = 0; i < drawableObjectStack.size(); ++i) {
			if (drawableObjectStack.get(i).show)
				this.paintDrawableObject(g, drawableObjectStack.get(i));
		}
		drawPlotInfo(g);
	}

	// 绘图方法簇
	private void drawLine(Graphics g, double x1, double y1, double x2, double y2) {
		g.drawLine(mappingX(x1), mappingY(y1), mappingX(x2), mappingY(y2));
	}
	// 绘制点
	private void drawPoint(Graphics g, double x, double y) {
		g.drawOval(mappingX(x, true), mappingY(y, true), attribute.width, attribute.width);
	}
	// 绘制柱状
	private void drawColumnar(Graphics g, double x, double y) {
		if (y > 0) {
			g.fillRect(mappingX(x) - point_w / 2, mappingY(y), point_w, (int) (y * h / (axis_h)) + 1);
		} else {
			y = -y;
			int hh = (int) (y * h / (axis_h)) + 1;
			g.fillRect(mappingX(x) - point_w / 2, mappingY(y) + hh, point_w, hh);
		}
	}
	// 填充点
	private void fillPoint(Graphics g, double x, double y) {
		g.fillOval(mappingX(x, true), mappingY(y, true), attribute.width, attribute.width);
	}
	// 信息
	private void drawPlotInfo(Graphics g) {
		// axis info
		g.setColor(Color.BLACK);
		String axisString = String.format("x:%.2f y:%.2f w:%.2f h:%.2f", this.axis_x, this.axis_y, this.axis_w, this.axis_h);
		FontMetrics lm = g.getFontMetrics();
		g.drawString(axisString, (w - lm.getWidths()[0] * axisString.length()) / 2, lm.getHeight());

		// axle coordinate
		g.setColor(Color.GRAY);
		g.drawLine(0, mappingY(0), w, mappingY(0));
		g.drawLine(mappingX(0), 0, mappingX(0), h);

		
		// x label info
		int hc = 10;
		double hk = this.axis_w/hc;
		//Math.abs(this.axis_x)
		for(int i=0; i<hc; ++i){
			double x = this.axis_x+hk*i;
			String lb = String.format("%.02f", x);
			g.setColor(Color.BLACK);
			g.drawString(lb, mappingX(x)-10, mappingY(0)+15);
			
			g.setColor(Color.RED);
			g.drawLine(mappingX(x), mappingY(0), mappingX(x), mappingY(0)-5);
		}
		
		
		// drawable Objects Control
		for (int i = 0; i < this.drawableObjectStack.size(); ++i) {
			DrawableObject drawo = this.drawableObjectStack.get(i);
			g.setColor(drawo.attribute.lineColor);
			if (drawo.show)
				g.fillRect(doControl_x + i * doControl_w, doControl_y, doControl_w, doControl_h);
			else
				g.drawRect(doControl_x + i * doControl_w, doControl_y, doControl_w - 1, doControl_h);
		}

		// draw cursor info
		if (this.showcur) {
			g.setColor(Color.GRAY);
			String info = String.format("(%d,%d) (%.02f,%.02f)", cur_x, cur_y, iMappingX(cur_x), iMappingY(cur_y));
			int sw = lm.getWidths()[0] * info.length();
			int sh = lm.getHeight();
			g.drawLine(cur_x, 0, cur_x, h);
			g.drawLine(0, cur_y, w, cur_y);
			g.setColor(Color.BLACK);
			g.drawString(info, (cur_x < w / 2) ? cur_x : cur_x - sw, (cur_y < h / 2) ? cur_y + sh : cur_y);
			// this.showcur = false;
		}
	}

	/**
	 * 根据坐标获取点击位置上的可绘制对象
	 * 
	 * @param x
	 *            横坐标
	 * @param y
	 *            纵坐标
	 * @return 索引，-1为没有
	 */
	public int getDrawableObjectByXY(int x, int y) {
		if (x < doControl_x || y < doControl_y || x >= (doControl_x + doControl_w * this.drawableObjectStack.size()) || y >= (doControl_y + doControl_h)) {
			return -1;
		}
		return (x - doControl_x) / doControl_w;
	}

	/**
	 * 反转可绘制对象的可视状态
	 * 
	 * @param index
	 *            索引
	 * @return 可绘制对象
	 */
	public DrawableObject toggleShow(int index) {
		if (index >= 0 || index < this.drawableObjectStack.size()) {
			DrawableObject drawo = this.drawableObjectStack.get(index);
			drawo.show = !drawo.show;
			this.updateUI();
			return drawo;
		} else {
			return null;
		}
	}

	/**
	 * 坐标映射
	 */
	private int mappingX(double x) {
		return this.mappingX(x, false);
	}

	private int mappingX(double x, boolean b) {
		double kx = w / (axis_w);
		return (int) ((x - axis_x) * kx) - (b ? point_w / 2 : 0);
	}

	private int mappingY(double y) {
		return this.mappingY(y, false);
	}

	private int mappingY(double y, boolean b) {
		double ky = h / (axis_h);
		return h - (int) ((y - axis_y) * ky) - (b ? point_w / 2 : 0);
	}

	private double iMappingX(int x) {
		double kx = axis_w / w;
		return axis_x + x * kx;
	}

	private double iMappingY(int y) {
		double ky = axis_h / h;
		return axis_y + (h - y) * ky;
	}

	/**
	 * 绘制可绘制对象
	 */
	private void paintDrawableObject(Graphics g, DrawableObject drawableObject) {
		Dimension size = this.getSize();
		w = size.width;
		h = size.height;

		double[] x = drawableObject.x;
		double[] y = drawableObject.y;
		int count = x.length;
		g.setColor(drawableObject.attribute.lineColor);
		this.attribute = drawableObject.attribute;
		this.point_w = attribute.width;
		boolean lefted = false, righted = false;
		for (int i = 0; i < count; ++i) {
			if (x[i] < axis_x) {
				if (lefted)
					continue;
				else
					lefted = true;
			}
			if (x[i] > (axis_x + axis_w)) {
				if (righted)
					continue;
				else
					righted = true;
			}
			// if (x[i] < axis_x || x[i] > (axis_x + axis_w))
			// continue;
			switch (drawableObject.type) {
			case Line:
				if (i > 0) {
					this.drawLine(g, x[i - 1], y[i - 1], x[i], y[i]);
				}
				break;
			case Point:
				this.drawPoint(g, x[i], y[i]);
				break;
			case Mark:
				this.fillPoint(g, x[i], y[i]);
				break;
			case MarkLine:
				if (i > 0) {
					this.drawLine(g, x[i - 1], y[i - 1], x[i], y[i]);
				}
				this.fillPoint(g, x[i], y[i]);
				break;
			case Columnar:
				this.drawColumnar(g, x[i], y[i]);
				break;
			case Step:
				if (i > 0) {
					this.drawLine(g, x[i - 1], y[i - 1], x[i], y[i - 1]);
					this.drawLine(g, x[i], y[i - 1], x[i], y[i]);
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 设置图形接口
	 */
	public void setHoldOn(boolean holdOn) {
		this.holdOn = holdOn;
	}

	/**
	 * 绘图
	 */
	public void plot(DrawableObject drawableObject) {
		if (!this.holdOn) {
			drawableObjectStack.clear();
		}
		drawableObjectStack.add(drawableObject);
		this.updateUI();
	}

	/**
	 * 设置显示范围
	 */
	public void axis(double sx, double ex, double sy, double ey) {
		this.axis_x = sx;
		this.axis_y = sy;
		this.axis_w = ex - sx;
		this.axis_h = ey - sy;
		this.updateUI();
	}

	/**
	 * 移动显示范围
	 */
	public void moveAxis(double sx, double sy) {
		this.axis_x = this.axis_x + sx * this.axis_w;
		this.axis_y = this.axis_y + sy * this.axis_h;
		this.updateUI();
	}

	/**
	 * 二倍放大，x,y为参考点
	 */
	public void zoomIn(int x, int y) {
		this.zoom(x, y, 2.0f);
	}

	/**
	 * 二倍缩小，x,y为参考点
	 */
	public void zoomOut(int x, int y) {
		this.zoom(x, y, 0.5f);
	}

	// 放大scale倍，横坐标scale-x，纵坐标scale-y，x,y为参考点
	public void zoom(int x, int y, double scale) {
		this.zoom(x, y, scale, scale);
	}

	/**
	 * 
	 * @param x
	 *            参考横坐标
	 * @param y
	 *            参考纵坐标
	 * @param scalex
	 *            横向倍数，1为不缩放，>1放大，<1缩小，不能为0
	 * @param scaley
	 *            纵向倍数，1为不缩放，>1放大，<1缩小，不能为0
	 */
	public void zoom(int x, int y, double scalex, double scaley) {
		y = this.h - y;
		double tw = this.axis_w / scalex, th = this.axis_h / scaley;

		double kx = ((double) x) / ((double) this.w);
		double ky = ((double) y) / ((double) this.h);

		this.axis_x -= ((tw - this.axis_w) * kx);
		this.axis_y -= ((th - this.axis_h) * ky);
		this.axis_w = tw;
		this.axis_h = th;
		this.updateUI();
	}

	// 放大scale倍，横坐标scale-x，纵坐标scale-y
	public void zoom(double scale) {
		this.zoom(scale, scale);
	}

	/**
	 * 缩放，以中心为参考坐标
	 * 
	 * @param scalex
	 *            横向倍数，1为不缩放，>1放大，<1缩小，不能为0
	 * @param scaley
	 *            纵向倍数，1为不缩放，>1放大，<1缩小，不能为0
	 */
	public void zoom(double scalex, double scaley) {
		double tw = this.axis_w / scalex, th = this.axis_h / scaley;

		double kx = 0.5f;
		double ky = 0.5f;

		this.axis_x -= ((tw - this.axis_w) * kx);
		this.axis_y -= ((th - this.axis_h) * ky);
		this.axis_w = tw;
		this.axis_h = th;
		this.updateUI();
	}

	/**
	 * 最佳视野
	 */
	public void suit() {
		if (this.drawableObjectStack.size() == 0)
			return;
		DrawableObject draw0 = this.drawableObjectStack.get(0);
		double sx = draw0.suitableSX();
		double ex = draw0.suitableEX();
		double sy = draw0.suitableSY();
		double ey = draw0.suitableEY();
		for (int i = 1; i < this.drawableObjectStack.size(); ++i) {
			DrawableObject draw = this.drawableObjectStack.get(i);
			if (draw.suitableSX() < sx)
				sx = draw.suitableSX();
			if (draw.suitableEX() > ex)
				ex = draw.suitableEX();
			if (draw.suitableSY() < sy)
				sy = draw.suitableSY();
			if (draw.suitableEY() > ey)
				ey = draw.suitableEY();
		}
		this.axis(sx, ex, sy, ey);
	}

	private int cur_x;
	private int cur_y;
	private boolean showcur = false;

	/**
	 * 设置并显示光标位置信息
	 * 
	 * @param x
	 *            横坐标
	 * @param y
	 *            纵坐标
	 */
	public void setCursorPoint(int x, int y) {
		cur_x = x;
		cur_y = y;
		if (showcur) {
			showcur = true;
			this.updateUI();
		}
	}

	public void setShowCurosr(boolean show) {
		if (this.showcur == show)
			return;
		this.showcur = show;
		this.updateUI();
	}
}
