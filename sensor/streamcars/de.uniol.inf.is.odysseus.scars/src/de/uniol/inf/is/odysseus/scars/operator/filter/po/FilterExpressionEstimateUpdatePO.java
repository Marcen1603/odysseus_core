/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
/**
 *
 */
package de.uniol.inf.is.odysseus.scars.operator.filter.po;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.objecttracking.MVTuple;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.metadata.IConnection;
import de.uniol.inf.is.odysseus.scars.metadata.IStreamCarsExpression;
import de.uniol.inf.is.odysseus.scars.metadata.IStreamCarsExpressionVariable;
import de.uniol.inf.is.odysseus.scars.metadata.StreamCarsExpression;
import de.uniol.inf.is.odysseus.scars.util.helper.TypeCaster;


/**
 * @author dtwumasi
 *
 */
public class FilterExpressionEstimateUpdatePO<M extends IGainIProbabilityIObjectTrackingLatencyIConnectionContainer> extends AbstractFilterExpressionPO<M> {

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
			String expressionString = exp.getValue();
			expressionString = expressionString.replace("'", "");
			expressions.add(new StreamCarsExpression(exp.getKey(), expressionString));
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
	public MVTuple<M> computeAll(MVTuple<M> object) {
		// latency
		object.getMetadata().setObjectTrackingLatencyStart("Filter Est Update");
		
		
		scannedTupleIndexPath = TupleIndexPath.fromSchemaIndexPath(this.getScannedObjectListSIPath(), object);
		predictedTupleIndexPath = TupleIndexPath.fromSchemaIndexPath(this.getPredictedObjectListSIPath(), object);
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
	private void compute(MVTuple<M> root, TupleIndexPath scannedObjectTupleIndex, TupleIndexPath predictedObjectTupleIndex) {
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
				} else if(variable.hasMetadataInfo() && variable.getMetadataInfo().equals(GAIN)) {
					MVTuple<M> car = (MVTuple<M>)predictedObjectTupleIndex.getTupleObject();
					double gain = car.getMetadata().getRestrictedGain(variable.getName());
					variable.bind(gain);
				}
			}
			expr.evaluate();
			double value = expr.getTarget().getDoubleValue();
			setValue(expr.getTarget().getPath(), root, value);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void setValue(int[] path, MVTuple<M> root, Object value) {
		Object currentTuple = root;
		for(int depth=0; depth<path.length-1; depth++) {
			if( currentTuple instanceof MVTuple) {
				currentTuple = ((MVTuple<?>) currentTuple).getAttribute(path[depth]);
			} else if( currentTuple instanceof List) {
				currentTuple = ((List<?>) currentTuple).get(path[depth]);
			}
		}
		Object oldValue = null;
		if( currentTuple instanceof MVTuple ) {
			oldValue = ((MVTuple<?>)currentTuple).getAttribute(path[path.length - 1]);
			Object newValue = TypeCaster.cast(value, oldValue);
			((MVTuple<?>)currentTuple).setAttribute(path[path.length-1], newValue);

		} else if( currentTuple instanceof List ) {
			oldValue = ((List<?>)currentTuple).get(path[path.length - 1]);
			Object newValue = TypeCaster.cast(value, oldValue);
			((List<Object>)currentTuple).set(path[path.length-1], newValue);
		}
			
	}

	@Override
	public AbstractPipe<MVTuple<M>, MVTuple<M>> clone() {
		return new FilterExpressionEstimateUpdatePO<M>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.sendPunctuation(timestamp);
	}

}
