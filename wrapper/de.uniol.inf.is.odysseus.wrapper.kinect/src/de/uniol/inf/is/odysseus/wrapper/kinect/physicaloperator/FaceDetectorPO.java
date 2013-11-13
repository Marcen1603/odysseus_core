package de.uniol.inf.is.odysseus.wrapper.kinect.physicaloperator;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.wrapper.kinect.datatype.KinectColorMap;

public class FaceDetectorPO extends AbstractPipe<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> {
    private SDFSchema schema;
	private int colorMapPos;
	private int depthMapPos;
	private int skeletonMapPos;

	/**
     * Constructs the operator using the given schema.
     * @param s
     * Schema of the data will be passed to this operator.
     */
    public FaceDetectorPO(final SDFSchema s) {
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
    public FaceDetectorPO(final FaceDetectorPO po) {
        this.schema = po.schema;
        this.colorMapPos = po.colorMapPos;
        this.depthMapPos = po.depthMapPos;
        this.skeletonMapPos = po.skeletonMapPos;
    }
	
	@Override
	public AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

    @Override
    protected void process_open() throws OpenFailedException {
        super.process_open();
    }

	@Override
	protected void process_next(Tuple<? extends ITimeInterval> object, int port) {
		if (colorMapPos >= 0) {
            KinectColorMap colorMap = (KinectColorMap) object.getAttribute(colorMapPos);
            BufferedImage img = colorMap.getBufferedImage();
            
            CascadeClassifier faceDetector = new CascadeClassifier(getClass().getResource("/haarcascade_frontalface_alt.xml").getPath());
            byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
            Mat image = new Mat(img.getHeight(),img.getWidth(),CvType.CV_8UC3);
            image.put(0, 0, pixels);

            // Detect faces in the image.
            // MatOfRect is a special container class for Rect.
            MatOfRect faceDetections = new MatOfRect();
            faceDetector.detectMultiScale(image, faceDetections);

            if(faceDetections.toArray().length>0) {
            	System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
            }

            Tuple<? extends ITimeInterval> tuple = new Tuple<>(0, false);
            tuple.setAttribute(0, faceDetections.toArray().length);
            this.transfer(tuple);
        }       
	}

	@Override
	public AbstractPipe<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> clone() {
		return new FaceDetectorPO(this);
	}	
}
