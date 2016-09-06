/*
 * Copyright 2015 Marcus Behrendt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
**/

package de.uniol.inf.is.odysseus.trajectory.logicaloperator;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.FileParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * The logical operator for comparing trajectories
 * @author marcus
 *
 */
@LogicalOperator(name = "TRAJECTORYCOMPARE", minInputPorts = 1, maxInputPorts = 1, doc="Compare Trajectories", category={LogicalOperatorCategory.ADVANCED})
public class TrajectoryCompareAO extends UnaryLogicalOp {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2522194934279154977L;
	
	/** Logger for debugging purposes */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(TrajectoryCompareAO.class);

	
	public static final int VEHICLE_ID_POS = 0;
	
	public static final int POINTS_POS = 2;
	
	/** the number of the k nearest routes to find */
	private int k;
	
	/** the algorithm to use */
	private String algorithm;
	
	/** the query route */
	private File queryTrajectory;
	
	/** map for textual Attributes */
	private Map<String, String> textualAttr;
	
	/** importance of spatial difference to textual difference */
	private double lambda = 0.5;
	
	/** the utm zone */
	private int utmZone;

	/** options for the spatial compare algorithm */
	private Map<String, String> optionsMap = new HashMap<>();

	
	public TrajectoryCompareAO() {
	}
	
	public TrajectoryCompareAO(final TrajectoryCompareAO trajectoryCompareAO) {
		super(trajectoryCompareAO);
		this.k = trajectoryCompareAO.k;
		this.algorithm = trajectoryCompareAO.algorithm;
		this.queryTrajectory = trajectoryCompareAO.queryTrajectory;
		this.utmZone = trajectoryCompareAO.utmZone;
		this.textualAttr = trajectoryCompareAO.textualAttr;
		this.lambda = trajectoryCompareAO.lambda;
		this.optionsMap = trajectoryCompareAO.optionsMap;
	}

	@Parameter(type = IntegerParameter.class, name = "K", doc = "The k-nearest trajectory to be returned.")
	public void setK(final int k) {
		this.k = k;
	}
	
	@Parameter(type = StringParameter.class, name = "ALGORITHM", doc = "The algorithm for measuring spatial distance.")
	public void setAlgorithm(final String algorithm) {
		this.algorithm = algorithm;
	}
	
	@Parameter(type = IntegerParameter.class, name = "UTMZONE")
	public void setUtmZone(final int utmZone) {
		this.utmZone = utmZone;
	}
	
	@Parameter(type = FileParameter.class, name = "QUERYTRAJECTORY", doc = "Filepath to query trajectory.")
	public void setQueryTrajectory(final File queryTrajectory) {
		this.queryTrajectory = queryTrajectory;
	}
	
	@Parameter(type = StringParameter.class, keytype = StringParameter.class , name = "TEXTUALATTR", isMap = true, optional = true, doc = "Textual attributes of the query trajectory.")
	public void setTextualAttr(final Map<String, String> textualAttr) {
		this.textualAttr = textualAttr;
	}
	
	@Parameter(type = DoubleParameter.class, name = "LAMBDA", optional = true, doc = "Importance of spatial difference to textual difference.")
	public void setLambda(final double lambda) {
		if(lambda > 1.0 || lambda < 0.0) {
			throw new IllegalArgumentException("lambda has to be in range [0,1]");
		}
		this.lambda = lambda;
	}
	
	@Parameter(type = OptionParameter.class, name = "OPTIONS", isList = true, doc = "Special options for choosen algorithm.")
	public void setOptions(final List<Option> value) {
		for(final Option option : value) {
			this.optionsMap.put(option.getName().toLowerCase(Locale.US), option.getValue());
		}
	}
	
	public int getK() {
		return this.k;
	}
	
	public String getAlgorithm() {
		return this.algorithm;
	}
	
	public int getUtmZone() {
		return this.utmZone;
	}
	
	public File getQueryTrajectory() {
		return this.queryTrajectory;
	}
	
	public Map<String, String> getTextualAttr() {
		return this.textualAttr;
	}
	
	public double getLambda() {
		return this.lambda;
	}
	
	public Map<String, String> getOptionsMap() {
		return this.optionsMap;
	}
	
	/**
	 * Output schema
	 */
	private final static SDFSchema OUTPUT_SCHEMA = SDFSchemaFactory.createNewTupleSchema(
			TrajectoryCompareAO.class.getName(), 
			new SDFAttribute(null, "result", SDFDatatype.LIST)
	);
	
	@Override
	public SDFSchema getOutputSchemaIntern(final int port) {
		return OUTPUT_SCHEMA;
	}
	
	
	@Override
	public AbstractLogicalOperator clone() {
		return new TrajectoryCompareAO(this);
	} 
}
