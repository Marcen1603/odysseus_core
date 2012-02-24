//package de.uniol.inf.is.odysseus.core.server.sparql.logicaloperator;
//
//import com.hp.hpl.jenaUpdated.sparql.expr.Expr;
//
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.AbstractLogicalOperator;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.SelectAO;
//import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFSchema;
//
//public class SparqlFilterAO extends SelectAO {
//
//	private Expr expr;
//
//	public SparqlFilterAO(SparqlFilterAO po){
//		super(po);
//		this.expr = po.expr;
//	}
//	
//	public SparqlFilterAO() {
//		super();
//	}
//
//	@Override
//	public SparqlFilterAO clone() {
//		return new SparqlFilterAO(this);
//	}
//
//	public void setExpression(Expr expr) {
//		this.expr = expr;
//	}
//	
//	
//	public void calcOutElements() {
//		AbstractLogicalOperator po = getInputAO();
//		this.setInputSchema(po.getOutputSchema());
//		
//		if (po != null){
//			SDFSchema l1 = po.getOutputSchema();		
//			SDFSchema jList = new SDFSchema();
//			jList.addAttributes(l1);
//			this.setOutputSchema(jList);
//		}
//		
//		// also calc out id size
//		this.calcOutIDSize();
//	}
//	
//	public void calcOutIDSize(){
//		this.setInputIDSize(this.getInputAO().getOutputIDSize());
//		this.setOutputIDSize(this.getInputIDSize());
//	}
//
//	public Expr getExpression() {
//		return expr;
//	}     
//
//        @Override
//        public String toString(){
//            return "Filter (" + this.expr.toString() + ")";
//        }
//}
