package de.uniol.inf.is.odysseus.wrapper.rpi.gpio.physicaloperator.access;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.StartFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.wrapper.rpi.gpio.logicaloperator.RPiGPIOSourceAO;

public class RPiGPIOSourcePO extends AbstractSource<Tuple<?>> {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(RPiGPIOSourcePO.class);
	private Integer pin;


	public RPiGPIOSourcePO(RPiGPIOSourceAO operator) {
		this.pin = operator.getPin();
	}

	public RPiGPIOSourcePO(RPiGPIOSourcePO other) {
		this.pin = other.pin;

	}


	@Override
	protected void process_open() throws OpenFailedException {

	}

	@Override
	protected void process_start() throws StartFailedException {

	}

	@Override
	public AbstractSource<Tuple<?>> clone() {
		return new RPiGPIOSourcePO(this);
	}
}
