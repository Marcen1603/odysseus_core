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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSink;
import de.uniol.inf.is.odysseus.wrapper.rpi.gpio.OSInvestigator;
import de.uniol.inf.is.odysseus.wrapper.rpi.gpio.OSInvestigator.OS;
import de.uniol.inf.is.odysseus.wrapper.rpi.gpio.logicaloperator.RPiGPIOSinkAO;

public class RPiGPIOSinkPO extends AbstractSink<Tuple<?>> {
	private static final Logger LOG = LoggerFactory
			.getLogger(RPiGPIOSinkPO.class);
	private Pin pin;
	private SDFSchema schema;
	private GpioController gpioController;
	private GpioPinDigitalOutput myLED;
	private boolean flagExceptionProcessNext = false;
	private PinState pinState;
	private boolean messageWasShownTupleLedNUll = false;
	private int pinStateIndex = -1;
	private OS os;
	private boolean pinStateMessageFlag = false;

	public RPiGPIOSinkPO(SDFSchema s) {
		initGPIOController();
		this.schema = s;

		final SDFAttribute pinStateAttribute = schema.findAttribute("PinState");
		if (pinStateAttribute != null) {
			this.pinStateIndex = schema.indexOf(pinStateAttribute);
		}
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
		this.pinStateIndex = other.pinStateIndex;

		initGPIOPins();
	}

	public RPiGPIOSinkPO(RPiGPIOSinkAO operator, SDFSchema schema) {
		this.schema = schema;

		initGPIOController();

		this.pin = operator.getPin2();
		this.pinState = operator.getPinStatePinState();

		initGPIOPins();
	}

	private void setPinState(PinState pinState) {
		this.pinState = pinState;
	}

	private void initGPIOController() {
		LOG.debug("RPiGPIOSinkPO init()");

		try {
			os = OSInvestigator.getCurrentOS();

			switch (os) {
			case NUX:
				gpioController = GpioFactory.getInstance();
				break;
			case WIN:
			case MAC:
			case UNKNOWN:
				LOG.warn(""
						+ RPiGPIOSinkAO.class
						+ " works only on raspberry pi with linux and pi4j library!");
				break;
			default:
				break;
			}

		} catch (UnsatisfiedLinkError ex) {
			LOG.error(ex.getMessage(), ex);
		}
	}

	private void initGPIOPins() {
		LOG.error("RPiGPIOSinkPO initGPIOPins() pin:" + this.pin);

		if (gpioController == null || !os.equals(OS.NUX)) {

			return;
		}

		try {
			// LOG.error("\n\r ---- pinState:"+pinState.toString()+"\n\r ------");
			if (pinState != null) {
				this.myLED = gpioController.provisionDigitalOutputPin(this.pin,
						this.pin.getName(), pinState);
			} else {
				this.myLED = gpioController.provisionDigitalOutputPin(this.pin,
						this.pin.getName(), PinState.LOW);
				LOG.debug("pinState==null");
			}
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
		}
	}

	@Override
	protected void process_next(Tuple<?> tuple, int port) {
		try {
			String m = "RPiGPIOSinkPO process_next:";
			if (tuple != null) {
				String value;
				if (this.pinStateIndex >= 0) {
					value = tuple.getAttribute(this.pinStateIndex).toString();
				} else {
					value = tuple.getAttribute(1).toString();
				}

				m += " tuple pinvalue:" + value;
			}

			LOG.debug(m);

			if (!pinStateMessageFlag) {
				LOG.debug("pin:" + this.pin.getAddress() + " pinStateIndex:"
						+ this.pinStateIndex);
				pinStateMessageFlag = true;
			}

			if (tuple == null || this.myLED == null) {
				if (!messageWasShownTupleLedNUll) {
					if (tuple == null) {
						LOG.error("tuple is null!");
					}
					if (this.myLED == null) {
						LOG.error("this.myLED is null!");

					}

					messageWasShownTupleLedNUll = true;
				}
				return;
			}
			messageWasShownTupleLedNUll = false;

			String value;
			if (this.pinStateIndex >= 0) {
				value = tuple.getAttribute(this.pinStateIndex).toString();
			} else {
				value = tuple.getAttribute(1).toString();
			}
			//TODO: make it possible to use low pinstates.

			String compareValue = "1";

			if (value.equals(compareValue)) {
				this.myLED.high();
			} else {
				this.myLED.low();
			}
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

	public int getPinStateIndex() {
		return this.pinStateIndex;
	}

}
