package de.uniol.inf.is.odysseus.spatial.grid.physicaloperator;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.concurrent.atomic.AtomicBoolean;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.spatial.grid.common.OpenCVUtil;
import de.uniol.inf.is.odysseus.spatial.grid.model.Grid;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class VisualGridSinkPO extends AbstractSink<Object> {
    private CanvasFrame canvas;
    private final SDFAttributeList schema;
    private final AtomicBoolean pause = new AtomicBoolean(false);

    public VisualGridSinkPO(final SDFAttributeList schema) {
        this.schema = schema;
    }

    public VisualGridSinkPO(final VisualGridSinkPO po) {
        this.schema = po.schema;
    }

    @Override
    public void open() throws OpenFailedException {
        super.open();
        this.canvas = new CanvasFrame("Grid");
        canvas.getCanvas().addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_SPACE) {
                    pause.set(!pause.get());
                }
            }

            @Override
            public void keyReleased(KeyEvent event) {

            }

            @Override
            public void keyTyped(KeyEvent event) {

            }

        });
    }

    @Override
    public void close() {
        super.close();
        if (this.canvas != null) {
            this.canvas.dispose();
            this.canvas = null;
        }
    }

    @Override
    public VisualGridSinkPO clone() {
        return new VisualGridSinkPO(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void process_next(final Object object, final int port, final boolean isReadOnly) {
        if ((this.canvas != null) && (canvas.isVisible()) && (!pause.get())) {
            Grid grid = (Grid) ((RelationalTuple<TimeInterval>) object).getAttribute(0);
            IplImage image = opencv_core.cvCreateImage(opencv_core.cvSize(grid.width, grid.depth),
                    opencv_core.IPL_DEPTH_8U, 1);
            OpenCVUtil.gridToImage(grid, image);
            this.canvas.showImage(image);
            opencv_core.cvReleaseImage(image);
            image = null;
        }
    }

    @Override
    public void processPunctuation(final PointInTime timestamp, final int port) {
        if ((this.canvas != null) && (canvas.isVisible()) && (!pause.get())) {
            this.canvas.setBackground(new Color(255, 0, 0));
        }
    }

}
