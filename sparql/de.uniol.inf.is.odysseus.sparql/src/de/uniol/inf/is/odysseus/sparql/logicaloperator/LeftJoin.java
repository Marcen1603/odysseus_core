///*
// * LeftJoin.java
// *
// * Created on 9. November 2007, 09:42
// *
// * To change this template, choose Tools | Template Manager
// * and open the template in the editor.
// */
//
//package de.uniol.inf.is.odysseus.sparql.logicaloperator;
//
//import com.hp.hpl.jenaUpdated.sparql.expr.Expr;
//
//import de.uniol.inf.is.odysseus.querytranslation.logicalops.sparql.NaturalJoinAO;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchemaElement;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import sun.reflect.generics.reflectiveObjects.NotImplementedException;
//
///**
// * This class represents a LeftJoin operator. A left join in SPARQL is a
// * natural join, with an extra list of predicates that can be specified
// * by filters that are in optional clauses.
// *
// * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
// */
//public class LeftJoin extends NaturalJoinAO{
//    
//    private List<Expr> filterExpressions;
//    
//    public LeftJoin(LeftJoin original){
//        super(original);
//        this.filterExpressions = original.getFilterExpressions();
//    }
//    
//    /**
//     * Creates a left join from a natural join.
//     * useful, if after creating a natural join
//     * there is an optional element
//     */
//    public LeftJoin(NaturalJoinAO nj){
//        super(nj);
//        super.setLeftJoin(true);
//        this.setNoOfInputs(nj.getNumberOfInputs());
//        this.filterExpressions = new ArrayList<Expr>();
//        // copy the inputs too;
//        for(int i = 0; i<nj.getNumberOfInputs(); i++){
//            this.setInputAO(i, nj.getInputAO(i));
//        }
//    }
//    
//    public LeftJoin(){
//        super();
//        super.setLeftJoin(true);
//        this.filterExpressions = new ArrayList<Expr>();
//    }
//    
//    /** Creates a new instance of LeftJoin */
//    public LeftJoin(List<Expr> filterExpr) {
//        super();
//        super.setLeftJoin(true);
//        this.setFilterExpressions(filterExpr);
//    }
//    
//    public void addFilterExpression(Expr ex){
//        this.filterExpressions.add(ex);
//    }
//    
//    public Expr removeFilterExpression(int i){
//        return this.filterExpressions.remove(i);
//    }
//    
//
//    public List<Expr> getFilterExpressions() {
//        return filterExpressions;
//    }
//
//    public void setFilterExpressions(List<Expr> filterExpressions) {
//        this.filterExpressions = filterExpressions;
//    }
//    
//    @Override
//    public String toString(){
//        String retval = "LeftJoin: Predicate: ";
//        for(int i = 0; i<this.filterExpressions.size(); i++){
//            retval += this.filterExpressions.get(i).toString();
//            if(i < this.filterExpressions.size() - 1){
//                retval += " AND ";
//            }
//        }
//        return retval;
//    }
//    
//	public void calcOutElements() {
////		calcPredicate();
//		SDFAttributeList l1 = getLeftInputSchema();
//		SDFAttributeList l2 = getRightInputSchema();
//
//		if (l1 != null && l2 != null) {
//
//			SDFAttributeList jList = new SDFAttributeList();
//			// Alle von links
//			jList.addAttributes(l1);
//			// erg�nzt um die fehlenden von rechts, die noch nicht links drin
//			// sind
//			for (SDFSchemaElement a : l2) {
//
//				if (!refersSameVar(jList, (SDFAttribute) a)) {
//					jList.add((SDFAttribute) a);
//				}
//			}
//			this.setOutputSchema(jList);
//		}
//		
//		// also calc out id size
//		this.calcOutIDSize();
//	}
//	
//	private void calcPredicate() {
//		throw new NotImplementedException();
//	}
//
//	public void calcOutIDSize(){
//		this.setInputIDSize(0, this.getLeftInput().getOutputIDSize());
//		this.setInputIDSize(1, this.getRightInput().getOutputIDSize());
//		this.setOutputIDSize(this.getInputIDSize(0) + this.getInputIDSize(1));
//	}
//
//	private boolean refersSameVar(SDFAttribute a, SDFAttribute b) {
//		return a.getQualName().equals(b.getQualName());
//	}
//
//	private boolean refersSameVar(SDFAttributeList list, SDFAttribute a) {
//		for (SDFSchemaElement e : list) {
//			if (refersSameVar((SDFAttribute) e, a)) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//    
//    
//}
