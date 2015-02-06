package de.uniol.inf.is.odysseus.wrapper.kinect.physicaloperator;

import static com.googlecode.javacv.cpp.opencv_core.CV_8UC3;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvSize;
import com.googlecode.javacv.cpp.opencv_objdetect;
import com.googlecode.javacv.cpp.opencv_objdetect.CascadeClassifier;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.wrapper.kinect.datatype.KinectColorMap;
import de.uniol.inf.is.odysseus.wrapper.kinect.utils.Helper;

public class FaceDetectorPO
		extends
		AbstractPipe<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> {
	private static final Logger log = LoggerFactory
			.getLogger(FaceDetectorPO.class);
	private SDFSchema schema;
	private int colorMapPos;
	private CascadeClassifier faceDetector;
	private static String trainingData;

	private static final boolean SEND_TEST_IMAGES = true;
	private static List<BufferedImage> SAMPLES;
	private Iterator<BufferedImage> sampleIterator;

	static {
		String resource = "haarcascade_frontalface_alt.xml";
		trainingData = Helper.getFileToResource(
				FaceDetectorPO.class.getResourceAsStream("/" + resource),
				resource);

		if (SEND_TEST_IMAGES) {
			SAMPLES = Lists.newArrayList();
			for (int i = 0; i < 42; i++) {
				try {
					if(i==40)
						continue;
					BufferedImage orgImg = ImageIO.read(FaceDetectorPO.class
							.getResourceAsStream("/fakedata/s" + i + ".png"));
					BufferedImage img = new BufferedImage(orgImg.getWidth(),
							orgImg.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
					Graphics2D g = img.createGraphics();
					g.drawImage(orgImg, 0, 0, img.getWidth(), img.getHeight(),
							null);
					g.dispose();
					SAMPLES.add(img);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Constructs the operator using the given schema.
	 * 
	 * @param s
	 *            Schema of the data will be passed to this operator.
	 */
	public FaceDetectorPO(final SDFSchema s) {
		this.schema = s;

		this.colorMapPos = -1;
		final SDFAttribute colorMapAttribute = schema.findAttribute("colorMap");
		if (colorMapAttribute != null) {
			this.colorMapPos = schema.indexOf(colorMapAttribute);
		}
	}

	/**
	 * Copy constructor.
	 * 
	 * @param po
	 *            Instance to copy from.
	 */
	public FaceDetectorPO(final FaceDetectorPO po) {
		this.schema = po.schema;
		this.colorMapPos = po.colorMapPos;
	}

	@Override
	public AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		if (trainingData == null || trainingData.length() == 0) {
			throw new RuntimeException(
					"Could not load training data for face detection");
		} 
		faceDetector = new CascadeClassifier(trainingData);

		if (SEND_TEST_IMAGES) {
			log.info("Configured to send sample images!");
			sampleIterator = SAMPLES.iterator();
		}
	}


	@Override
	protected void process_next(Tuple<? extends ITimeInterval> object, int port) {
		if (SEND_TEST_IMAGES) {
			if (sampleIterator.hasNext()) {
				BufferedImage img = sampleIterator.next();
				generateAndSendTuple(img, object);
			} else {
				sampleIterator = SAMPLES.iterator();
				process_next(object, port);
			}
		} else {
			if (colorMapPos >= 0) {
				KinectColorMap colorMap = (KinectColorMap) object
						.getAttribute(colorMapPos);
				BufferedImage img = colorMap.getBufferedImage();

				detectFacesAndSendTuples(img, object);
			}
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
	private void detectFacesAndSendTuples(BufferedImage img,
			Tuple<? extends ITimeInterval> object) {
		byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer())
				.getData();
		CvMat image = CvMat.create(img.getHeight(), img.getWidth(), CV_8UC3);
		image.getByteBuffer().put(pixels);

		CvRect faceDetections = new CvRect(null);
		faceDetector.detectMultiScale(image, faceDetections, 1.1, 2,
				0 | opencv_objdetect.CV_HAAR_SCALE_IMAGE, new CvSize(0, 0),
				new CvSize(image.cols(), image.rows()));

		for (int i = 0; i < faceDetections.capacity(); i++) {
			generateAndSendTuple(img.getSubimage(faceDetections.x(),
					faceDetections.y(), faceDetections.width(),
					faceDetections.height()), object);
			faceDetections.position(i);
		}
		faceDetections.position(0);
	}

	private void generateAndSendTuple(BufferedImage img,
			Tuple<? extends ITimeInterval> object) {
		Tuple<ITimeInterval> tuple = new Tuple<>(1, false);
		tuple.setAttribute(0, img);
		tuple.setMetadata(object.getMetadata().clone());
		this.transfer(tuple);
	}

	@Override
	public AbstractPipe<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> clone() {
		return new FaceDetectorPO(this);
	}
}
