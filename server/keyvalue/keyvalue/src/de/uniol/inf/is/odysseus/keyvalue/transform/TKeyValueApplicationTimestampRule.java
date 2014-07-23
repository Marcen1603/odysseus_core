package de.uniol.inf.is.odysseus.keyvalue.transform;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataUpdater;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.keyvalue.KeyValueTimestampAttributeTimeIntervalMFactory;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

/**
 * @author Jan Soeren Schwarz
 */
public class TKeyValueApplicationTimestampRule extends AbstractKeyValueIntervalTransformationRule<TimestampAO> {

	@Override
	public void execute(TimestampAO timestampAO,
			TransformationConfiguration transformConfig) throws RuleException {
		boolean clearEnd = timestampAO.isClearEnd();
		String startKey = timestampAO.getStartTimestamp().getQualName();
//		int pos = schema.indexOf(timestampAO.getStartTimestamp());

		@SuppressWarnings("rawtypes")
		IMetadataUpdater mUpdater;
		if (KeyValueObject.class
				.isAssignableFrom(timestampAO.getInputSchema().getType())) {
			if (startKey != null && startKey.length() > 0) {
				String endKey = timestampAO.hasEndTimestamp() ? timestampAO.getEndTimestamp().getQualName() : null;
				mUpdater = new KeyValueTimestampAttributeTimeIntervalMFactory(
						startKey, endKey, clearEnd, timestampAO.getDateFormat(),
						timestampAO.getTimezone(), timestampAO.getLocale(),
						timestampAO.getFactor(), timestampAO.getOffset());
			} else {
				String year = timestampAO.getStartTimestampYear().getQualName();
				String month = timestampAO.getStartTimestampMonth().getQualName();
				String day = timestampAO.getStartTimestampDay().getQualName();
				String hour = timestampAO.getStartTimestampHour().getQualName();
				String minute = timestampAO.getStartTimestampMinute().getQualName();
				String second = timestampAO.getStartTimestampSecond().getQualName();
				String millisecond = timestampAO.getStartTimestampMillisecond().getQualName();
				int factor = timestampAO.getFactor();
				mUpdater = new KeyValueTimestampAttributeTimeIntervalMFactory(
						year, month, day, hour, minute, second, millisecond,
						factor, clearEnd, timestampAO.getTimezone());
			}
		} else {
			throw new TransformationException("Cannot set Time with "
					+ timestampAO.getInputSchema().getType());
		}

		@SuppressWarnings("unchecked")
		MetadataUpdatePO<?, ?> po = new MetadataUpdatePO<ITimeInterval, KeyValueObject<? extends ITimeInterval>>(mUpdater);
		defaultExecute(timestampAO, po, transformConfig, true, true);
	}

	@Override
	public boolean isExecutable(TimestampAO operator, TransformationConfiguration transformConfig) {
		if (super.isExecutable(operator, transformConfig)) {
			if (!operator.isUsingSystemTime()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getPriority() {
		return 0;
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
