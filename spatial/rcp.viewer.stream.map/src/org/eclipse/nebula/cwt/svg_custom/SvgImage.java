package org.eclipse.nebula.cwt.svg_custom;

import java.io.IOException;
import java.net.URL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.PathData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;

public class SvgImage extends SvgGraphic {

	URL url = null;
	Image image = null;
	PathData pathData = null;
	SvgImage(SvgContainer container, String id) {
		super(container, id);
	}

	private void doApply(GC gc) {
		gc.setAntialias(SWT.ON);

//		SvgFill derivedFill = getFill();
//		SvgStroke derivedStroke = getStroke();
		Transform derivedTransform = getTransform(gc);

		Transform bak = null;
		if(!derivedTransform.isIdentity()) {
			bak = new Transform(gc.getDevice());
			gc.getTransform(bak);
			gc.setTransform(derivedTransform);
		}

//		if(derivedFill.isPaintable()) {
//			derivedFill.create(gc);
//			derivedFill.apply();
//			doFill(gc);
//			derivedFill.dispose();
//		}

//		if(derivedStroke.isPaintable()) {
//			derivedStroke.create(gc);
//			derivedStroke.apply();
//			doStroke(gc);
//			derivedStroke.dispose();
//		}
		drawImage(gc);
		
		if(bak != null) {
			gc.setTransform(bak);
		}
		derivedTransform.dispose();
	}

	@Override
	public void apply(GC gc) {
		if (image == null)
			try {
				image = new Image(gc.getDevice(), url.openStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		doApply(gc);
	}

	
	private void drawImage(GC gc){
		Rectangle srcBounds = image.getBounds();
		Rectangle bounds = new Rectangle((int) pathData.points[0], (int) pathData.points[1], (int) pathData.points[2], (int) pathData.points[3]);
		gc.drawImage(image, srcBounds.x, srcBounds.y, srcBounds.width, srcBounds.height, bounds.x, bounds.y, bounds.width, bounds.height);
	}
//	/**
//	 * Returns whether or not the given point is contained by this shape.
//	 * @param x
//	 * @param y
//	 * @param gc
//	 * @param outline
//	 * @return true if the given point is contained, false otherwise
//	 * @see Path#contains(float, float, GC, boolean)
//	 */
//	public boolean contains(float x, float y, GC gc, boolean outline) {
//		Transform t = new Transform(gc.getDevice());
//		gc.getTransform(t);
//		t.invert();
//		float[] pts = new float[] { x, y };
//		t.transform(pts);
//		t.dispose();
//		return path.contains(pts[0], pts[1], gc, outline);
//	}
//
//	private void doFill(GC gc) {
//		if(path != null) {
//			gc.fillPath(path);
//		} else if(pathData.points.length == 4) {
//			int w = (int) (2 * pathData.points[2]);
//			int h = (int) (2 * pathData.points[3]);
//			if(w > 0 && h > 0) {
//				int x = (int) (pathData.points[0]-pathData.points[2]);
//				int y = (int) (pathData.points[1]-pathData.points[3]);
//				gc.fillOval(x, y, w, h);
//			}
//		} else if(pathData.points.length == 6) {
//			int w = (int) pathData.points[2];
//			int h = (int) pathData.points[3];
//			if(w > 0 && h > 0) {
//				int x = (int) pathData.points[0];
//				int y = (int) pathData.points[1];
//				int rx = (int) pathData.points[4];
//				int ry = (int) pathData.points[5];
//				if(rx > 0 || ry > 0) {
//					gc.fillRoundRectangle(x, y, w, h, getRadiusX(), getRadiusY());
//				} else {
//					gc.fillRectangle(x, y, w, h);
//				}
//			}
//		}
//	}

//	private void doStroke(GC gc) {
//		if(path != null) {
//			gc.drawPath(path);
//		} else if(pathData.points.length == 4) {
//			int w = (int) (2 * pathData.points[2]);
//			int h = (int) (2 * pathData.points[3]);
//			if(w > 0 && h > 0) {
//				int x = (int) (pathData.points[0]-pathData.points[2]);
//				int y = (int) (pathData.points[1]-pathData.points[3]);
//				gc.drawOval(x, y, w, h);
//			}
//		} else if(pathData.points.length == 6) {
//			int w = (int) pathData.points[2];
//			int h = (int) pathData.points[3];
//			if(w > 0 && h > 0) {
//				int x = (int) pathData.points[0];
//				int y = (int) pathData.points[1];
//				int rx = (int) pathData.points[4];
//				int ry = (int) pathData.points[5];
//				if(rx > 0 || ry > 0) {
//					gc.drawRoundRectangle(x, y, w, h, getRadiusX(), getRadiusY());
//				} else {
//					gc.drawRectangle(x, y, w, h);
//				}
//			}
//		}
//	}

//	float[] getBounds() {
//		if(path != null) {
//			// TODO Path#getBounds
//			float minx = Float.POSITIVE_INFINITY;
//			float miny = Float.POSITIVE_INFINITY;
//			float maxx = Float.NEGATIVE_INFINITY;
//			float maxy = Float.NEGATIVE_INFINITY;
//			for(int i = 0; i < pathData.points.length - 1; i++) {
//				minx = min(minx, pathData.points[i]);
//				maxx = max(maxx, pathData.points[i]);
//				i++;
//				miny = min(miny, pathData.points[i]);
//				maxy = max(maxy, pathData.points[i]);
//			}
//			return new float[] { minx, miny, abs(maxx - minx), abs(maxy - miny) };
//		} else if(pathData.points.length == 4) {
//			int x = (int) (pathData.points[0]-pathData.points[2]);
//			int y = (int) (pathData.points[1]-pathData.points[3]);
//			int w = (int) (2 * pathData.points[2]);
//			int h = (int) (2 * pathData.points[3]);
//			return new float[] { x, y, w, h };
//		} else if(pathData.points.length == 6) {
//			int x = (int) pathData.points[0];
//			int y = (int) pathData.points[1];
//			int w = (int) pathData.points[2];
//			int h = (int) pathData.points[3];
//			return new float[] { x, y, w, h };
//		}
//		throw new UnsupportedOperationException();
//	}

//	private int getRadiusX() {
//		if(pathData.points[4] > 0) {
//			return (int) (2 * pathData.points[4]);
//		}
//		if(pathData.points[5] > 0) {
//			return getRadiusY();
//		}
//		return 0;
//	}
//
//	private int getRadiusY() {
//		if(pathData.points[5] > 0) {
//			return (int) (2 * pathData.points[5]);
//		}
//		if(pathData.points[4] > 0) {
//			return getRadiusX();
//		}
//		return 0;
//	}
//
}