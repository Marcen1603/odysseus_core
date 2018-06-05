/*******************************************************************************
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.canvas;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Disk;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.PartialDisk;
import org.lwjgl.util.glu.Sphere;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.rcp.dashboard.AbstractDashboardPart;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.IColorSpace;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.colorspace.RGB;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.objects.Car;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.objects.House;
import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.objects.Tree;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id: AbstractGLCanvasDashboardPart.java | Fri Apr 10 16:46:52 2015
 *          +0800 | Christian Kuka $
 *
 */
@SuppressWarnings("unused")
public abstract class AbstractGLCanvasDashboardPart extends AbstractDashboardPart implements PaintListener, KeyListener {
    private final static String MAX_ELEMENTS = "maxElements";

    GLCanvas canvas;
    private final Queue<IStreamObject<?>> queue = new ConcurrentLinkedQueue<>();
    private GLCanvasUpdater updater;
    private Camera camera;
    private float xrot;
    private float xoff;
    private float yrot;
    private float yoff;
    private float zrot;
    private float zoff;
    private GC gc;
    private int maxElements = 100;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createPartControl(final Composite parent, final ToolBar toolbar) {
        parent.setLayout(new FillLayout());
        final Composite composite = new Composite(parent, SWT.BORDER);
        composite.setLayout(new FillLayout());
        this.xrot = this.yrot = this.zrot = 0.0f;
        this.xoff = this.yoff = 0.0f;
        this.zoff = -5.0f;

        final GLData data = new GLData();
        data.doubleBuffer = true;
        this.canvas = new GLCanvas(composite, SWT.NONE, data);
        this.canvas.setCurrent();

        try {
            GLContext.useContext(this.canvas);
        }
        catch (final LWJGLException e) {
            e.printStackTrace();
        }
        this.canvas.addListener(SWT.Resize, new Listener() {
            @Override
            public void handleEvent(final Event event) {
                final Rectangle bounds = AbstractGLCanvasDashboardPart.this.canvas.getBounds();
                final float fAspect = (float) bounds.width / (float) bounds.height;
                AbstractGLCanvasDashboardPart.this.canvas.setCurrent();
                try {
                    GLContext.useContext(AbstractGLCanvasDashboardPart.this.canvas);
                }
                catch (final LWJGLException e) {
                    e.printStackTrace();
                }
                GL11.glViewport(0, 0, bounds.width, bounds.height);
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glLoadIdentity();
                GLU.gluPerspective(45.0f, fAspect, 0.5f, 400.0f);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glLoadIdentity();
            }
        });

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClearDepth(1.0f);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);

        this.canvas.addPaintListener(this);
        this.canvas.addKeyListener(this);
        parent.layout();
        this.camera = new Camera();
        this.updater = new GLCanvasUpdater(this, this.canvas);
        this.updater.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyReleased(final KeyEvent e) {
        // TODO Auto-generated method stub

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void keyPressed(final KeyEvent e) {
        switch (e.keyCode) {
            case SWT.ARROW_UP:
                if ((e.stateMask & SWT.CTRL) != 0) {
                    this.xrot -= 0.5f;
                }
                else {
                    this.yoff += 0.05f;
                }
                break;
            case SWT.ARROW_DOWN:
                if ((e.stateMask & SWT.CTRL) != 0) {
                    this.xrot += 0.5f;
                }
                else {
                    this.yoff -= 0.05f;
                }
                break;
            case SWT.ARROW_LEFT:
                if ((e.stateMask & SWT.CTRL) != 0) {
                    this.yrot -= 0.5f;
                }
                else {
                    this.xoff -= 0.05f;
                }
                break;
            case SWT.ARROW_RIGHT:
                if ((e.stateMask & SWT.CTRL) != 0) {
                    this.yrot += 0.5f;
                }
                else {
                    this.xoff += 0.05f;
                }
                break;
            case SWT.PAGE_UP:
                this.zoff += 0.05f;
                break;
            case SWT.PAGE_DOWN:
                this.zoff -= 0.05f;
                break;
            default:
                break;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void streamElementReceived(final IPhysicalOperator operator, final IStreamObject<?> element, final int port) {
        this.queue.offer(element);
        while (this.queue.size() > this.getMaxElements()) {
            this.queue.poll();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void punctuationElementReceived(final IPhysicalOperator operator, final IPunctuation punctuation, final int port) {
        // Empty method
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paintControl(final PaintEvent event) {
        this.gc = new GC(this.getCanvas());
        this.canvas.setCurrent();
        try {
            GLContext.useContext(this.getCanvas());
        }
        catch (final LWJGLException e) {
            e.printStackTrace();
        }
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glLoadIdentity();
        GL11.glTranslatef(this.xoff, this.yoff, this.zoff);
        GL11.glRotatef(this.xrot, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(this.yrot, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(this.zrot, 0.0f, 0.0f, 1.0f);
        this.doPaint();

        this.canvas.swapBuffers();
        this.gc.dispose();
        this.gc = null;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void onLoad(Map<String, String> saved) {
        maxElements = Integer.valueOf(saved.get(MAX_ELEMENTS) != null ? saved.get(MAX_ELEMENTS) : "100");
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> onSave() {
        Map<String, String> toSaveMap = Maps.newHashMap();
        toSaveMap.put(MAX_ELEMENTS, String.valueOf(maxElements));
        return toSaveMap;
    }

    /**
     * @return the canvas
     */
    public GLCanvas getCanvas() {
        return this.canvas;
    }

    public void swap() {
        if ((this.canvas != null) && (!this.canvas.isDisposed())) {
            this.canvas.swapBuffers();
        }
    }

    /**
     * @return the object
     */
    public Collection<IStreamObject<?>> getObjects() {
        return Collections.unmodifiableCollection(this.queue);
    }

    public abstract void doPaint();

    /**
     * @return the maxElements
     */
    public int getMaxElements() {
        return this.maxElements;
    }

    /**
     * @param maxElements
     *            the maxElements to set
     */
    public void setMaxElements(final int maxElements) {
        this.maxElements = maxElements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        if (this.updater != null) {
            this.updater.interrupt();
        }

        if ((this.canvas != null) && (!this.canvas.isDisposed())) {
            this.canvas.removePaintListener(this);
            this.canvas.dispose();
        }

    }

    public void drawLine(final Coordinate start, final Coordinate end, final IColorSpace color) {
        final Color rgb = this.toColor(color);
        GL11.glBegin(GL11.GL_LINES);
        try {
            GL11.glColor3d(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
            GL11.glVertex3d(start.x, start.y, start.z);
            GL11.glVertex3d(end.x, end.y, end.z);
        }
        finally {
            GL11.glEnd();
        }
    }

    public void drawLine(final Coordinate[] points, final IColorSpace color) {
        final Color rgb = this.toColor(color);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        try {
            GL11.glColor3d(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
            for (final Coordinate point : points) {
                GL11.glVertex3d(point.x, point.y, point.z);
            }
        }
        finally {
            GL11.glEnd();
        }
    }

    /**
     * Draws a tree.
     *
     * @param trunkRadius
     * @param foliageRadius
     * @param height
     * @param slices
     * @param stacks
     * @param color
     */
    public void drawTree(final float trunkRadius, final float foliageRadius, final float height, final int slices, final int stacks, final IColorSpace color) {
        final Color rgb = this.toColor(color);
        final Tree tree = new Tree();
        tree.draw(trunkRadius, foliageRadius, height, slices, stacks);
    }

    /**
     * Draws a cylinder.
     *
     * @param baseRadius
     * @param topRadius
     * @param height
     * @param slices
     * @param stacks
     * @param color
     */
    public void drawCylinder(final float baseRadius, final float topRadius, final float height, final int slices, final int stacks, final IColorSpace color) {
        final Color rgb = this.toColor(color);
        final Cylinder cylinder = new Cylinder();
        cylinder.draw(baseRadius, topRadius, height, slices, stacks);
    }

    /**
     * Draws a sphere of the given radius.
     *
     * @param radius
     * @param slices
     * @param stacks
     * @param color
     */
    public void drawSphere(final float radius, final int slices, final int stacks, final IColorSpace color) {
        final Color rgb = this.toColor(color);
        final Sphere sphere = new Sphere();
        sphere.draw(radius, slices, stacks);
    }

    /**
     * Draws a disk
     *
     * @param innerRadius
     * @param outerRadius
     * @param slices
     * @param loops
     * @param color
     */
    public void drawDisk(final float innerRadius, final float outerRadius, final int slices, final int loops, final IColorSpace color) {
        final Color rgb = this.toColor(color);
        final Disk disk = new Disk();
        disk.draw(innerRadius, outerRadius, slices, loops);
    }

    /**
     *
     * @param innerRadius
     * @param outerRadius
     * @param slices
     * @param loops
     * @param startAngle
     * @param sweepAngle
     * @param color
     */
    public void drawPartialDisk(final float innerRadius, final float outerRadius, final int slices, final int loops, final float startAngle, final float sweepAngle, final IColorSpace color) {
        final Color rgb = this.toColor(color);
        final PartialDisk partialDisk = new PartialDisk();
        partialDisk.draw(innerRadius, outerRadius, slices, loops, startAngle, sweepAngle);
    }

    /**
     *
     * @param color
     */
    public void drawHouse(final IColorSpace color) {
        final Color rgb = this.toColor(color);
        final House house = new House();
        house.draw();
    }

    /**
     *
     * @param color
     */
    public void drawCar(final IColorSpace color) {
        final Color rgb = this.toColor(color);
        final Car car = new Car();
        car.draw();
    }

    public void drawTorus(final double r, final double R, final int nsides, final int rings, final IColorSpace color) {
        final Color rgb = this.toColor(color);

        final double ringDelta = (2.0 * (float) Math.PI) / rings;
        final double sideDelta = (2.0 * (float) Math.PI) / nsides;
        double theta = 0.0, cosTheta = 1.0, sinTheta = 0.0;
        for (int i = rings - 1; i >= 0; i--) {
            final double theta1 = theta + ringDelta;
            final double cosTheta1 = Math.cos(theta1);
            final double sinTheta1 = Math.sin(theta1);
            GL11.glBegin(GL11.GL_QUAD_STRIP);
            GL11.glColor3d(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
            double phi = 0.0;
            for (int j = nsides; j >= 0; j--) {
                phi += sideDelta;
                final double cosPhi = Math.cos(phi);
                final double sinPhi = Math.sin(phi);
                final double dist = R + (r * cosPhi);
                GL11.glNormal3d(cosTheta1 * cosPhi, -sinTheta1 * cosPhi, sinPhi);
                GL11.glVertex3d(cosTheta1 * dist, -sinTheta1 * dist, r * sinPhi);
                GL11.glNormal3d(cosTheta * cosPhi, -sinTheta * cosPhi, sinPhi);
                GL11.glVertex3d(cosTheta * dist, -sinTheta * dist, r * sinPhi);
            }
            GL11.glEnd();
            theta = theta1;
            cosTheta = cosTheta1;
            sinTheta = sinTheta1;
        }
    }

    //@SuppressWarnings("static-method")
    public void drawBox(final Coordinate point, final double height, final double width, final double depth) {
        final double lx = (width * 0.5f);
        final double ly = (height * 0.5f);
        final double lz = (depth * 0.5f);

        GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
        GL11.glNormal3f(-1, 0, 0);
        GL11.glVertex3d(point.x - lx, point.y - ly, point.z - lz);
        GL11.glVertex3d(point.x - lx, point.y - ly, point.z + lz);
        GL11.glVertex3d(point.x - lx, point.y + ly, point.z - lz);
        GL11.glVertex3d(point.x - lx, point.y + ly, point.z + lz);
        GL11.glNormal3f(0, 1, 0);
        GL11.glVertex3d(point.x + lx, point.y + ly, point.z - lz);
        GL11.glVertex3d(point.x + lx, point.y + ly, point.z + lz);
        GL11.glNormal3f(1, 0, 0);
        GL11.glVertex3d(point.x + lx, point.y - ly, point.z - lz);
        GL11.glVertex3d(point.x + lx, point.y - ly, point.z + lz);
        GL11.glNormal3f(0, -1, 0);
        GL11.glVertex3d(point.x - lx, point.y - ly, point.z - lz);
        GL11.glVertex3d(point.x - lx, point.y - ly, point.z + lz);
        GL11.glEnd();

        // top face
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glNormal3f(0, 0, 1);
        GL11.glVertex3d(point.x - lx, point.y - ly, point.z + lz);
        GL11.glVertex3d(point.x + lx, point.y - ly, point.z + lz);
        GL11.glVertex3d(point.x + lx, point.y + ly, point.z + lz);
        GL11.glVertex3d(point.x - lx, point.y + ly, point.z + lz);
        GL11.glEnd();

        // bottom face
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glNormal3f(0, 0, -1);
        GL11.glVertex3d(point.x - lx, point.y - ly, point.z - lz);
        GL11.glVertex3d(point.x - lx, point.y + ly, point.z - lz);
        GL11.glVertex3d(point.x + lx, point.y + ly, point.z - lz);
        GL11.glVertex3d(point.x + lx, point.y - ly, point.z - lz);
        GL11.glEnd();
    }

    public Color toColor(final IColorSpace value) {
        final RGB rgb = value.toRGB();
        return new Color(this.gc.getDevice(), new org.eclipse.swt.graphics.RGB((int) (rgb.R), (int) (rgb.G), (int) (rgb.B)));
    }

    public GC getGC() {
        return this.gc;
    }
}
