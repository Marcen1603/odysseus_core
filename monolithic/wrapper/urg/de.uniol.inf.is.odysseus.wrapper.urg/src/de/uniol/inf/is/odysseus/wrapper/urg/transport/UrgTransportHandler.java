package de.uniol.inf.is.odysseus.wrapper.urg.transport;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.urg.Communication;
import de.uniol.inf.is.odysseus.wrapper.urg.DisableLaserCommand;
import de.uniol.inf.is.odysseus.wrapper.urg.EnableLaserCommand;
import de.uniol.inf.is.odysseus.wrapper.urg.MessageListener;
import de.uniol.inf.is.odysseus.wrapper.urg.StartMeasurementCommand;
import de.uniol.inf.is.odysseus.wrapper.urg.utils.DeviceConnectionException;

public class UrgTransportHandler extends AbstractPushTransportHandler {
	private String comPort = null;
	
	/**
	 * Default constructor.
	 */
	public UrgTransportHandler() {
		super();
	}
	
	/**
     * Copy constructor.
     * @param protocolHandler
     * Instance to copy.
     */
    public UrgTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
        super(protocolHandler, options);
		if (options.containsKey("port")) comPort = options.get("port");
    }

	@Override
	public void send(byte[] message) throws IOException {
	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		UrgTransportHandler handler = new UrgTransportHandler(protocolHandler, options);
		return handler;
	}

	@Override
	public String getName() {
		return "URG";
	}

	@Override
	public void processInOpen() throws IOException {
		if (comPort == null) {
			ArrayList<String> ports = Communication.getInstance().getAvailablePorts();
			if (ports.size() == 0) {
				throw new DeviceConnectionException("No serial port device found.");
			}
			comPort = ports.get(0);
		}
		
		Communication.getInstance().connect(comPort);
		Communication.getInstance().addMessageListener(messageListener);
		Communication.getInstance().executeCommand(new EnableLaserCommand());
		Communication.getInstance().executeCommand(new StartMeasurementCommand());
	}

	@Override
	public void processInClose() throws IOException {
		Communication.getInstance().removeMessageListener(messageListener);
		Communication.getInstance().executeCommand(new DisableLaserCommand());
		Communication.getInstance().disconnect();
	}

	@Override
	public void processOutOpen() throws IOException {
		// In only
	}

	@Override
	public void processOutClose() throws IOException {
		// In only
	}
	
	private MessageListener messageListener = new MessageListener() {
		@Override
		public void messageReceived(ByteBuffer buffer) {
			buffer.position(buffer.limit());
			fireProcess(buffer);
		}
	};
	
    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) {
    	if(!(o instanceof UrgTransportHandler)) {
    		return false;
    	}
    	UrgTransportHandler other = (UrgTransportHandler)o;
    	if(!this.comPort.equals(other.comPort)) {
    		return false;
    	}
    	
    	return true;
    }
}
