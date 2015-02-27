package de.uniol.inf.is.odysseus.p2p_new.physicaloperator;

import net.jxta.id.ID;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.DistributedQueryRepresentationAO;

@SuppressWarnings("rawtypes")
public class DistributedQueryRepresentationPO<T extends IStreamObject> extends AbstractSource<T> {

	private final ID sharedQueryID;
	
	public DistributedQueryRepresentationPO( DistributedQueryRepresentationAO ao) {
		super();
		Preconditions.checkNotNull(ao, "AO must not be null!");
		setOutputSchema(SDFSchemaFactory.createNewTupleSchema("", Lists.<SDFAttribute>newArrayList()));
	
		this.sharedQueryID = ao.getSharedQueryID();
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		// no nothing, just represent...
	}

	public ID getSharedQueryID() {
		return sharedQueryID;
	}
	
	@Override
	public String toString() {
		if( sharedQueryID != null ) {
			return DistributedQueryRepresentationPO.class.getSimpleName() + " of " + sharedQueryID.toString();
		} 
		
		return DistributedQueryRepresentationPO.class.getSimpleName();
	}
	
}
