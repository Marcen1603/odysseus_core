package de.uniol.inf.is.odysseus.wrapper.rpi.gpio.logicaloperator;


import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractSenderAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.wrapper.rpi.gpio.RPiGPIOTransportHandler;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "RPIGPIOSINK", doc="Sink for Raspberry Pi GPIO-Port", category={LogicalOperatorCategory.SINK})
public class RPiGPIOSinkAO extends AbstractSenderAO {
	//extends AbstractLogicalOperator
//AbstractSenderAO
//AbstractLogicalOperator AbstractAccessAO
	private static final long serialVersionUID = 1L;
	private Integer pin;
	
	public RPiGPIOSinkAO() {
		super();
		init();
	}

	private void init() {
		setTransportHandler(RPiGPIOTransportHandler.NAME);
		setWrapper(Constants.GENERIC_PULL);
		setProtocolHandler("none");
		//setDataHandler();
		//setDataHandler("Tuple");
	}
	
	public RPiGPIOSinkAO(RPiGPIOSinkAO rpiGPIOSinkAO) {
		super(rpiGPIOSinkAO);
		init();
		this.pin = rpiGPIOSinkAO.pin;
	}
	
	@Parameter(name = "pin", doc = "GPIO Pin Number", type = IntegerParameter.class, optional = false)
    public void setPin(Integer pin) {
        this.pin = pin;
    }
	
	public Integer getPin(){
		return this.pin;
	}

	@Override
	public AbstractLogicalOperator clone() {
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
