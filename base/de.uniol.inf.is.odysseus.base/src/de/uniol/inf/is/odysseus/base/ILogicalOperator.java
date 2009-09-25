package de.uniol.inf.is.odysseus.base;

import java.util.List;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public interface ILogicalOperator extends IClone, IOwnedOperator {

	public ILogicalOperator clone();

	public ILogicalOperator deepClone();

	/**
	 * @return the outElements
	 * @uml.property name="outElements"
	 */
	public SDFAttributeList getOutputSchema();

	public SDFAttributeList getOutElements();

	/**
	 * @param outElements
	 *            the outElements to set
	 * @uml.property name="outElements"
	 */
	public void setOutputSchema(SDFAttributeList outElements);

	/**
	 * @return the predicate
	 * @uml.property name="predicate"
	 */
	@SuppressWarnings("unchecked")
	public IPredicate getPredicate();

	/**
	 * @param predicate
	 *            the predicate to set
	 * @uml.property name="predicate"
	 */
	@SuppressWarnings("unchecked")
	public void setPredicate(IPredicate predicate);

	public SDFAttributeList getInputSchema(int pos);

	public void setInputSchema(int pos, SDFAttributeList schema);

	public void setInputAO(int pos, ILogicalOperator input);

	public void setNoOfInputs(int count);

	public ILogicalOperator getInputAO(int pos);

	public List<ILogicalOperator> getInputAOs();

	public boolean replaceInput(ILogicalOperator oldInput,
			ILogicalOperator newInput);

	public int getInputPort(ILogicalOperator abstractLogicalOperator);

	public int getNumberOfInputs();

	public String getPOName();

	public void setPOName(String name);

	public int hashCode();

	public boolean equals(Object obj);

	public void setPhysInputPO(int port, IPhysicalOperator physPO);

	public IPhysicalOperator getPhysInputPO(int port);
	
	public List<IPhysicalOperator> getPhysInputPOs();

	public void setPhysInputAtAOPosition(
			ILogicalOperator abstractLogicalOperator,
			IPhysicalOperator source);

	public void replaceInput(ILogicalOperator abstractLogicalOperator,
			IPhysicalOperator source);
}