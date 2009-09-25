package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;



public abstract class AbstractBinaryPNPipe<Read extends IMetaAttributeContainer<? extends IPosNeg>, Write extends IMetaAttributeContainer<? extends IPosNeg>> extends AbstractPNPipe<Read, Write> {
	
	int LEFT = 0;
	int RIGHT = 1;
	
	protected AbstractBinaryPNPipe(){
	}

	@Override
	public final void subscribeTo(ISource<? extends Read> source, int port) {
		if (port != 0 && port != 1) {
			throw new IllegalArgumentException("Subscription on illegal port ("
					+ port + ") for binary opperator");
		}
		super.subscribeTo(source, port);
	}
}
