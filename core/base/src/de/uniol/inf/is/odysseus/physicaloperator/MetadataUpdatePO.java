package de.uniol.inf.is.odysseus.physicaloperator;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.IMetadataUpdater;
import de.uniol.inf.is.odysseus.metadata.PointInTime;

/**
 * @author Jonas Jacobi
 */
public class MetadataUpdatePO<M extends IClone, T extends IMetaAttributeContainer<? extends M>> extends AbstractPipe<T, T>{

	private IMetadataUpdater<M, T> metadataFactory;

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
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
	
	@Override
	public String toString(){
		return super.toString() + " updateFac: " + this.metadataFactory.getClass();
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {	
		sendPunctuation(timestamp);
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof MetadataUpdatePO)) {
			return false;
		}
		MetadataUpdatePO mdupo = (MetadataUpdatePO) ipo;
		if(this.getSubscribedToSource().equals(mdupo.getSubscribedToSource()) &&
				this.metadataFactory.equals(mdupo.metadataFactory)) {
			return true;
		}
		return false;
	}
}
