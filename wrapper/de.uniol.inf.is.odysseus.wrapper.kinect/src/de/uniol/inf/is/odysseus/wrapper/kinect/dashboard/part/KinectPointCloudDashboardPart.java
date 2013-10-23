package de.uniol.inf.is.odysseus.wrapper.kinect.dashboard.part;

import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.MILLISECONDS_IN_SECOND;

import java.util.Collection;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLProfile;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.media.opengl.glu.GLU;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.wrapper.kinect.datatype.KinectPointCloud;
import de.uniol.inf.is.odysseus.wrapper.kinect.datatype.KinectSkeletonMap;

/**
 * This dashboard part can be used with the dashboard feature. It shows the current
 * {@link KinectPointCloud} in the stream. This dashboard part is using hardware
 * acceleration with OpenGL.
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public class KinectPointCloudDashboardPart extends AbstractDashboardPart
        implements MouseListener, MouseMoveListener, MouseWheelListener {
    /** Key value for setting. */
    public static final String SETTINGS_FPS_SHOW = "FpsShow";
    
    /** Key value for setting. */
    public static final String SETTINGS_POINT_SIZE = "PointSize";
    
    /** Default zoom level. */
    public static final float DEFAULT_ZOOM = 3;

    /** Defines the maximal zoom level. */
    public static final float MAX_ZOOM = 20;
    
    /** Defines the minimal point size. */
    public static final int MIN_POINT_SIZE = 1;

    /** Defines the maximal point size. */
    public static final int MAX_POINT_SIZE = 10;

    /** Defines the red clear color value. */
    public static final float CLEAR_R = 0.3f;

    /** Defines the green clear color value. */
    public static final float CLEAR_G = 0.5f;

    /** Defines the blue clear color value. */
    public static final float CLEAR_B = 0.8f;

    /** Field of view in degrees. */
    public static final float FIELD_OF_VIEW = 45;

    /** Range of the near clipping plane. */
    public static final float NEAR_PLANE = 0.1f;

    /** Range of the far clipping plane. */
    public static final float FAR_PLANE = 50;

    /** Position of the data in tuple. */
    private int pointCloudPos;
    
    /** Position of the data in tuple. */
    private int skeletonMapPos;

    /** Last fetched point cloud data. */
    private KinectPointCloud pointCloud;
    
    /** Last fetched skeleton map data. */
    private KinectSkeletonMap skeletonMap;

    /** Holds the parent component. */
    private Composite parent;

    /** Label for the FPS Counter. */
    private Label label;

    /** Holds the OpenGL canvas. */
    private Composite composite;

    /** Widget that displays OpenGL content. */
    private GLCanvas glcanvas;

    /** Used to get OpenGL object that we need to access OpenGL functions. */
    private GLContext glcontext;

    /** Current zoom level (distance from object). */
    private float zoom = DEFAULT_ZOOM;

    /** Rotation angle around the object in degrees. */
    private int rotY = 0;

    /** Rotation angle up and down in degrees. */
    private int rotX = 0;

    /** Stores the rotation before mouse down. */
    private int startRotX = 0;

    /** Stores the rotation before mouse down. */
    private int startRotY = 0;

    /** Stores the mouse position on click. */
    private int startMouseX = 0;

    /** Stores the mouse position on click. */
    private int startMouseY = 0;

    /** Size of the points. */
    private int pointSize = 2;

    /** Stores the mouse down state. */
    private boolean mouseDown = false;

    /** Counter for frames since last second. */
    private int frames = 0;

    /** Last measured FPS, to show (updated each second). */
    private int fps = 0;

    /** Stores the time, when FPS was last updated. */
    private long startTime;

    /** Defines, whether to show the FPS counter. */
    private boolean fpsShow = false;

    @Override
    public void onStart(Collection<IPhysicalOperator> physicalRoots) throws Exception {
    	super.onStart(physicalRoots);
    	
        findPointCloud(physicalRoots);
        findSkeletonMap(physicalRoots);
    }
    
    private void findPointCloud(Collection<IPhysicalOperator> physicalRoots) {
        for (IPhysicalOperator po : physicalRoots) {
            SDFSchema schema = po.getOutputSchema();
            this.pointCloudPos = -1;
            final SDFAttribute pointCloudAttribute = schema
                    .findAttribute("pointCloud");
            if (pointCloudAttribute != null) {
                this.pointCloudPos = schema.indexOf(pointCloudAttribute);
                return;
            }
        }
    }
    
    private void findSkeletonMap(Collection<IPhysicalOperator> physicalRoots) {
        for (IPhysicalOperator po : physicalRoots) {
            SDFSchema schema = po.getOutputSchema();
            this.skeletonMapPos = -1;
            final SDFAttribute skeletonMapAttribute = schema
                    .findAttribute("skeletonMap");
            if (skeletonMapAttribute != null) {
                this.skeletonMapPos = schema.indexOf(skeletonMapAttribute);
                return;
            }
        }
    }

    @Override
    public void createPartControl(Composite compositeParent, ToolBar toolbar) {
        this.parent = compositeParent;
        GLProfile glprofile = GLProfile.get(GLProfile.GL2);

        composite = new Composite(compositeParent, SWT.BORDER);
        composite.setLayout(new FillLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

        GLData gldata = new GLData();
        gldata.doubleBuffer = true;
        glcanvas = new GLCanvas(composite, SWT.NO_BACKGROUND, gldata);
        glcanvas.setCurrent();
        glcontext = GLDrawableFactory.getFactory(glprofile)
                .createExternalGLContext();

        glcanvas.addListener(SWT.Resize, new Listener() {
            public void handleEvent(Event event) {
                Rectangle bounds = glcanvas.getBounds();
                float fAspect = (float) bounds.width / (float) bounds.height;
                glcanvas.setCurrent();
                glcontext.makeCurrent();
                GL2 gl = glcontext.getGL().getGL2();
                gl.glViewport(0, 0, bounds.width, bounds.height);
                gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
                gl.glLoadIdentity();
                GLU glu = new GLU();
                glu.gluPerspective(FIELD_OF_VIEW, fAspect, NEAR_PLANE,
                        FAR_PLANE);
                gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
                gl.glLoadIdentity();
                glcontext.release();
            }
        });

        glcanvas.addMouseListener(this);
        glcanvas.addMouseMoveListener(this);
        glcanvas.addMouseWheelListener(this);

        glcontext.makeCurrent();
        GL2 gl = glcontext.getGL().getGL2();
        gl.setSwapInterval(0);
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        gl.glClearDepth(1.0);
        gl.glLineWidth(2);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_POINT_SIZE);
        glcontext.release();

        (new Thread() {
            public void run() {
                while ((glcanvas != null) && !glcanvas.isDisposed()) {
                    render();
                    try {
                        sleep(1);
                    } catch (InterruptedException interruptedexception) {
                    }
                }
            }
        }).start();

        label = new Label(parent, SWT.NO_BACKGROUND);
        label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        label.setBackground(Display.getCurrent()
                .getSystemColor(SWT.COLOR_WHITE));
        label.setText("FPS : ?");

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

    /**
     * Sets the point size.
     * @param size
     * size of the points in px.
     */
    public void setPointSize(int size) {
        if (size < MIN_POINT_SIZE) {
            size = MAX_POINT_SIZE;
        }
        if (size > MAX_POINT_SIZE) {
            size = MAX_POINT_SIZE;
        }
        pointSize = size;
    }
    
    public int getPointSize() {
        return pointSize;
    }

    /**
     * Main rendering function. Is called rapidly.
     */
    protected void render() {
        // frame counter
        frames++;
        long nowTime = System.currentTimeMillis();
        if (nowTime - startTime > MILLISECONDS_IN_SECOND) {
            startTime = nowTime;
            fps = frames;
            frames = 0;
            PlatformUI.getWorkbench().getDisplay().syncExec(fpsUpdate);
        }
        PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
            public void run() {
                if ((glcanvas != null) && !glcanvas.isDisposed()) {
                    glcanvas.setCurrent();
                    glcontext.makeCurrent();
                    GL2 gl = glcontext.getGL().getGL2();
                    gl.glPointSize(pointSize);
                    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
                    gl.glClearColor(CLEAR_R, CLEAR_G, CLEAR_B, 1.0f);
                    gl.glLoadIdentity();

                    GLU glu = new GLU();
                    glu.gluLookAt(0, 0, zoom, 0, 0, 0, 0, 1, 0);
                    gl.glRotatef(rotX, 1, 0, 0);
                    gl.glRotatef(rotY, 0, 1, 0);

                    renderPointCloud(gl);
                    renderSkeleton(gl);
                    
                    glcanvas.swapBuffers();
                    glcontext.release();
                }
            }
        });
    }

    /**
     * helper method for rendering. Only handle point cloud here.
     * @param gl
     * GL Object to render on.
     */
    protected void renderPointCloud(GL2 gl) {
        if (pointCloud == null) {
            return;
        }

        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_POINT);
        pointCloud.render(gl);
    }
    
    /**
     * helper method for rendering. Only handle skeletons here.
     * @param gl
     * GL Object to render on.
     */
    protected void renderSkeleton(GL2 gl) {
        if (skeletonMap == null) {
            return;
        }

        gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
        gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2GL3.GL_LINE);
        gl.glBegin(GL2.GL_LINES);
        skeletonMap.render(gl);
        gl.glEnd();
    }

    @Override
    public void dispose() {
        glcanvas.dispose();
        super.dispose();
    }

    @Override
    public void streamElementRecieved(IPhysicalOperator senderOperator, IStreamObject<?> element, int port) {
        findPointCloudInStream(element, port);
        findSkeletonMapInStream(element, port);
    }
    
    private void findPointCloudInStream(IStreamObject<?> element, int port) {
        Tuple<?> tuple = (Tuple<?>) element;
        if (tuple.size() <= this.pointCloudPos) {
            return;
        }
        Object obj = tuple.getAttribute(this.pointCloudPos);
        if (!KinectPointCloud.class.isInstance(obj)) {
            return;
        }

        pointCloud = (KinectPointCloud) obj;
    }
    
    private void findSkeletonMapInStream(IStreamObject<?> element, int port) {
        Tuple<?> tuple = (Tuple<?>) element;
        if (tuple.size() <= this.skeletonMapPos) {
            return;
        }
        Object obj = tuple.getAttribute(this.skeletonMapPos);
        if (!KinectSkeletonMap.class.isInstance(obj)) {
            return;
        }

        skeletonMap = (KinectSkeletonMap) obj;
    }

    @Override
    public void punctuationElementRecieved(IPhysicalOperator senderOperator, IPunctuation point, int port) {
    }

    @Override
    public void securityPunctuationElementRecieved(IPhysicalOperator senderOperator, ISecurityPunctuation sp,
            int port) {
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
    }

    @Override
    public void mouseDown(MouseEvent e) {
        startRotX = rotX;
        startRotY = rotY;
        startMouseX = e.x;
        startMouseY = e.y;
        mouseDown = true;
        glcanvas.forceFocus();
    }

    @Override
    public void mouseUp(MouseEvent e) {
        mouseDown = false;
    }

    @Override
    public void mouseMove(MouseEvent e) {
        if (!mouseDown) {
            return;
        }

        int dx = e.x - startMouseX;
        int dy = e.y - startMouseY;
        rotX = startRotX + dy;
        rotY = startRotY + dx;
    }

    @Override
    public void mouseScrolled(MouseEvent e) {
        float nZoom = zoom - Math.signum(e.count);
        if (nZoom < 2) {
            nZoom = 2;
        }
        if (nZoom > MAX_ZOOM) {
            nZoom = MAX_ZOOM;
        }
        zoom = nZoom;
    }

    /**
     * Delegate runnable to invoke a FPS counter update from an other thread.
     */
    private Runnable fpsUpdate = new Runnable() {
        @Override
        public void run() {
            if (label != null) {
                label.setText("FPS : " + fps);
            }
        }
    };
    
    @Override
    public void onLoad(Map<String, String> saved) {
        if (saved.containsKey(SETTINGS_FPS_SHOW))
            fpsShow = Boolean.valueOf(saved.get(SETTINGS_FPS_SHOW));
        if (saved.containsKey(SETTINGS_POINT_SIZE))
            pointSize = Integer.valueOf(saved.get(SETTINGS_POINT_SIZE));
    }
    
    @Override
    public Map<String, String> onSave() {
        Map<String, String> saveMap = Maps.newHashMap();
        saveMap.put(SETTINGS_FPS_SHOW, String.valueOf(fpsShow));
        saveMap.put(SETTINGS_POINT_SIZE, String.valueOf(pointSize));
        return saveMap;
    }
}
