package de.uniol.inf.is.odysseus.peer.rcp.views;

import java.util.Collection;

import net.jxta.peer.PeerID;

import org.apache.commons.math.geometry.Vector3D;
import org.eclipse.swt.SWT;
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

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.util.RepeatingJobThread;
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;
import de.uniol.inf.is.odysseus.peer.ping.IPingMapNode;
import de.uniol.inf.is.odysseus.peer.rcp.RCPP2PNewPlugIn;

public class PingMapView extends ViewPart implements PaintListener {

	private RepeatingJobThread updateThread;
	
	@Override
	public void createPartControl(Composite parent) {
		Composite rootComposite = new Composite(parent, SWT.NONE);
		rootComposite.setLayout(new GridLayout(1, true));
		
		final Canvas canvas = new Canvas(rootComposite, SWT.BORDER);
		canvas.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
		canvas.addPaintListener(this);
		
		updateThread = new RepeatingJobThread(2000) {
			@Override
			public void doJob() {
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
		};
		updateThread.start();
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		updateThread.stopRunning();
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void paintControl(PaintEvent e) {
		IPingMap pingMap = RCPP2PNewPlugIn.getPingMap();
		Collection<IPingMapNode> nodes = collectNodes(pingMap); 
		Collection<Vector3D> points = collectPoints(nodes);
		Vector3D localPoint = pingMap.getLocalPosition();
		
		Collection<Vector3D> shiftedPoints = shiftPoints( localPoint, points );

		// consider centered local coordinates and default size of 15
		double minX = -15;
		double minY = -15;
		double maxX = 15;
		double maxY = 15;
		
		for( Vector3D shiftedPoint : shiftedPoints ) {
			if( shiftedPoint.getX() < minX ) {
				minX = shiftedPoint.getX();
			}
			if( shiftedPoint.getY() < minY ) {
				minY = shiftedPoint.getY();
			}

			if( shiftedPoint.getX() > maxX ) {
				maxX = shiftedPoint.getX();
			}
			if( shiftedPoint.getY() > maxY ) {
				maxY = shiftedPoint.getY();
			}
		}
		
		double xDist = maxX - minX;
		double yDist = maxY - minY;
		
		double xFactor = e.width / ( xDist + e.width * 0.1);
		double yFactor = e.height / ( yDist + e.height * 0.1);
		
		GC gc = e.gc;
		drawBackground(gc, e, xFactor, yFactor);

		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		drawPingSymbol(gc, e.width / 2, e.height / 2);
		
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		for( Vector3D shiftedPoint : shiftedPoints ) {
			double x = ( e.width / 2 ) + ( shiftedPoint.getX() * xFactor);
			double y = ( e.height / 2 ) + ( shiftedPoint.getY() * yFactor);
			
			drawPingSymbol(gc, (int)x, (int)y);
		}
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

	private static void drawPingSymbol(GC gc, int x, int y) {
		gc.fillOval(x - 5, y - 5, 10, 10);
	}

	private static Collection<IPingMapNode> collectNodes(IPingMap pingMap) {
		Collection<IPingMapNode> nodes = Lists.newArrayList(); 
		for( PeerID peerID : pingMap.getPeerIDs()) {
			nodes.add(pingMap.getNode(peerID).get());
		}
		return nodes;
	}
	
	private static Collection<Vector3D> collectPoints(Collection<IPingMapNode> nodes) {
		Collection<Vector3D> points = Lists.newArrayList();
		for( IPingMapNode node : nodes ) {
			points.add(node.getPosition());
		}
		return points;
	}

	private static Collection<Vector3D> shiftPoints(Vector3D localPoint, Collection<Vector3D> points) {
		Collection<Vector3D> result = Lists.newArrayList();
		for( Vector3D point : points ) {
			result.add(point.subtract(localPoint));
		}
		return result;
	}
}
