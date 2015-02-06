package de.uniol.inf.is.odysseus.wrapper.kinect.physicaloperator;

import static com.googlecode.javacv.cpp.opencv_contrib.createLBPHFaceRecognizer;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RGB2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvEqualizeHist;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Properties;

import com.googlecode.javacv.cpp.opencv_contrib.FaceRecognizer;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.imageresizer.ImageResizer;
import de.uniol.inf.is.odysseus.wrapper.kinect.utils.Helper;

public class FaceRecognizerPO extends AbstractPipe<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> {
	private SDFSchema schema;
	private static String trainingData;
	// private static final Logger log =
	// LoggerFactory.getLogger(FaceRecognizerPO.class);
	private FaceRecognizer faceRecognizer;
	private static Properties nameDb;

	static {
		String resource = "trainingData_lbp.xml";
		trainingData = Helper.getFileToResource(FaceRecognizerPO.class.getResourceAsStream("/" + resource), resource);

		try {
			nameDb = new Properties();
			nameDb.load(FaceRecognizerPO.class.getResourceAsStream("/labelToNameMap.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructs the operator using the given schema.
	 * 
	 * @param s
	 *            Schema of the data will be passed to this operator.
	 */
	public FaceRecognizerPO(final SDFSchema s) {
		this.schema = s;
	}

	/**
	 * Copy constructor.
	 * 
	 * @param po
	 *            Instance to copy from.
	 */
	public FaceRecognizerPO(final FaceRecognizerPO po) {
		this.schema = po.schema;
	}

	@Override
	public AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		if (trainingData == null || trainingData.length() == 0) {
			throw new RuntimeException("Could not load training data for face detection");
		}
		// faceRecognizer = createFisherFaceRecognizer();
		// faceRecognizer = createEigenFaceRecognizer();
		faceRecognizer = createLBPHFaceRecognizer();
		faceRecognizer.load(trainingData);

	}

	@Override
	protected void process_next(Tuple<? extends ITimeInterval> object, int port) {
		BufferedImage img = object.getAttribute(0);
		if (img != null) {
			try {
				img = ImageResizer.getNormalizedImage(img);
				IplImage testImage = normalizeImage(img);
				int[] predictedLabel = new int[] { -1 };
				double[] confidence = new double[] { 0.0 };
				faceRecognizer.predict(testImage, predictedLabel, confidence);

				Tuple<ITimeInterval> tuple = new Tuple<>(3, false);
				tuple.setAttribute(0, predictedLabel[0]);
				String name = (nameDb.getProperty("" + predictedLabel[0]) != null) ? nameDb.getProperty("" + predictedLabel[0]) : "unknown";
				tuple.setAttribute(1, name);
				tuple.setAttribute(2, confidence[0]);

				tuple.setMetadata(object.getMetadata());
				this.transfer(tuple);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	
	@Override
	public AbstractPipe<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> clone() {
		return new FaceRecognizerPO(this);
	}

	private IplImage normalizeImage(BufferedImage img) {
		IplImage originalImage = IplImage.create(img.getWidth(), img.getHeight(), IPL_DEPTH_8U, 3);
		originalImage.copyFrom(img);
		IplImage gray = IplImage.create(img.getWidth(), img.getHeight(), IPL_DEPTH_8U, 1);
		cvCvtColor(originalImage, gray, CV_RGB2GRAY);

		if (gray.isNull())
			return new IplImage(null);

		IplImage gr_eq = IplImage.create(gray.width(), gray.height(), IPL_DEPTH_8U, 1);
		cvEqualizeHist(gray, gr_eq); // maybe not strictly nessecary, but does
										// wonders on the quality!

		return gr_eq;
	}
}
