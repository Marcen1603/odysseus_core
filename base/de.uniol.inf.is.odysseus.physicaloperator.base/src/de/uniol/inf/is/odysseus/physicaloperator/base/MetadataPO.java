package de.uniol.inf.is.odysseus.physicaloperator.base;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.base.IMetadataFactory;


public class MetadataPO<M extends IClone, In extends IMetaAttribute, Out extends IMetaAttribute<M>> extends AbstractPipe<In, Out>{

	private IMetadataFactory<M, In> mFac;
	
	public MetadataPO(IMetadataFactory<M, In> mFac){
		this.mFac = mFac;
	}
	
	@Override
	public boolean modifiesInput() {
		return true;
	}
	
	public void process_next(In object, int port){
		M metadata = this.mFac.createMetadata(object);
		object.setMetadata(metadata);
		this.transfer((Out)object);
	}
	
	public IMetadataFactory<M, In> getMetadataFactory(){
		return this.mFac;
	}
	
	public String toString(){
		return super.toString() + " " + mFac.toString();
	}
}
