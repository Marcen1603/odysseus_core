package de.uniol.inf.is.odysseus.recovery.incomingelements.sourcesync.physicaloperator;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataUpdater;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;

// TODO javaDoc
// If TimeInterval is used, a MetadataUpdatePO exists or the Access can handle the meta data. In the second case, I need the metadata updater of the access po.
public class TimeStampHelper {

	public static boolean isTimeIntervalUsed(IMetaAttribute attr) {
		return Arrays.asList(attr.getClasses()).contains(ITimeInterval.class);
	}

	public static MetadataUpdatePO<?, ?> searchMetaDataUpdatePO(AbstractSource<?> operator) {
		if (MetadataUpdatePO.class.isInstance(operator)) {
			return (MetadataUpdatePO<?, ?>) operator;
		} else if (!AbstractPipe.class.isInstance(operator)
				|| ((AbstractPipe<?, ?>) operator).getSubscribedToSource().isEmpty()) {
			return null;
		}
		return searchMetaDataUpdatePO(
				(AbstractSource<?>) ((AbstractPipe<?, ?>) operator).getSubscribedToSource(0).getTarget());
	}

	public static ReceiverPO<?, ?> searchReceiverPO(AbstractSource<?> operator) {
		if (ReceiverPO.class.isInstance(operator)) {
			return (ReceiverPO<?, ?>) operator;
		} else if (!AbstractPipe.class.isInstance(operator)
				|| ((AbstractPipe<?, ?>) operator).getSubscribedToSource().isEmpty()) {
			return null;
		}
		return searchReceiverPO(
				(AbstractSource<?>) ((AbstractPipe<?, ?>) operator).getSubscribedToSource(0).getTarget());
	}

	private static RelationalTimestampAttributeTimeIntervalMFactory cloneIntervalFactory(
			RelationalTimestampAttributeTimeIntervalMFactory fac) {
		if (fac.getStartAttrPos() != -1) {
			return new RelationalTimestampAttributeTimeIntervalMFactory(fac.getStartAttrPos(), fac.getEndAttrPos(),
					fac.isClearEnd(), fac.getDateFormat(), fac.getTimezone().getID(), fac.getLocale(), fac.getFactor(),
					fac.getOffset());
		}
		return new RelationalTimestampAttributeTimeIntervalMFactory(fac.getStartTimestampYearPos(),
				fac.getStartTimestampMonthPos(), fac.getStartTimestampDayPos(), fac.getStartTimestampHourPos(),
				fac.getStartTimestampMinutePos(), fac.getStartTimestampSecondPos(),
				fac.getStartTimestampMillisecondPos(), fac.getFactor(), fac.isClearEnd(), fac.getTimezone().getID());
	}

	public static IMetadataUpdater<?, ?> createInitializer(MetadataUpdatePO<?, ?> operator) {
		RelationalTimestampAttributeTimeIntervalMFactory fac = (RelationalTimestampAttributeTimeIntervalMFactory) operator
				.getMetadataFactory();
		return cloneIntervalFactory(fac);
	}

	public static IMetadataUpdater<?, ?> createInitializer(ReceiverPO<?, ?> operator) {
		RelationalTimestampAttributeTimeIntervalMFactory fac = (RelationalTimestampAttributeTimeIntervalMFactory) operator
				.getMetadataFactory();
		return cloneIntervalFactory(fac);
	}

}