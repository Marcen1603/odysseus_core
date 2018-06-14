package de.uniol.inf.is.odysseus.net.querydistribute.physicaloperator;

import java.util.UUID;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.StartFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.net.querydistribute.logicaloperator.DistributedQueryRepresentationAO;

@SuppressWarnings("rawtypes")
public class DistributedQueryRepresentationPO<T extends IStreamObject> extends AbstractSource<T> {

	private final UUID sharedQueryID;

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

	@Override
	protected void process_start() throws StartFailedException {
		// no nothing, just represent...
	}

	public UUID getSharedQueryID() {
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
