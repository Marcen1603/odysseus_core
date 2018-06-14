package de.uniol.inf.is.odysseus.net.rcp.views;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.math.geometry.Vector3D;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNetException;
import de.uniol.inf.is.odysseus.net.ping.IPingMap;
import de.uniol.inf.is.odysseus.net.ping.IPingMapListener;
import de.uniol.inf.is.odysseus.net.ping.IPingMapNode;
import de.uniol.inf.is.odysseus.net.rcp.OdysseusNetRCPPlugIn;

public class PingMapView extends ViewPart implements PaintListener, MouseMoveListener, IPingMapListener {

	public static final String VIEW_ID = "de.uniol.inf.is.odysseus.net.rcp.PingMap";
	
	private Map<Vector3D, String> currentPoints = Maps.newHashMap();

	private Canvas canvas;
	
	@Override
	public void createPartControl(Composite parent) {
		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayout(new GridLayout(1, true));
		
		canvas = new Canvas(rootComposite, SWT.BORDER);
		canvas.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
		canvas.addPaintListener(this);
		canvas.addMouseMoveListener(this);
		
		OdysseusNetRCPPlugIn.getPingMap().addListener(this);
	}
	
	@Override
	public void dispose() {
		OdysseusNetRCPPlugIn.getPingMap().removeListener(this);
		
		super.dispose();
	}

	@Override
	public void setFocus() {
		canvas.setFocus();
	}

	@Override
	public void paintControl(PaintEvent e) {
		IPingMap pingMap = OdysseusNetRCPPlugIn.getPingMap();
		Collection<IPingMapNode> nodes = collectNodes(pingMap); 
		Map<Vector3D, String> points = collectPoints(nodes);
		Vector3D localPoint = pingMap.getLocalPosition();
		
		Map<Vector3D, String> shiftedPoints = shiftPoints( localPoint, points );

		double xFactor = 3;
		double yFactor = 3;
		
		GC gc = e.gc;
		drawBackground(gc, e, xFactor, yFactor);

		currentPoints.clear();
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		drawPingSymbol(gc, e.width / 2, e.height / 2, 1);
		
		try {
			currentPoints.put(new Vector3D(e.width / 2, e.height / 2), OdysseusNetRCPPlugIn.getOdysseusNodeManager().getLocalNode().getName());
		} catch (OdysseusNetException e1) {
		}
		
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		for( Vector3D shiftedPoint : shiftedPoints.keySet() ) {
			double x = ( e.width / 2 ) + ( shiftedPoint.getX() * xFactor);
			double y = ( e.height / 2 ) + ( shiftedPoint.getY() * yFactor);
			double z = shiftedPoint.getZ();
			
			double sizeFactor = ( z / localPoint.getZ() ) * 20;
			drawPingSymbol(gc, (int)x, (int)y, sizeFactor);
			currentPoints.put(new Vector3D(x, y, z), shiftedPoints.get(shiftedPoint));
		}
		
		gc.dispose();
	}
	
	private static void drawBackground(GC gc, PaintEvent e, double xFactor, double yFactor) {
		gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		drawCenteredCircle(gc, e, 10, xFactor, yFactor);
		drawCenteredCircle(gc, e, 20, xFactor, yFactor);
		drawCenteredCircle(gc, e, 30, xFactor, yFactor);
		drawCenteredCircle(gc, e, 40, xFactor, yFactor);
		drawCenteredCircle(gc, e, 50, xFactor, yFactor);
		gc.drawLine(e.width / 2, 0, e.width / 2, e.height);
		gc.drawLine(0, e.height / 2, e.width, e.height / 2);
	}

	private static void drawCenteredCircle(GC gc, PaintEvent e, int size, double xFactor, double yFactor) {
		int xRadius = (int)(size * 2 * xFactor);
		int yRadius = (int)(size * 2 * yFactor);
		gc.drawOval(e.width / 2 - (xRadius / 2), e.height / 2 - (yRadius / 2), xRadius, yRadius);
	}

	private static void drawPingSymbol(GC gc, int x, int y, double factor) {
		int size = (int)(10 * factor);
		size = Math.min(30, Math.max(size, 3));
		gc.fillOval(x - (size / 2), y - (size / 2), size, size);
	}

	private static Collection<IPingMapNode> collectNodes(IPingMap pingMap) {
		Collection<IPingMapNode> nodes = Lists.newArrayList(); 
		for( IOdysseusNode node : OdysseusNetRCPPlugIn.getOdysseusNodeCommunicator().getDestinationNodes()) {
			Optional<IPingMapNode> optPingNode = pingMap.getPingNode(node);
			if( optPingNode.isPresent() ) {
				nodes.add(optPingNode.get());
			}
		}
		return nodes;
	}
	
	private static Map<Vector3D, String> collectPoints(Collection<IPingMapNode> nodes) {
		Map<Vector3D, String> points = Maps.newHashMap();
		for( IPingMapNode node : nodes ) {
			points.put(node.getPosition(), determineName(node));
		}
		return points;
	}

	private static String determineName(IPingMapNode pingNode) {
		return pingNode.getNode().getName();
	}

	private static Map<Vector3D, String> shiftPoints(Vector3D localPoint, Map<Vector3D, String> points) {
		Map<Vector3D, String> result = Maps.newHashMap();
		for( Vector3D point : points.keySet() ) {
			result.put(point.subtract(localPoint), points.get(point));
		}
		return result;
	}

	@Override
	public void mouseMove(MouseEvent e) {
		double minDist = Double.MAX_VALUE;
		String minName = null;
		for( Vector3D currentPoint : currentPoints.keySet() ) {
			double distX = currentPoint.getX() - e.x;
			double distY = currentPoint.getY() - e.y;
			double dist = (distX * distX) + (distY * distY);
			
			if( dist < minDist ) {
				minDist = dist;
				minName = currentPoints.get(currentPoint);
			}
		}
		
		if( minDist < 15) {
			canvas.setToolTipText(minName);
		} else {
			canvas.setToolTipText("");
		}
	}

	@Override
	public void pingMapChanged() {
		if( canvas.isDisposed() ) {
			return;
		}
		
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				if( !canvas.isDisposed() ) {
					canvas.redraw();
				}
			}
		});
	}
}
