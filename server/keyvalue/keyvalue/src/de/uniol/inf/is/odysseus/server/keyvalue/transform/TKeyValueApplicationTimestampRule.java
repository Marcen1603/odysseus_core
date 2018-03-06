package de.uniol.inf.is.odysseus.server.keyvalue.transform;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataInitializer;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataUpdater;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.keyvalue.KeyValueTimestampAttributeTimeIntervalMFactory;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

/**
 * @author Jan Soeren Schwarz
 */
public class TKeyValueApplicationTimestampRule extends AbstractKeyValueIntervalTransformationRule<TimestampAO> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(TimestampAO timestampAO,
			TransformationConfiguration transformConfig) throws RuleException {
		boolean clearEnd = timestampAO.isClearEnd();
		// TODO: Expression could contains more than attributepath ...
		String startKey = timestampAO.getStartExpression().toString();
//		int pos = schema.indexOf(timestampAO.getStartTimestamp());

		IMetadataUpdater mUpdater;
		if (KeyValueObject.class
				.isAssignableFrom(timestampAO.getInputSchema().getType())) {
			if (startKey != null && startKey.length() > 0) {
				String endKey = timestampAO.hasEndTimestampExpression() ? timestampAO.getEndExpression().toString() : null;
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
