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

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.FileParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.spatial.sourcedescription.sdf.schema.SDFSpatialDatatype;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.builder.NestAggregateItem;
import de.uniol.inf.is.odysseus.trajectory.logicaloperator.builder.NestAggregateItemParameter;

/**
 * the logical operator for constructing trajectories from GPS sample points.
 * 
 * @author marcus
 *
 */
@LogicalOperator(name = "TRAJECTORYCONSTRUCT", minInputPorts = 1, maxInputPorts = 1, doc="Construct Trajectories", category={LogicalOperatorCategory.ADVANCED})
public class TrajectoryConstructAO extends UnaryLogicalOp {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8226843002104428660L;
		
	/** indicates whether subtrajectories shall be constructed */
	private boolean subtrajectories = false;
	
	/**
	 * the attribute where the vehicle Id is stored.
	 * Only sample points with same vehicle id may belong to
	 * a trajectory.
	 */
	private SDFAttribute vehicleId;
	
	/** the attribute where the vehicle Id is stored */
	private NestAggregateItem positionMapping;
	
	/**
	 * the path to the LevelDB where textual attributes for
	 * vehicles are stored.
	 */
	private File levelDBPath;
	
	
	public TrajectoryConstructAO() {
	}
	
	public TrajectoryConstructAO(final TrajectoryConstructAO trajectoryConstructAO) {
		super(trajectoryConstructAO);
		
		this.subtrajectories = trajectoryConstructAO.subtrajectories;
		this.vehicleId = trajectoryConstructAO.vehicleId;
		this.positionMapping = trajectoryConstructAO.positionMapping;
		this.levelDBPath = trajectoryConstructAO.levelDBPath;
	}
	
	
//#######################################
	// Own parameters
	
	@Parameter(name = "SUBTRAJECTORIES", type = BooleanParameter.class)
	public void setSubtrajectories(final boolean subtrajectories) {
		this.subtrajectories = subtrajectories;
	}
	
	@Parameter(name = "TRAJECTORYID", type = ResolvedSDFAttributeParameter.class)
	public void setTrajectoryId(final SDFAttribute trajectoryId) {
		this.vehicleId = trajectoryId;
	}
	
	@Parameter(name = "POSITIONMAP", type = NestAggregateItemParameter.class)
	public void setPositionMapping(final NestAggregateItem positionMapping) {
		this.positionMapping = positionMapping;
	}
	
	@Parameter(name = "LEVELDBPATH", type = FileParameter.class, optional = true)
	public void setLevelDBPath(final File levelDBPath) {
		this.levelDBPath = levelDBPath;
	}
	
	
	public boolean getSubtrajectories() {
		return this.subtrajectories;
	}
	
	public SDFAttribute getTrajectoryId() {
		return this.vehicleId;
	}
	
	public NestAggregateItem getPositionMapping() {
		return this.positionMapping;
	}
	
	public File getLevelDBPath() {
		return this.levelDBPath;
	}
	
	
	/**
	 * Output schema
	 */
	public final static SDFSchema OUTPUT_SCHEMA = SDFSchemaFactory.createNewTupleSchema(
			TrajectoryConstructAO.class.getName(), 
			new SDFAttribute(null, "VehicleId", SDFDatatype.STRING),
			new SDFAttribute(null, "TrajectoryId", SDFDatatype.INTEGER),
			new SDFAttribute(null, "Positions", SDFSpatialDatatype.LIST),
			new SDFAttribute(null, "Textual Attributes", SDFSpatialDatatype.LIST)
	);
	
	@Override
	protected SDFSchema getOutputSchemaIntern(final int pos) {
		return OUTPUT_SCHEMA;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new TrajectoryConstructAO(this);
	}
	
}
