package de.uniol.inf.is.odysseus.objecttracking.metadata.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.base.AbstractMetadataUpdater;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;
import de.uniol.inf.is.odysseus.scars.objecttracking.OrAttributeResolver;
@SuppressWarnings("unchecked")

public class CovarianceInitiator extends AbstractMetadataUpdater<ObjectTrackingMetadata, MVRelationalTuple<ObjectTrackingMetadata>> {

	SDFAttributeList schema;
	
	@Override
	public void updateMetadata(MVRelationalTuple<ObjectTrackingMetadata> inElem) {
		
		double[][] cov = null;
		ArrayList<int[]> paths = OrAttributeResolver.getPathsOfMeasurements(this.schema);
		
		int counter = 0;
		
		// For each SDFAttribute in the schema:
		for(SDFAttribute attr: this.schema){
			if(SDFDatatypes.isMeasurementValue(attr.getDatatype())){
				ArrayList covariance = ((SDFAttribute)attr).getCovariance();
				
				if(cov == null){
					cov = new double[covariance.size()][covariance.size()];
				}
				
				for(int i = 0; i<covariance.size(); i++){
					cov[counter][i] = (Double)covariance.get(i);
				}
				
				counter++;
			}
		}
		
		inElem.getMetadata().setCovariance(cov);
		inElem.getMetadata().setAttributePaths(paths);
	}
}
