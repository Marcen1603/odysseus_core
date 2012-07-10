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
package de.uniol.inf.is.odysseus.scars.metadata;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.AbstractMetadataUpdater;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaHelper;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaIndexPath;
import de.uniol.inf.is.odysseus.objecttracking.MVTuple;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.IProbabilityConnectionContainerTimeIntervalLatency;
import de.uniol.inf.is.odysseus.scars.util.helper.CovarianceMapper;

/**
 * Initializes tuple (of type {@link MVRelationalTuple}) meta data used by
 * project group StreamCars.
 * <p>
 * See public methods for details.
 * 
 * @author Hauke
 * @author Sven
 */
public class StreamCarsMetaDataInitializer<M extends IProbabilityConnectionContainerTimeIntervalLatency>
		extends AbstractMetadataUpdater<M, MVTuple<M>> {
	// set by constructor/initMetadata
	// used by updateMetadata
	private CovarianceExpressionMatrix covarianceExpressionMatrix;

	private SDFSchema schema;
	private SchemaIndexPath timeStampSchemaIndexPath;
	private String objectListPath = "";
	private SchemaHelper schemaHelper;

	private SchemaIndexPath objListSchemaIndexPath;

	/**
	 * Creates a StreamCarsMetaDataInitializer object and initializes the meta
	 * data used by {@link #updateMetaData(MVTuple)}.
	 * <p>
	 * NOTE: schema is used by {@link #updateMetaData(MVTuple)} as
	 * root to navigate through the tuple given. So it has to represent the
	 * schema of the tuples which shall be initialized by this object.
	 * <p>
	 * Probability Meta Data (type IProbability)
	 * <p>
	 * As basis for initializing the schema given is used. It is searched for
	 * measurement value attributes by invoking
	 * {@link SDFDatatypes#isMeasurementValue(de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype)}
	 * for each attribute or subattribute.
	 * <p>
	 * Finally, each row of the covariance matrix used by
	 * {@link #updateMetaData(MVTuple)} is set to the covariance list
	 * of each measurement value attribute found in the schema respectively (see
	 * {@link SDFAttribute#getAddInfo()}).
	 * 
	 * @param schema
	 *            the schema, used to initialize meta data, NOTE: has to be the
	 *            schema of the tuples which shall be initialized by this
	 *            object!
	 */
	public StreamCarsMetaDataInitializer(SDFSchema schema) {
		this.schema = schema;

		this.schemaHelper = new SchemaHelper(schema);
		this.timeStampSchemaIndexPath = this.schemaHelper
				.getSchemaIndexPath(this.schemaHelper
						.getStartTimestampFullAttributeName());

		this.covarianceExpressionMatrix = new CovarianceExpressionMatrix(
				this.schema);
	}

	/**
	 * Initializes tuple (of type {@link MVRelationalTuple}) meta data used by
	 * project group StreamCars.
	 * <p>
	 * The meta data used to update tuple is being set by the constructor. See
	 * {@link #StreamCarsMetaDataInitializer(SDFSchema)} for details.
	 * <p>
	 * NOTE: It is assumed that the schema used to initialize this object
	 * represents the schema of the tuple initialized by this method.
	 * 
	 * @param tuple
	 *            tuple of which meta data (M) should be initialized, NOTE: has
	 *            to match schema given to constructor!
	 */
	@Override
	public void updateMetadata(MVTuple<M> tuple) {
		this.initMetaDataOfTuple(tuple);
	}

	private void initMetaDataOfTuple(MVTuple<M> tupleGiven) {
		this.initProbabilityMetaDataOfTuple(tupleGiven);
		this.initAssoziationMetaData(tupleGiven);
		this.initTimeStampMetaData(tupleGiven);
		this.initLatencyData(tupleGiven);
		// invoke more tuple metadata initializers here
	}

	private void initTimeStampMetaData(MVTuple<M> tupleGiven) {
		PointInTime p = new PointInTime((Long) TupleIndexPath.fromSchemaIndexPath(timeStampSchemaIndexPath, tupleGiven).getTupleObject());
		tupleGiven.getMetadata().setStart(p);
	}

	@SuppressWarnings("static-method")
    private void initLatencyData(MVTuple<M> tupleGiven) {
		tupleGiven.getMetadata().setLatencyStart(System.nanoTime());
	}

	@SuppressWarnings("static-method")
    private void initAssoziationMetaData(MVTuple<M> tupleGiven) {
		tupleGiven.getMetadata().setConnectionList(new ConnectionList());
	}

	private void initProbabilityMetaDataOfTuple(MVTuple<M> tupleGiven) {
		if (this.objListSchemaIndexPath == null) {
			this.objListSchemaIndexPath = this.schemaHelper
					.getSchemaIndexPath(this.objectListPath);
		}

		TupleIndexPath objectListTupleIndexPath = TupleIndexPath.fromSchemaIndexPath(this.objListSchemaIndexPath, tupleGiven);
		if (objectListTupleIndexPath != null) {
			@SuppressWarnings("unchecked")
			List<Object> objList = (List<Object>) objectListTupleIndexPath
					.getTupleObject();

			this.covarianceExpressionMatrix.setRootTuple(tupleGiven);
			CovarianceMapper mapper = new CovarianceMapper(this.schema);
			for (int k = 0; k < objList.size(); k++) {
				@SuppressWarnings("unchecked")
				MVTuple<M> object = (MVTuple<M>)objList.get(k);
				object.getMetadata().setCovariance(
						this.covarianceExpressionMatrix.calculateMatrix(k));
				object.getMetadata().setAttributeMapping(mapper.getMapping());
			}
		}
	}

	public String getObjectListPath() {
		return this.objectListPath;
	}

	public void setObjectListPath(String objectListPath) {
		this.objectListPath = objectListPath;
	}
}
