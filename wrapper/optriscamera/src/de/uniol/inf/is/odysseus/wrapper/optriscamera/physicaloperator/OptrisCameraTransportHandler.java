package de.uniol.inf.is.odysseus.wrapper.optriscamera.physicaloperator;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.avutil.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.wrapper.optriscamera.swig.OptrisCamera;
import de.uniol.inf.is.odysseus.wrapper.optriscamera.swig.TFlagState;

public class OptrisCameraTransportHandler extends AbstractPushTransportHandler {
	private static final Logger LOG = LoggerFactory.getLogger(OptrisCameraTransportHandler.class);

	public static final String ATTRNAME_FLAGSTATE = "FlagState";
	public static final String NAME = "OptrisCamera";
	public static final String SERIALNUMBER = "serialnumber";

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(OptrisCameraTransportHandler.class);
	private final Object processLock = new Object();

	private String serialNumber;
	private OptrisCamera cameraCapture;
	private ImageJCV image;

	@Override
	public String getName() {
		return NAME;
	}

	public OptrisCameraTransportHandler() {
		super();
	}

	public OptrisCameraTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		serialNumber = options.get(SERIALNUMBER, "");
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new OptrisCameraTransportHandler(protocolHandler, options);
	}

	@Override
	public void processInOpen() throws IOException {
		synchronized (processLock) {
			try {
				cameraCapture = new OptrisCamera("", serialNumber) {
					Thread firstFrameThread = null;
					boolean firstFrameProcessed = false;

					@Override
					public void onNewFrame(long timeStamp, TFlagState flagState, ByteBuffer buffer) {
						Tuple<IMetaAttribute> tuple = generateTuple(timeStamp, flagState, buffer);

						// TODO: Since this handler is often used with
						// streaming, which may take some seconds to set up, the
						// Optris SDK stops sending images.
						// Until the first image has been processed, subsequent
						// images get discarded until the first image has been
						// written.
						// Use load shedding instead!
						if (!firstFrameProcessed) {
							if (firstFrameThread == null) {
								firstFrameThread = new Thread() {
									@Override
									public void run() {
										fireProcess(tuple);
										firstFrameProcessed = true;
										firstFrameThread = null;
									}
								};
								firstFrameThread.start();
							}
							/*
							 * else System.out.println(
							 * "Drop frame since first frame didn't finish yet!"
							 * );
							 */
						} else
							fireProcess(tuple);
					}
				};
				cameraCapture.start();
				fireOnConnect();

			} catch (RuntimeException e) {
				processInClose();
				throw new IOException(e);
			}
		}
	}

	@Override
	public void processInClose() {
		synchronized (processLock) {
			if (cameraCapture != null) {
				cameraCapture.stop();
				cameraCapture = null;
				image = null;
			}
		}

		fireOnDisconnect();
	}

	private double smoothFPS = 0.0f;
	private double alpha = 0.95f;
	private long lastTime = 0;
	private int imageCount = 0;

	private void logStats(long now) {
		imageCount++;
		double dt = (now - lastTime) / 1.0e3;
		double fps = 1.0 / dt;

		smoothFPS = alpha * smoothFPS + (1.0 - alpha) * fps;

		System.out.println(String.format("%d optris: %.4f FPS (%.4f)", imageCount, smoothFPS, fps));
		lastTime = now;
	}

	private Tuple<IMetaAttribute> generateTuple(double cameraTimePassed, TFlagState flagState, ByteBuffer buffer) {
		// TODO: Use camera timestamp
		long timestamp = System.currentTimeMillis(); // startupTimeStamp +
														// (long)
														// (cameraTimePassed *
														// 1000);
		if (LOG.isTraceEnabled()) {
			logStats(timestamp);
		}

		// if (System.currentTimeMillis() > ImageJCV.startTime + 10000) return;
		// System.out.println("Timestamp = " + timeStamp);

		int attrs[];
		Tuple<IMetaAttribute> newTuple = new Tuple<IMetaAttribute>(getSchema().size(), true);

		attrs = getSchema().getSDFDatatypeAttributePositions(SDFImageJCVDatatype.IMAGEJCV);
		if (attrs.length > 0) {
			if (image == null)
				image = new ImageJCV(cameraCapture.getImageWidth(), cameraCapture.getImageHeight(), IPL_DEPTH_16U,
						cameraCapture.getImageChannels(), AV_PIX_FMT_GRAY16);

			image.getImageData().put(buffer);
			newTuple.setAttribute(attrs[0], image);
		}

		attrs = getSchema().getSDFDatatypeAttributePositions(SDFDatatype.START_TIMESTAMP);
		if (attrs.length > 0) {
			newTuple.setAttribute(attrs[0], timestamp);
		}

		for (int i = 0; i < getSchema().size(); i++) {
			SDFAttribute attr = getSchema().getAttribute(i);
			if (attr.getAttributeName().equalsIgnoreCase(ATTRNAME_FLAGSTATE)) {
				newTuple.setAttribute(i, flagState.toString());
				break;
			}
		}

		// System.out.println("Optris generated image @ " +
		// System.currentTimeMillis());
		return newTuple;
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler o) {
		if (!(o instanceof OptrisCameraTransportHandler)) {
			return false;
		}
		OptrisCameraTransportHandler other = (OptrisCameraTransportHandler) o;
		if (!this.serialNumber.equals(other.serialNumber))
			return false;

		return true;
	}

	@Override
	public void processOutOpen() throws IOException {
		throw new UnsupportedOperationException("Operator can not be used as sink");
	}

	@Override
	public void processOutClose() throws IOException {
		// throw new UnsupportedOperationException("Operator can not be used as
		// sink");
	}

	@Override
	public void send(byte[] message) throws IOException {
		throw new UnsupportedOperationException("Operator can not be used as sink");
	}
}
