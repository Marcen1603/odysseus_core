package de.uniol.inf.is.odysseus.wrapper.rpi.gpio.physicaloperator.access;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.Pin;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.wrapper.rpi.gpio.logicaloperator.RPiGPIOSourceAO;

public class RPiGPIOSourcePO extends AbstractSource<Tuple<?>> {
	private static final Logger LOG = LoggerFactory.getLogger(RPiGPIOSourcePO.class);
	private Pin pin;

	public RPiGPIOSourcePO(RPiGPIOSourceAO operator) {
		this.pin = operator.getPin2();
	}

	public RPiGPIOSourcePO(RPiGPIOSourcePO other) {
		this.pin = other.pin;
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		LOG.debug("process_open");
		
	}
	

	@Override
	public AbstractSource<Tuple<?>> clone() {
		return new RPiGPIOSourcePO(this);
	}
}
