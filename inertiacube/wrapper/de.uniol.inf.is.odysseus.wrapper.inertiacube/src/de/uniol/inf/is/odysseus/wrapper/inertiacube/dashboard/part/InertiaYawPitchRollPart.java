package de.uniol.inf.is.odysseus.wrapper.inertiacube.dashboard.part;

import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.wrapper.inertiacube.datatype.YawPitchRoll;

public class InertiaYawPitchRollPart extends AbstractDashboardPart implements
		PaintListener {
	public static final String[] WORDS = new String[]{"Yaw", "Pitch", "Roll"};

    /** Holds the parent component. */
    private Composite parent;

    /** Canvas to draw the color map on. */
    private Canvas canvas;

    /** Is the dashboard part running. */
    private boolean isRunning = false;

    /** Position of the data. */
    private int yawPitchRollPos;

    /** Last received data. */
    private YawPitchRoll data;

    @Override
    public void onStart(Collection<IPhysicalOperator> physicalRoots)
    		throws Exception {
    	super.onStart(physicalRoots);

    	for (IPhysicalOperator po : physicalRoots) {
            SDFSchema schema = po.getOutputSchema();
            this.yawPitchRollPos = -1;
            final SDFAttribute yawPitchRollAttribute = schema
                    .findAttribute("yawPitchRoll");
            if (yawPitchRollAttribute != null) {
                this.yawPitchRollPos = schema.indexOf(yawPitchRollAttribute);
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
	}

	@Override
	public void paintControl(PaintEvent e) {
		e.gc.setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		e.gc.fillRectangle(canvas.getClientArea());

		int w = 100;
		int l = 10;
		int t = 30;
		int padding = 20;

		for (int i = 0; i < 3; i++) {
			e.gc.setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_DARK_CYAN));
			e.gc.fillArc(l, t, w, w, 0, 360);
			e.gc.setForeground(canvas.getDisplay().getSystemColor(SWT.COLOR_WHITE));
			e.gc.drawArc(l, t, w, w, 0, 360);

			e.gc.setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));
			e.gc.setForeground(canvas.getDisplay().getSystemColor(SWT.COLOR_GREEN));
			Point size = e.gc.textExtent(WORDS[i]);
			e.gc.drawString(WORDS[i], l + (w - size.x) / 2, (t - size.y) / 2);

			l = l + w + padding;
		}

		if (data == null)
			return;

		l = 10;
		e.gc.setBackground(canvas.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		for (int i = 0; i < 3; i++) {
			float val = data.getValue(i);
			int x = (int)(Math.sin(Math.toRadians(val)) * w * 0.45);
			int y = (int)(Math.cos(Math.toRadians(val)) * w * 0.45);
			e.gc.setForeground(canvas.getDisplay().getSystemColor(SWT.COLOR_GREEN));
			e.gc.drawLine(l + (w / 2), t + (w / 2), l + (w / 2) + x, t + (w / 2) - y);

			String valStr = String.format("%.2f", val);
			Point size = e.gc.textExtent(valStr);
			e.gc.setForeground(canvas.getDisplay().getSystemColor(SWT.COLOR_WHITE));
			e.gc.drawString(valStr, l + (w - size.x) / 2, t + w + size.y);

			l = l + w + padding;
		}
	}

	@Override
	public void streamElementRecieved(IPhysicalOperator senderOperator, IStreamObject<?> element, int port) {
		Tuple<?> tuple = (Tuple<?>) element;
        if (tuple.size() <= this.yawPitchRollPos) {
            return;
        }
        Object obj = tuple.getAttribute(this.yawPitchRollPos);
        if (!YawPitchRoll.class.isInstance(obj)) {
            return;
        }

        data = (YawPitchRoll) obj;
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
}
