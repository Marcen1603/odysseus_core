/*
 * Created on 07.06.2006
 *
 */
package de.uniol.inf.is.odysseus.logicaloperator.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Marco Grawunder
 */
public abstract class AbstractLogicalOperator implements Serializable,
		ILogicalOperator {

	private static final long serialVersionUID = -4425148851059140851L;

	private String poname = null;

	private List<ILogicalOperator> inputAO = new ArrayList<ILogicalOperator>();

	private List<SDFAttributeList> inputSchemas = new ArrayList<SDFAttributeList>();

	private List<IPhysicalOperator> physInputPOs;

	private ArrayList<IOperatorOwner> owner = new ArrayList<IOperatorOwner>();

	private SDFAttributeList outputSchema = null;

	@SuppressWarnings("unchecked")
	private IPredicate predicate = null;;

	public AbstractLogicalOperator(AbstractLogicalOperator po) {
		this.inputAO = po.inputAO;
		outputSchema = po.outputSchema;
		predicate = po.predicate;
		inputSchemas = new ArrayList<SDFAttributeList>(po.inputSchemas);

		physInputPOs = po.physInputPOs == null ? null
				: new ArrayList<IPhysicalOperator>(po.physInputPOs);
	}

	public AbstractLogicalOperator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#clone()
	 */
	public AbstractLogicalOperator clone() {
		AbstractLogicalOperator clone;
		try {
			clone = (AbstractLogicalOperator) super.clone();

			clone.inputAO = new ArrayList<ILogicalOperator>(this.inputAO);
			clone.inputSchemas = new ArrayList<SDFAttributeList>(
					this.inputSchemas);
			if (this.outputSchema != null)
				clone.outputSchema = this.outputSchema.clone();
			clone.physInputPOs = new ArrayList<IPhysicalOperator>(
					this.physInputPOs);
			clone.poname = this.poname;
			if (this.predicate != null)
				clone.predicate = this.predicate.clone();

			// TODO ueberall kopien von anlegen
			return clone;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#deepClone
	 * ()
	 */
	public AbstractLogicalOperator deepClone() {
		AbstractLogicalOperator clone = this.clone();
		ArrayList<ILogicalOperator> inputs = new ArrayList<ILogicalOperator>();
		// TODO erzeugt bei mehreren verweisen auf die selbe ao instanz im baum
		// mehrere kopien, sollte daran also subplansharing erkannt werden,
		// ginge das hiermit verloren
		for (ILogicalOperator curAo : this.inputAO) {
			inputs.add(curAo.deepClone());
		}
		clone.inputAO = inputs;
		return clone;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#
	 * getOutputSchema()
	 */
	public SDFAttributeList getOutputSchema() {
		return getOutElements();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#getOutElements
	 * ()
	 */
	public SDFAttributeList getOutElements() {
		return outputSchema;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#
	 * setOutputSchema
	 * (de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList)
	 */
	public void setOutputSchema(SDFAttributeList outElements) {
		this.outputSchema = outElements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#getPredicate
	 * ()
	 */
	@SuppressWarnings("unchecked")
	public IPredicate getPredicate() {
		return predicate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#setPredicate
	 * (de.uniol.inf.is.odysseus.base.predicate.IPredicate)
	 */
	@SuppressWarnings("unchecked")
	public void setPredicate(IPredicate predicate) {
		this.predicate = predicate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inputAO == null) ? 0 : inputAO.hashCode());
		result = prime * result
				+ ((inputSchemas == null) ? 0 : inputSchemas.hashCode());
		result = prime * result
				+ ((outputSchema == null) ? 0 : outputSchema.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result
				+ ((physInputPOs == null) ? 0 : physInputPOs.hashCode());
		result = prime * result + ((poname == null) ? 0 : poname.hashCode());
		result = prime * result
				+ ((predicate == null) ? 0 : predicate.hashCode());
		return result;
	}

	/**
	 * Make sure that every subclass that adds fields overrides this method.
	 * Uses getClass() == obj.getClass() instead of instanceOf comparison to
	 * ensure the symmetric attribute of the equals method.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractLogicalOperator other = (AbstractLogicalOperator) obj;
		if (inputAO == null) {
			if (other.inputAO != null)
				return false;
		} else if (!inputAO.equals(other.inputAO))
			return false;
		if (inputSchemas == null) {
			if (other.inputSchemas != null)
				return false;
		} else if (!inputSchemas.equals(other.inputSchemas))
			return false;
		if (outputSchema == null) {
			if (other.outputSchema != null)
				return false;
		} else if (!outputSchema.equals(other.outputSchema))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (physInputPOs == null) {
			if (other.physInputPOs != null)
				return false;
		} else if (!physInputPOs.equals(other.physInputPOs))
			return false;
		if (poname == null) {
			if (other.poname != null)
				return false;
		} else if (!poname.equals(other.poname))
			return false;
		if (predicate == null) {
			if (other.predicate != null)
				return false;
		} else if (!predicate.equals(other.predicate))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#getInputSchema
	 * (int)
	 */
	public SDFAttributeList getInputSchema(int pos) {
		SDFAttributeList ret = inputSchemas.get(pos);
		if (ret == null) {
			ILogicalOperator op = getInputAO(pos);
			if (op != null) {
				ret = op.getOutputSchema();
			}
		}

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#setInputSchema
	 * (int,
	 * de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList)
	 */
	public void setInputSchema(int pos, SDFAttributeList schema) {
		inputSchemas.set(pos, schema);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#setInputAO
	 * (int,
	 * de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator)
	 */
	public void setInputAO(int pos, ILogicalOperator input) {
		this.inputAO.set(pos, input);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#setNoOfInputs
	 * (int)
	 */
	public void setNoOfInputs(int count) {
		this.inputAO = new ArrayList<ILogicalOperator>(count);
		this.physInputPOs = new ArrayList<IPhysicalOperator>(count);
		for (int i = 0; i < count; i++) {
			inputAO.add(null);
			inputSchemas.add(null);
			physInputPOs.add(null);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#getInputAO
	 * (int)
	 */
	public ILogicalOperator getInputAO(int pos) {
		return inputAO.get(pos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#getInputAOs
	 * ()
	 */
	public List<ILogicalOperator> getInputAOs() {
		return this.inputAO;
	}

	public List<IPhysicalOperator> getPhysInputPOs() {
		return this.physInputPOs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#replaceInput
	 * (de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator,
	 * de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator)
	 */
	public boolean replaceInput(ILogicalOperator oldInput,
			ILogicalOperator newInput) {
		boolean replaced = false;
		for (int i = 0; i < getNumberOfInputs(); ++i) {
			if (this.inputAO.get(i) == oldInput) {
				this.inputAO.set(i, newInput);
				replaced = true;
			}
		}
		// System.out.println("Replace Input "+replaced);
		return replaced;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#getInputPort
	 * (de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator)
	 */
	public int getInputPort(ILogicalOperator abstractLogicalOperator) {
		for (int i = 0; i < getNumberOfInputs(); i++) {
			if (getInputAO(i) == abstractLogicalOperator) {
				return i;
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#
	 * getNumberOfInputs()
	 */
	public int getNumberOfInputs() {
		return inputAO.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#getPOName
	 * ()
	 */
	public String getPOName() {
		if (poname == null) {
			return this.getClass().getSimpleName();
		} else {
			return poname;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#setPOName
	 * (java.lang.String)
	 */
	public void setPOName(String name) {
		this.poname = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#setPhysInputPO
	 * (int, de.uniol.inf.is.odysseus.base.IPhysicalOperator)
	 */
	public void setPhysInputPO(int port, IPhysicalOperator physPO) {
		this.physInputPOs.set(port, physPO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#getPhysInputPO
	 * (int)
	 */
	public IPhysicalOperator getPhysInputPO(int port) {
		if (this.physInputPOs == null) {
			this.physInputPOs = new ArrayList<IPhysicalOperator>();
			return null;
		}
		if (port >= 0 && port < this.physInputPOs.size()) {
			return this.physInputPOs.get(port);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#
	 * setPhysInputAtAOPosition
	 * (de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator,
	 * de.uniol.inf.is.odysseus.base.IPhysicalOperator)
	 */
	public void setPhysInputAtAOPosition(
			ILogicalOperator abstractLogicalOperator, IPhysicalOperator source) {
		for (int i = 0; i < getNumberOfInputs(); i++) {
			if (getInputAO(i) == abstractLogicalOperator) {
				setPhysInputPO(i, source);
				// break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator#replaceInput
	 * (de.uniol.inf.is.odysseus.logicaloperator.base.ILogicalOperator,
	 * de.uniol.inf.is.odysseus.base.IPhysicalOperator)
	 */
	public void replaceInput(ILogicalOperator abstractLogicalOperator,
			IPhysicalOperator source) {
		for (int i = 0; i < getNumberOfInputs(); i++) {
			if (getInputAO(i) == abstractLogicalOperator) {
				setPhysInputPO(i, source);
				setInputAO(i, null);
				// break;
			}
		}
	}

	@Override
	public void addOwner(IOperatorOwner owner) {
		this.owner.add(owner);
	}

	@Override
	public void removeOwner(IOperatorOwner owner) {
		this.owner.remove(owner);
	}

	@Override
	public boolean isOwnedBy(IOperatorOwner owner) {
		return this.owner.contains(owner);
	}

	@Override
	public boolean hasOwner() {
		return this.owner.size() > 0;
	}

	@Override
	public List<IOperatorOwner> getOwner() {
		return this.owner;
	}

}
