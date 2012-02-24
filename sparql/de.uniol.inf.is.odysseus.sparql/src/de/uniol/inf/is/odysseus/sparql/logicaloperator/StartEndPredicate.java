///*
// * StartEndPredicate.java
// *
// * Created on 9. November 2007, 08:28
// *
// * To change this template, choose Tools | Template Manager
// * and open the template in the editor.
// */
//
//package de.uniol.inf.is.odysseus.core.server.sparql.logicaloperator;
//
//import com.hp.hpl.jena.graph.Triple;
//import com.hp.hpl.jenaUpdated.sparql.core.Var;
//import com.hp.hpl.jenaUpdated.sparql.engine.binding.Binding;
//import com.hp.hpl.jenaUpdated.sparql.engine.binding.BindingMap;
//import com.hp.hpl.jenaUpdated.sparql.expr.Expr;
//
//import de.uniol.inf.is.odysseus.core.server.queryexecution.po.base.object.IClone;
//
///**
// * This class represents a start/end predicate for a start/end predicate window.
// * 
// * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
// */
//public class StartEndPredicate implements IClone {
//
//	/**
//	 * This is the triple the specifies an RDF-statement
//	 */
//	private Triple triple;
//
//	/**
//	 * This is the filter expression of the filter that can be defined after the
//	 * triple definition of a start/end predicate
//	 */
//	private Expr filterExpr;
//
//	/** Creates a new instance of StartEndPredicate */
//	public StartEndPredicate() {
//	}
//
//	public StartEndPredicate(Triple t, Expr expr) {
//		this.triple = t;
//		this.filterExpr = expr;
//	}
//
//	private StartEndPredicate(StartEndPredicate original) {
//		this.triple = original.getTriple();
//		this.filterExpr = original.getFilter();
//	}
//
//	public StartEndPredicate clone() {
//		return new StartEndPredicate(this);
//	}
//
//	public Triple getTriple() {
//		return triple;
//	}
//
//	public void setTriple(Triple triple) {
//		this.triple = triple;
//	}
//
//	public Expr getFilter() {
//		return filterExpr;
//	}
//
//	public void setFilter(Expr filter) {
//		this.filterExpr = filter;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (!(obj instanceof StartEndPredicate)) {
//			return false;
//		}
//		StartEndPredicate sep = (StartEndPredicate) obj;
//		if (this.getFilter().equals(sep.getFilter())
//				&& this.triple.equals(sep.triple)) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	public boolean evaluate(Triple s) {
//		if ((this.triple.getSubject().isVariable() || this.triple.getSubject()
//				.sameValueAs(s.getSubject()))
//				&& (this.triple.getPredicate().isVariable() || this.triple
//						.getPredicate().sameValueAs(s.getPredicate()))
//				&& (this.triple.getObject().isVariable() || this.triple
//						.getObject().sameValueAs(s.getObject()))) {
//			if (this.filterExpr != null) {
//				// first create a binding from the triple
//				Binding b = new BindingMap();
//
//				// all the variable have to be added
//				// if isVariable() returns true, the node
//				// is a variable and can be casted, see sparql.jj and
//				// ParserBase.java
//				if (this.triple.getSubject().isVariable()) {
//					b.add((Var) this.triple.getSubject(), s.getSubject());
//				}
//				if (this.triple.getPredicate().isVariable()) {
//					b.add((Var) this.triple.getPredicate(), s.getPredicate());
//				}
//				if (this.triple.getObject().isVariable()) {
//					b.add((Var) this.triple.getObject(), s.getObject());
//				}
//				return this.filterExpr.isSatisfied(b, null);
//			} else {
//				return true;
//			}
//
//		}
//		return false;
//	}
//
//}
