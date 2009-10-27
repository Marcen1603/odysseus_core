package de.uniol.inf.is.odysseus.objecttracking.metadata.factory;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.queryexecution.po.base.object.IntervalProbabilityLatencyPrediction;
import de.uniol.inf.is.odysseus.queryexecution.po.base.object.Latency;
import de.uniol.inf.is.odysseus.queryexecution.po.base.object.LinearProbabilityPredictionFunction;
import de.uniol.inf.is.odysseus.queryexecution.po.base.object.Probability;
import de.uniol.inf.is.odysseus.queryexecution.po.relational.object.MVRelationalTuple;
import de.uniol.inf.is.odysseus.querytranslation.parser.transformation.CQLAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * This class generates a new IntervalProbabilityLatencyPrediction object.
 * It fills latencyStart and the covariance. The start timestamp must be
 * set by another MFactory object. The prediction function will
 * be set by the SetPredictionOperator.
 * 
 * @author Andre Bolles
 *
 */
public class IntervalProbabilityLatencyPredictionMFactory implements IMetadataFactory<IntervalProbabilityLatencyPrediction, MVRelationalTuple<IntervalProbabilityLatencyPrediction>>{

	SDFAttributeList schema;
	
	public IntervalProbabilityLatencyPredictionMFactory(SDFAttributeList schema){
		this.schema = schema;
	}
	
	@Override
	public IntervalProbabilityLatencyPrediction createMetadata(
			MVRelationalTuple<IntervalProbabilityLatencyPrediction> inElem) {
		return this.createMetadata();
	}

	@Override
	public IntervalProbabilityLatencyPrediction createMetadata() {
		// TODO auch andere PredictionFunction als linear sollten genutzt werden können
		IntervalProbabilityLatencyPrediction mData = new IntervalProbabilityLatencyPrediction(new LinearProbabilityPredictionFunction(null, null, null), new Probability(), new Latency());
		mData.setLatencyStart(System.nanoTime());
		
		double[][] cov = null;
		int counter = 0;
		for(SDFAttribute attr: this.schema){
			if(SDFDatatypes.isMeasurementValue(attr.getDatatype())){
				ArrayList covariance = ((CQLAttribute)attr).getCovariance();
				if(cov == null){
					cov = new double[covariance.size()][covariance.size()];
				}
				
				for(int i = 0; i<covariance.size(); i++){
					cov[counter][i] = (Double)covariance.get(i);
				}
				counter++;
			}
		}
		
		mData.setCovariance(cov);
		
		return mData;
	}

}
