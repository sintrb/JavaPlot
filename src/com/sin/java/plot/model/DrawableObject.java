package com.sin.java.plot.model;

import java.awt.Color;

/**
 * 可绘制对象
 * 
 * @author RobinTang
 * 
 */
public class DrawableObject {
	public double[] x = null;
	public double[] y = null;
	public DrawableType type;
	public DrawableAttribute attribute;
	public boolean show = true;

	// 内部标记
	private boolean dataUpdated; // 数据是否被更新过

	
	// 全局静态属性
	private double suitableHorizon = 0.9;	// 视野（最佳视野时用到）
	
	public DrawableObject(double[] x, double[] y) {
		this(x, y, DrawableType.Line, new DrawableAttribute());
	}

	public DrawableObject(double[] x, double[] y, DrawableType type, DrawableAttribute attribute) {
		this.initDrawableObject(x, y, type, attribute);
	}

	public DrawableObject(double[] x, double[] y, String pams) {
		this.initDrawableObject(x, y, pams);
	}

	public DrawableObject(int x, int y, String pams) {
		this.initDrawableObject(new double[] { x }, new double[] { y }, pams);
	}

	public DrawableObject(double[] y) {
		this(y, "");
	}

	public DrawableObject(double[] y, String pams) {
		int len = y.length;
		double[] x = new double[len];
		for (int i = 0; i < len; ++i) {
			x[i] = (double) i;
		}
		this.initDrawableObject(x, y, pams);
	}

	public void initDrawableObject(double[] x, double[] y, String pams) {
		this.setData(x, y);
		this.setAttribute(pams);
	}
	
	public void setAttribute(String pams){
		DrawableType type = null;
		DrawableAttribute attb = new DrawableAttribute();
		pams = pams.toLowerCase();
		if (pams.indexOf("-") >= 0) {
			type = DrawableType.Line;
		} else if (pams.indexOf("*") >= 0) {
			type = DrawableType.Mark;
		} else if (pams.indexOf("o") >= 0) {
			type = DrawableType.Point;
		} else if (pams.indexOf("n") >= 0) {
			type = DrawableType.Columnar;
		} else if (pams.indexOf("$") >= 0) {
			type = DrawableType.MarkLine;
		} else if (pams.indexOf("z") >= 0) {
			type = DrawableType.Step;
		} else {
			type = DrawableType.Line;
		}

		if (pams.indexOf("r") >= 0) {
			attb.lineColor = Color.red;
			attb.fillColor = attb.lineColor;
		} else if (pams.indexOf("g") >= 0) {
			attb.lineColor = Color.green;
			attb.fillColor = attb.lineColor;
		} else if (pams.indexOf("b") >= 0) {
			attb.lineColor = Color.blue;
			attb.fillColor = attb.lineColor;
		}
		this.type = type;
		this.attribute = attb;
	}

	private void initDrawableObject(double[] x, double[] y, DrawableType type, DrawableAttribute attribute) {
		this.type = type;
		this.attribute = attribute;
		this.setData(x, y);
	}

	/*
	 * 设置数据
	 */
	private void setData(double[] x, double[] y) {
		int len = x.length;
		this.x = new double[len];
		this.y = new double[len];
		System.arraycopy(x, 0, this.x, 0, len);
		System.arraycopy(y, 0, this.y, 0, len);
		this.dataUpdated = true;
	}

	// 对外方法
	
	/*
	 * 复制对象
	 */
	public DrawableObject copy(){
		return new DrawableObject(x, y, type, attribute.copy());
	}
	
	/*
	 * 获取数据长度
	 */
	public int length() {
		return this.x != null ? this.x.length : 0;
	}

	private double maxX;
	private double maxY;
	private double minX;
	private double minY;

	/*
	 * 计算极值
	 */
	private void reLimitValue() {
		maxX = minX = this.x[0];
		maxY = minY = this.y[0];
		int len = this.length();
		for (int i = 0; i < len; ++i) {
			if (this.x[i] < minX)
				minX = this.x[i];
			if (this.x[i] > maxX)
				maxX = this.x[i];
			if (this.y[i] < minY)
				minY = this.y[i];
			if (this.y[i] > maxY)
				maxY = this.y[i];
		}
		this.dataUpdated = false;
	}

	// 获取极值
	public double getMaxX() {
		if (this.dataUpdated)
			this.reLimitValue();
		return maxX;
	}

	public double getMaxY() {
		if (this.dataUpdated)
			this.reLimitValue();
		return maxY;
	}

	public double getMinX() {
		if (this.dataUpdated)
			this.reLimitValue();
		return minX;
	}

	public double getMinY() {
		if (this.dataUpdated)
			this.reLimitValue();
		return minY;
	}
	
	// 获取最佳视野位置
	public double suitableSX(){
		return getMinX()-getWidth()*(1-suitableHorizon)/2;
	}
	
	public double suitableEX(){
		return getMaxX()+getWidth()*(1-suitableHorizon)/2;
	}
	
	public double suitableSY(){
		return getMinY()-getHeight()*(1-suitableHorizon)/2;
	}
	
	public double suitableEY(){
		return getMaxY()+getHeight()*(1-suitableHorizon)/2;
	}
	

	// 范围
	public double getWidth() {
		return this.getMaxX() - this.getMinX();
	}

	public double getHeight() {
		return this.getMaxY() - this.getMinY();
	}

}
