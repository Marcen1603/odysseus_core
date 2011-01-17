package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.metadata.AbstractMetadataUpdater;
import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.util.CovarianceMapper;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * Initializes tuple (of type {@link MVRelationalTuple}) meta data used by
 * project group StreamCars.
 * <p>
 * See public methods for details.
 * 
 * @author Hauke
 * @author Sven
 */
public class StreamCarsMetaDataInitializer<M extends IProbability & IConnectionContainer & ITimeInterval & ILatency>
		extends AbstractMetadataUpdater<M, MVRelationalTuple<M>> {
	// set by constructor/initMetadata
	// used by updateMetadata
	private CovarianceExpressionMatrix covarianceExpressionMatrix;

	private SDFAttributeList schema;
	private SchemaIndexPath timeStampSchemaIndexPath;
	private String objectListPath = "";
	private SchemaHelper schemaHelper;

	private SchemaIndexPath objListSchemaIndexPath;

	/**
	 * Creates a StreamCarsMetaDataInitializer object and initializes the meta
	 * data used by {@link #updateMetaData(MVRelationalTuple)}.
	 * <p>
	 * NOTE: schema is used by {@link #updateMetaData(MVRelationalTuple)} as
	 * root to navigate through the tuple given. So it has to represent the
	 * schema of the tuples which shall be initialized by this object.
	 * <p>
	 * Probability Meta Data (type IProbability)
	 * <p>
	 * As basis for initializing the schema given is used. It is searched for
	 * measurement value attributes by invoking
	 * {@link SDFDatatypes#isMeasurementValue(de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype)}
	 * for each attribute or subattribute.
	 * <p>
	 * Finally, each row of the covariance matrix used by
	 * {@link #updateMetaData(MVRelationalTuple)} is set to the covariance list
	 * of each measurement value attribute found in the schema respectively (see
	 * {@link SDFAttribute#getCovariance()}).
	 * 
	 * @param schema
	 *            the schema, used to initialize meta data, NOTE: has to be the
	 *            schema of the tuples which shall be initialized by this
	 *            object!
	 */
	public StreamCarsMetaDataInitializer(SDFAttributeList schema) {
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
	 * {@link #StreamCarsMetaDataInitializer(SDFAttributeList)} for details.
	 * <p>
	 * NOTE: It is assumed that the schema used to initialize this object
	 * represents the schema of the tuple initialized by this method.
	 * 
	 * @param tuple
	 *            tuple of which meta data (M) should be initialized, NOTE: has
	 *            to match schema given to constructor!
	 */
	@Override
	public void updateMetadata(MVRelationalTuple<M> tuple) {
		this.initMetaDataOfTuple(tuple);
	}

	private void initMetaDataOfTuple(MVRelationalTuple<M> tupleGiven) {
		this.initProbabilityMetaDataOfTuple(tupleGiven);
		this.initAssoziationMetaData(tupleGiven);
		this.initTimeStampMetaData(tupleGiven);
		this.initLatencyData(tupleGiven);
		// invoke more tuple metadata initializers here
	}

	private void initTimeStampMetaData(MVRelationalTuple<M> tupleGiven) {
		PointInTime p = new PointInTime((Long) timeStampSchemaIndexPath
				.toTupleIndexPath(tupleGiven).getTupleObject());
		tupleGiven.getMetadata().setStart(p);
	}

	private void initLatencyData(MVRelationalTuple<M> tupleGiven) {
		tupleGiven.getMetadata().setLatencyStart(System.nanoTime());
	}

	private void initAssoziationMetaData(MVRelationalTuple<M> tupleGiven) {
		tupleGiven.getMetadata().setConnectionList(new ConnectionList());
	}

	private void initProbabilityMetaDataOfTuple(MVRelationalTuple<M> tupleGiven) {
		if (this.objListSchemaIndexPath == null) {
			this.objListSchemaIndexPath = this.schemaHelper
					.getSchemaIndexPath(this.objectListPath);
		}

		TupleIndexPath objectListTupleIndexPath = this.objListSchemaIndexPath
				.toTupleIndexPath(tupleGiven);
		if (objectListTupleIndexPath != null) {
			@SuppressWarnings("unchecked")
			MVRelationalTuple<M> objList = (MVRelationalTuple<M>) objectListTupleIndexPath
					.getTupleObject();

			this.covarianceExpressionMatrix.setRootTuple(tupleGiven);
			CovarianceMapper mapper = new CovarianceMapper(this.schema);
			for (int k = 0; k < objList.getAttributeCount(); k++) {
				MVRelationalTuple<M> object = objList.getAttribute(k);
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
