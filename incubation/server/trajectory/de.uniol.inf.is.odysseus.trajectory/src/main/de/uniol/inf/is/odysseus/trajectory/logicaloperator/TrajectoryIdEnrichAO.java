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

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * The logical operator for enriching trajectories with Ids.
 * 
 * @author marcus
 *
 */
@LogicalOperator(name = "PLEASEDONTUSEMEBECAUSEIAMANOPERATORONLYFORACERTAINUSECASEYESYOUARERIGHTANDBECAUSEOFTHISIHAVETHISNAMENOTTOCNAMECLASHWITHOTHEROPERATORSYEAHMAYBEIAMTHEOPERATORWITHTHELONGESTINNAMEINWHOLEFUCKINGODYSSEUS", minInputPorts = 1, maxInputPorts = 1, doc="Enrich Trajectory with Ids", category={LogicalOperatorCategory.ADVANCED}, deprecation=true)
public class TrajectoryIdEnrichAO extends UnaryLogicalOp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6559476897670445930L;

	public TrajectoryIdEnrichAO() {	}
	
	public TrajectoryIdEnrichAO(final TrajectoryIdEnrichAO trajectoryIdEnricherAO) {	
		super(trajectoryIdEnricherAO);
	}
	
	@Override
	public SDFSchema getOutputSchemaIntern(final int port) {
		return TrajectoryConstructAO.OUTPUT_SCHEMA;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new TrajectoryIdEnrichAO(this);
	}
}
