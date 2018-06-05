package de.uniol.inf.is.odysseus.recovery.badast.util;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetadataUpdater;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull.AccessPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;
import de.uniol.inf.is.odysseus.recovery.badast.physicaloperator.BaDaStRecoveryPO;
import de.uniol.inf.is.odysseus.relational_interval.RelationalTimestampAttributeTimeIntervalMFactory;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SystemTimeIntervalFactory;

/**
 * Helper class for the {@link BaDaStRecoveryPO} to handle meta data and time
 * stamps for the elements, which are sent by BaDaSt. <br />
 * <br />
 * If {@link ITimeInterval} is used, a {@link MetadataUpdatePO} exists or the
 * {@link AccessPO} can handle the meta data. In the second case, we need the
 * {@link IMetadataUpdater} of the {@link AccessPO}
 * 
 * @author Michael Brand
 *
 */
public class TimeStampHelper {

	/**
	 * Checks, if {@link ITimeInterval} is used.
	 * 
	 * @param attr
	 *            The meta schema.
	 * @return True, if {@code attr} contains {@link ITimeInterval} as class.
	 */
	public static boolean isTimeIntervalUsed(IMetaAttribute attr) {
		return Arrays.asList(attr.getClasses()).contains(ITimeInterval.class);
	}

	/**
	 * Searches for any {@link MetadataUpdatePO} recursively downwards to
	 * sources.
	 * 
	 * @param operator
	 *            The root of the plan to check.
	 * @return A {@link MetadataUpdatePO}, if {@code operator} or any operator
	 *         between {@code operator} and sources in a
	 *         {@link MetadataUpdatePO}; else: {@code null}.
	 */
	public static MetadataUpdatePO<?, ?> searchMetaDataUpdatePO(AbstractSource<?> operator) {
		if (MetadataUpdatePO.class.isInstance(operator)) {
			return (MetadataUpdatePO<?, ?>) operator;
		} else if (!AbstractPipe.class.isInstance(operator)
				|| ((AbstractPipe<?, ?>) operator).getSubscribedToSource().isEmpty()) {
			return null;
		}
		return searchMetaDataUpdatePO(
				(AbstractSource<?>) ((AbstractPipe<?, ?>) operator).getSubscribedToSource(0).getSource());
	}

	/**
	 * Searches for any {@link ReceiverPO} recursively downwards to sources.
	 * 
	 * @param operator
	 *            The root of the plan to check.
	 * @return A {@link ReceiverPO}, if {@code operator} or any operator between
	 *         {@code operator} and sources in a {@link ReceiverPO}; else:
	 *         {@code null}.
	 */
	public static ReceiverPO<?, ?> searchReceiverPO(AbstractSource<?> operator) {
		if (ReceiverPO.class.isInstance(operator)) {
			return (ReceiverPO<?, ?>) operator;
		} else if (!AbstractPipe.class.isInstance(operator)
				|| ((AbstractPipe<?, ?>) operator).getSubscribedToSource().isEmpty()) {
			return null;
		}
		return searchReceiverPO(
				(AbstractSource<?>) ((AbstractPipe<?, ?>) operator).getSubscribedToSource(0).getSource());
	}

	/**
	 * Clones a {@link RelationalTimestampAttributeTimeIntervalMFactory}.
	 * 
	 * @param fac
	 *            The factory to clone.
	 * @return A copy of {@code fac}.
	 */
	private static IMetadataUpdater<?, ?> cloneIntervalFactory(RelationalTimestampAttributeTimeIntervalMFactory fac) {
		if (fac.getStartAttrPos() != -1) {
			return new RelationalTimestampAttributeTimeIntervalMFactory(fac.getStartAttrPos(), fac.getEndAttrPos(),
					fac.isClearEnd(), fac.getDateFormat(), fac.getTimezone().getId(), fac.getLocale(), fac.getFactor(),
					fac.getOffset(),null,null);
		}
		return new RelationalTimestampAttributeTimeIntervalMFactory(fac.getStartTimestampYearPos(),
				fac.getStartTimestampMonthPos(), fac.getStartTimestampDayPos(), fac.getStartTimestampHourPos(),
				fac.getStartTimestampMinutePos(), fac.getStartTimestampSecondPos(),
				fac.getStartTimestampMillisecondPos(), fac.getFactor(), fac.isClearEnd(), fac.getTimezone().getId());
	}

	/**
	 * Clones a {@link SystemTimeIntervalFactory}.
	 * 
	 * @param fac
	 *            The factory to clone.
	 * @return A copy of {@code fac}.
	 */
	private static IMetadataUpdater<?, ?> cloneIntervalFactory(SystemTimeIntervalFactory<?, ?> fac) {
		SystemTimeIntervalFactory<?, ?> copy = new SystemTimeIntervalFactory<>();
		copy.clearEnd(fac.isClearEnd());
		return copy;
	}

	/**
	 * Creates a copy of an {@link IMetadataUpdater}.
	 * 
	 * @param operator
	 *            The {@link MetadataUpdatePO}, of which
	 *            {@link MetadataUpdatePO#getMetadataFactory()} shall be copied.
	 * @return A clone of the original metadata factory.
	 */
	public static IMetadataUpdater<?, ?> copyInitializer(MetadataUpdatePO<?, ?> operator) {
		IMetadataUpdater<?, ?> fac = operator.getMetadataFactory();
		if(fac == null) {
			return null;
		} else if (fac instanceof RelationalTimestampAttributeTimeIntervalMFactory) {
			return cloneIntervalFactory((RelationalTimestampAttributeTimeIntervalMFactory) fac);
		}
		return cloneIntervalFactory((SystemTimeIntervalFactory<?, ?>) fac);
	}

	/**
	 * Creates a copy of an {@link IMetadataUpdater}.
	 * 
	 * @param operator
	 *            The {@link ReceiverPO}, of which
	 *            {@link ReceiverPO#getMetadataFactory()} shall be copied.
	 * @return A clone of the original metadata factory.
	 */
	public static IMetadataUpdater<?, ?> copyInitializer(ReceiverPO<?, ?> operator) {
		IMetadataUpdater<?, ?> fac = operator.getMetadataFactory();
		if(fac == null) {
			return null;
		} else if (fac instanceof RelationalTimestampAttributeTimeIntervalMFactory) {
			return cloneIntervalFactory((RelationalTimestampAttributeTimeIntervalMFactory) fac);
		}
		return cloneIntervalFactory((SystemTimeIntervalFactory<?, ?>) fac);
	}

}