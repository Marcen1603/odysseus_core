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
package de.uniol.inf.is.odysseus.transform.rule;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.rule.AbstractRule;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;

public abstract class AbstractTransformationRule<T> extends
		AbstractRule<T, TransformationConfiguration> {

	static Logger logger = LoggerFactory
			.getLogger(AbstractTransformationRule.class);

	protected void defaultExecute(ILogicalOperator logical,
			IPhysicalOperator physical, TransformationConfiguration config,
			boolean retract, boolean insert, boolean ignoreSinkInput) {
		// Check if operator has an id and if this id is not already defined
		// Attention, id can be null
		handleOperatorID(logical, physical);

		updatePhysicalOperator(logical, physical);

		replace(logical, physical, config, ignoreSinkInput);

		if (retract) {
			retract(logical);
		}
		if (insert) {
			insert(physical);
		}
	}

	protected void updatePhysicalOperator(ILogicalOperator logical,
			IPhysicalOperator physical) {
		physical.setOutputSchema(logical.getOutputSchema());
		if (logical.getOutputSchema() == null) {
			logger.warn("Operator " + logical + " has not output schema");
		}
		physical.setName(logical.getName());
	}

	protected void handleOperatorID(ILogicalOperator logical,
			IPhysicalOperator physical) {
		String id = logical.getUniqueIdentifier() == null ? null
				: getDataDictionary().createUserUri(
						logical.getUniqueIdentifier(), getCaller());

		if (id != null) {
			if (getDataDictionary().containsOperator(id)) {
				throw new TransformationException("Operator with id " + id
						+ " is already registered.");
			} else {
				getDataDictionary().setOperator(id, physical);
				for (IOperatorOwner owner : logical.getOwner()) {
					physical.addUniqueId(owner, id);
				}
			}
		}
	}

	protected void defaultExecute(ILogicalOperator logical,
			IPhysicalOperator physical, TransformationConfiguration config,
			boolean retract, boolean insert) {
		defaultExecute(logical, physical, config, retract, insert, false);
	}

	protected void replace(ILogicalOperator oldOperator,
			IPhysicalOperator newOperator,
			TransformationConfiguration transformationConfig) {
		replace(oldOperator, newOperator, transformationConfig, false);
	}

	protected void replace(ILogicalOperator oldOperator,
			IPhysicalOperator newOperator,
			TransformationConfiguration transformationConfig,
			boolean ignoreSinkInput) {

		Collection<ILogicalOperator> toUpdate = new ArrayList<ILogicalOperator>();
		if (newOperator.isPipe()) {
			toUpdate = transformationConfig.getTransformationHelper().replace(
					oldOperator, (IPipe<?, ?>) newOperator, ignoreSinkInput);
		} else if (newOperator.isSource()) {
			toUpdate = transformationConfig.getTransformationHelper().replace(
					oldOperator, (ISource<?>) newOperator);
		} else if (newOperator.isSink()) {
			toUpdate = transformationConfig.getTransformationHelper().replace(
					oldOperator, (ISink<?>) newOperator, ignoreSinkInput);
		} else {
			LoggerSystem.printlog("ERROR");
			throw new RuntimeException(
					new TransformationException(
							"replace in rule \""
									+ getName()
									+ "\" failed because the new operator is not an ISink, ISource or IPipe!"));
		}
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}
	}

}
