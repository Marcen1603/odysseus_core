package de.uniol.inf.is.odysseus.wrapper.navicoradar.physicaloperator;


import java.io.IOException;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.command.Command;
import de.uniol.inf.is.odysseus.core.command.ICommandProvider;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.wrapper.navicoradar.SWIG.NavicoRadarWrapper;



public class NavicoRadarTransportHandler extends AbstractPushTransportHandler implements ICommandProvider
{
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(NavicoRadarTransportHandler.class);
	private final Object processLock = new Object();


	private NavicoRadarWrapper navico;
	//private RadarCallback cb;
	private int AntennaHeightMiliMeter;
	private int RangeMeter;
	private String RadarSerial;
	private int UnlockKeylength;
	
	
	
	public NavicoRadarTransportHandler() 
	{
		super();
	}
	
	/**
	 * @param protocolHandler
	 */
	public NavicoRadarTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		super(protocolHandler, options);
		
		RadarSerial = "";
	}
	

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		return new NavicoRadarTransportHandler(protocolHandler, options);
	}


	@Override public String getName() { return "NavicoRadar"; }

	
	
	@Override public void processInOpen() throws IOException 
	{
		synchronized (processLock)
		{
			navico = new NavicoRadarWrapper(AntennaHeightMiliMeter, RangeMeter, RadarSerial, null, UnlockKeylength)
			
			//cb = new RadarCallback()
								 	{
										@Override public void onSpokeUpdate(ByteBuffer buffer) 
										{
											NavicoRadarTransportHandler.this.onSpokeUpdate(buffer);
											
											//NavicoRadarTransportHandler.this.onSpokeUpdate(spoke);
/*											ByteBuffer copy = ByteBuffer.allocate(buffer.capacity());
											copy.put(buffer);
												
											fireProcess(copy);*/
										}

										@Override public void onCat240SpokeUpdate(ByteBuffer buffer1) 
										{
											//NavicoRadarTransportHandler.this.onCat240SpokeUpdate(spoke);
/*											ByteBuffer copy1 = ByteBuffer.allocate(buffer1.capacity());
											copy1.put(buffer1);
												
											fireProcess(copy1);*/
										}

										@Override public void onTargetUpdate(ByteBuffer buffer2) 
										{
											NavicoRadarTransportHandler.this.onTargetUpdate(buffer2);
/*											ByteBuffer copy2 = ByteBuffer.allocate(buffer2.capacity());
											copy2.put(buffer2);
												
											fireProcess(copy2);*/
										}
										@Override public void onTargetUpdateTTM(String ttmMessage) 
										{
											NavicoRadarTransportHandler.this.onTargetUpdateTTM(ttmMessage);
										}
										
								 	};
								 	//navico.setCallback(	cb);
		}
		navico.start();
		fireOnConnect();
	}
	
protected void onTargetUpdate(ByteBuffer target) {
			
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
		tuple.setAttribute(0, "Test target");
		tuple.setAttribute(1, target);
		fireProcess(tuple);
	}

	protected void onSpokeUpdate(ByteBuffer spoke) {
		//int len=spoke.capacity();
	
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
		tuple.setAttribute(0, "Test spoke");
		tuple.setAttribute(1, spoke);
		fireProcess(tuple);
//		fireProcess(byteBuffer);
	}
	
	protected void onTargetUpdateTTM(String ttmMessage) {
		System.out.println("TTM: " + ttmMessage);
/*		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
		tuple.setAttribute(0, ttmMessage);
		tuple.setAttribute(1, null);
		fireProcess(tuple);*/
	}	
/*
	protected void onCat240SpokeUpdate(cat240Spoke spoke) {
		int len=spoke.getLength();
		byte[] buffer = new byte[len+1];
		buffer[0] = 3;
		for (int i = 0; i < len; i++) {
			buffer[i+1] = spoke.getData(i);
		}
		
		ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
		//String s = javax.xml.bind.DatatypeConverter.printHexBinary(buffer);
		
		byteBuffer.position(byteBuffer.limit());
	
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(1, false);
		tuple.setAttribute(0, "Test cat240");
		fireProcess(tuple);
		
///		fireProcess(byteBuffer);
	}*/

	
	@Override public void processInClose() throws IOException 
	{
		synchronized (processLock)
		{
			fireOnDisconnect();
			
			navico.stop();
			navico = null;
			
			//cb = null;
		}
	}

		
    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) {
    	if(!(o instanceof NavicoRadarTransportHandler)) {
    		return false;
    	}
    	//NavicoRadarTransportHandler other = (NavicoRadarTransportHandler)o;
    	
    	
    	return true;
    }

	@Override
	public void processOutOpen() throws IOException {
		throw new IllegalArgumentException("Operator is not a Sink");
		
	}

	@Override
	public void processOutClose() throws IOException {
		throw new IllegalArgumentException("Operator is not a Sink");
		
	}

	@Override
	public void send(byte[] message) throws IOException 
	{
		throw new IllegalArgumentException("Sending Not Supported");
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Command getCommandByName(String commandName, SDFSchema schema) 
	{
		switch (commandName)
		{
		case "setownshipdata": 
		{
			final int speedTypeAttribute = schema.findAttributeIndexException("speedType");
			final int speedAttribute = schema.findAttributeIndexException("speed");
			final int headingTypeAttribute = schema.findAttributeIndexException("speedType");
			final int headingAttribute = schema.findAttributeIndexException("speedType");
			return new Command()
								 {
								 	@Override public void run(IStreamObject<?> input) 
								 	{
								 		int speedType,headingType;
								 		double speed,heading;
								 		
								 		
								 		if (input instanceof Tuple<?>)
								 		{
								 			Tuple<?> tuple = (Tuple<?>) input;								 			
								 			speedType = ((Number) tuple.getAttribute(speedTypeAttribute)).intValue();
								 			speed = ((Number) tuple.getAttribute(speedAttribute)).doubleValue();
								 			headingType= ((Number) tuple.getAttribute(headingTypeAttribute)).intValue();
								 			heading= ((Number) tuple.getAttribute(headingAttribute)).doubleValue();
								 		}
								 		else
								 		if (input instanceof KeyValueObject)
								 		{
								 			KeyValueObject<?> kv = (KeyValueObject<?>)input;
								 			speedType = (int) kv.getAttribute("speedType");
								 			speed = (double) kv.getAttribute("speed");
								 			headingType= (int) kv.getAttribute("headingType");
								 			heading= (double) kv.getAttribute("heading");
								 		}
								 		else
								 			throw new IllegalArgumentException("Cannot execute command on input type " + input.getClass().getName());
								 		
								 		NavicoRadarTransportHandler.this.navico.SetBoatSpeed(speedType, speed, headingType, heading);
									}
 								 };
		}
		case "settargetdata":	{
			final int TargetIdAttribute = schema.findAttributeIndexException("TargetId");
			final int rangeAttribute = schema.findAttributeIndexException("range");
			final int bearingAttribute = schema.findAttributeIndexException("bearing");
			final int bearingtypeAttribute = schema.findAttributeIndexException("bearingtype"); 
			return new Command()
		 {
		 	@Override public void run(IStreamObject<?> input) 
		 	{
		 		int TargetId, range, bearing, bearingtype;
		 		
		 		if (input instanceof Tuple<?>)
		 		{
		 			Tuple<?> tuple = (Tuple<?>) input;								 			
		 			TargetId = ((Number) tuple.getAttribute(TargetIdAttribute)).intValue();
		 			range = ((Number) tuple.getAttribute(rangeAttribute)).intValue();
		 			bearing = ((Number) tuple.getAttribute(bearingAttribute)).intValue();
		 			bearingtype = ((Number) tuple.getAttribute(bearingtypeAttribute)).intValue();
		 		}
		 		else
		 		if (input instanceof KeyValueObject)
		 		{
		 			KeyValueObject<?> kv = (KeyValueObject<?>)input;
		 			TargetId = (int) kv.getAttribute("TargetId");
		 			range = (int) kv.getAttribute("range");
		 			bearing = (int) kv.getAttribute("bearing");
		 			bearingtype = (int) kv.getAttribute("bearingtype");
		 		}
		 		else
		 			throw new IllegalArgumentException("Cannot execute command on input type " + input.getClass().getName());
		 		
		 		NavicoRadarTransportHandler.this.navico.AcquireTargets(TargetId, range, bearing, bearingtype);
			}
		 };					 
		}						 
 		default: return null;
		}
	}
}
