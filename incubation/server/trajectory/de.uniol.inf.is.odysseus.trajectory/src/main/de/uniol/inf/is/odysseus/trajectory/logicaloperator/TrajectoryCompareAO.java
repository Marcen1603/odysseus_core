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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;

@LogicalOperator(name = "TRAJECTORYCOMPARE", minInputPorts = 1, maxInputPorts = 1, doc="Compare Trajectories", category={LogicalOperatorCategory.ADVANCED})
public class TrajectoryCompareAO extends UnaryLogicalOp {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2522194934279154977L;
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(TrajectoryCompareAO.class);

	// die Anzahl der k-ähnlichsten Routen
	private int k;
	
	private boolean start;
	
	private boolean end;
	
	// the query route
	private List<String> queryTrajectory;
	
	private String referenceSystem;
	
	/**
	 * Output schema
	 */
	private final SDFSchema outputSchema = new SDFSchema(
			"Dummy", 
			Tuple.class, 
			new SDFAttribute(null, "k", SDFDatatype.INTEGER, null),
			new SDFAttribute(null, "VehicleId", SDFDatatype.STRING, null),
			new SDFAttribute(null, "Distance", SDFDatatype.DOUBLE, null),
			new SDFAttribute(null, "Points", SDFSpatialDatatype.LIST , null),
			new SDFAttribute(null, "State", SDFDatatype.INTEGER, null)
	);
	
	public TrajectoryCompareAO() {
		this(1);
	}
	
	// hier nur optionale Parameter im Konstruktor
	public TrajectoryCompareAO(int k) {
		this.k = k;
	}
	
	public TrajectoryCompareAO(TrajectoryCompareAO trajectoryCompareAO) {
		super(trajectoryCompareAO);
		this.k = trajectoryCompareAO.k;
		this.queryTrajectory = trajectoryCompareAO.queryTrajectory;
		this.referenceSystem = trajectoryCompareAO.referenceSystem;
	}
	
    
	@Override
	public SDFSchema getOutputSchemaIntern(int port) {
		return this.outputSchema;
	}
	
	
	@Override
	public AbstractLogicalOperator clone() {
		return new TrajectoryCompareAO(this);
	}

	@Parameter(type = IntegerParameter.class, name = "K", optional=true)
	public void setK(int k) {
		this.k = k;
	}

	public int getK() {
		return k;
	}
	
	@Parameter(type = StringParameter.class, name = "REFERENCESYSTEM", optional=false)
	public void setReferenceSystem(String referenceSystem) {
		this.referenceSystem = referenceSystem;
	}
	
	public String getReferenceSystem() {
		return this.referenceSystem;
	}
	
	@Parameter(type = StringParameter.class, name = "QUERYTRAJECTORY", isList = true, optional = false)
	public void setQueryTrajectory(List<String> queryTrajectory) {
		this.queryTrajectory = queryTrajectory;
	}
	
	public List<String> getQueryTrajectory() {
		return this.queryTrajectory;
	}
	
	@Parameter(type = BooleanParameter.class, name = "START", optional = true)
	public void setStart(boolean start) {
		this.start = start;
	}
	
	public boolean getStart() {
		return this.start;
	}

	@Parameter(type = BooleanParameter.class, name = "END", optional = true)
	public void setEnd(boolean end) {
		this.end = end;
	}
	
	public boolean getEnd() {
		return end;
	}
}
