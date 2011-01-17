package de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.CovarianceExpressionHelper;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.CovarianceHelper;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IObjectTrackingLatency;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IStreamCarsExpressionVariable;
import de.uniol.inf.is.odysseus.scars.util.CovarianceMapper;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;

/**
 * @author dtwumasi
 *
 */
public class FilterExpressionCovarianceUpdatePO<M extends IGain & IProbability & IObjectTrackingLatency & IConnectionContainer> extends AbstractFilterExpressionPO<M> {
	
	private static final String METADATA_COV = "COVARIANCE";
	private static final String GAIN = "GAIN";
	private static final String IDENTITY_MATRIX = "IDENTITY_MATRIX";
	
	private TupleIndexPath scannedTupleIndexPath;
	private TupleIndexPath predictedTupleIndexPath;
	private CovarianceHelper predCovHelper;
	private CovarianceHelper scanCovHelper;
	private CovarianceMapper covMapper;
	
	private CovarianceExpressionHelper covHelper;
	
	public FilterExpressionCovarianceUpdatePO() {
		super();
	}

	public FilterExpressionCovarianceUpdatePO(FilterExpressionCovarianceUpdatePO<M> copy) {
		super(copy);
		this.scannedTupleIndexPath = copy.scannedTupleIndexPath.clone();
		this.predictedTupleIndexPath = copy.predictedTupleIndexPath.clone();
//		this.predCovHelper = new CovarianceHelper(copy.predCovHelper);
//		this.scanCovHelper = new CovarianceHelper(copy.scanCovHelper);
		this.covMapper = new CovarianceMapper(copy.covMapper);
		this.covHelper = copy.covHelper;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		predCovHelper = new CovarianceHelper(this.getOutputSchema());
		scanCovHelper = new CovarianceHelper(this.getOutputSchema());
		covMapper = new CovarianceMapper(this.getInputSchema());
		this.covHelper = new CovarianceExpressionHelper();
	}
	
	
	@SuppressWarnings("unchecked")
	public void compute(IConnection con, MVRelationalTuple<M> tuple) {
		for(IStreamCarsExpressionVariable variable : expression.getVariables()) {
			if(variable.isSchemaVariable() && METADATA_COV.equals(variable.getMetadataInfo())) {
				if(variable.isInList(scannedTupleIndexPath)) {
					variable.replaceVaryingIndex(con.getLeftPath().getLastTupleIndex().toInt());
					MVRelationalTuple<M> car = (MVRelationalTuple<M>)con.getLeftPath().getTupleObject();
//					double[][] cov = car.getMetadata().getCovariance();
					String[] restrictedVariables = car.getMetadata().getRestrictedList();
//					cov = scanCovHelper.getCovarianceForRestrictedVariables(cov, restrictedVariables);
//					variable.bind(cov);
					variable.bind(covHelper.getCovarianceForRestictedAttributes(restrictedVariables, car.getMetadata()));
				} else if(variable.isInList(predictedTupleIndexPath)) {
					variable.replaceVaryingIndex(con.getRightPath().getLastTupleIndex().toInt());
					MVRelationalTuple<M> car = (MVRelationalTuple<M>)con.getRightPath().getTupleObject();
//					double[][] cov = car.getMetadata().getCovariance();
					String[] restrictedVariables = car.getMetadata().getRestrictedList();
					
//					cov = predCovHelper.getCovarianceForRestrictedVariables(cov, restrictedVariables);
//					variable.bind(cov);
					variable.bind(covHelper.getCovarianceForRestictedAttributes(restrictedVariables, car.getMetadata()));
				}
			} else if(variable.isSchemaVariable() && variable.getMetadataInfo().equals(GAIN)) {
				MVRelationalTuple<M> car = (MVRelationalTuple<M>)con.getRightPath().getTupleObject();
				variable.bind(car.getMetadata().getGain());
			} else if(!variable.isSchemaVariable() && variable.getName().equals(IDENTITY_MATRIX)) {
				MVRelationalTuple<M> car = (MVRelationalTuple<M>)con.getRightPath().getTupleObject();
				double[][] gain = car.getMetadata().getGain();
				variable.bind(makeIdentityMatrix(gain));
			}
		}
		expression.evaluate();
		double[][] newCovariance = (double[][])expression.getValue();
		MVRelationalTuple<M> car = (MVRelationalTuple<M>)con.getRightPath().getTupleObject();
		String[] restrictedVariables = car.getMetadata().getRestrictedList();
		updateCovariance(car, newCovariance, restrictedVariables);
		
	}
	
	private void updateCovariance(MVRelationalTuple<M> car, double[][] restCov, String[] restrictedList) {
		List<Integer> restrictedIndicesList = new ArrayList<Integer>(restrictedList.length);
//		int[] restrictedIndices = new int[restrictedList.length];
		IProbability prob = car.getMetadata();
		for(String attr : restrictedList) {
//			restrictedIndices[i] = prob.getCovarianceIndex(restrictedList[i]);
			int index = prob.getCovarianceIndex(attr);
			if(index != -1 && !restrictedIndicesList.contains(index)) {
				restrictedIndicesList.add(index);
			}
			
		}
		double[][] cov = car.getMetadata().getCovariance();
		
		for(int i=0; i<restrictedIndicesList.size(); i++) {
			double[] covLine = cov[restrictedIndicesList.get(i)];
			double[] restCovLine = restCov[i];
			updateCovarianceLine(covLine, restCovLine, restrictedIndicesList);
		}
	}
	
	private void updateCovarianceLine(double[] covLine, double[] restCovLine, List<Integer> restrictedIndices) {
		for(int i=0; i<restrictedIndices.size(); i++) {
			covLine[restrictedIndices.get(i)] = restCovLine[i];
		}
	}
	
	public static double[][] makeIdentityMatrix(double[][] template) {
		double[][] identityMatrix = new double[template.length][template.length];
		for (int i = 0; i < template.length; i++) {
			for (int j = 0; j < template.length; j++) {
				if (i == j) {
					identityMatrix[i][j] = 1;
				} else if (i != j) {
					identityMatrix[i][j] = 0;
				}
			}
		}
		return identityMatrix;
	}

	@Override
	public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {
		
		// latency
		object.getMetadata().setObjectTrackingLatencyStart("Filter Cov Update");
		
		scannedTupleIndexPath = this.getScannedObjectListSIPath().toTupleIndexPath(object);
		predictedTupleIndexPath = this.getPredictedObjectListSIPath().toTupleIndexPath(object);
		
		// list of connections
		ArrayList<IConnection> tmpConList = object.getMetadata().getConnectionList();

		// traverse connection list and filter
		for (IConnection connected : tmpConList) {
			compute(connected, object);
		}
		
		// latency
		object.getMetadata().setObjectTrackingLatencyEnd("Filter Cov Update");
		
		return object;
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new FilterExpressionCovarianceUpdatePO<M>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.sendPunctuation(timestamp);
	}




}
