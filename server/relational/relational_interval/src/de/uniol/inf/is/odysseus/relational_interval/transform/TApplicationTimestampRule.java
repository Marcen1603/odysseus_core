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
package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataUpdater;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class TApplicationTimestampRule extends
		AbstractRelationalIntervalTransformationRule<TimestampAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(TimestampAO timestampAO,
			TransformationConfiguration transformConfig) throws RuleException {
		SDFSchema schema = timestampAO.getInputSchema();
		boolean clearEnd = timestampAO.isClearEnd();
		int pos = schema.indexOf(timestampAO.getStartTimestamp());

		IMetadataUpdater mUpdater;
		if (Tuple.class
				.isAssignableFrom(timestampAO.getInputSchema().getType())) {
			if (pos >= 0) {
				int posEnd = timestampAO.hasEndTimestamp() ? timestampAO
						.getInputSchema()
						.indexOf(timestampAO.getEndTimestamp()) : -1;
				mUpdater = new RelationalTimestampAttributeTimeIntervalMFactory(
						pos, posEnd, clearEnd, timestampAO.getDateFormat(),
						timestampAO.getTimezone(), timestampAO.getLocale(),
						timestampAO.getFactor(), timestampAO.getOffset());
			} else {

				int year = schema.indexOf(timestampAO.getStartTimestampYear());
				int month = schema
						.indexOf(timestampAO.getStartTimestampMonth());
				int day = schema.indexOf(timestampAO.getStartTimestampDay());
				int hour = schema.indexOf(timestampAO.getStartTimestampHour());
				int minute = schema.indexOf(timestampAO
						.getStartTimestampMinute());
				int second = schema.indexOf(timestampAO
						.getStartTimestampSecond());
				int millisecond = schema.indexOf(timestampAO
						.getStartTimestampMillisecond());
				int factor = timestampAO.getFactor();
				mUpdater = new RelationalTimestampAttributeTimeIntervalMFactory(
						year, month, day, hour, minute, second, millisecond,
						factor, clearEnd, timestampAO.getTimezone());
			}
		} else {
			throw new TransformationException("Cannot set Time with "
					+ timestampAO.getInputSchema().getType());
		}

		// two case:
		// 1) the input operator can process meta data updates
		if (timestampAO.getPhysInputPOs().size() == 1
				&& timestampAO.getPhysInputOperators().get(0) instanceof IMetadataInitializer) {
			IPhysicalOperator source = timestampAO.getPhysInputOperators().get(
					0);
			((IMetadataInitializer) source).addMetadataUpdater(mUpdater);
			// Use the existing physical operator as replacement ...
			defaultExecute(timestampAO, source, transformConfig, true, false,
					false);
		} else {
			MetadataUpdatePO<?, ?> po = new MetadataUpdatePO<ITimeInterval, Tuple<? extends ITimeInterval>>(
					mUpdater);
			defaultExecute(timestampAO, po, transformConfig, true, true);
		}
	}

	@Override
	public boolean isExecutable(TimestampAO operator,
			TransformationConfiguration transformConfig) {
		if (super.isExecutable(operator, transformConfig)) {
			if (!operator.isUsingSystemTime()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super TimestampAO> getConditionClass() {
		return TimestampAO.class;
	}

}
