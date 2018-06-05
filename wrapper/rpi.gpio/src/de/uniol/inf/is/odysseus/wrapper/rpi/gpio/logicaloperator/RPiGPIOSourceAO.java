package de.uniol.inf.is.odysseus.wrapper.rpi.gpio.logicaloperator;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiGpioProvider;
import com.pi4j.io.gpio.impl.PinImpl;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.util.Constants;
import de.uniol.inf.is.odysseus.wrapper.rpi.gpio.RPiGPIOTransportHandler;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "RPiGPIOSource", doc="Source for Raspberry Pi GPIO-Port", category={LogicalOperatorCategory.SOURCE})
public class RPiGPIOSourceAO extends AbstractAccessAO {
	
	private static final long serialVersionUID = 1L;
	private Pin pin;
	
	public RPiGPIOSourceAO() {
		super();
		init();
	}

	public RPiGPIOSourceAO(RPiGPIOSourceAO other) {
		super(other);
		init();
		this.pin = other.pin;
	}
	
	private void init() {
		setTransportHandler(RPiGPIOTransportHandler.NAME);
		setWrapper(Constants.GENERIC_PULL);
		setDataHandler("Tuple");
		setProtocolHandler("none");
		
		
		//SDF Schema:
		List<SDFAttribute> schema = new LinkedList<>();
		schema.add(new SDFAttribute(null, "PinNumber", SDFDatatype.STRING));
		schema.add(new SDFAttribute(null, "PinState", SDFDatatype.LONG));
		setAttributes(schema);
		//['PinNumber', 'String'],['PinState', 'String']
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new RPiGPIOSourceAO(this);
	}

	@Parameter(name = "pin", doc = "GPIO Pin Number", type = IntegerParameter.class, optional = false)
    public void setPin(Integer pin) {
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
	
	@Override
	public boolean isSourceOperator() {
		return true;
	}
	
	@Override
	public boolean isSinkOperator() {
		return false;
	}
}
