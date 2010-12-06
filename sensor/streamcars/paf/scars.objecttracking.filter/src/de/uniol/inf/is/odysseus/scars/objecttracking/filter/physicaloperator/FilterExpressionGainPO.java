/**
 *
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.CovarianceHelper;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IObjectTrackingLatency;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IStreamCarsExpressionVariable;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;


/**
 * @author dtwumasi
 * @param <StreamCarsMetaData>
 *
 */
public class FilterExpressionGainPO<M extends IProbability & IObjectTrackingLatency & IConnectionContainer & IGain> extends AbstractFilterExpressionPO<M> {

	private static final String METADATA_COV = "COVARIANCE";
	
	private TupleIndexPath scannedTupleIndexPath;
	private TupleIndexPath predictedTupleIndexPath;
	private CovarianceHelper covHelper;
	private String[] restrictedVariables;
	
	
	public FilterExpressionGainPO() {
		super();
	}

	public FilterExpressionGainPO(FilterExpressionGainPO<M> copy) {
		super(copy);
		
	}

	public void setRestrictedVariables(String[] restrictedVariables) {
		this.restrictedVariables = restrictedVariables;
	}
	
	public String[] getRestrictedVariables() {
		return restrictedVariables;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		covHelper = new CovarianceHelper(this.restrictedVariables, this.getOutputSchema());
	}

	@Override
	public FilterExpressionGainPO<M> clone() {
		return new FilterExpressionGainPO<M>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.sendPunctuation(timestamp);
	}

	@Override
	public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {
		object.getMetadata().setObjectTrackingLatencyStart("Filter Gain");
		
		scannedTupleIndexPath = this.getScannedObjectListSIPath().toTupleIndexPath(object);
		predictedTupleIndexPath = this.getPredictedObjectListSIPath().toTupleIndexPath(object);
		
		for (IConnection connected : object.getMetadata().getConnectionList()) {
			compute(object, connected);
		}
		
		object.getMetadata().setObjectTrackingLatencyEnd("Filter Gain");
		return object;
	}
	
	protected void compute(MVRelationalTuple<M> root, IConnection con) {
		
		for(IStreamCarsExpressionVariable variable : expression.getVariables()) {
			
			if(variable.isSchemaVariable() && variable.getMetadataInfo().equals(METADATA_COV)) {
				if(variable.isInList(scannedTupleIndexPath)) {
					variable.replaceVaryingIndex(con.getLeftPath().getLastTupleIndex().toInt());
					MVRelationalTuple<M> car = (MVRelationalTuple<M>)variable.getSchemaIndexPath().toTupleIndexPath(root).getTupleObject();
					double[][] cov = car.getMetadata().getCovariance();
					cov = covHelper.getCovarianceForRestrictedVariables(cov);
					variable.bind(covHelper.getCovarianceForRestrictedVariables(cov));
				} else if(variable.isInList(predictedTupleIndexPath)) {
					variable.replaceVaryingIndex(con.getRightPath().getLastTupleIndex().toInt());
					MVRelationalTuple<M> car = (MVRelationalTuple<M>)variable.getSchemaIndexPath().toTupleIndexPath(root).getTupleObject();
					double[][] cov = car.getMetadata().getCovariance();
					cov = covHelper.getCovarianceForRestrictedVariables(cov);
					variable.bind(covHelper.getCovarianceForRestrictedVariables(cov));
				}
			}
		}
		
		expression.evaluate();
		double[][] gain = (double[][])expression.getValue();
		MVRelationalTuple<M> predictedCar = (MVRelationalTuple<M>)con.getRightPath().getTupleObject();
		predictedCar.getMetadata().setGain(gain);
	}


}
