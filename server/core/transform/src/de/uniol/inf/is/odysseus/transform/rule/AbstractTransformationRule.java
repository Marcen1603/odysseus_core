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
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.ISubscribable;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.ruleengine.rule.AbstractRule;
import de.uniol.inf.is.odysseus.transform.engine.TransformationWorkingMemory;

public abstract class AbstractTransformationRule<T> extends
		AbstractRule<T, TransformationConfiguration> implements
		ITransformationRule {

	public static final String OPERATOR_IDS_SET = "OperatorIDsSet";

	final static Logger LOG = LoggerFactory
			.getLogger(AbstractTransformationRule.class);

	protected ISession getCaller() {
		return ((TransformationWorkingMemory) this.getCurrentWorkingMemory())
				.getCaller();
	}

	protected IDataDictionaryWritable getDataDictionary() {
		return (IDataDictionaryWritable) ((TransformationWorkingMemory) this
				.getCurrentWorkingMemory()).getDataDictionary();
	}

	static Logger logger = LoggerFactory
			.getLogger(AbstractTransformationRule.class);

	protected boolean isLastOne(ILogicalOperator operator) {
		if (operator.getSubscriptions().size() == 1) {
			if (operator.getSubscriptions().iterator().next().getTarget() instanceof TopAO) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	protected void defaultExecute(ILogicalOperator logical,
			IPhysicalOperator physical, TransformationConfiguration config,
			boolean retract, boolean insert, boolean ignoreSinkInput,
			boolean rename) {
		if (LOG.isTraceEnabled()) {
			LOG.trace("Replacing " + logical + " with " + physical);
		}
		// Check if operator has an id and if this id is not already defined
		// Attention, id can be null
		handleOperatorID(logical, physical, config);

		updatePhysicalOperator(logical, physical, rename);

		replace(logical, physical, config, ignoreSinkInput);

		if (LOG.isTraceEnabled()) {
			if (physical instanceof ISubscribable) {
				LOG.trace("Subscriptions of phyiscal "
						+ ((ISubscribable) physical).getSubscriptions());
			}

		}

		if (retract) {
			retract(logical);
		}
		if (insert) {
			insert(physical);
		}
	}

	protected void updatePhysicalOperator(ILogicalOperator logical,
			IPhysicalOperator physical, boolean rename) {
		physical.setOutputSchema(logical.getOutputSchema());
		if (logical.getOutputSchema() == null) {
			logger.warn("Operator " + logical + " has not output schema");
		}
		if (rename) {
			physical.setName(logical.getName());
		}
		physical.setDebug(logical.isDebug());
		physical.setSuppressPunctuations(logical.isSuppressPunctuations());
		physical.getParameterInfos().putAll(logical.getParameterInfos());
	}

	protected void handleOperatorID(ILogicalOperator logical,
			IPhysicalOperator physical, TransformationConfiguration config) {
		Resource id = logical.getUniqueIdentifier() == null ? null
				: new Resource(getCaller().getUser(),
						logical.getUniqueIdentifier());

		if (id != null) {
			if (!config.isVirtualTransformation()) {

				if (getDataDictionary().containsOperator(id)) {
					throw new TransformationException("Operator with id " + id
							+ " is already registered.");
				} else {
					getDataDictionary().setOperator(id, physical);
					@SuppressWarnings("unchecked")
					List<Resource> newDefinedRessources = (List<Resource>) this
							.getCurrentWorkingMemory().getFromKeyValueMap(
									OPERATOR_IDS_SET);
					if (newDefinedRessources == null) {
						newDefinedRessources = new LinkedList<Resource>();
						this.getCurrentWorkingMemory().addToKeyValueMap(
								OPERATOR_IDS_SET, newDefinedRessources);
					}
					newDefinedRessources.add(id);

					for (IOperatorOwner owner : logical.getOwner()) {
						physical.addUniqueId(owner, id);
					}
				}
			} else {
				logger.debug("Virtual Transformation: IDs ignored");
				id = null;
			}
		}

	}

	protected void defaultExecute(ILogicalOperator logical,
			IPhysicalOperator physical, TransformationConfiguration config,
			boolean retract, boolean insert) {
		defaultExecute(logical, physical, config, retract, insert, false, true);
	}

	protected void defaultExecute(ILogicalOperator logical,
			IPhysicalOperator physical, TransformationConfiguration config,
			boolean retract, boolean insert, boolean rename) {
		defaultExecute(logical, physical, config, retract, insert, false,
				rename);
	}

	protected void replace(ILogicalOperator oldOperator,
			IPhysicalOperator newOperator,
			TransformationConfiguration transformationConfig) {
		replace(oldOperator, newOperator, transformationConfig, false);
	}

	protected void replace(ILogicalOperator oldOperator,
			ILogicalOperator newOperator,
			TransformationConfiguration transformationConfig) {
		transformationConfig.getTransformationHelper().replace(oldOperator,
				newOperator);
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

	protected void insertTimestampAO(ILogicalOperator operator,
			String dateFormat) {
		TimestampAO timestampAO = new TimestampAO();
		timestampAO.setDateFormat(dateFormat);
		if (operator.getOutputSchema() != null) {

			for (SDFAttribute attr : operator.getOutputSchema()) {
				if (SDFDatatype.START_TIMESTAMP.toString().equalsIgnoreCase(
						attr.getDatatype().getURI())
						|| SDFDatatype.START_TIMESTAMP_STRING.toString()
								.equalsIgnoreCase(attr.getDatatype().getURI())) {
					timestampAO.setStartTimestamp(attr);
				}

				if (SDFDatatype.END_TIMESTAMP.toString().equalsIgnoreCase(
						attr.getDatatype().getURI())
						|| SDFDatatype.END_TIMESTAMP_STRING.toString()
								.equalsIgnoreCase(attr.getDatatype().getURI())) {
					timestampAO.setEndTimestamp(attr);
				}

			}
		}
		// timestampAO.subscribeTo(operator, operator.getOutputSchema());
		timestampAO.setName(timestampAO.getStandardName());
		RestructHelper.insertOperatorBefore(timestampAO, operator);
		insert(timestampAO);
	}

	protected boolean hasTimestampAOAsFather(ILogicalOperator operator) {
		boolean hasTimestampAOAsFather = false;
		for (LogicalSubscription sub : operator.getSubscriptions()) {
			if (sub.getTarget() instanceof TimestampAO) {
				hasTimestampAOAsFather = true;
				break;
			}
		}
		return hasTimestampAOAsFather;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

}
