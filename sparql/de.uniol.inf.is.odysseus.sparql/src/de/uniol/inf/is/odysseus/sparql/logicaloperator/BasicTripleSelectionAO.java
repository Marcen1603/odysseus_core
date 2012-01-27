package de.uniol.inf.is.odysseus.sparql.logicaloperator;

import com.hp.hpl.jena.graph.Triple;

import de.uniol.inf.is.odysseus.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class BasicTripleSelectionAO extends SelectAO {

	private static final long serialVersionUID = -8187061597335204197L;
	protected Triple triple;
	
	public BasicTripleSelectionAO(Triple t) {
		this.triple = t;
		calcOutElements();
	}
	
	public void calcOutElements(){
		SDFAttributeList l = new SDFAttributeList("");
		if (triple.getSubject().isVariable()){
			l.add(new SDFAttribute(null,this.hashCode()+"#"+triple.getSubject().getName(), SDFDatatype.STRING));
		}
		if (triple.getPredicate().isVariable()){
			l.add(new SDFAttribute(null,this.hashCode()+"#"+triple.getPredicate().getName(), SDFDatatype.STRING));
		}
		if (triple.getObject().isVariable()){
			l.add(new SDFAttribute(null,this.hashCode()+"#"+triple.getObject().getName(), SDFDatatype.STRING));
		}
		if (getInputAO() != null && getInputSchema() != null){
			l.addAll(getInputSchema());
		}
//		this.setOutputSchema(l);
	}

//	@Override
//	public void setInputAO(AbstractLogicalOperator po) {
//		super.setInputAO(po);
//		calcOutElements();
//	}
	
	public BasicTripleSelectionAO(BasicTripleSelectionAO selection) {
		super();
		this.triple = selection.triple;
	}
	
	public Triple getTriple() {
		return triple;
	}

	public void setT(Triple t) {
		this.triple = t;
	}

	@Override
	public BasicTripleSelectionAO clone() {
		return new BasicTripleSelectionAO(this);
	}

        @Override
        public String toString(){
            return "BasicTripleSelection (" + this.triple.toString() + ")";
        }
}
