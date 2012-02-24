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
package de.uniol.inf.is.odysseus.scars.operator.association.po;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaHelper;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaIndexPath;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleHelper;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.IProbabilityConnectionContainerObjectTrackingLatency;
import de.uniol.inf.is.odysseus.scars.metadata.CovarianceExpressionHelper;
import de.uniol.inf.is.odysseus.scars.metadata.CovarianceHelper;
import de.uniol.inf.is.odysseus.scars.metadata.IConnection;
import de.uniol.inf.is.odysseus.scars.metadata.IStreamCarsExpressionVariable;
import de.uniol.inf.is.odysseus.scars.metadata.StreamCarsExpression;
import de.uniol.inf.is.odysseus.scars.metadata.StreamCarsExpressionVariable;

/**
 * <p>
 * Physical operator for the <i>expression based</i> rating of <strong>Connections</strong>
 * ({@link de.uniol.inf.is.odysseus.scars.metadata.Connection}). The expressions
 * is set within the query. Only existing connections will be considered. To rate each
 * possible pair of detected and predicted objects, HypothesisExpressionGatingPO
 * ({@link HypothesisExpressionGatingPO}) should be used.
 * </p>
 * 
 * @author Volker Janz
 */

public class HypothesisExpressionEvaluationPO<M extends IProbabilityConnectionContainerObjectTrackingLatency> extends
		AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private StreamCarsExpression expression;
	private String expressionString;
	private SchemaHelper schemaHelper;
	private String predictedObjectListPath;
	private String scannedObjectListPath;
	private SchemaIndexPath predictedObjectListSIPath;
	private SchemaIndexPath scannedObjectListSIPath;

	private CovarianceHelper covarianceHelper;
	private CovarianceExpressionHelper covHelper;
	
	private static final String EXP_ASSOCIATION_CONNECTION_VALUE = "ASSOCIATION_CON_VAL";
	private static final String METADATA_COV = "COVARIANCE";

	public HypothesisExpressionEvaluationPO() {
		super();
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new HypothesisExpressionEvaluationPO<M>(this);
	}

	public HypothesisExpressionEvaluationPO(HypothesisExpressionEvaluationPO<M> clone) {
		super(clone);
		this.expression = clone.expression;
		this.expressionString = clone.expressionString;
		this.schemaHelper = clone.schemaHelper;
		this.predictedObjectListPath = clone.predictedObjectListPath;
		this.scannedObjectListPath = clone.scannedObjectListPath;
		this.predictedObjectListSIPath = clone.predictedObjectListSIPath;
		this.scannedObjectListSIPath = clone.scannedObjectListSIPath;
		this.covarianceHelper = clone.covarianceHelper;
		this.covHelper = clone.covHelper;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		this.expressionString = this.expressionString.replace("'", "");
		this.expression = new StreamCarsExpression(this.expressionString);
		this.expression.init(this.getOutputSchema());
		this.schemaHelper = new SchemaHelper(getOutputSchema());

		this.setScannedObjectListSIPath(this.schemaHelper.getSchemaIndexPath(this.scannedObjectListPath));
		this.setPredictedObjectListSIPath(this.schemaHelper.getSchemaIndexPath(this.predictedObjectListPath));

		this.covarianceHelper = new CovarianceHelper(this.expression, this.getOutputSchema());
		covHelper = new CovarianceExpressionHelper();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		TupleIndexPath scannedTupleIndexPath = TupleIndexPath.fromSchemaIndexPath(this.getScannedObjectListSIPath(), object);
		TupleIndexPath predictedTupleIndexPath = TupleIndexPath.fromSchemaIndexPath(this.getPredictedObjectListSIPath(), object);

		TupleHelper tupleHelper = new TupleHelper(object);

		for (IConnection con : object.getMetadata().getConnectionList()) {

			double currentRating = con.getRating();

			for (IStreamCarsExpressionVariable ivar : expression.getVariables()) {
				StreamCarsExpressionVariable var = (StreamCarsExpressionVariable) ivar;

				if (!var.isSchemaVariable() && var.getName().equals(EXP_ASSOCIATION_CONNECTION_VALUE)) { // "Custom" Variable
					if (currentRating != -1) {
						var.bind(currentRating);
					} else {
						var.bind(0);
					}
				} else if (var.isSchemaVariable() && !var.hasMetadataInfo()) { // "Normale" Variable
					if (var.isInList(scannedTupleIndexPath)) {
						var.replaceVaryingIndex(con.getLeftPath().getLastTupleIndex().toInt());
						var.bindTupleValue(object);
					} else if (var.isInList(predictedTupleIndexPath)) {
						var.replaceVaryingIndex(con.getRightPath().getLastTupleIndex().toInt());
						var.bindTupleValue(object);
					}
				} else if (var.hasMetadataInfo()) { // Metadaten Variable
					if (var.isInList(scannedTupleIndexPath.toArray())) {
						var.replaceVaryingIndex(con.getLeftPath().getLastTupleIndex().toInt());
						Object val = tupleHelper.getObject(var.getPath());
						String metaDataInfo = var.getMetadataInfo();
						if (metaDataInfo.equals(METADATA_COV)) {
							var.bind(covHelper.getCovarianceForExpressionVariables(this.expression, ((MVRelationalTuple<M>) val).getMetadata()));
//							var.bind(this.covarianceHelper.getCovarianceForAttributes(((MVRelationalTuple<M>) val).getMetadata().getCovariance()));
						}
					} else if (var.isInList(predictedTupleIndexPath.toArray())) {
						var.replaceVaryingIndex(con.getRightPath().getLastTupleIndex().toInt());
						Object val = tupleHelper.getObject(var.getPath());
						String metaDataInfo = var.getMetadataInfo();
						if (metaDataInfo.equals(METADATA_COV)) {
							var.bind(covHelper.getCovarianceForExpressionVariables(this.expression, ((MVRelationalTuple<M>) val).getMetadata()));
//							var.bind(this.covarianceHelper.getCovarianceForAttributes(((MVRelationalTuple<M>) val).getMetadata().getCovariance()));
						}
					}
				}
			}
			expression.evaluate();

			con.setRating(expression.getDoubleValue());
		}

		transfer(object);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.sendPunctuation(timestamp);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	/* SETTER AND GETTER */

	public StreamCarsExpression getExpression() {
		return expression;
	}

	public void setExpression(StreamCarsExpression expression) {
		this.expression = expression;
	}

	public String getExpressionString() {
		return expressionString;
	}

	public void setExpressionString(String expressionString) {
		this.expressionString = expressionString;
	}

	public SchemaHelper getSchemaHelper() {
		return schemaHelper;
	}

	public void setSchemaHelper(SchemaHelper schemaHelper) {
		this.schemaHelper = schemaHelper;
	}

	public String getPredictedObjectListPath() {
		return predictedObjectListPath;
	}

	public void setPredictedObjectListPath(String predictedObjectListPath) {
		this.predictedObjectListPath = predictedObjectListPath;
	}

	public String getScannedObjectListPath() {
		return scannedObjectListPath;
	}

	public void setScannedObjectListPath(String scannedObjectListPath) {
		this.scannedObjectListPath = scannedObjectListPath;
	}

	public SchemaIndexPath getPredictedObjectListSIPath() {
		return predictedObjectListSIPath;
	}

	public void setPredictedObjectListSIPath(SchemaIndexPath predictedObjectListSIPath) {
		this.predictedObjectListSIPath = predictedObjectListSIPath;
	}

	public SchemaIndexPath getScannedObjectListSIPath() {
		return scannedObjectListSIPath;
	}

	public void setScannedObjectListSIPath(SchemaIndexPath scannedObjectListSIPath) {
		this.scannedObjectListSIPath = scannedObjectListSIPath;
	}
}
