package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.newimpl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AggregateAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.AbstractFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.HorizontalFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.AbstractFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

/**
 * The fragmentation uses the given query parts and informations from
 * Odysseus-Script to fragment a data stream for each query part as follows: <br />
 * 1. Make as many copies as the degree of fragmentation from the query parts to
 * build fragments. <br />
 * 2. Insert fragment operators and reunion operators for those fragments. <br />
 * 3. Attach all query parts not to build fragments to the modified fragments. <br />
 * <br />
 * A horizontal fragmentation splits the data streams by routing complete
 * elements to different operators.
 * 
 * @author Michael Brand
 */
public abstract class AbstractHorizontalFragmentationQueryPartModificator
		extends AbstractFragmentationQueryPartModificator {

	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractHorizontalFragmentationQueryPartModificator.class);

	/**
	 * Searches a single aggregation within a given query part and changes it's
	 * copies to send partial aggregates.
	 * 
	 * @param part
	 *            The query part to modify.
	 * @param helper
	 *            The {@link AbstractFragmentationHelper} instance.
	 *            @param bundle
	 *            The {@link FragmentationInfoBundle} instance.
	 * @return The origin aggregation, if there is exactly one within the query
	 *         part.
	 * @param modificatorParameters
	 *            The parameters for the modification given by the user without
	 *            the parameter <code>fragmentation-strategy-name</code>.
	 * @throws QueryPartModificationException
	 *             if there is none or more than one aggregation.
	 */
	private AggregateAO handleAggregation(ILogicalQueryPart part,
			AbstractFragmentationHelper helper, FragmentationInfoBundle bundle)
			throws QueryPartModificationException {

		Preconditions.checkNotNull(part,
				"Query part must be not null!");
		Preconditions.checkNotNull(helper,
				"Fragmentation helper must be not null!");
		Preconditions.checkArgument(helper instanceof HorizontalFragmentationHelper,
				"Fragmentation helper must be a HorizontalFragmentationHelper!");
		Preconditions.checkNotNull(bundle,
				"Fragmentation info bundle must be not null!");

		AggregateAO aggregation = null;

		for (ILogicalOperator operator : part.getOperators()) {

			if (operator instanceof AggregateAO) {

				if (!((HorizontalFragmentationHelper) helper).needPartialAggregates(operator)) {
					continue;

				} else if (aggregation == null) {

					aggregation = HorizontalFragmentationHelper
							.changeAggregation(part, aggregation, bundle);
					AbstractHorizontalFragmentationQueryPartModificator.LOG
							.debug("Found {} as an aggregation, which needs to be changed in {}",
									operator, part);

				} else
					throw new QueryPartModificationException(
							"Can not fragment a query part containing more than one aggregation!");

			}

		}

		if (aggregation == null) {

			throw new QueryPartModificationException(
					"No aggregation found within " + part + "!");

		}

		return aggregation;

	}

	@Override
	protected AbstractFragmentationHelper createFragmentationHelper(
			List<String> fragmentationParameters) {

		return new HorizontalFragmentationHelper(fragmentationParameters);

	}

	@Override
	protected FragmentationInfoBundle createFragmentationInfoBundle(
			AbstractFragmentationHelper helper) {

		return new FragmentationInfoBundle();

	}

	@Override
	protected ILogicalOperator createReunionOperator(int numFragments,
			FragmentationInfoBundle bundle)
			throws QueryPartModificationException {

		return new UnionAO();

	}

	@Override
	protected void processSpecialHandling(
			Collection<ILogicalOperator> copiedSources,
			Collection<ILogicalOperator> copiedTargets,
			Optional<LogicalSubscription> subscription,
			Optional<ILogicalQueryPart> partOfOriginalSource,
			Collection<ILogicalQueryPart> partsOfCopiedSource,
			ILogicalQueryPart partOfOriginalTarget,
			Collection<ILogicalQueryPart> partsOfCopiedTargets,
			AbstractFragmentationHelper helper, 
			FragmentationInfoBundle bundle)
			throws QueryPartModificationException {

		// Creation of a fragment operator if needed
		Optional<ILogicalOperator> optFragmentOperator = Optional.absent();
		if (copiedSources.size() > 1 && subscription.isPresent()) {

			ILogicalOperator fragmentOperator = this.insertFragmentOperator(
					copiedTargets, copiedSources.iterator().next(),
					subscription.get(), bundle);
			fragmentOperator.unsubscribeFromAllSources();
			optFragmentOperator = Optional.of(fragmentOperator);

		}

		// Creation of a reunion operator
		Optional<ILogicalQueryPart> optPartOfCopiedSource = Optional.absent();
		if (!partsOfCopiedSource.isEmpty()) {

			optPartOfCopiedSource = Optional.of(partsOfCopiedSource.iterator()
					.next());

		}
		ILogicalOperator reunionOperator = this.insertReunionOperator(
				optFragmentOperator, copiedTargets, subscription,
				partOfOriginalSource, optPartOfCopiedSource, bundle);
		reunionOperator.unsubscribeFromAllSinks();

		// Determine query part of the reunion operator (and fragment operator,
		// if given)
		ILogicalQueryPart partOfReunionOperator = LogicalQueryHelper
				.determineQueryPart(bundle.getCopyMap().keySet(),
						reunionOperator).get();
		Collection<ILogicalQueryPart> copiedPartsOfReunionOperator = bundle
				.getCopyMap().get(partOfReunionOperator);

		// Handling of aggregations
		AggregateAO aggregation = this.handleAggregation(partOfOriginalTarget, helper,
				bundle);

		// Subscribe the new aggregation
		reunionOperator.subscribeSink(aggregation, 0, 0,
				reunionOperator.getOutputSchema());
		if (optFragmentOperator.isPresent()) {

			aggregation.subscribeSink(optFragmentOperator.get(), 0, 0,
					reunionOperator.getOutputSchema());

		}

		// Add the aggregation to the reunion query part
		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copyMap = bundle
				.getCopyMap();
		copyMap.remove(partOfReunionOperator);
		partOfReunionOperator.addOperator(aggregation);
		for (ILogicalQueryPart copiedPart : copiedPartsOfReunionOperator) {

			copiedPart.addOperator(aggregation);

		}
		copyMap.put(partOfReunionOperator, copiedPartsOfReunionOperator);
		bundle.setCopyMap(copyMap);
		bundle.addReunionOperator(reunionOperator);
		if(optFragmentOperator.isPresent()) {
			
			bundle.addFragmentOperator(optFragmentOperator.get());
			
		}

	}
}