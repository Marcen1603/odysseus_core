package de.uniol.inf.is.odysseus.objecttracking.metadata.factory;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.metadata.base.AbstractMetadataUpdater;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IntervalProbabilityLatencyPrediction;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;
@SuppressWarnings("unchecked")
/**
 * This class generates a new IntervalProbabilityLatencyPrediction object.
 * It fills latencyStart and the covariance. The start timestamp must be
 * set by another MFactory object. The prediction function will
 * be set by the SetPredictionOperator.
 * 
 * @author Andre Bolles
 * @deprecated Sollte eigentlich nirgends mehr verwendet werden. Es muss ja
 *             nun eine Metadatum erzeugt werden, dass ILatency, IProbability,
 *             ITimeInterval, IApplicationTime und IPredictionFunctionKey
 *             erfüllt
 *
 */
@Deprecated
public class IntervalProbabilityLatencyPredictionMFactory extends AbstractMetadataUpdater<IntervalProbabilityLatencyPrediction, MVRelationalTuple<IntervalProbabilityLatencyPrediction>>{

	SDFAttributeList schema;
	
	public IntervalProbabilityLatencyPredictionMFactory(SDFAttributeList schema){
		this.schema = schema;
	}
	
	@Override
	public void updateMetadata(MVRelationalTuple<IntervalProbabilityLatencyPrediction> inElem) {
		inElem.getMetadata().setLatencyStart(System.nanoTime());
		
		double[][] cov = null;
		int counter = 0;
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
	}

}
