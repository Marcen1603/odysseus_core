package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.newimpl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.AbstractFragmentationHelper;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.AbstractFragmentationQueryPartModificator;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.FragmentationInfoBundle;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl.IFragmentationRule;
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
			AbstractFragmentationHelper helper, FragmentationInfoBundle bundle)
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

		Map<ILogicalQueryPart, Collection<ILogicalQueryPart>> copyMap = bundle
				.getCopyMap();

		// Handling of aggregations
		for (ILogicalOperator operator : partOfOriginalTarget.getOperators()) {

			Collection<IFragmentationRule<AbstractFragmentationQueryPartModificator, ILogicalOperator>> rules = AbstractFragmentationHelper
					.getFragmentationRules(this, operator);

			for (IFragmentationRule<AbstractFragmentationQueryPartModificator, ILogicalOperator> rule : rules) {
				// XXX Assuming exact one rule: the rule for aggregations
				
				if (!rule.needSpecialHandlingForQueryPart(partOfOriginalTarget,
						operator, helper)) {

					continue;

				}

				ILogicalOperator specialOperator = rule.specialHandling(
						partOfOriginalTarget, helper, bundle);

				// Subscribe the new aggregation
				reunionOperator.subscribeSink(specialOperator, 0, 0,
						reunionOperator.getOutputSchema());
				if (optFragmentOperator.isPresent()) {

					specialOperator.subscribeSink(optFragmentOperator.get(), 0,
							0, reunionOperator.getOutputSchema());

				}

				// Add the aggregation to the reunion query part
				copyMap.remove(partOfReunionOperator);
				partOfReunionOperator.addOperator(specialOperator);
				for (ILogicalQueryPart copiedPart : copiedPartsOfReunionOperator) {

					copiedPart.addOperator(specialOperator);

				}
				
				break;

			}

		}

		copyMap.put(partOfReunionOperator, copiedPartsOfReunionOperator);
		bundle.setCopyMap(copyMap);
		bundle.addReunionOperator(reunionOperator);
		if (optFragmentOperator.isPresent()) {

			bundle.addFragmentOperator(optFragmentOperator.get());

		}

	}

}