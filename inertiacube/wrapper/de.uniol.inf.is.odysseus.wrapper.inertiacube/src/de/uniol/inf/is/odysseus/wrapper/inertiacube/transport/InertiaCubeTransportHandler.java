package de.uniol.inf.is.odysseus.wrapper.inertiacube.transport;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Pointer;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces.ISD_HARDWARE_INFO_TYPE;
import de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces.ISD_STATION_DATA_TYPE;
import de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces.ISD_TRACKER_INFO_TYPE;
import de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces.ISD_TRACKING_DATA_TYPE;
import de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces.ISenseLib;
import de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces.ISenseLib.Bool;
import de.uniol.inf.is.odysseus.wrapper.inertiacube.interfaces.ISenseLib.Hwnd;

public class InertiaCubeTransportHandler extends AbstractPushTransportHandler  {
	/** Logger for this class. */
    private static Logger log = LoggerFactory.getLogger(InertiaCubeTransportHandler.class);
    
    /** Handle to the device. */
	private int handle;
	
	/** Is the sensor running. */
	private boolean running;
	
	/** Update timer. */
	private Timer updateTimer;
	
	/** Last tracked data. */
	private ISD_TRACKING_DATA_TYPE data = new ISD_TRACKING_DATA_TYPE();
	
	/** Stores all stations. */
	//private ISD_STATION_INFO_TYPE[] stations = new ISD_STATION_INFO_TYPE[8];
	
	/** Stores tracker info. */
	private ISD_TRACKER_INFO_TYPE trackerInfo;
	
	/** Stores hardware info. */
	private ISD_HARDWARE_INFO_TYPE hwInfo;
	
	/** Used station number. */
	short station = 1;
	
	/** Maximal possible stations. */
	int maxStations = 8;
	
	/** Last update time. */
	float lastTime;
	
	/**
	 * Default constructor.
	 */
	public InertiaCubeTransportHandler() {
		super();
	}
	
	/**
     * Copy constructor.
     * @param protocolHandler
     * Instance to copy.
     */
    public InertiaCubeTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
        super(protocolHandler, options);
    }

	@Override
	public void send(byte[] message) throws IOException {
		
	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		InertiaCubeTransportHandler handler = new InertiaCubeTransportHandler(protocolHandler, options);
		return handler;
	}

	@Override
	public String getName() {
		return "InertiaCube";
	}

	@Override
	public void processInOpen() throws IOException {
		if (running)
			return;

		Hwnd hWnd = new Hwnd();
		hWnd.setPointer(new Pointer(0));
		Bool bool = new Bool();
		bool.setPointer(new Pointer(0));
		handle = ISenseLib.ISD_OpenTracker(hWnd, 0, bool, bool);

		if (handle < 0) {
			log.error("Failed to detect InterSense tracking device");
		} else {
			log.debug("Connected to InertiaCube");

			trackerInfo = new ISD_TRACKER_INFO_TYPE();
			hwInfo = new ISD_HARDWARE_INFO_TYPE();

			bool.setPointer(new Pointer(1));
			ISenseLib.ISD_GetTrackerConfig(handle, trackerInfo, bool);

			if (ISenseLib.ISD_GetSystemHardwareInfo(handle, hwInfo)) {
				if (hwInfo.Valid) {
					maxStations = hwInfo.Capability.MaxStations;
				}
			}

			lastTime = ISenseLib.ISD_GetTime();
			running = true;

			updateTimer = new Timer();
			updateTimer.schedule(new UpdateTimerTask(), 0, 100);
		}
	}

	@Override
	public void processOutOpen() throws IOException {
		// In only
	}

	@Override
	public void processInClose() throws IOException {
		if (!running)
			return;
		
		running = false;
		updateTimer.cancel();
		updateTimer.purge();
		ISenseLib.ISD_CloseTracker(handle);
	}

	@Override
	public void processOutClose() throws IOException {
		// In only
	}

	class UpdateTimerTask extends TimerTask {
		@Override
		public void run() {
			ISenseLib.ISD_GetTrackingData(handle, data.byReference());
			ISD_STATION_DATA_TYPE d = data.Station[0];

			ByteBuffer res = ByteBuffer.allocateDirect(3 * 4);
			res.putFloat(d.Euler[0]);
			res.putFloat(d.Euler[1]);
			res.putFloat(d.Euler[2]);
			
			res.position(res.limit());
			fireProcess(res);
		}
	}
	
    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) {
    	if(!(o instanceof InertiaCubeTransportHandler)) {
    		return false;
    	} 	
    	return true;
    }
}
