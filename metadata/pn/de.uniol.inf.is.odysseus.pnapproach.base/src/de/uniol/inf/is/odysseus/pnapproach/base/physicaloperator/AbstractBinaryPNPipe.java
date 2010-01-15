package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;



public abstract class AbstractBinaryPNPipe<Read extends IMetaAttributeContainer<? extends IPosNeg>, Write extends IMetaAttributeContainer<? extends IPosNeg>> extends AbstractPNPipe<Read, Write> {
	
	int LEFT = 0;
	int RIGHT = 1;
	
	protected AbstractBinaryPNPipe(){
	}

	@Override
	public final void subscribeToSource(ISource<? extends Read> source, int sinkPort, int sourcePort, SDFAttributeList schema) {
		if (sinkPort != 0 && sinkPort != 1) {
			throw new IllegalArgumentException("Subscription on illegal port ("
					+ sinkPort + ") for binary opperator");
		}
		super.subscribeToSource(source, sinkPort, sourcePort, schema);
	}
}
