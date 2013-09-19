package de.uniol.inf.is.odysseus.wrapper.kinect.dashboard.part;

import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.MILLISECONDS_IN_SECOND;

import java.util.Collection;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.wrapper.kinect.datatype.KinectDepthMap;

/**
 * This dashboard part can be used with the dashboard feature. It shows the current
 * {@link KinectDepthMap} in the stream.
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public class KinectDepthMapDashboardPart extends AbstractDashboardPart
        implements PaintListener {
    /** Key value for setting. */
    public static final String SETTINGS_FPS_SHOW = "FpsShow";

    /** Stores the position of the color map in the tuple. */
    private int depthMapPos;

    /** Is the dashboard part running. */
    private boolean isRunning = false;

    /** Defines, whether to show the FPS counter. */
    private boolean fpsShow = false;

    /** Holds the parent component. */
    private Composite parent;

    /** Canvas to draw the color map on. */
    private Canvas canvas;

    /** Label for the FPS Counter. */
    private Label label;

    /** Last fetched colorMap of the stream. */
    private KinectDepthMap depthMap;

    /** Counter for frames since last second. */
    private int frames = 0;

    /** Last measured FPS, to show (updated each second). */
    private int fps = 0;

    /** Stores the time, when FPS was last updated. */
    private long startTime;

    @Override
    public void onStart(Collection<IPhysicalOperator> physicalRoots) throws Exception {
        for (IPhysicalOperator po : physicalRoots) {
            SDFSchema schema = po.getOutputSchema();
            this.depthMapPos = -1;
            final SDFAttribute depthMapAttribute = schema
                    .findAttribute("depthMap");
            if (depthMapAttribute != null) {
                this.depthMapPos = schema.indexOf(depthMapAttribute);
                startTime = System.currentTimeMillis();
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

        label = new Label(parent, SWT.NO_BACKGROUND);
        label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        label.setBackground(Display.getCurrent()
                .getSystemColor(SWT.COLOR_WHITE));
        label.setText("FPS : ?");

        //fpsShow = getSettingValue(FPS_COUNTER, false);
        setFpsShow(fpsShow);
    }

    /**
     * Set the FPS counter visible and do the layout.
     * @param visible
     * boolean.
     */
    public void setFpsShow(boolean fpsShow) {
        if (label == null || parent == null) {
            return;
        }

        this.fpsShow = fpsShow;
        label.setVisible(fpsShow);
        ((GridData) label.getLayoutData()).exclude = !fpsShow;
        parent.layout(true);
    }
    
    /**
     * Determines, whether the FPS counter is visible
     * @return
     * true if the FPS counter is visible.
     */
    public boolean getFpsShow() {
        return this.fpsShow;
    }

    @Override
    public void paintControl(PaintEvent e) {
        if (depthMap == null) {
            return;
        }

        e.gc.fillRectangle(canvas.getClientArea());
        ImageData imgData = depthMap.getImageData();
        Image img = new Image(canvas.getDisplay(), imgData);

        float aspectRatio = (float) imgData.width / (float) imgData.height;
        int width = canvas.getClientArea().width;
        int height = (int) (canvas.getClientArea().width / aspectRatio);
        if (height > canvas.getClientArea().height) {
            height = canvas.getClientArea().height;
            width = (int) (canvas.getClientArea().height * aspectRatio);
        }
        e.gc.drawImage(img, 0, 0, imgData.width, imgData.height, 0, 0, width,
                height);

        if (fpsShow) {
            label.setText("FPS : " + fps);
        }
        img.dispose();
    }

    @Override
    public void streamElementRecieved(IPhysicalOperator senderOperator, IStreamObject<?> element, int port) {
        Tuple<?> tuple = (Tuple<?>) element;
        if (tuple.size() <= this.depthMapPos) {
            return;
        }
        Object obj = tuple.getAttribute(this.depthMapPos);
        if (!KinectDepthMap.class.isInstance(obj)) {
            return;
        }
        depthMap = (KinectDepthMap) obj;

        // frame counter
        frames++;
        long nowTime = System.currentTimeMillis();
        if (nowTime - startTime > MILLISECONDS_IN_SECOND) {
            startTime = nowTime;
            fps = frames;
            frames = 0;
        }

        canvas.getDisplay().asyncExec(redraw);
    }

    @Override
    public void punctuationElementRecieved(IPhysicalOperator senderOperator, IPunctuation point, int port) {
    }

    @Override
    public void securityPunctuationElementRecieved(IPhysicalOperator senderOperator, ISecurityPunctuation sp,
            int port) {
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
    public void onLoad(Map<String, String> saved) {
        if (saved.containsKey(SETTINGS_FPS_SHOW))
            fpsShow = Boolean.valueOf(saved.get(SETTINGS_FPS_SHOW));
    }
    
    @Override
    public Map<String, String> onSave() {
        Map<String, String> saveMap = Maps.newHashMap();
        saveMap.put(SETTINGS_FPS_SHOW, String.valueOf(fpsShow));
        return saveMap;
    }
}
