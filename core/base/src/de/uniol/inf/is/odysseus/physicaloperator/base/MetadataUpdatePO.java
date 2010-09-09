package de.uniol.inf.is.odysseus.physicaloperator.base;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.base.IMetadataUpdater;

/**
 * @author Jonas Jacobi
 */
public class MetadataUpdatePO<M extends IClone, T extends IMetaAttributeContainer<? extends M>> extends AbstractPipe<T, T>{

	private IMetadataUpdater<M, T> metadataFactory;

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
	
	public MetadataUpdatePO(IMetadataUpdater<M, T> mFac) {
		super();
		this.metadataFactory = mFac;
		this.setName(getName()+" "+mFac.getName());
	}

	public MetadataUpdatePO(MetadataUpdatePO<M, T> metadataUpdatePO) {
		this.metadataFactory = metadataUpdatePO.metadataFactory;
	}

	@Override
	protected void process_next(T object, int port) {
		this.metadataFactory.updateMetadata(object);
		transfer(object);
	}
	
	@Override
	public MetadataUpdatePO<M, T> clone() {
		return new MetadataUpdatePO<M,T>(this);
	}
	
	public String toString(){
		return super.toString() + " updateFac: " + this.metadataFactory.getClass();
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {	
		sendPunctuation(timestamp);
	}
}
