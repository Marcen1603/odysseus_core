package de.uniol.inf.is.odysseus.wrapper.kinect.physicaloperator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
import de.uniol.inf.is.odysseus.wrapper.kinect.datatype.KinectDepthMap;
import de.uniol.inf.is.odysseus.wrapper.kinect.datatype.KinectSkeleton;
import de.uniol.inf.is.odysseus.wrapper.kinect.datatype.KinectSkeletonMap;

public class StoreKinectPO extends AbstractSink<Tuple<? extends ITimeInterval>> {
    /** Stores the schema for the sink. */
    private final SDFSchema schema;
    
    /** Position of the color map in the data tuple. */
    private int colorMapPos;

    /** Position of the color map in the data tuple. */
    private int depthMapPos;

    /** Position of the skeleton map in the data tuple. */
    private int skeletonMapPos;
    
    /** Path to save files. */
    private String path;
    
    /** Path for color maps. */
    private String colorPath;
    
    /** Path for depth maps. */
    private String depthPath;
    
    /** Path for skeleton maps. */
    private String skeletonPath;
    
    /** New line character. */
    private static String newLine = System.getProperty("line.separator");

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
        
        this.skeletonMapPos = -1;
        final SDFAttribute skeletonMapAttribute = schema.findAttribute("skeletonMap");
        if (skeletonMapAttribute != null) {
            this.skeletonMapPos = schema.indexOf(skeletonMapAttribute);
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
        this.skeletonMapPos = po.skeletonMapPos;
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
        
        if (colorMapPos >= 0) {
            colorPath = path + "/colorMap";
            File file2 = new File(colorPath);
            if (!file2.exists()) {
                if (!file2.mkdirs())
                    throw new RuntimeException("Path '" + colorPath + "' could not be created.");
            }
        }
        
        if (depthMapPos >= 0) {
            depthPath = path + "/depthMap";
            File file2 = new File(depthPath);
            if (!file2.exists()) {
                if (!file2.mkdirs())
                    throw new RuntimeException("Path '" + depthPath + "' could not be created.");
            }
        }
        
        if (skeletonMapPos >= 0) {
            skeletonPath = path + "/skeletonMap";
            File file2 = new File(skeletonPath);
            if (!file2.exists()) {
                if (!file2.mkdirs())
                    throw new RuntimeException("Path '" + skeletonPath + "' could not be created.");
            }
        }
    }

    @Override
    protected void process_next(Tuple<? extends ITimeInterval> object, int port) {
        if (colorMapPos >= 0) {
            KinectColorMap colorMap = (KinectColorMap) object.getAttribute(colorMapPos);
            try {
                ImageIO.write(colorMap.getBufferedImage(), "png", new File(colorPath + "/" + System.currentTimeMillis() + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        if (depthMapPos >= 0) {
            KinectDepthMap depthMap = (KinectDepthMap) object.getAttribute(depthMapPos);
            try {
                ImageIO.write(depthMap.getBufferedImage(), "png", new File(depthPath + "/" + System.currentTimeMillis() + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        if (skeletonMapPos >= 0) {
            KinectSkeletonMap skeletonMap = (KinectSkeletonMap) object.getAttribute(skeletonMapPos);
            if (skeletonMap.getSkeletons().size() > 0) {
                BufferedWriter writer = null;
                try {
                    FileWriter fstream = new FileWriter(skeletonPath + "/" + System.currentTimeMillis() + ".csv");
                    writer = new BufferedWriter(fstream);
                    writer.write(KinectSkeleton.CSV_HEADER + newLine);
                    for (KinectSkeleton skeleton : skeletonMap.getSkeletons()) {
                        writer.write(skeleton.toCsvString() + newLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
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
