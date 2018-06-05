package de.uniol.inf.is.odysseus.rcp.dashboard.windpower;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Map;

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

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;

/**
 * The WindTurbineDashboardPart is a DashboardPart that can visualize a rotating
 * rotor. Incoming stream elements must contain the current rotational speed.
 *
 * @author Dennis Nowak
 *
 */
public class WindTurbineDashboardPart extends AbstractDashboardPart implements
		PaintListener {

	private int updateIntervalMilis = 100;
	private boolean showWarning = true;
	private boolean showError = true;
	private boolean showText = true;
	private String rotorImagePath = "/resource/rotor.png";

	public boolean isShowText() {
		return showText;
	}

	public void setShowText(boolean showText) {
		this.showText = showText;
	}

	public int getUpdateInterval() {
		return updateIntervalMilis;
	}

	public void setUpdateInterval(int interval) {
		updateIntervalMilis = interval;
	}

	public boolean isShowWarning() {
		return showWarning;
	}

	public void setShowWarning(boolean showWaring) {
		this.showWarning = showWaring;
	}

	public boolean isShowError() {
		return showError;
	}

	public void setShowError(boolean showError) {
		this.showError = showError;
	}

	public String getRotorImagePath() {
		return rotorImagePath;
	}

	public void setRotorImagePath(String rotorImagePath) {
		this.rotorImagePath = rotorImagePath;
	}

	private Composite windTurbineComposite;
	private Canvas windTurbineCanvas;
	private CanvasUpdater canvasUpdater;

	private Image rotor;
	private double currentRotationalSpeed;
	// Position of the Rotor in degrees
	private double currentRotation;
	private long lastDraw;
	private boolean isError;
	private boolean isWarning;

	@Override
	public void createPartControl(Composite parent, ToolBar toolbar) {
		parent.setLayout(new FillLayout());
		currentRotation = 0;
		// currentRotationalSpeed = 0;
		lastDraw = new Date().getTime();
		this.windTurbineComposite = new Composite(parent, SWT.NONE);
		this.windTurbineComposite.setLayout(new FillLayout());
		this.windTurbineCanvas = new Canvas(windTurbineComposite, SWT.NONE);
		this.windTurbineCanvas.setBackground(Display.getCurrent()
				.getSystemColor(SWT.COLOR_WHITE));
		this.windTurbineCanvas.addPaintListener(this);
		rotor = loadImage(rotorImagePath);
		canvasUpdater = new CanvasUpdater(this.windTurbineCanvas,
				updateIntervalMilis);
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
		Image image = new Image(Display.getCurrent(), path);
		return image;
	}

	@Override
	public void streamElementRecieved(IPhysicalOperator senderOperator,
			IStreamObject<?> element, int port) {
		Tuple<?> tuple = (Tuple<?>) element;
		this.currentRotationalSpeed = tuple.getAttribute(0);
		isError = tuple.getAttribute(2);
		isWarning = tuple.getAttribute(1);

	}

	@Override
	public void punctuationElementRecieved(IPhysicalOperator senderOperator,
			IPunctuation point, int port) {
		// Do nothing

	}

	@Override
	public void dispose() {
		canvasUpdater.stopRunning();

		if (!windTurbineCanvas.isDisposed()) {
			windTurbineCanvas.removePaintListener(this);
			windTurbineCanvas.dispose();
		}

	}

	@Override
	public synchronized void paintControl(PaintEvent e) {
		// paintTower(gc);
		GC gc = e.gc;
		paintRotor(gc);
		if (showText) {
			gc.setTransform(null);
			NumberFormat nf = java.text.NumberFormat.getInstance();
			nf.setMaximumFractionDigits(2);
			String text = nf.format(new BigDecimal(currentRotationalSpeed));
			e.gc.drawText(String.valueOf(text), 0, 0);
		}
	}

	public void paintTower(GC gc) {
		gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
		int[] towerShape = { 60, 10, 50, 1000, 80, 1000, 70, 10 };
		gc.fillPolygon(towerShape);
	}

	public void paintRotor(GC gc) {
		Transform transform = new Transform(gc.getDevice());
		int width = rotor.getBounds().width;
		int height = rotor.getBounds().height;
		// Scaling
		float widthRatio = ((float) this.windTurbineCanvas.getBounds().width)
				/ width;
		float heightRatio = ((float) this.windTurbineCanvas.getBounds().height)
				/ height;
		float ratio = Math.min(widthRatio, heightRatio);

		// Positioning
		int x = 0;
		int y = 0;
		// Rotating
		long now = new Date().getTime();
		float time = (now - lastDraw) / 1000f;
		currentRotation = ((currentRotation + (currentRotationalSpeed * 360 * time)) % 360);
		// paint error or warning
		if (showError && isError) {
			windTurbineCanvas.setBackground(windTurbineCanvas.getDisplay()
					.getSystemColor(SWT.COLOR_RED));
		} else if (showWarning && isWarning) {
			windTurbineCanvas.setBackground(windTurbineCanvas.getDisplay()
					.getSystemColor(SWT.COLOR_YELLOW));
		} else {
			windTurbineCanvas.setBackground(windTurbineCanvas.getDisplay()
					.getSystemColor(SWT.COLOR_WHITE));
		}
		transform.translate(x + (width * ratio) / 2, y + (height * ratio) / 2);
		transform.rotate((float) currentRotation);
		transform
				.translate(-x - (width * ratio) / 2, -y - (height * ratio) / 2);

		transform.scale(ratio, ratio);
		gc.setTransform(transform);
		lastDraw = now;
		gc.drawImage(rotor, x, y);
	}

	@Override
	public Map<String, String> onSave() {
		Map<String, String> map = Maps.newHashMap();
		map.put("showError", String.valueOf(showError));
		map.put("showWarning", String.valueOf(showWarning));
		map.put("showText", String.valueOf(showText));
		map.put("rotorImagePath", rotorImagePath);
		map.put("updateInterval", String.valueOf(updateIntervalMilis));
		return map;
	}

	@Override
	public void onLoad(Map<String, String> saved) {
		String showError = saved.get("showError");
		String showWarning = saved.get("showWarning");
		String showText = saved.get("showText");
		String rotorImagePath = saved.get("rotorImagePath");
		String updateInterval = saved.get("updateInterval");

		this.showError = Strings.isNullOrEmpty(showError) ? true : Boolean
				.parseBoolean(showError);
		this.showWarning = Strings.isNullOrEmpty(showWarning) ? true : Boolean
				.parseBoolean(showWarning);
		this.showText = Strings.isNullOrEmpty(showText) ? true : Boolean
				.parseBoolean(showText);
		this.rotorImagePath = Strings.isNullOrEmpty(rotorImagePath) ? "/resource/rotor.png"
				: rotorImagePath;
		this.updateIntervalMilis = Strings.isNullOrEmpty(updateInterval) ? 100
				: Integer.parseInt(updateInterval);
	}

}
