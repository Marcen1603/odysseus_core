package de.offis.salsa.obsrec.ui;

import java.awt.Graphics;

public abstract class CanvasElement {


	
	protected int offsetX = 350;
	protected int offsetY = 100;
	

	public static int dragOffsetX = 0;
	public static int dragOffsetY = 0;
	
	public static double zoom = 1;

	public void setOffset(int x, int y) {
		offsetX = x;
		offsetY = y;
	}

	public abstract void paint(Graphics g);
}
