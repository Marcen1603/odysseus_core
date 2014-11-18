package de.uniol.inf.is.odysseus.wrapper.rpi.gpio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiGpioProvider;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.impl.PinImpl;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.rpi.gpio.OSInvestigator.OS;

public class RPiGPIOPushTransportHandler extends AbstractTransportHandler {
	private static final Logger LOG = LoggerFactory
			.getLogger(RPiGPIOPushTransportHandler.class);

	public static final String NAME = "RPiGPIOPush";
	private static final String PINNUMBER = "pin";

	// Example PushTransportHandler: OPCDATransportHandler<T>

	private Pin pin;
	private GpioController gpioController;
	private GpioPinDigitalInput myButton;
	private boolean flagExceptionThrown;

	private boolean getOutputStreamFlag = false;
	private boolean getInputStreamFlag = false;

	public RPiGPIOPushTransportHandler() {
	}

	public RPiGPIOPushTransportHandler(
			final IProtocolHandler<?> protocolHandler, final OptionMap options) {
		super(protocolHandler, options);
		this.init(options);
		this.init();
	}

	private void init(OptionMap options) {
		if (options.containsKey(PINNUMBER)) {
			this.setPinNumber(options.get(PINNUMBER));
		}
	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		return new RPiGPIOPushTransportHandler(protocolHandler, options);
	}

	private void setPinNumber(String pinNumber) {
		Integer pinInteger = Integer.parseInt(pinNumber);

		LOG.debug("setPinNumber:" + pinNumber);

		if (pinInteger.intValue() == 1) {
			this.pin = new PinImpl(RaspiGpioProvider.NAME,
					pinInteger.intValue(), "GPIO " + pinNumber.toString(),
					EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT,
							PinMode.PWM_OUTPUT), PinPullResistance.all());
		} else {
			this.pin = new PinImpl(RaspiGpioProvider.NAME,
					pinInteger.intValue(), "GPIO " + pinNumber.toString(),
					EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT),
					PinPullResistance.all());
		}
	}

	private void init() {
		OS os = OSInvestigator.getCurrentOS();

		switch (os) {
		case NUX:
			break;
		case WIN:
		case MAC:
		case UNKNOWN:
			LOG.warn("The RPiGPIOPushTransportHandler works only on raspberry pi with linux and pi4j library!");
			//startRepeatedTestValues();
			return;
		default:
			break;
		}

		initIfNeeded();

	}

	private void startRepeatedTestValues() {
		LOG.debug("startRepeatedTestValues");

		@SuppressWarnings("rawtypes")
		Tuple<?> tuple = new Tuple(2, false);
		tuple.setAttribute(0, "pinNumber7");
		tuple.setAttribute(1, 0);

		fireProcess(tuple);

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					@SuppressWarnings("rawtypes")
					Tuple<?> tuple = new Tuple(2, false);
					tuple.setAttribute(0, "pinNumber7");
					tuple.setAttribute(1, 1);

					fireProcess(tuple);

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
		});
		t.setName("test values RPiGPIOPushTransportHandler gpio input pins");
		t.setDaemon(true);
		t.start();
	}

	private void initIfNeeded() {
		if (gpioController == null) {
			gpioController = GpioFactory.getInstance();
		}

		if (myButton == null) {
			myButton = gpioController.provisionDigitalInputPin(this.pin,
					"MyButton");
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {
		LOG.debug("processInOpen");

		OS os = OSInvestigator.getCurrentOS();

		switch (os) {
		case NUX:
			myButton.addListener(new GpioPinListenerDigital() {
				@Override
				public void handleGpioPinDigitalStateChangeEvent(
						GpioPinDigitalStateChangeEvent event) {

					LOG.debug(" --> GPIO PIN STATE CHANGE: " + event.getPin()
							+ " = " + event.getState());

					// LOG.debug("init with pin:" + pin.toString());

					@SuppressWarnings("rawtypes")
					Tuple<?> tuple = new Tuple(2, false);

					try {
						boolean buttonPressed = myButton.isHigh();

						String buttonState = "";
						if (buttonPressed) {
							buttonState = "1";
						} else {
							buttonState = "0";
						}

						tuple.setAttribute(0, "pinNumber"
								+ myButton.getPin().getAddress());
						tuple.setAttribute(1, Long.parseLong(buttonState));
						// TODO:
						// tuple.setAttribute(2, System.currentTimeMillis());
						// Starttimestamp
					} catch (ClassCastException ex) {
						LOG.error(ex.getMessage(), ex);
					} catch (Exception ex) {
						if (!flagExceptionThrown) {
							LOG.error("On Raspberry Pi? pi4j installed? ");
							flagExceptionThrown = true;
						}

						tuple.setAttribute(0, "pinNumber7");
						tuple.setAttribute(1, 1);
					}

					fireProcess(tuple);
				}
			});
			break;
		case WIN:
		case MAC:
		case UNKNOWN:

			startRepeatedTestValues();
			return;
		default:
			break;
		}

	}

	@Override
	public void processOutOpen() throws IOException {
		LOG.debug("processOutOpen");

	}

	@Override
	public void processInClose() throws IOException {
		LOG.debug("processInClose");
		
		myButton.removeAllListeners();
		gpioController.removeAllListeners();
		
		myButton=null;
		gpioController=null;
	}

	@Override
	public void processOutClose() throws IOException {
		LOG.debug("processOutClose");
	}

	@Override
	public void send(byte[] message) throws IOException {
		LOG.debug("send message:" + message.length);

		String messageString = new String(message);

		LOG.debug("send messageString:" + messageString);

		Long number = Long.parseLong(messageString);
		LOG.debug("send number:" + number);

	}

	@Override
	public InputStream getInputStream() {
		if (!getInputStreamFlag) {
			LOG.debug("getInputStream");
			getInputStreamFlag = true;
		}

		return null;
	}

	@Override
	public OutputStream getOutputStream() {
		if (!getOutputStreamFlag) {
			LOG.debug("getOutputStream");
			getOutputStreamFlag = true;
		}

		return null;
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}
}
