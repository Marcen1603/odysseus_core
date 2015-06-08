package de.uniol.inf.is.odysseus.wrapper.kinect.physicaloperator;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.wrapper.kinect.datatype.KinectColorMap;

public class SliceImagePO
		extends
		AbstractPipe<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> {
	private SDFSchema schema;
	private int colorMapPos;
	private int depthMapPos;
	private int skeletonMapPos;
	private Rectangle rectangle;

	/**
	 * Constructs the operator using the given schema.
	 * 
	 * @param s
	 *            Schema of the data will be passed to this operator.
	 */
	public SliceImagePO(final SDFSchema s) {
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
		final SDFAttribute skeletonMapAttribute = schema
				.findAttribute("skeletonMap");
		if (skeletonMapAttribute != null) {
			this.skeletonMapPos = schema.indexOf(skeletonMapAttribute);
		}
	}

	/**
	 * Copy constructor.
	 * 
	 * @param po
	 *            Instance to copy from.
	 */
	public SliceImagePO(final SliceImagePO po) {
		this.schema = po.schema;
		this.colorMapPos = po.colorMapPos;
		this.depthMapPos = po.depthMapPos;
		this.skeletonMapPos = po.skeletonMapPos;
		this.rectangle = po.rectangle;
	}

	@Override
	public AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();

		if (rectangle == null)
			throw new RuntimeException("Slice rectangle may not be null.");
	}

	@Override
	protected void process_next(Tuple<? extends ITimeInterval> object, int port) {
		if (colorMapPos >= 0) {
			KinectColorMap colorMap = (KinectColorMap) object
					.getAttribute(colorMapPos);
			BufferedImage img = colorMap.getBufferedImage();

			BufferedImage dst = new BufferedImage(
					(int) this.rectangle.getWidth(),
					(int) this.rectangle.getHeight(),
					BufferedImage.TYPE_3BYTE_BGR);

			Graphics2D g =dst.createGraphics();
			g.drawImage(
					img = img.getSubimage((int) this.rectangle.getX(),
							(int) this.rectangle.getY(),
							(int) this.rectangle.getWidth(),
							(int) this.rectangle.getHeight()), 0, 0,
					(int) this.rectangle.getWidth(),
					(int) this.rectangle.getHeight(), null);

			colorMap = new KinectColorMap(
					((DataBufferByte) (dst.getRaster().getDataBuffer()))
							.getData(),
					(int) rectangle.getWidth(), (int) rectangle.getHeight());
			object.setAttribute(colorMapPos, colorMap);
		}

		if (depthMapPos >= 0) {
			// KinectDepthMap depthMap = (KinectDepthMap)
			// object.getAttribute(depthMapPos);
			// BufferedImage img = depthMap.getBufferedImage();
			//
			// BufferedImage dst = new
			// BufferedImage((int)this.rectangle.getWidth(),
			// (int)this.rectangle.getHeight(),
			// BufferedImage.TYPE_USHORT_GRAY);
			//
			// Graphics2D g = dst.createGraphics();
//			g.drawImage(
//					img = img.getSubimage((int) this.rectangle.getX(),
//							(int) this.rectangle.getY(),
//							(int) this.rectangle.getWidth(),
//							(int) this.rectangle.getHeight()), 0, 0,
//					(int) this.rectangle.getWidth(),
//					(int) this.rectangle.getHeight(), null);
			//
			// depthMap = new
			// KinectDepthMap(((DataBufferUShort)(dst.getRaster().getDataBuffer())).getData(),
			// (int)rectangle.getWidth(), (int)rectangle.getHeight());
			// object.setAttribute(depthMapPos, depthMap);
			throw new RuntimeException("Not implemented yey");
		}

		this.transfer(object);
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

}
