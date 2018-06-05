package de.uniol.inf.is.odysseus.rcp.dashboard.soccer;

import java.util.Collection;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

public abstract class AbstractSoccerDashboardPart extends AbstractDashboardPart implements PaintListener {

	private static final int UPDATE_INTERVAL_MILLIS = 50;
	private static final int FONT_SIZE = 8;
	private static final int POINT_SIZE_PIXELS = 8;

	private Composite soccerComposite;
	private Canvas soccerCanvas;
	private CanvasUpdater canvasUpdater;

	private Font playerIDFont;
	private Font timeFont;

	private Map<String, Integer> attributeIndexMap;

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		parent.setLayout(new FillLayout());
		
		soccerComposite = new Composite(parent, SWT.BORDER);
		soccerComposite.setLayout(new FillLayout());

		soccerCanvas = new Canvas(soccerComposite, SWT.BORDER);
		soccerCanvas.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
		initResources();
		soccerCanvas.addPaintListener(this);
		
		canvasUpdater = new CanvasUpdater(soccerCanvas, UPDATE_INTERVAL_MILLIS);
		canvasUpdater.start();
		
		parent.layout();
	}

	private void initResources() {
		playerIDFont = new Font(Display.getCurrent(), "Arial", FONT_SIZE, SWT.BOLD | SWT.ITALIC);
		timeFont = new Font(Display.getCurrent(), "Arial", 9, SWT.BOLD | SWT.ITALIC);
	}
	
	protected final Font getPlayerFont() {
		return playerIDFont;
	}
	
	protected final Font getFont() {
		return timeFont;
	}
	
	protected final Canvas getCanvas() {
		return soccerCanvas;
	}
	
	protected final Composite getComposite() {
		return soccerComposite;
	}
	
	protected final void renderBackground(GC gc) {
		gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		gc.setLineWidth(3);
		
		double xFactor = getCanvas().getSize().x / 3450.0; // streckung/dehnung
		double yFactor = getCanvas().getSize().y / 2160.0; // streckung/dehnung
		
		// side lines
		drawRectangle(gc, 150 * xFactor, 76 * yFactor, 3300 * xFactor, 2083 * yFactor);
		
		// left strafraum
		drawRectangle(gc, 150 * xFactor, 478 * yFactor, 644 * xFactor, 1675 * yFactor);
		drawRectangle(gc, 150 * xFactor, 812 * yFactor, 314 * xFactor, 1350 * yFactor);
		drawRectangle(gc, 120 * xFactor, 975 * yFactor, 150 * xFactor, 1188 * yFactor);
		
		// right
		drawRectangle(gc, 2805 * xFactor, 478 * yFactor, 3300 * xFactor, 1675 * yFactor);
		drawRectangle(gc, 3135 * xFactor, 812 * yFactor, 3300 * xFactor, 1350 * yFactor);
		drawRectangle(gc, 3300 * xFactor, 975 * yFactor, 3330 * xFactor, 1188 * yFactor);
		
		// middle
		gc.drawLine((int)(1725 * xFactor), (int)(76 * yFactor), (int)(1725 * xFactor), (int)(2083 * yFactor));
		gc.drawOval((int)(1450 * xFactor), (int)(810 * yFactor), (int)(550 * xFactor), (int)(540 * yFactor));
		
		// points
		drawPoint(gc, 1725 * xFactor, 1080 * yFactor);
		drawPoint(gc, 480 * xFactor, 1080 * yFactor);
		drawPoint(gc, 2970 * xFactor, 1080 * yFactor);
		
		// arcs
		gc.drawArc((int)(2695 * xFactor), (int)(812 * yFactor), (int)(550 * xFactor), (int)(534 * yFactor), 127, 105);
		gc.drawArc((int)(205 * xFactor), (int)(812 * yFactor), (int)(550 * xFactor), (int)(534 * yFactor), 305, 108);
	}
	
	private static void drawRectangle( GC gc, double x, double y, double x2, double y2 ) {
		gc.drawRectangle((int)(x), (int)(y), (int)(x2 - x), (int)(y2 - y));
	}
	
	private static void drawPoint( GC gc, double x, double y ) {
		gc.fillOval((int)(x - (POINT_SIZE_PIXELS / 2)), (int)(y - (POINT_SIZE_PIXELS / 2)), POINT_SIZE_PIXELS, POINT_SIZE_PIXELS);
	}
	
	protected final int getCoordX( int absX ) {
		int old = getCoordXImpl(absX);
		double factor = getCanvas().getSize().x / 862.0; // streckung/dehnung
		return (int)(old * factor);
	}
	
	protected final int getCoordY( int absY ) {
		int old = getCoordYImpl(absY);
		double factor = getCanvas().getSize().y / 532.0; // streckung/dehnung
		return (int)(old * factor);
	}

	private static int getCoordXImpl(int absX) {
		return (int) (((absX + 33960) / 67920f) * 790) + 36;
	}

	private static int getCoordYImpl(int absY) {
		return (int) ((absY / 52489f) * 506) + 15;
	}
	
	@Override
	public void onStart(Collection<IPhysicalOperator> physicalRoots) throws Exception {
		super.onStart(physicalRoots);
		
		attributeIndexMap = createAttributeIndexMap(physicalRoots.iterator().next().getOutputSchema());
	}
	
	protected final Integer getAttributeIndex( String attributeName ) {
		Integer index = attributeIndexMap.get(attributeName);
		return index != null ? index : -1;
	}
	
	private static Map<String, Integer> createAttributeIndexMap(SDFSchema schema) {
		Map<String, Integer> attributeIndexMap = Maps.newHashMap();
		for (int i = 0; i < schema.getAttributes().size(); i++) {
			attributeIndexMap.put(schema.getAttribute(i).getAttributeName(), i);
		}
		return attributeIndexMap;
	}
	
	@Override
	public void dispose() {
		canvasUpdater.stopRunning();
		
		if( !soccerCanvas.isDisposed() ) {
			soccerCanvas.removePaintListener(this);
			soccerCanvas.dispose();
		}
		
		disposeResources();
	}

	private void disposeResources() {
		playerIDFont.dispose();
		timeFont.dispose();
	}
}
