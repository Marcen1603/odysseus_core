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

public class OptrisCameraTransportHandler extends AbstractPushTransportHandler 
{
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(OptrisCameraTransportHandler.class);
	private final Object processLock = new Object();

	private String ethernetAddress;
	private OptrisCamera cameraCapture;
	private ImageJCV image;
	
	public OptrisCameraTransportHandler() 
	{
		super();
	}
	
	/**
	 * @param protocolHandler
	 */
	public OptrisCameraTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		super(protocolHandler, options);
		
		ethernetAddress = options.get("ethernetaddress", "");
	}
	
	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		return new OptrisCameraTransportHandler(protocolHandler, options);
	}

	@Override public String getName() { return "OptrisCamera"; }

	@Override public void processInOpen() throws IOException 
	{
		synchronized (processLock)
		{
			try
			{
		 		cameraCapture = new OptrisCamera("", ethernetAddress)
		 						{		 			
		 							@Override public void onNewFrame(long timeStamp, TFlagState flagState, ByteBuffer buffer)
		 							{
		 								fireProcess(generateTuple(timeStamp, flagState, buffer));		 								
		 							}
		 						};
				cameraCapture.start();
							
			}
			catch (RuntimeException e) 
			{
				cameraCapture = null;
				throw new IOException(e.getMessage());
			}
		}
		
		fireOnConnect();
	}
	
	@Override public void processInClose() throws IOException 
	{
		synchronized (processLock)
		{
			cameraCapture.stop();
			cameraCapture = null;
			image = null;
		}

		fireOnDisconnect();
	}

	private double smoothFPS = 0.0f;
	private double alpha = 0.95f;
	private long lastTime = 0;
	private int imageCount = 0;

	private void logStats()
	{
		long now = System.nanoTime();
		double dt = (now - lastTime) / 1.0e9;
		double fps = 1.0/dt;

		smoothFPS = alpha*smoothFPS + (1.0-alpha)*fps; 

		System.out.println(String.format("%d optris: %.4f FPS (%.4f)", imageCount, smoothFPS, fps));
		lastTime = now;		
	}		 				

	private Tuple<IMetaAttribute> generateTuple(double cameraTimePassed, TFlagState flagState, ByteBuffer buffer)
	{
//		if (System.currentTimeMillis() > ImageJCV.startTime + 10000) return;		
//		System.out.println("Timestamp = " + timeStamp);

		imageCount++;		
//		logStats();
				
		int attrs[];
		Tuple<IMetaAttribute> newTuple = new Tuple<IMetaAttribute>(getSchema().size(), true);
		
		attrs = getSchema().getSDFDatatypeAttributePositions(SDFImageJCVDatatype.IMAGEJCV);
		if (attrs.length > 0)
		{		 								
			if (image == null)
				image = new ImageJCV(cameraCapture.getImageWidth(), cameraCapture.getImageHeight(), IPL_DEPTH_16U, cameraCapture.getImageChannels(), AV_PIX_FMT_GRAY16);
			
			image.getImageData().put(buffer);				 								
			newTuple.setAttribute(attrs[0], image); 
		}
		
		attrs = getSchema().getSDFDatatypeAttributePositions(SDFDatatype.START_TIMESTAMP);
		if (attrs.length > 0) 
		{
/*			long timestamp = startupTimeStamp + (long) (cameraTimePassed * 1000);
			newTuple.setAttribute(attrs[0], timestamp);*/
		}		
		
		for (int i=0; i<getSchema().size(); i++)
		{
			SDFAttribute attr = getSchema().getAttribute(i);
			if (attr.getAttributeName().equalsIgnoreCase("FlagState"))
			{
				newTuple.setAttribute(i, flagState.toString());
				break;
			}
		}
		
		System.out.println("Optris generated image @ " + System.currentTimeMillis());
		return newTuple;
	}
	
	
    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) {
    	if(!(o instanceof OptrisCameraTransportHandler)) {
    		return false;
    	}
    	OptrisCameraTransportHandler other = (OptrisCameraTransportHandler)o;
    	if(!this.ethernetAddress.equals(other.ethernetAddress))
    		return false;
    	
    	return true;
    }

	@Override
	public void processOutOpen() throws IOException 
	{
		throw new UnsupportedOperationException("Operator can not be used as sink");
	}

	@Override
	public void processOutClose() throws IOException 
	{
//		throw new UnsupportedOperationException("Operator can not be used as sink");
	}

	@Override
	public void send(byte[] message) throws IOException 
	{
		throw new UnsupportedOperationException("Operator can not be used as sink");
	}
}
