package de.offis.salsa.obsrec.ui;


public abstract class PositionCanvasElement extends CanvasElement {

	private double x;
	private double y;
	private double width;
	private double height;
	
	public PositionCanvasElement(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;		
	}
	
	public double getHeight() {
		return height * zoom;
	}
	
	public double getWidth() {
		return width * zoom;
	}
	
	public double getX() {
		return (x + offsetX) * zoom;
	}
	
	public double getY() {
		return (y + offsetY) * zoom;
	}
}

