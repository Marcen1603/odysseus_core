package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.newimpl;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartModificationException;

/**
 * A fragmentation rule can be implemented, if a combination of an operator and
 * a fragmentation strategy needs a special handling. <br />
 * Such a special handling may be that certain operators can not be part of a
 * fragment or that they can be part but not without further handling (e.g., the
 * usage of partial aggregates).
 * 
 * @author Michael Brand
 * 
 * @param <Strategy>
 *            The given strategy type.
 * @param <Operator>
 *            The given operator type.
 *
 */
public interface IFragmentationRule<Strategy extends AbstractFragmentationQueryPartModificator, Operator extends ILogicalOperator> {

	/**
	 * The class of the fragmentation strategy.
	 * @return The class of the generic parameter <code>Strategy</code>.
	 */
	public Class<Strategy> getStrategyClass();
	
	/**
	 * The class of the logical operator.
	 * @return The class of the generic parameter <code>Operator</code>.
	 */
	public Class<Operator> getOperatorClass();
	
	/**
	 * Checks, if a given operator can be part of a fragment.
	 * 
	 * @param operator
	 *            The given operator.
	 * @param strategy
	 *            The given strategy.
	 * @return True, if <code>operator</code> can be executed in
	 *         intra-operational parallelism and therefore be part of a
	 *         fragment.
	 */
	public boolean canOperatorBePartOfFragments(Strategy strategy,
			Operator operator);

	/**
	 * Checks if a query part needs a special handling. <br />
	 * This may be the usage of partial aggregates.
	 * 
	 * @param part
	 *            The query part.
	 * @param operator
	 *            The given operator.
	 * @param helper
	 *            The current fragmentation helper.
	 * @return True, if <code>part</code> needs a special handling. This makes
	 *         direct connection without fragmentation and/or reunion operators
	 *         impossible.
	 */
	public boolean needSpecialHandlingForQueryPart(ILogicalQueryPart part,
			Operator operator, AbstractFragmentationHelper helper);

	/**
	 * Does a special handling for a given query part.
	 * 
	 * @param part
	 *            The query part to modify.
	 * @param helper
	 *            The {@link AbstractFragmentationHelper} instance.
	 * @param bundle
	 *            The {@link FragmentationInfoBundle} instance.
	 * @return The modified operator.
	 * @param modificatorParameters
	 *            The parameters for the modification given by the user without
	 *            the parameter <code>fragmentation-strategy-name</code>.
	 * @throws QueryPartModificationException
	 *             if there is none or more than one aggregation.
	 */
	public Operator specialHandling(ILogicalQueryPart part,
			AbstractFragmentationHelper helper, FragmentationInfoBundle bundle)
			throws QueryPartModificationException;

}