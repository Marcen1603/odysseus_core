package de.uniol.inf.is.odysseus.rcp.viewer.view.render.impl;

import de.uniol.inf.is.odysseus.rcp.viewer.view.graph.Vector;


public final class RenderRange {

	public int x;
	public int y;
	public int height;
	public int width;

	public boolean contains( int x, int y ) {
		return x >= this.x && x <= this.x + width && y >= this.y && y <= this.y + height;
	}
	
	public boolean contains( Vector v ) {
		return contains( (int)v.getX(), (int)v.getY() );
	}
	
	public boolean intersects (int x, int y, int width, int height) {
		return (x < this.x + this.width) && (y < this.y + this.height) &&
			(x + width > this.x) && (y + height > this.y);
	}
	
	public boolean intersects( Vector start, int width, int height ) {
		return intersects( (int)start.getX(), (int)start.getY(), width, height );
	}
}
