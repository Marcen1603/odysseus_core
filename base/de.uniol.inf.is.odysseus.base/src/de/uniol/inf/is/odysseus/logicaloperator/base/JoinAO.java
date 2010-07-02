/*
 * Created on 07.12.2004
 *
 */
package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Marco Grawunder
 * 
 */
public class JoinAO extends BinaryLogicalOp {

	private static final long serialVersionUID = 3710951139395164614L;
	protected SDFAttributeList outputSchema = null;
	
	public JoinAO() {
		super();
	}

	public JoinAO(IPredicate<?> joinPredicate) {
		super();
		this.setPredicate(joinPredicate);
	}

	public JoinAO(JoinAO joinPO) {
		super(joinPO);
		// the outputSchema is only not null
		// if method getOuputSchema has already been
		// called
		if(joinPO.outputSchema != null){
			this.outputSchema = joinPO.outputSchema.clone();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public synchronized void setPredicate(IPredicate joinPredicate) {
		super.setPredicate(joinPredicate);
	}

	public @Override
	JoinAO clone() {
		return new JoinAO(this);
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString());
		ret.append(" Predicate " + getPredicate());
		return ret.toString();
	}

	@Override
	public synchronized SDFAttributeList getOutputSchema() {
		// The Sum of all InputSchema
		if (outputSchema == null || recalcOutputSchemata){
			outputSchema = new SDFAttributeList();
			for (LogicalSubscription l:getSubscribedToSource()){
				outputSchema.addAttributes(l.getSchema());
			}
			recalcOutputSchemata = false;
		}
		return outputSchema;
	}
	
}
