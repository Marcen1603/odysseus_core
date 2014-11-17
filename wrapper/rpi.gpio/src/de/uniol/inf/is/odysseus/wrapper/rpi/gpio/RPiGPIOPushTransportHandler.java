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

public class RPiGPIOPushTransportHandler extends AbstractTransportHandler {
	private static final Logger LOG = LoggerFactory
			.getLogger(RPiGPIOPushTransportHandler.class);

	private static final String PINNUMBER = "pin";

	// Example PushTransportHandler: OPCDATransportHandler<T>

	private Pin pin;
	private GpioController gpioController;
	private GpioPinDigitalInput myButton;
	private boolean flagExceptionThrown;

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, OptionMap options) {
		RPiGPIOPushTransportHandler tHandler = new RPiGPIOPushTransportHandler();

		if (options.containsKey(PINNUMBER)) {
			tHandler.setPinNumber(options.get(PINNUMBER));
		}

		tHandler.init();
		protocolHandler.setTransportHandler(tHandler);

		return tHandler;
	}

	private void setPinNumber(String pinNumber) {
		Integer pinInteger = Integer.parseInt(pinNumber);

		LOG.error("setPinNumber");

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
		initIfNeeded();

		myButton.addListener(new GpioPinListenerDigital() {
			@Override
			public void handleGpioPinDigitalStateChangeEvent(
					GpioPinDigitalStateChangeEvent event) {

				LOG.debug(" --> GPIO PIN STATE CHANGE: " + event.getPin()
						+ " = " + event.getState());

				LOG.debug("init with pin:" + pin.toString());

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
	}

	private void initIfNeeded() {
		if (gpioController == null) {
			gpioController = GpioFactory.getInstance();
			if (myButton == null) {
				myButton = gpioController.provisionDigitalInputPin(this.pin,
						"MyButton");
			}
		}
	}

	@Override
	public String getName() {
		return "RPiGPIOPush";
	}

	@Override
	public void processInOpen() throws IOException {
	}

	@Override
	public void processOutOpen() throws IOException {
	}

	@Override
	public void processInClose() throws IOException {
	}

	@Override
	public void processOutClose() throws IOException {
	}

	@Override
	public void send(byte[] message) throws IOException {
	}

	@Override
	public InputStream getInputStream() {
		return null;
	}

	@Override
	public OutputStream getOutputStream() {
		return null;
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}
}
