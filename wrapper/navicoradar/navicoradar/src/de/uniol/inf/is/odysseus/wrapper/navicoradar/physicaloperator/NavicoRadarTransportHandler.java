package de.uniol.inf.is.odysseus.wrapper.navicoradar.physicaloperator;


import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.ByteBufferWrapper;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.navicoradar.SWIG.NavicoRadarWrapper;

public class NavicoRadarTransportHandler extends AbstractPushTransportHandler
{
	public static final String NAME = "NavicoRadar";
	
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(NavicoRadarTransportHandler.class);
	private final Object processLock = new Object();

	private NavicoRadarWrapper navico;
	private int antennaHeightMiliMeter;
	private int rangeMeter;
	private String radarSerial;
	private String radarUnlockKey;
	//private ByteBuffer unlockKey;
	
	
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
		
		radarSerial = options.get("radarserial",null);
		radarUnlockKey = options.get("radarunlockkey",null);
		rangeMeter = options.getInt("range", 0);
		antennaHeightMiliMeter  = options.getInt("heightmm", 0);
	}	

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		return new NavicoRadarTransportHandler(protocolHandler, options);
	}

	@Override public String getName() { return NAME; }
	
	@Override public void processInOpen() throws IOException 
	{
		synchronized (processLock)
		{
			navico = new NavicoRadarWrapper(antennaHeightMiliMeter, rangeMeter, radarSerial, radarUnlockKey)
			{
				@Override public void onSpokeUpdate(ByteBuffer buffer) 
				{
					NavicoRadarTransportHandler.this.onSpokeUpdate(buffer);
				}

				@Override public void onCat240SpokeUpdate(ByteBuffer buffer) 
				{
					onCat240SpokeUpdate(buffer);
				}
				@Override public void onTargetUpdateTTM(String ttmMessage) 
				{
					NavicoRadarTransportHandler.this.onTargetUpdateTTM(ttmMessage);
				}
				@Override public void onTargetUpdate(ByteBuffer buffer) 
				{
					NavicoRadarTransportHandler.this.onTargetUpdate(buffer);
				}
				@Override public void onPictureUpdate(ByteBuffer buffer) 
				{
					NavicoRadarTransportHandler.this.onPictureUpdate(buffer);
				}
			};
			
			navico.start();			
		}
		
		fireOnConnect();
	}
	
	protected void onTargetUpdate(ByteBuffer target) 
	{
		/*Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
		tuple.setAttribute(0, "NoTTM");
		tuple.setAttribute(1, new ByteBufferWrapper(target));
		fireProcess(tuple);*/
	}

	protected void onSpokeUpdate(ByteBuffer spoke) 
	{
		/*Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
		tuple.setAttribute(0, "NoTTM");
		tuple.setAttribute(1, new ByteBufferWrapper(spoke));
		fireProcess(tuple);*/
	}
	
	protected void onTargetUpdateTTM(String ttmMessage) {
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
		tuple.setAttribute(0, ttmMessage);
		fireProcess(tuple);
		System.out.println("TTM: " + ttmMessage);
//		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
//		tuple.setAttribute(0, "TTM Radar Track");
//		tuple.setAttribute(1, ttmMessage);
//		fireProcess(tuple);	
 /* Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
		tuple.setAttribute(0, ttmMessage);
		tuple.setAttribute(1, null);
		fireProcess(tuple);*/
	}	

	protected void onCat240SpokeUpdate(ByteBuffer cat240spoke) 
	{
		/*Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
		tuple.setAttribute(0, "Test cat240spoke");
		tuple.setAttribute(1, new ByteBufferWrapper(cat240spoke));
		fireProcess(tuple);*/
	}		
	
	protected void onPictureUpdate(ByteBuffer picture) 
	{
		// TODO: Necessary to do a rewind here?		
		picture.rewind();
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
		tuple.setAttribute(0, "NoTTM");
		tuple.setAttribute(1, new ByteBufferWrapper(picture));
		fireProcess(tuple);
	}
	
	@Override public void processInClose() throws IOException 
	{
		synchronized (processLock)
		{
			fireOnDisconnect();
			
			navico.stop();
			navico = null;
		}
	}

		
    @Override public boolean isSemanticallyEqualImpl(ITransportHandler o) 
    {
    	if(!(o instanceof NavicoRadarTransportHandler)) return false;
    	NavicoRadarTransportHandler other = (NavicoRadarTransportHandler)o;
    	
    	return other.radarSerial.equals(radarSerial);
    }

	@Override public void processOutOpen() throws IOException 
	{
		throw new IllegalArgumentException("Operator is not a Sink");
	}

	@Override public void processOutClose() throws IOException 
	{
		throw new IllegalArgumentException("Operator is not a Sink");
	}

	@Override public void send(byte[] message) throws IOException 
	{
		throw new IllegalArgumentException("Sending Not Supported");
	}

	public boolean setTargetData(int targetId, int range, int bearing, int bearingType) {
		return navico.AcquireTargets(targetId, range, bearing, bearingType);
	}

	public boolean setOwnShipData(int speedType, double speed, int headingType, double heading) {
		return navico.SetBoatSpeed(speedType, speed, headingType, heading);		
	}
}
