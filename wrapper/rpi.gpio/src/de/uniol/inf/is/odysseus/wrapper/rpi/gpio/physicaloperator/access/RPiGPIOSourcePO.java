package de.uniol.inf.is.odysseus.wrapper.rpi.gpio.physicaloperator.access;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.wrapper.rpi.gpio.logicaloperator.RPiGPIOSourceAO;

public class RPiGPIOSourcePO extends AbstractSource<Tuple<?>> {

	public RPiGPIOSourcePO(RPiGPIOSourceAO operator) {
		//this... = operator.....;
		
	}

	public RPiGPIOSourcePO(RPiGPIOSourcePO other) {
		//this... = other.....;
		
	}

	@Override
	protected void process_open() throws OpenFailedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AbstractSource<Tuple<?>> clone() {
		return new RPiGPIOSourcePO(this);
	}
}
