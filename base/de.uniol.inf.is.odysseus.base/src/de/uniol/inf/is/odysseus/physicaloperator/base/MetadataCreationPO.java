package de.uniol.inf.is.odysseus.physicaloperator.base;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

/**
 * @author Jonas Jacobi
 */
public class MetadataCreationPO<M extends IMetaAttribute, In extends IMetaAttributeContainer<M>> extends
		AbstractPipe<In, In> {

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
		return new MetadataCreationPO(this);
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {	
		sendPunctuation(timestamp);
	}

}
