package de.uniol.inf.is.odysseus.wrapper.optriscamera.physicaloperator;

import static org.bytedeco.javacpp.opencv_core.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.wrapper.optriscamera.swig.OptrisCamera;

public class OptrisCameraTransportHandler extends AbstractPushTransportHandler 
{
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(OptrisCameraTransportHandler.class);
	private final Object processLock = new Object();

	private String 			ethernetAddress;
	private OptrisCamera 	cameraCapture;
	
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
//		throw new UnsupportedOperationException("Operator can not be run in pull mode");
		
		synchronized (processLock)
		{
			try
			{
		 		cameraCapture = new OptrisCamera("", ethernetAddress)
		 						{
		 							@Override public void onNewFrame(ByteBuffer buffer)
		 							{
		 								Tuple<IMetaAttribute> tuple = new Tuple<>(getSchema().size(), true);
		 								int[] attrs = getSchema().getSDFDatatypeAttributePositions(SDFImageJCVDatatype.IMAGEJCV);
		 								if (attrs.length > 0)
		 								{
			 								IplImage img = cvCreateImage(cvSize(cameraCapture.getImageWidth(), cameraCapture.getImageHeight()), IPL_DEPTH_16S, cameraCapture.getImageChannels());			
			 								img.getByteBuffer().put(buffer);		 									
		 									tuple.setAttribute(attrs[0], new ImageJCV(img));
		 								}
		 								
		 								fireProcess(tuple);
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
		}
		
		fireOnDisconnect();
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
		throw new UnsupportedOperationException("Operator can not be used as sink");
	}

	@Override
	public void send(byte[] message) throws IOException 
	{
		throw new UnsupportedOperationException("Operator can not be used as sink");
	}
}
