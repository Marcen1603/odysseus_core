/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
 ******************************************************************************/
/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.database.transform;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.database.logicaloperator.DatabaseSourceAO;
import de.uniol.inf.is.odysseus.database.physicaloperator.DatabaseSourcePO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Dennis Geesen Created at: 22.08.2011
 * @author Marco Grawunder (new params)
 * @author Tobias Brandt (metadata)
 */
public class TDatabaseSourceAORule extends AbstractTransformationRule<DatabaseSourceAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(DatabaseSourceAO sourceAO, TransformationConfiguration config) throws RuleException {
		@SuppressWarnings("rawtypes")
		DatabaseSourcePO databaseSourcePO = new DatabaseSourcePO(sourceAO.getTableName(), sourceAO.getConnection(),
				sourceAO.getWaitMillis(), sourceAO.isEscapeNames(), sourceAO.isUseDatatypeMappings());
		processMetaData(sourceAO, databaseSourcePO, config);
		defaultExecute(sourceAO, databaseSourcePO, config, true, true);
	}

	/**
	 * Adds the meta data to the input. Adds a timestampAO to set the system
	 * timestamp as start
	 * 
	 * @param databaseSourceAO
	 * @param databaseSourcePO
	 * @param config
	 */
	@SuppressWarnings("rawtypes")
	private void processMetaData(DatabaseSourceAO databaseSourceAO, DatabaseSourcePO databaseSourcePO,
			TransformationConfiguration config) {
		// Set the type we get from the query
		IMetaAttribute type = MetadataRegistry.getMetadataType(config.getDefaultMetaTypeSet());
		((IMetadataInitializer<?, ?>) databaseSourcePO).setMetadataType(type);

		// Add a timestamp operator (if we would not set this, both start and
		// end timestamp would be set to infinity)
		TimestampAO tsAO = getTimestampAOAsFather(databaseSourceAO);
		Class<? extends IMetaAttribute> toC = ITimeInterval.class;

		if (MetadataRegistry.contains(type.getClasses(), toC) && tsAO == null) {
			tsAO = insertTimestampAO(databaseSourceAO);
		}
	}

	@Override
	public boolean isExecutable(DatabaseSourceAO operator, TransformationConfiguration config) {
		return true;
	}

	@Override
	public String getName() {
		return "DatabaseSourceAO -> DatabaseSourcePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}

	@Override
	public Class<? super DatabaseSourceAO> getConditionClass() {
		return DatabaseSourceAO.class;
	}

}
