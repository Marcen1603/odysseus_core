package de.uniol.inf.is.odysseus.wrapper.rpi.gpio.logicaloperator;


import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiGpioProvider;
import com.pi4j.io.gpio.impl.PinImpl;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "RPIGPIOSINK", doc="Sink for Raspberry Pi GPIO-Port", category={LogicalOperatorCategory.SINK})
public class RPiGPIOSinkAO extends AbstractLogicalOperator {
	private static final Logger LOG = LoggerFactory
			.getLogger(RPiGPIOSinkAO.class);
	
	private static final long serialVersionUID = 1L;
	private Pin pin;
	private PinState pinState;
	
	public RPiGPIOSinkAO() {
		super();
		init();
	}

	private void init() {
		//setTransportHandler(RPiGPIOPushTransportHandler.NAME);
		//setWrapper(Constants.GENERIC_PUSH);
		
		//setProtocolHandler("none");
		//setDataHandler();
		//setDataHandler("Tuple");
		
		//setDataHandler(new TupleDataHandler().getSupportedDataTypes().get(0));
	}
	
	public RPiGPIOSinkAO(RPiGPIOSinkAO other) {
		super(other);
		init();
		this.pin = other.pin;
	}
	
	@Parameter(name = "pin", doc = "GPIO Pin Number", type = IntegerParameter.class, optional = false)
    public void setPin(Integer pin) {
		LOG.error("setPin:"+pin.intValue());
		
		// see class: com.pi4j.io.gpio.RaspiPin;
		if(pin.intValue() == 1){
			this.pin = new PinImpl(RaspiGpioProvider.NAME, pin.intValue(), "GPIO "+pin.toString(), 
                    EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT, PinMode.PWM_OUTPUT),
                    PinPullResistance.all());
		}else{
			this.pin = new PinImpl(RaspiGpioProvider.NAME, pin.intValue(), "GPIO "+pin.toString(), 
	                EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT),
	                PinPullResistance.all());
		}
    }
	
	public Integer getPin(){
		return new Integer(this.pin.getAddress());
	}
	
	public Pin getPin2(){
		return this.pin;
	}
	
	@Parameter(name = "pinstate", doc = "GPIO Pin state ('high' or 'low')", type = StringParameter.class, optional = false)
	public void setPinState(String pinState) throws Exception {
		LOG.debug("setPinState:"+pinState);
		
		switch (pinState.toLowerCase()) {
		case "high":
			this.pinState = PinState.HIGH;
			return;
		case "low":
			this.pinState = PinState.LOW;
			return;
		default:
			break;
		}
		throw new Exception("No correct pin state was entered.");
	}
	
	public String getPinState(){
		return this.pinState.toString();
	}
	
	public PinState getPinStatePinState(){
		return this.pinState;
	}

	@Override
    public RPiGPIOSinkAO clone() {
        return new RPiGPIOSinkAO(this);
    }
	
	@Override
	public boolean isSourceOperator() {
		return false;
	}
	
	@Override
	public boolean isSinkOperator() {
		return true;
	}
}
