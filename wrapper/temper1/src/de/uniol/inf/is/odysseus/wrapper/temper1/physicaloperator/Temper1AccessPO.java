package de.uniol.inf.is.odysseus.wrapper.temper1.physicaloperator;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.wrapper.temper1.logicaloperator.Temper1AccessAO;

public class Temper1AccessPO extends AbstractSource<Tuple<?>> {
	
	private Integer tempNumber;

	public Temper1AccessPO(Temper1AccessAO operator) {
		this.tempNumber = operator.getTempNumber();
		//addParameterInfo(Temper1TransportHandler.TEMPNUMBER, this.tempNumber);
		
		
	}

	public Temper1AccessPO(Temper1AccessPO other) {
		this.tempNumber = other.tempNumber;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AbstractSource<Tuple<?>> clone() {
		return new Temper1AccessPO(this);
	}
}
