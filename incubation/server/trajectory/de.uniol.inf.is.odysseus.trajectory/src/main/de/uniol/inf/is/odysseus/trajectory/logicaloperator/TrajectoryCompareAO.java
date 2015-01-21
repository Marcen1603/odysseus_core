package de.uniol.inf.is.odysseus.trajectory.logicaloperator;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;
import de.uniol.inf.is.odysseus.trajectory.SDFTrajectoryDataType;

@LogicalOperator(name = "TRAJECTORYCOMPARE", minInputPorts = 1, maxInputPorts = 1, doc="Compare Trajectories", category={LogicalOperatorCategory.ADVANCED})
public class TrajectoryCompareAO extends UnaryLogicalOp {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2522194934279154977L;
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(TrajectoryCompareAO.class);

	/** the number of the k nearest routes to find */
	private int k;
	
	/** the algorithm to use */
	private String algorithm;
	
	// the query route
	private List<String> queryTrajectory;
	
	private String referenceSystem;
	
	/**
	 * Output schema
	 */
	private final SDFSchema outputSchema = new SDFSchema(
			TrajectoryCompareAO.class.getName(), 
			Tuple.class, 
			new SDFAttribute(null, "QTId", SDFDatatype.STRING, null),
			new SDFAttribute(null, "k", SDFDatatype.INTEGER, null),
			new SDFAttribute(null, "Total", SDFDatatype.INTEGER, null),
			new SDFAttribute(null, "Contains", SDFSpatialDatatype.INTEGER , null),
			new SDFAttribute(null, "tet", SDFTrajectoryDataType.OBJECT, new SDFSchema("", Tuple.class, 
					new SDFAttribute(null, "id", SDFDatatype.STRING, null),
					new SDFAttribute(null, "points", SDFDatatype.STRING, null)))
	);
	
	public TrajectoryCompareAO() {
		this(1, "OWD");
	}
	
	// hier nur optionale Parameter im Konstruktor
	public TrajectoryCompareAO(final int k, final String algorithm) {
		this.k = k;
		this.algorithm = algorithm;
	}
	
	public TrajectoryCompareAO(TrajectoryCompareAO trajectoryCompareAO) {
		super(trajectoryCompareAO);
		this.k = trajectoryCompareAO.k;
		this.algorithm = trajectoryCompareAO.algorithm;
		this.queryTrajectory = trajectoryCompareAO.queryTrajectory;
		this.referenceSystem = trajectoryCompareAO.referenceSystem;
	}

	@Parameter(type = IntegerParameter.class, name = "K", optional=true)
	public void setK(int k) {
		this.k = k;
	}

	public int getK() {
		return k;
	}
	
	@Parameter(type = StringParameter.class, name = "ALGORITHM", optional=false)
	public void setAlgorithm(final String algorithm) {
		this.algorithm = algorithm;
	}
	
	public String getAlgorithm() {
		return this.algorithm;
	}
	
	@Parameter(type = StringParameter.class, name = "REFERENCESYSTEM", optional=true)
	public void setReferenceSystem(final String referenceSystem) {
		this.referenceSystem = referenceSystem;
	}
	
	public String getReferenceSystem() {
		return this.referenceSystem;
	}
	
	@Parameter(type = StringParameter.class, name = "QUERYTRAJECTORY", isList = true, optional = false)
	public void setQueryTrajectory(final List<String> queryTrajectory) {
		this.queryTrajectory = queryTrajectory;
	}
	
	public List<String> getQueryTrajectory() {
		return this.queryTrajectory;
	}
	
	
	
	@Override
	public SDFSchema getOutputSchemaIntern(int port) {
		return this.outputSchema;
	}
	
	
	@Override
	public AbstractLogicalOperator clone() {
		return new TrajectoryCompareAO(this);
	}
}
