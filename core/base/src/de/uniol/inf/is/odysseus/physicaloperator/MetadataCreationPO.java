package de.uniol.inf.is.odysseus.physicaloperator;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFMetaAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFMetaAttributeList;

/**
 * @author Jonas Jacobi
 */
public class MetadataCreationPO<M extends IMetaAttribute, In extends IMetaAttributeContainer<M>> extends
		AbstractPipe<In, In> implements Serializable{

	private static final long serialVersionUID = 3783851208646530940L;

	private Class<M> type;

	public MetadataCreationPO(Class<M> type) {
		this.type = type;
	}

	public MetadataCreationPO(MetadataCreationPO<M, In> metadataCreationPO) {
		this.type = metadataCreationPO.type;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public void process_next(In object, int port) {
		try {
			object.setMetadata(type.newInstance());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		this.transfer(object);
	}

	@Override
	public String toString() {
		return super.toString() + " " + type.getName();
	}

	public Class<M> getType() {
		return type;
	}
	
	@Override
	public MetadataCreationPO<M, In> clone()  {
		return new MetadataCreationPO<M,In>(this);
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {	
		sendPunctuation(timestamp);
	}
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof MetadataCreationPO)) {
			return false;
		}
		MetadataCreationPO<?,?> mdcpo = (MetadataCreationPO<?,?>) ipo;
		if(this.getSubscribedToSource().equals(mdcpo.getSubscribedToSource()) && (this.getType().toString().equals(mdcpo.getType().toString()))) {
			return true;
		}
		return false;
	}
		
	@Override
	public SDFMetaAttributeList getMetaAttributeSchema() {
		SDFMetaAttributeList metalist = super.getMetaAttributeSchema();
		SDFMetaAttribute mataAttribute = new SDFMetaAttribute(type);
		if(!metalist.contains(mataAttribute)){
			metalist.add(mataAttribute);
		}
		return metalist;
	}		
}
