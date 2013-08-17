package de.uniol.inf.is.odysseus.wrapper.kinect.physicaloperator;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.wrapper.kinect.datatype.KinectColorMap;

public class StoreKinectPO extends AbstractSink<Tuple<? extends ITimeInterval>> {
    /** Stores the schema for the sink. */
    private final SDFSchema schema;
    
    /** Position of the color map in the data tuple. */
    private int colorMapPos;

    /** Position of the color map in the data tuple. */
    private int depthMapPos;
    
    /** Path to save files. */
    private String path;

    /**
     * Constructs the operator using the given schema.
     * @param s
     * Schema of the data will be passed to this operator.
     */
    public StoreKinectPO(final SDFSchema s) {
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
    public StoreKinectPO(final StoreKinectPO po) {
        this.schema = po.schema;
        this.colorMapPos = po.colorMapPos;
        this.depthMapPos = po.depthMapPos;
        this.path = po.path;
    }
    
    @Override
    protected void process_open() throws OpenFailedException {
        super.process_open();
        
        if (path.equals("") || path == null)
            throw new RuntimeException("Path may not be null.");
        
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs())
                throw new RuntimeException("Path '" + path + "' could not be created.");
        }
        
        if (!file.isDirectory())
            throw new RuntimeException("Path should be a directory.");
    }

    @Override
    protected void process_next(Tuple<? extends ITimeInterval> object, int port) {
        KinectColorMap colorMap = (KinectColorMap) object.getAttribute(colorMapPos);
        try {
            ImageIO.write(colorMap.getBufferedImage(), "png", new File(path + "/" + System.currentTimeMillis() + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processPunctuation(IPunctuation punctuation, int port) {
    }

    @Override
    public AbstractSink<Tuple<? extends ITimeInterval>> clone() {
        return new StoreKinectPO(this);
    }
    
    public void setPath(String path) {
        this.path = path;
    }

}
