package de.uniol.inf.is.odysseus.fusion.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

/**
 * @author Kai Pancratz <kai@pancratz.net>
 */
public class ExtPolygonMap extends JPanel implements KeyListener {

	private static final long serialVersionUID = 8022249253673132751L;
	private final List<Tuple<? extends IMetaAttribute>> segments = new CopyOnWriteArrayList<Tuple<? extends IMetaAttribute>>();
	HashMap<Integer, ArrayList<Coordinate>> paths = new HashMap<Integer, ArrayList<Coordinate>>();

	private int zoom = 2;
	private Double angle = 0.0;
	private Double[] offset = new Double[] { 0.0, 0.0 };

	public ExtPolygonMap() {
		this.setFocusable(true);
		this.addKeyListener(this);
	}

	public void onFeature(
			final Tuple<? extends IMetaAttribute> segment) {
		this.segments.add(segment);
		if (this.segments.size() > 10) {
			this.repaint();
		}
	}

	@Override
	public void paint(final Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, 1000, 1000);
		graphics.setColor(Color.WHITE);
		graphics.drawLine(500, 0, 500, 1000);
		graphics.drawLine(0, 500, 1000, 500);

		double angle = Math.toRadians(this.angle);
		for (final Tuple<? extends IMetaAttribute> tuple : this.segments) {

			Polygon segment = (Polygon) tuple.getAttribute(0);
			int objectClass = tuple.getAttribute(1);
			//Polygon prediction = (Polygon) tuple.getAttribute(2);
			//int objectInstance = tuple.getAttribute(3);
			int objectInstance = 0;
					
			final Coordinate[] coordinates = segment.getCoordinates();
			int[] xPoints = new int[coordinates.length];
			int[] yPoints = new int[coordinates.length];
			int index = 0;

			for (final Coordinate coordinate : coordinates) {
				double x = ((coordinate.x - this.offset[0]) * Math.cos(angle) - (coordinate.y - this.offset[1])
						* Math.sin(angle));
				double y = ((coordinate.x - this.offset[0]) * Math.sin(angle) + (coordinate.y - this.offset[1])
						* Math.cos(angle));
				xPoints[index] = 500 + (int) (x / zoom);
				yPoints[index] = 500 - (int) (y / zoom);
				index++;
			}
			graphics.setColor(Color.WHITE);

			if (objectClass == 0) {
				graphics.setColor(Color.GRAY);
				graphics.drawString("Unkown Object", xPoints[0], yPoints[0]);
			}

			if (objectClass == 1) {
				graphics.setColor(Color.RED);
				graphics.drawString("Human", xPoints[0], yPoints[0]);
				graphics.drawString("Instance " + objectInstance, xPoints[0], yPoints[0] + 10);
				graphics.drawPolyline(xPoints, yPoints, coordinates.length);
			}
			if (objectClass == 2) {
				graphics.setColor(Color.GREEN);
				graphics.drawString("Scooter", xPoints[0], yPoints[0]);
				graphics.drawString("Instance " + objectInstance, xPoints[0],
						yPoints[0] + 10);
				graphics.drawPolyline(xPoints, yPoints, coordinates.length);
			}

			
//			if (objectClass > 0) {
//				ArrayList<Coordinate> list;
//				if (paths.containsKey(objectInstance)) {
//					list = paths.get(objectInstance);
//					list.add(new Coordinate(xPoints[0], yPoints[0]));		
//				} else {
//					list = new ArrayList<Coordinate>();
//					list.add(new Coordinate(xPoints[0], yPoints[0]));
//					paths.put(objectInstance, list);
//				}
//			}
//			graphics.setColor(Color.GRAY);
//			for(ArrayList<Coordinate> list :paths.values()){
//			
//				int[] pathXPoints = new int[list.size()];
//				int[] pathYPoints = new int[list.size()];
//				int pathIndex = 0;
//				
//				if(list.size() > 400){
//					list.remove(0);
//				}
//				for (final Coordinate coordinate : list) {
//					pathXPoints[pathIndex] =  (int) coordinate.x;
//					pathYPoints[pathIndex] = (int) coordinate.y;
//					pathIndex++;
//				}
//				graphics.drawPolyline(pathXPoints, pathYPoints, list.size());
//			}
			
			
			graphics.setColor(Color.YELLOW);
//			for (Polygon prediction : predictions) {
//				final Coordinate[] preCoordinates = prediction.getCoordinates();
//				int[] preXPoints = new int[preCoordinates.length];
//				int[] preYPoints = new int[preCoordinates.length];
//				int preIndex = 0;
//
//				for (final Coordinate coordinate : preCoordinates) {
//					double x = ((coordinate.x - this.offset[0])
//							* Math.cos(angle) - (coordinate.y - this.offset[1])
//							* Math.sin(angle));
//					double y = ((coordinate.x - this.offset[0])
//							* Math.sin(angle) + (coordinate.y - this.offset[1])
//							* Math.cos(angle));
//					preXPoints[preIndex] = 500 + (int) (x / zoom);
//					preYPoints[preIndex] = 500 - (int) (y / zoom);
//					preIndex++;
//				}
//				graphics.drawPolyline(preXPoints, preYPoints,
//						preCoordinates.length);
//			}
//			if(prediction != null){
//				final Coordinate[] preCoordinates = prediction.getCoordinates();
//				int[] preXPoints = new int[preCoordinates.length];
//				int[] preYPoints = new int[preCoordinates.length];
//				int preIndex = 0;
//	
//				for (final Coordinate coordinate : preCoordinates) {
//					double x = ((coordinate.x - this.offset[0])
//							* Math.cos(angle) - (coordinate.y - this.offset[1])
//							* Math.sin(angle));
//					double y = ((coordinate.x - this.offset[0])
//							* Math.sin(angle) + (coordinate.y - this.offset[1])
//							* Math.cos(angle));
//					preXPoints[preIndex] = 500 + (int) (x / zoom);
//					preYPoints[preIndex] = 500 - (int) (y / zoom);
//					preIndex++;
//				}
//				graphics.drawPolyline(preXPoints, preYPoints,
//						preCoordinates.length);
//			}
			
			
			graphics.setColor(Color.WHITE);
			graphics.drawString(
					"Off: " + this.offset[0] + "/" + this.offset[1], 5, 35 + 20);
			graphics.drawString("Angle: " + this.angle, 5, 35 + 30);
		}
		this.segments.clear();
	}

	@Override
	public void keyPressed(KeyEvent event) {
		double angle = Math.toRadians(this.angle);
		double radian = Math.toRadians(180);
		switch (event.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			if (event.isControlDown()) {
				this.offset[0] += (10.0 * Math.cos(radian - angle));
				this.offset[1] += (10.0 * Math.sin(radian - angle));
			} else {
				this.offset[0] += Math.cos(radian - angle);
				this.offset[1] += Math.sin(radian - angle);
			}
			break;
		case KeyEvent.VK_LEFT:
			if (event.isControlDown()) {
				this.offset[0] -= (10.0 * Math.cos(radian - angle));
				this.offset[1] -= (10.0 * Math.sin(radian - angle));
			} else {
				this.offset[0] -= Math.cos(radian - angle);
				this.offset[1] -= Math.sin(radian - angle);
			}
			break;
		case KeyEvent.VK_DOWN:
			if (event.isControlDown()) {
				this.offset[0] += (10.0 * Math.sin(angle));
				this.offset[1] += (10.0 * Math.cos(angle));
			} else {
				this.offset[0] += Math.sin(angle);
				this.offset[1] += Math.cos(angle);
			}
			break;
		case KeyEvent.VK_UP:
			if (event.isControlDown()) {
				this.offset[0] -= (10.0 * Math.sin(angle));
				this.offset[1] -= (10.0 * Math.cos(angle));
			} else {
				this.offset[0] -= Math.sin(angle);
				this.offset[1] -= Math.cos(angle);
			}
			break;
		case KeyEvent.VK_PAGE_UP:
			if (event.isControlDown()) {
				if (zoom > 1) {
					zoom--;
					// System.out.println(zoom);
				}
			} else {
				this.angle += 0.5f;
			}
			break;
		case KeyEvent.VK_PAGE_DOWN:
			if (event.isControlDown()) {
				zoom++;
				// System.out.println(zoom);
			} else {
				this.angle -= 0.5f;
			}
			break;
		}

	}

	int keyCode;

	@Override
	public void keyReleased(KeyEvent event) {
		keyCode = event.getKeyCode();
	}

	@Override
	public void keyTyped(KeyEvent event) {
		keyCode = event.getKeyCode();
	}

	// @Override
	// public void keyReleased(KeyEvent arg0) {
	// throw new RuntimeException("NotImplemented");
	// }
	//
	// @Override
	// public void keyTyped(KeyEvent arg0) {
	// throw new RuntimeException("NotImplemented");
	// }

}
