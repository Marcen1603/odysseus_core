package de.uniol.inf.is.odysseus.fusion.metadata;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.AbstractMetadataUpdater;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

/**
 * 
 * @author Kai Pancratz
 * 
 */
@SuppressWarnings({"rawtypes"})
public class FusionProbabilityMetadataFactory extends AbstractMetadataUpdater<IFusionProbability, Tuple<IFusionProbability>>{

	SDFSchema schema;
	
	public FusionProbabilityMetadataFactory(SDFSchema schema){
		this.schema = schema;
	}
	
	@Override
	public void updateMetadata(Tuple<IFusionProbability> inElem) {
		
		//Nicht gut!!!
		inElem.setMetadata(new FusionProbability(inElem.getMetadata()));
	}

}