/**
 *
 */
package de.uniol.inf.is.odysseus.scars.objecttracking.filter.physicaloperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IObjectTrackingLatency;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IStreamCarsExpression;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IStreamCarsExpressionVariable;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsExpression;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TypeCaster;


/**
 * @author dtwumasi
 *
 */
public class FilterExpressionEstimateUpdatePO<M extends IGain & IProbability & IObjectTrackingLatency & IConnectionContainer> extends AbstractFilterExpressionPO<M> {

	private static final String GAIN = "GAIN";
	
	private TupleIndexPath scannedTupleIndexPath;
	private TupleIndexPath predictedTupleIndexPath;
	private List<IStreamCarsExpression> expressions;
	
	
	public FilterExpressionEstimateUpdatePO() {
		super();
	}

	public FilterExpressionEstimateUpdatePO(FilterExpressionEstimateUpdatePO<M> copy) {
		super(copy);
		this.scannedTupleIndexPath = copy.scannedTupleIndexPath.clone();
		this.predictedTupleIndexPath = copy.predictedTupleIndexPath.clone();
		this.expressions = new ArrayList<IStreamCarsExpression>(copy.getExpressions());
	}
	
	public void setExpressions(Map<String, String> expressionsMap) {
		this.expressions = new ArrayList<IStreamCarsExpression>(expressionsMap.size());
		for(Entry<String, String> exp : expressionsMap.entrySet()) {
			expressions.add(new StreamCarsExpression(exp.getKey(), exp.getValue()));
		}
	}
	
	public List<IStreamCarsExpression> getExpressions() {
		return this.expressions;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		
		super.process_open();
		for(IStreamCarsExpression exp : this.expressions) {
			exp.init(getOutputSchema());
		}

	}

	@Override
	public MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object) {
		// latency
		object.getMetadata().setObjectTrackingLatencyStart("Filter Est Update");
		
		
		scannedTupleIndexPath = this.getScannedObjectListSIPath().toTupleIndexPath(object);
		predictedTupleIndexPath = this.getPredictedObjectListSIPath().toTupleIndexPath(object);
		// list of connections
		ArrayList<IConnection> objConList = object.getMetadata().getConnectionList();

		// traverse connection list and filter
		for (IConnection connected : objConList) {
			compute(object, connected.getLeftPath(), connected.getRightPath());
		}
		
		// latency
		object.getMetadata().setObjectTrackingLatencyEnd("Filter Est Update");
		
		return object;
	}

	@SuppressWarnings("unchecked")
	private void compute(MVRelationalTuple<M> root, TupleIndexPath scannedObjectTupleIndex, TupleIndexPath predictedObjectTupleIndex) {
		for(IStreamCarsExpression expr : expressions) {
			for(IStreamCarsExpressionVariable variable : expr.getVariables()) {
				if(variable.isSchemaVariable() && !variable.hasMetadataInfo()) {
					if(variable.isInList(scannedTupleIndexPath)) {
						variable.replaceVaryingIndex(scannedObjectTupleIndex.getLastTupleIndex().toInt());
						variable.bindTupleValue(root);
					} else if(variable.isInList(predictedTupleIndexPath)) {
						variable.replaceVaryingIndex(predictedObjectTupleIndex.getLastTupleIndex().toInt());
						variable.bindTupleValue(root);
					}
				} else if(!variable.isSchemaVariable() && variable.getName().equals(GAIN)) {
					MVRelationalTuple<M> car = (MVRelationalTuple<M>)predictedObjectTupleIndex.getTupleObject();
					double gain = car.getMetadata().getRestrictedGain(variable.getName());
					variable.bind(gain);
				}
			}
			expr.evaluate();
			double value = expr.getTarget().getDoubleValue();
			setValue(expr.getTarget().getPath(), root, value);
		}
	}
	
	protected void setValue(int[] path, MVRelationalTuple<M> root, Object value) {
		MVRelationalTuple<?> currentTuple = root;
		for(int depth=0; depth<path.length-1; depth++) {
			currentTuple = ((MVRelationalTuple<?>) currentTuple).<MVRelationalTuple<?>>getAttribute(path[depth]);
		}
		Object oldValue = currentTuple.getAttribute(path[path.length - 1]);
		Object newValue = TypeCaster.cast(value, oldValue);
		currentTuple.setAttribute(path[path.length-1], newValue);
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new FilterExpressionEstimateUpdatePO<M>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.sendPunctuation(timestamp);
	}

}
