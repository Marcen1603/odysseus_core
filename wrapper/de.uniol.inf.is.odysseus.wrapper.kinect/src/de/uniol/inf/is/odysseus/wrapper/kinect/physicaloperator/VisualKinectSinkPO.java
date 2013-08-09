package de.uniol.inf.is.odysseus.wrapper.kinect.physicaloperator;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.wrapper.kinect.datatype.KinectColorMap;
import de.uniol.inf.is.odysseus.wrapper.kinect.datatype.KinectDepthMap;

import static de.uniol.inf.is.odysseus.wrapper.kinect.utils.Constants.MILLISECONDS_IN_SECOND;

/**
 * Physical operator for a visual sink. It opens a Frame and shows the given color and
 * depth maps.
 * @author Juergen Boger <juergen.boger@offis.de>
 */
public class VisualKinectSinkPO extends
        AbstractSink<Tuple<? extends ITimeInterval>> {
    /** Logger for this class. */
    private static Logger log = LoggerFactory.getLogger(VisualKinectSinkPO.class);
    
    /** Stores the opened frame. */
    private Frame frame;
    
    /** Stores the schema for the sink. */
    private final SDFSchema schema;
    
    /** Position of the color map in the data tuple. */
    private int colorMapPos;

    /** Position of the color map in the data tuple. */
    private int depthMapPos;
    
    /** Stores whether the stream is paused. */
    private final AtomicBoolean pause = new AtomicBoolean(false);

    /** Stores the current frames since last second. */
    private int frames = 0;
    
    /** Stores the time, when last FPS update was done. */
    private long startTime = System.currentTimeMillis();

    /**
     * Constructs the operator using the given schema.
     * @param s
     * Schema of the data will be passed to this operator.
     */
    public VisualKinectSinkPO(final SDFSchema s) {
        this.schema = s;
        this.colorMapPos = -1;
        final SDFAttribute colorMapAttribute = schema.findAttribute("colorMap");
        if (colorMapAttribute != null) {
            this.colorMapPos = schema.indexOf(colorMapAttribute);
        }

        this.depthMapPos = -1;
        final SDFAttribute depthMapAttribute = schema.findAttribute("depthMap");
        if (depthMapAttribute != null) {
            this.depthMapPos = schema.indexOf(depthMapAttribute);
        }
    }

    /**
     * Copy constructor.
     * @param po
     * Instance to copy from.
     */
    public VisualKinectSinkPO(final VisualKinectSinkPO po) {
        this.schema = po.schema;
    }

    @Override
    protected void process_open() {
        if (!this.isOpen()) {
            super.process_open();
            try {
                this.frame = new Frame("Kinect");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new OpenFailedException(e);
            }
        }
    }

    @Override
    protected void process_close() {
        if (this.isOpen()) {
            try {
                if (this.frame != null) {
                    this.frame.dispose();
                    this.frame = null;
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new OpenFailedException(e);
            }
            super.process_close();
        }
    }

    @Override
    public VisualKinectSinkPO clone() {
        return new VisualKinectSinkPO(this);
    }

    @Override
    protected void process_next(final Tuple<? extends ITimeInterval> object,
            final int port) {
        frames++;
        long nowTime = System.currentTimeMillis();
        if (nowTime - startTime > MILLISECONDS_IN_SECOND) {
            startTime = nowTime;
            frame.setTitle("ColorMap FPS: " + frames);
            frames = 0;
        }

        final KinectDepthMap depthMap = (KinectDepthMap) object
                .getAttribute(this.depthMapPos);
        final KinectColorMap colorMap = (KinectColorMap) object
                .getAttribute(this.colorMapPos);
        if ((this.frame != null) && (this.frame.isVisible())
                && (!this.pause.get())) {
            try {
                if (colorMap != null) {
                    this.frame.setColorImage(colorMap.getBufferedImage());
                }
                if (depthMap != null) {
                    this.frame.setDepthImage(depthMap.getBufferedImage());
                }
            } catch (final Exception e) {
                log.error(e.getMessage(), e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void processPunctuation(final IPunctuation timestamp, final int port) {

    }

}
