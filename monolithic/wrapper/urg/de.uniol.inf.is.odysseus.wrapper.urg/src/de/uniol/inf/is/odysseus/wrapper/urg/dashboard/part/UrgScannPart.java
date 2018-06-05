package de.uniol.inf.is.odysseus.wrapper.urg.dashboard.part;

import java.util.Collection;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.wrapper.urg.datatype.UrgScann;

public class UrgScannPart extends AbstractDashboardPart implements
		PaintListener, MouseWheelListener, MouseListener {
	public static final String SETTINGS_LINE_COLOR = "lineColor";
	public static final String SETTINGS_POINT_COLOR = "pointColor";

	/** Front constant. */
	public static final int FRONT = 384;

	public static final int MAX_ZOOM = 2;

	public static final int MIN_ZOOM = -10;

	/** Holds the parent component. */
	private Composite parent;

	/** Canvas to draw the color map on. */
	private Canvas canvas;

	/** Is the dashboard part running. */
	private boolean isRunning = false;

	/** Position of the data. */
	private int urgScannPos;

	/** Last fetched data. */
	private UrgScann data;

	/** Zoom Level. */
	private int zoomLevel = -4;

	private int lineColorInt = UrgScannConfigurer.rgbToInt(new RGB(255, 240,
			240));

	private Color lineColor;

	private int pointColorInt = UrgScannConfigurer.rgbToInt(new RGB(255, 0, 0));

	private Color pointColor;

	@Override
	public void onStart(Collection<IPhysicalOperator> physicalRoots)
			throws Exception {
		super.onStart(physicalRoots);

		for (IPhysicalOperator po : physicalRoots) {
			SDFSchema schema = po.getOutputSchema();
			this.urgScannPos = -1;
			final SDFAttribute urgScannAttribute = schema
					.findAttribute("urgScann");
			if (urgScannAttribute != null) {
				this.urgScannPos = schema.indexOf(urgScannAttribute);
				isRunning = true;
				return;
			}
		}
	}

	@Override
	public void onStop() {
		isRunning = false;
	}

	@Override
	public void createPartControl(Composite p, ToolBar toolbar) {
		this.parent = p;
		canvas = new Canvas(parent, SWT.BORDER | SWT.NO_BACKGROUND
				| SWT.DOUBLE_BUFFERED);
		canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
		canvas.setBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_WHITE));
		canvas.addPaintListener(this);
		canvas.addMouseWheelListener(this);
		canvas.addMouseListener(this);

		setLineColor(lineColorInt);
		setPointColor(pointColorInt);
	}

	@Override
	public void paintControl(PaintEvent e) {
		e.gc.setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		e.gc.fillRectangle(canvas.getClientArea());

		if (data == null)
			return;

		float[] len = data.getData();
		if (len == null)
			return;

		float step = (float) (Math.PI / 512.0f);
		float start = step * data.getStartingStep();
		float rot = (512 - FRONT) * step;
		int cX = canvas.getClientArea().width / 2;
		int cY = canvas.getClientArea().height / 2;
		float zoom = (float) Math.pow(1.5f, zoomLevel);

		e.gc.setAntialias(SWT.ON);
		e.gc.setForeground(lineColor);
		for (int i = 0; i < len.length; i++) {
			float current = start + step * i + rot;
			float x = (float) Math.sin(current) * len[i] * zoom;
			float y = (float) Math.cos(current) * len[i] * zoom;

			e.gc.drawLine(cX, cY, cX + (int) x, cY + (int) y);
		}

		e.gc.setBackground(pointColor);
		for (int i = 0; i < len.length; i++) {
			float current = start + step * i + rot;
			float x = (float) Math.sin(current) * len[i] * zoom;
			float y = (float) Math.cos(current) * len[i] * zoom;

			e.gc.fillArc(cX + (int) x - 1, cY + (int) y - 1, 3, 3, 0, 360);
		}
	}

	public void setLineColor(int color) {
		lineColorInt = color;
		RGB rgb = UrgScannConfigurer.intToRgb(lineColorInt);
		lineColor = new Color(parent.getShell().getDisplay(), rgb);
	}

	public int getLineColor() {
		return lineColorInt;
	}

	public void setPointColor(int color) {
		pointColorInt = color;
		RGB rgb = UrgScannConfigurer.intToRgb(pointColorInt);
		pointColor = new Color(parent.getShell().getDisplay(), rgb);
	}

	public int getPointColor() {
		return pointColorInt;
	}

	@Override
	public void streamElementRecieved(IPhysicalOperator senderOperator, IStreamObject<?> element, int port) {
		Tuple<?> tuple = (Tuple<?>) element;
		if (tuple.size() <= this.urgScannPos) {
			return;
		}
		Object obj = tuple.getAttribute(this.urgScannPos);
		if (!UrgScann.class.isInstance(obj)) {
			return;
		}

		data = (UrgScann) obj;
		canvas.getDisplay().asyncExec(redraw);
	}

	/**
	 * Delegate runnable to invoke a redraw from an other thread.
	 */
	private Runnable redraw = new Runnable() {
		@Override
		public void run() {
			if (isRunning) {
				canvas.redraw();
			}
		}
	};

	@Override
	public void punctuationElementRecieved(IPhysicalOperator senderOperator, IPunctuation point, int port) {
	}

	@Override
	public void mouseScrolled(MouseEvent e) {
		int nZ = zoomLevel;
		if (e.count < 0) {
			nZ -= 1;
		} else {
			nZ += 1;
		}
		if (nZ > MAX_ZOOM)
			nZ = MAX_ZOOM;
		if (nZ < MIN_ZOOM)
			nZ = MIN_ZOOM;
		zoomLevel = nZ;
		canvas.getDisplay().asyncExec(redraw);
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
	}

	@Override
	public void mouseDown(MouseEvent e) {
		canvas.setFocus();
	}

	@Override
	public void mouseUp(MouseEvent e) {
	}

	@Override
	public void onLoad(Map<String, String> saved) {
		if (saved.containsKey(SETTINGS_LINE_COLOR))
			lineColorInt = Integer.parseInt(saved.get(SETTINGS_LINE_COLOR));
		if (saved.containsKey(SETTINGS_POINT_COLOR))
			pointColorInt = Integer.parseInt(saved.get(SETTINGS_POINT_COLOR));
	}

	@Override
	public Map<String, String> onSave() {
		Map<String, String> saveMap = Maps.newHashMap();
		saveMap.put(SETTINGS_LINE_COLOR, String.valueOf(lineColorInt));
		saveMap.put(SETTINGS_POINT_COLOR, String.valueOf(pointColorInt));
		return saveMap;
	}
}