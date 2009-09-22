package de.uniol.inf.is.odysseus.base.planmanagement.query;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.planmanagement.IOperatorControl;
import de.uniol.inf.is.odysseus.base.planmanagement.query.querybuiltparameter.QueryBuildParameter;

/**
 * Describes an object which represents a basic query in odyessus. This
 * interface is used as an intern view on a query. Editing these queries should
 * not be delimited.
 * 
 * It has a unique ID and consists of a logical plan, a physical plan, a
 * priority and a execution state.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IEditableQuery extends IOperatorControl, IQuery {
	/**
	 * Set the logical plan of this query.
	 * 
	 * @param newLogicalAlgebra
	 *            The new logical plan of this query.
	 */
	public void setLogicalPlan(ILogicalOperator newLogicalAlgebra);

	/**
	 * Returns the logical plan of this query.
	 * 
	 * @return The logical plan of this query.
	 */
	public ILogicalOperator getLogicalPlan();

	/**
	 * Initializes the physical plan of this query. Should be used if a new plan
	 * is set. This method also opens the physical plan and sets the owner
	 * relationship between the query and the operators.
	 * 
	 * The new physical plan is stored as the initial physical plan and is set
	 * as the current active physical root.
	 * 
	 * @param physicalChilds
	 *            The new physical plan of this Query.
	 * @throws OpenFailedException
	 *             An {@link Exception} occurs during opening an opertaor.
	 */
	public void initializePhysicalPlan(IPhysicalOperator physicalChilds)
			throws OpenFailedException;

	/**
	 * Sets the current physical plan which will be executed. Owner relationship
	 * between the query and the operators are not affected. The physical plan
	 * could be modified by inner operations. If the physical plan should be
	 * used after setting it use the return value oder {@link #getRoot()}.
	 * 
	 * This method should be only used if special optimizations are processed.
	 * For initial setting the physical plan use
	 * {@link #initializePhysicalPlan(IPhysicalOperator)}.
	 * 
	 * @param root
	 *            The new root of the physical plan.
	 * @return The new physical plan of this query.
	 * @throws OpenFailedException
	 *             An {@link Exception} occurred during opening an opertaor.
	 */
	public IPhysicalOperator setRoot(IPhysicalOperator root)
			throws OpenFailedException;

	/**
	 * Returns the physical plan of this query.
	 * 
	 * @return The physical plan of this query.
	 */
	public IPhysicalOperator getRoot();

	/**
	 * Sets the physical children of this query. These children are the physical
	 * operators which are necessary for the execution of this query. It also
	 * sets the owner relationship between the query and the operators.
	 * 
	 * This method should be only used if special optimizations are processed.
	 * For initial setting the physical plan use
	 * {@link #initializePhysicalPlan(IPhysicalOperator)}.
	 * 
	 * @param physicalChilds
	 *            Physical operators which are necessary for the execution of
	 *            this query.
	 */
	public void setPhysicalChilds(ArrayList<IPhysicalOperator> physicalChilds);

	/**
	 * Returns the direct physical children which are necessary for the
	 * execution of this query.
	 * 
	 * TODO: rename to getPhysicalChilds()?
	 * 
	 * @return The direct physical children which are necessary for the
	 *         execution of this query.
	 */
	public ArrayList<IPhysicalOperator> getIntialPhysicalPlan();

	/**
	 * Removes the ownership of this query and the registered child operators.
	 * After this method this query has no relationship to any operator.
	 */
	public void removeOwnerschip();

	/**
	 * Returns the {@link QueryBuildParameter} of this query.
	 * 
	 * @return The {@link QueryBuildParameter} of this query.
	 */
	public QueryBuildParameter getBuildParameter();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.base.planmanagement.IOperatorControl#stop()
	 */
	@Override
	public void stop();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.base.planmanagement.IOperatorControl#start()
	 */
	@Override
	public void start();
}
