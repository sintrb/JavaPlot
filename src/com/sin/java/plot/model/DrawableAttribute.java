package com.sin.java.plot.model;

import java.awt.Color;

/**
 * 可绘制对象属性
 * @author RobinTang
 *
 */
public class DrawableAttribute {
	public int width = 10;
	public Color lineColor = Color.black;
	public Color fillColor = Color.black;
	
	/*
	 * 属性拷贝
	 */
	public DrawableAttribute copy() {
		DrawableAttribute ret = new DrawableAttribute();
		ret.width = this.width;
		ret.lineColor = this.lineColor;
		ret.fillColor = this.fillColor;
		return ret;
	}
}
