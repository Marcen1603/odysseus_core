package de.uniol.inf.is.odysseus.rcp.viewer.swt.symbol;

/**
 * Container class for margins of GUI objects
 * @author Thomas Vogelgesang
 *
 */
public final class Margin {

	private int left;
	private int right;
	private int top;
	private int bottom;

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getBottom() {
		return bottom;
	}

	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	public Margin(int left, int right, int top, int bottom) {
		super();
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
	}

}
