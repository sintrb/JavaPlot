package com.sin.java.plot.test;

import com.sin.java.plot.Plot;
import com.sin.java.plot.model.DrawableObject;

public class TestMain {

	public static void main(String[] args) {
		Plot.figrue();
		Plot.hold_on();
		
		int len = 100;
		double[] y1 = new double[len];
		double[] y2 = new double[len];
		for (int i = 0; i < y1.length; i++) {
			y1[i] = Math.sin(i / 10.0f);
			y2[i] = Math.cos(i / 10.0f);
		}
		DrawableObject draw1 = new DrawableObject(y1, "-b");
		DrawableObject draw2 = new DrawableObject(y2, "-r");
		Plot.plot(draw1);
		Plot.plot(draw2);
	}
}
