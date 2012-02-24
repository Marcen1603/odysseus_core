/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.scars.operator.filter.po;

import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaHelper;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaIndexPath;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.metadata.IStreamCarsExpression;
import de.uniol.inf.is.odysseus.scars.metadata.StreamCarsExpression;

public abstract class AbstractFilterExpressionPO<M extends IGainIProbabilityIObjectTrackingLatencyIConnectionContainer> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private String predictedObjectListPath;
	private String scannedObjectListPath;
	
	private SchemaIndexPath predictedObjectListSIPath;
	private SchemaIndexPath scannedObjectListSIPath;
	
	private SDFSchema inputSchema;
	
	private SchemaHelper schemaHelper;
	
	private String expressionString;
	protected IStreamCarsExpression expression;
	
	public void setExpression(IStreamCarsExpression expression) {
		this.expression = expression;
	}

	public AbstractFilterExpressionPO() {
		super();
	}

	public AbstractFilterExpressionPO(AbstractFilterExpressionPO<M> copy) {
		super(copy);
		this.setExpressionString(copy.getExpressionString());
		this.setPredictedObjectListPath(copy.getPredictedObjectListPath());
		this.setPredictedObjectListSIPath(copy.getPredictedObjectListSIPath().clone());
		this.setScannedObjectListPath(copy.getScannedObjectListPath());
		this.setScannedObjectListSIPath(copy.getScannedObjectListSIPath().clone());
		// TODO clone?
		this.setExpression(copy.getExpression());
		this.setInputSchema(copy.getInputSchema().clone());
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		if(expressionString != null) {
			this.expressionString = this.expressionString.replace("'", "");
			expression = new StreamCarsExpression(expressionString);
			expression.init(getOutputSchema());
		}

		
		this.schemaHelper = new SchemaHelper(getOutputSchema());

		this.setScannedObjectListSIPath(this.schemaHelper.getSchemaIndexPath(this.getScannedObjectListPath()));
		this.setPredictedObjectListSIPath(this.schemaHelper.getSchemaIndexPath(this.getPredictedObjectListPath()));
		
		this.setInputSchema(this.getSubscribedToSource(0).getTarget().getOutputSchema());
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		object.getMetadata().setObjectTrackingLatencyStart();
		object = computeAll(object);
		// transfer to broker
		object.getMetadata().setObjectTrackingLatencyEnd();
		transfer(object);
	}

	public abstract MVRelationalTuple<M> computeAll(MVRelationalTuple<M> object);

	@Override
	public abstract AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone();

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	// Getter & Setter
	
	public IStreamCarsExpression getExpression() {
		return expression;
	}
	
	public void setExpressionString(String expressionString) {
		this.expressionString = expressionString;
	}
	
	public String getExpressionString() {
		return expressionString;
	}

	public void setPredictedObjectListPath(String predictedObjectListPath) {
		this.predictedObjectListPath = predictedObjectListPath;
	}

	public String getPredictedObjectListPath() {
		return predictedObjectListPath;
	}

	public void setScannedObjectListPath(String scannedObjectListPath) {
		this.scannedObjectListPath = scannedObjectListPath;
	}

	public String getScannedObjectListPath() {
		return scannedObjectListPath;
	}

	public void setPredictedObjectListSIPath(SchemaIndexPath predictedObjectListSIPath) {
		this.predictedObjectListSIPath = predictedObjectListSIPath;
	}

	public SchemaIndexPath getPredictedObjectListSIPath() {
		return predictedObjectListSIPath;
	}

	public void setScannedObjectListSIPath(SchemaIndexPath scannedObjectListSIPath) {
		this.scannedObjectListSIPath = scannedObjectListSIPath;
	}

	public SchemaIndexPath getScannedObjectListSIPath() {
		return scannedObjectListSIPath;
	}

	/**
	 * @param inputSchema the inputSchema to set
	 */
	public void setInputSchema(SDFSchema inputSchema) {
		this.inputSchema = inputSchema;
	}

	/**
	 * @return the inputSchema
	 */
	public SDFSchema getInputSchema() {
		return inputSchema;
	}
}
