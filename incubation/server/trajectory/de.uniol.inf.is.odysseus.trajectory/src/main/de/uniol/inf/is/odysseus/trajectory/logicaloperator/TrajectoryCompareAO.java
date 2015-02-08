package de.uniol.inf.is.odysseus.trajectory.logicaloperator;


import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.FileParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.Option;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
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
	private File queryTrajectory;
	
	/** the utm zone */
	private int utmZone;

	
	private final Map<String, String> optionsMap = new HashMap<>();
	
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
		this.utmZone = trajectoryCompareAO.utmZone;
	}

	@Parameter(type = IntegerParameter.class, name = "K")
	public void setK(int k) {
		this.k = k;
	}
	
	@Parameter(type = StringParameter.class, name = "ALGORITHM")
	public void setAlgorithm(final String algorithm) {
		this.algorithm = algorithm;
	}
	
	@Parameter(type = IntegerParameter.class, name = "UTMZONE")
	public void setUtmZone(final int utmZone) {
		this.utmZone = utmZone;
	}
	
	@Parameter(type = FileParameter.class, name = "QUERYTRAJECTORY")
	public void setQueryTrajectory(final File queryTrajectory) {
		this.queryTrajectory = queryTrajectory;
	}
	
	@Parameter(type = OptionParameter.class, name = "OPTIONS", isList = true, doc = "Special options for algorithm.")
	public void setOptions(List<Option> value) {
		for(final Option option : value) {
			this.optionsMap.put(option.getName().toLowerCase(), option.getValue());
		}
	}
	
	public int getK() {
		return this.k;
	}
	
	public String getAlgorithm() {
		return this.algorithm;
	}
	
	public File getQueryTrajectory() {
		return this.queryTrajectory;
	}

	public int getUtmZone() {
		return this.utmZone;
	}
	
	public Map<String, String> getOptionsMap() {
		return this.optionsMap;
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
