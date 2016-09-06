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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractEnrichAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.FileParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;

/**
 * The logical operator for enriching stream elements with information from a <i>LevelDB</i>.
 * 
 * @author marcus
 *
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "LEVELDBENRICH", doc="Enrich stream objects with information from a LevelDB.", category={LogicalOperatorCategory.ENRICH, LogicalOperatorCategory.DATABASE})
public class LevelDBEnrichAO extends AbstractEnrichAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3545605159481674813L;
	
	/** the file path to the <i>LevelDB</i> */
	private File levelDBPath;
	
	/** the key attribute */
	private SDFAttribute in;
	
	/** the attribute to store the data */
	private SDFAttribute out;
	
	public LevelDBEnrichAO() { }
	
	
	public LevelDBEnrichAO(final LevelDBEnrichAO levelDBEnrichAO) {
		super(levelDBEnrichAO);
		
		this.levelDBPath = levelDBEnrichAO.levelDBPath;
		this.in = levelDBEnrichAO.in;
		this.out = levelDBEnrichAO.out;
	}
	
	@Parameter(name = "LEVELDBPATH", type = FileParameter.class)
	public void setLevelDBPath(final File levelDBPath) {
		this.levelDBPath = levelDBPath;
	}

	@Parameter(name = "IN", type = ResolvedSDFAttributeParameter.class)
	public void setIn(final SDFAttribute in) {
		this.in = in;
	}
	
	@Parameter(name = "OUT", type = ResolvedSDFAttributeParameter.class)
	public void setOut(final SDFAttribute out) {
		this.out = out;
	}
	
	
	public File getLevelDBPath() {
		return this.levelDBPath;
	}
	
	public SDFAttribute getIn() {
		return this.in;
	}
	
	public SDFAttribute getOut() {
		return this.out;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new LevelDBEnrichAO(this);
	}

}
