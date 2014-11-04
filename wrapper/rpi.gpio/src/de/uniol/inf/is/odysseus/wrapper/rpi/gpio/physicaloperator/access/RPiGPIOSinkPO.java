package de.uniol.inf.is.odysseus.wrapper.rpi.gpio.physicaloperator.access;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.wrapper.rpi.gpio.OSInvestigator;
import de.uniol.inf.is.odysseus.wrapper.rpi.gpio.logicaloperator.RPiGPIOSinkAO;

public class RPiGPIOSinkPO extends AbstractSink<Tuple<?>> {
	private static final Logger LOG = LoggerFactory
			.getLogger(RPiGPIOSinkPO.class);
	private Pin pin;
	@SuppressWarnings("unused")
	private SDFSchema schema;
	private GpioController gpioController;
	private GpioPinDigitalOutput myLED;
	private boolean flagExceptionProcessNext = false;
	private PinState pinState;
	private boolean messageWasShownTupleLedNUll = false;

	public RPiGPIOSinkPO(SDFSchema s) {
		initGPIOController();
		this.schema = s;
	}

	public RPiGPIOSinkPO(RPiGPIOSinkAO operator) {
		initGPIOController();

		this.pin = operator.getPin2();
		this.pinState = operator.getPinStatePinState();

		initGPIOPins();
	}

	public RPiGPIOSinkPO(RPiGPIOSinkPO other) {
		super(other);

		initGPIOController();

		setPin(other.pin);
		setPinState(other.pinState);

		initGPIOPins();
	}

	private void setPinState(PinState pinState) {
		this.pinState = pinState;
	}

	private void initGPIOController() {
		LOG.debug("RPiGPIOSinkPO init()");

		try {
			OSInvestigator.OS os = OSInvestigator.getCurrentOS();

			switch (os) {
			case NUX:
				gpioController = GpioFactory.getInstance();
				break;
			case WIN:
			case MAC:
			case UNKNOWN:
				LOG.warn("The RPiGPIO TransportHandler works only on the raspberry pi with linux and the pi4j library!");
				break;
			default:
				break;
			}

		} catch (UnsatisfiedLinkError ex) {
			LOG.error(ex.getMessage(), ex);
		}
	}

	private void initGPIOPins() {
		if (gpioController == null) {
			return;
		}

		LOG.debug("initGPIOPins()");
		try {
			// LOG.error("\n\r ---- pinState:"+pinState.toString()+"\n\r ------");
			if (pinState != null) {
				myLED = gpioController.provisionDigitalOutputPin(pin,
						pin.getName(), pinState);
			} else {
				myLED = gpioController.provisionDigitalOutputPin(pin,
						pin.getName(), PinState.LOW);
				LOG.debug("pinState==null");
			}
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
		}
	}

	@Override
	protected void process_next(Tuple<?> tuple, int port) {
		if (tuple == null || myLED == null) {
			if (!messageWasShownTupleLedNUll) {
				LOG.error("tuple or myLED is null!");
				messageWasShownTupleLedNUll = true;
			}
			return;
		} else {
			messageWasShownTupleLedNUll = false;
		}

		try {
			@SuppressWarnings("unused")
			String w = "";

			if (tuple != null) {
				w += "tuple.size:" + tuple.size();
				if (tuple.size() > 0) {
					w += " Att1:" + tuple.getAttribute(0);
				}
				if (tuple.size() > 1) {
					w += " Att2:" + tuple.getAttribute(1);

					// LOG.error("Att:"+tuple.getAttribute(1));

					String value = tuple.getAttribute(1).toString();
					String compareValue = (String) "1";

					if (value.equals(compareValue)) {
						myLED.high();
					} else {
						myLED.low();
					}
				}
				if (tuple.size() > 2) {
					w += " Att3:" + tuple.getAttribute(2);
				}
			}

			// LOG.error("process_next port:"+port+" w:"+w);

			// String pin = tuple.getAttribute(0);
			// String pinState = tuple.getAttribute(1);

			// LOG.debug("pin:"+pin+" pinState:"+pinState);

			// System.out.println("pin:"+pin+" pinState:"+pinState);

		} catch (Exception ex) {
			if (!flagExceptionProcessNext) {
				LOG.error(
						"Method: process_next(Tuple<?> tuple, int port) ExceptionMessage:"
								+ ex.getMessage(), ex);
				flagExceptionProcessNext = true;
			}
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub

	}

	public void setPin(Pin pin) {
		this.pin = pin;
	}

	@Override
	public AbstractSink<Tuple<?>> clone() {
		return new RPiGPIOSinkPO(this);
	}

}
