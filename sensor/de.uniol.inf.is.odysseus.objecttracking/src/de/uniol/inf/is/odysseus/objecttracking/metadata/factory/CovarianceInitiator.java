package de.uniol.inf.is.odysseus.objecttracking.metadata.factory;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.metadata.base.AbstractMetadataUpdater;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

public class CovarianceInitiator extends AbstractMetadataUpdater<ObjectTrackingMetadata, MVRelationalTuple<ObjectTrackingMetadata>> {

	SDFAttributeList schema;
	
	@Override
	public void updateMetadata(MVRelationalTuple<ObjectTrackingMetadata> inElem) {
		
		double[][] cov = null;
		ArrayList<int[]> paths = getPathsOfMeasurements(this.schema);
		
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
	
	public static ArrayList<int[]> getPathsOfMeasurements(SDFAttributeList attrList) {
		ArrayList<int[]> tmp = new ArrayList<int[]>();
		
		return getPathsOfMeasurementValue(attrList, null, tmp);
	}
	
	private static ArrayList<int[]> getPathsOfMeasurementValue(SDFAttributeList attrList, int[] preliminaryPath, ArrayList<int[]> tmp) {
		
		//Erster Durchlauf:
		if (preliminaryPath == null) {
			preliminaryPath = new int[0];
		}
		
		int counter = 0;
		for (SDFAttribute attr : attrList) {
			int[] singlePath = new int[preliminaryPath.length+1];
			for (int i=0; i<singlePath.length-1; i++) {
				singlePath[i] = preliminaryPath[i];	
			}
			singlePath[singlePath.length-1] = counter;
			
			if (SDFDatatypes.isMeasurementValue(attr.getDatatype())){
				tmp.add(singlePath);
			}
			if (!SDFDatatypes.isMeasurementValue(attr.getDatatype()) && attr.getSubattributes() != null) {
				tmp = getPathsOfMeasurementValue(attr.getSubattributes(), singlePath, tmp);
			}
			counter++;
		}
		
		return tmp;
	}

}
