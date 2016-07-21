package de.uniol.inf.is.odysseus.net.querydistribute.modify;

import java.util.Collection;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryPartModificationException;

/**
 * A modification helper provides useful methods to handle modification
 * parameters.
 * 
 * @author Michael Brand
 */
public interface IModificationParameterHelper {

	/**
	 * Determines the degree of modification (e.g., the number of fragments).
	 * 
	 * @return The degree of modification given by the parameters for
	 *         modification.
	 * @throws QueryPartModificationException
	 *             if any error occurs.
	 */
	public int determineDegreeOfModification()
			throws QueryPartModificationException;
	
	/**
	 * Determines the operators, where the modification starts and ends given by
	 * the parameters for modification.
	 * 
	 * @param queryParts
	 *            The given query parts.
	 * @return A pair as follows: <br />
	 *         {@link IPair#getE1()} is the operator, where the modification
	 *         starts or {@link Optional#absent()}, if no id for an start operator is
	 *         given. <br />
	 *         {@link IPair#getE2()} is the operator, where the modification ends
	 *         or {@link Optional#absent()}, if no id for an end operator is
	 *         given.
	 * @throws QueryPartModificationException if any error occurs.
	 */
	public IPair<Optional<ILogicalOperator>, Optional<ILogicalOperator>> determineStartAndEndPoints(
			Collection<ILogicalQueryPart> queryParts)
			throws QueryPartModificationException;

}