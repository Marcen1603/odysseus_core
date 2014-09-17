package de.uniol.inf.is.odysseus.rcp.dashboard.windpower;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

public class WindTurbineDashboardPart extends AbstractDashboardPart implements PaintListener{
	
	private static final int UPDATE_INTERVAL_MILLIS = 100;
	private Composite windTurbineComposite;
	private Canvas windTurbineCanvas;
	private CanvasUpdater canvasUpdater;
	
	private Image rotor;
	private double currentRotationalSpeed = 0.3;
	// Position of the Rotor in degrees
	private double currentRotation;
	private long lastDraw;
	private GC gc;
	private Transform transform;
	
	private int x;
	private int y;

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		parent.setLayout(new FillLayout());
		currentRotation = 0;
		//currentRotationalSpeed = 0;
		lastDraw = new Date().getTime();
		this.windTurbineComposite = new Composite(parent, SWT.NONE);
		this.windTurbineComposite.setLayout(new FillLayout());
		this.windTurbineCanvas = new Canvas(windTurbineComposite, SWT.NONE);
		this.windTurbineCanvas.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		this.windTurbineCanvas.addPaintListener(this);
		this.gc = new GC(windTurbineCanvas);
		this.transform = new Transform(gc.getDevice());
		rotor = loadImage("/resource/rotor_noalpha.png");
		canvasUpdater = new CanvasUpdater(this.windTurbineCanvas, this, UPDATE_INTERVAL_MILLIS);
		canvasUpdater.start();
		parent.layout();
		
	}
	
	private Image loadImage(String pathInBundle) {
		URL url = Activator.getContext().getBundle().getResource(pathInBundle);
		String path;
		try {
			path = FileLocator.toFileURL(url).getPath();
		} catch (IOException e) {
			
			e.printStackTrace();
			return null;
		}
		Image image = new Image(Display.getCurrent(),path);
		return image;
	}

	@Override
	public void streamElementRecieved(IPhysicalOperator senderOperator,
			IStreamObject<?> element, int port) {
		Tuple<?> tuple = (Tuple<?>) element;
		this.currentRotationalSpeed = tuple.getAttribute(0);
	}

	@Override
	public void punctuationElementRecieved(IPhysicalOperator senderOperator,
			IPunctuation point, int port) {
		// Do nothing
		
	}

	@Override
	public void securityPunctuationElementRecieved(
			IPhysicalOperator senderOperator, ISecurityPunctuation sp, int port) {
		// Do nothing
		
	}
	
	@Override
	public void dispose() {
		canvasUpdater.stopRunning();
		
		if( !windTurbineCanvas.isDisposed() ) {
			windTurbineCanvas.removePaintListener(this);
			windTurbineCanvas.dispose();
		}
		
		disposeResources();
	}
	
	private void disposeResources() {
		transform.dispose();
		gc.dispose();
	}

	@Override
	public synchronized void paintControl(PaintEvent e) {
		// paintTower(gc);
		paintRotor(gc);
	}
	
	public void paintTower(GC gc) {
		gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		int[] towerShape = {60,10,50,1000,80,1000,70,10};
		gc.fillPolygon(towerShape);
	}
	
	public void paintRotor(GC gc) {
		gc.drawImage(rotor, x, y);
	}
	
	/*
	 * 
	 */
	public void computeTransformation() {
		transform.identity();
		// Scaling
		// Positioning
		x = 0;
		y = 0;
		int width = rotor.getBounds().width;
		int height = rotor.getBounds().height;
		// Rotating
		long now = new Date().getTime();
		float time = (now - lastDraw)/ 1000f;
		currentRotation = ((currentRotation + (currentRotationalSpeed * 360 * time)) %360);
		transform.translate(x+width/2, y+height/2);
        transform.rotate((float)currentRotation);
        transform.translate(-x-width/2, -y-height/2);
		gc.setTransform(transform);
		lastDraw = now;
	}


}
