package de.uniol.inf.is.odysseus.fusion.metadata;


import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.AbstractMetadataUpdater;

/**
 * 
 * @author Kai Pancratz
 * 
 */
public class FusionProbabilityMetadataFactory extends AbstractMetadataUpdater<IFusionProbability, Tuple<IFusionProbability>>{

	SDFSchema schema;
	
	public FusionProbabilityMetadataFactory(SDFSchema schema){
		this.schema = schema;
	}
	
	@Override
	public void updateMetadata(Tuple<IFusionProbability> inElem) {
		inElem.setMetadata(new FusionProbability(inElem.getMetadata()));
	}

}