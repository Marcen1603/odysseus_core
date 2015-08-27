/*
 * TriplePatternMatching.java
 *
 * Created on 12. November 2007, 11:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.uniol.inf.is.odysseus.sparql.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.predicate.TruePredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.rdf.datamodel.Triple;
import de.uniol.inf.is.odysseus.rdf.datamodel.Variable;
import de.uniol.inf.is.odysseus.sparql.parser.helper.SPARQLDirectAttributeResolver;

/**
 * This operator repesents a triple pattern matching operator. This logical
 * operator differs only in one point from a basic triple selection operator. A
 * triple pattern matching operator can have graph variable to set. This means,
 * if a triple pattern matching operator is inside a GRAPH VAR clause, the
 * variable VAR has to be set to the name of the graph from which the sparql
 * solution has been taken.
 * 
 * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
 */
@SuppressWarnings({ "rawtypes"})
public class TriplePatternMatchingAO extends AbstractLogicalOperator implements IHasPredicate{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3371043653340988294L;

	private static int sourceNameCounter = 0;

	private Triple triple;
	private IPredicate<?> predicate;

	
	/**
	 * The variable to set in every sparql solution.
	 */
	private Variable graphVar;

	/**
	 * the value of graph variable, that has to be set in every sparql solution.
	 */
	private String stream_name;

	public Triple getTriple() {
		return triple;
	}

	/**
	 * the name of the virtual source. Each triple pattern must have a unique
	 * virtual source name, since e. g. two triple pattern over the same stream
	 * will result in a self join over this stream. While in CQL for this we
	 * have the "AS" clause in SPARQL we don't have such a clause. Therefore the
	 * virtual source names will be set automatically.
	 */
	private String sourceName;

	/**
	 * The restrict list defines which attributes of an input tuple will be in
	 * the output tuple.
	 */
	// private int[] restrictList;

	// public int[] getRestrictList() {
	// return restrictList;
	// }

	/**
	 * The is the select predicate for the physical operator. It will be
	 * generated as follows
	 * 
	 */
	private IPredicate selectionPredicate;

	public TriplePatternMatchingAO(Triple t) {
		super();
		this.triple = t;
	}

	private TriplePatternMatchingAO(TriplePatternMatchingAO tpm) {
		super(tpm);
		this.triple = tpm.triple;
		this.graphVar = tpm.getGraphVar();
		this.stream_name = tpm.getStream_name();
		this.selectionPredicate = tpm.selectionPredicate;
		// don't replace prefixes here
		// this must have been done in
		// original triple pattern matching
		if (tpm.predicate != null){
			this.predicate = tpm.predicate.clone();
		}
	}

	public TriplePatternMatchingAO(Triple t, Variable n, String stream_name) {
		super();
		this.triple = t;
		this.graphVar = n;
		this.stream_name = stream_name;
	}

	@Override
	public TriplePatternMatchingAO clone() {
		return new TriplePatternMatchingAO(this);
	}

	@Override
	public String toString() {
		String retval = super.toString();
		if (this.graphVar != null) {
			retval += "GRAPH " + this.graphVar + " = " + this.stream_name;
		}
		return retval;
	}

	private SDFSchema calcOutputSchema() {

		/**
		 * The outputschema of this operator is set to subject variable |
		 * predicate variable | object variable each only if the variable is
		 * available. e. g. a triple pattern ?x :pred ?y results in a schema
		 * |x|y|
		 */

		List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
		if (triple.getSubject().isVariable()) {
			SDFAttribute subject = new SDFAttribute(this.sourceName, triple
					.getSubject().getName(), SDFDatatype.STRING, null, null, null);
			attrs.add(subject);
		}
		if (triple.getPredicate().isVariable()) {
			SDFAttribute predicate = new SDFAttribute(this.sourceName, triple
					.getPredicate().getName(), SDFDatatype.STRING, null, null, null);
			attrs.add(predicate);
		}
		if (triple.getObject().isVariable()) {
			SDFAttribute object = new SDFAttribute(this.sourceName, triple
					.getObject().getName(), SDFDatatype.STRING, null, null, null);
			attrs.add(object);
		}
		// Wozu braucht man das?
		// if (getInputAO() != null && getInputSchema() != null){
		// l.addAll(getInputSchema());
		// }

		// adding the graphVar
		if (this.graphVar != null && this.stream_name != null) {
			boolean alreadyAdded = false;
			SDFAttribute graphVarAtt = new SDFAttribute(this.sourceName,
					this.graphVar.getName(), SDFDatatype.STRING, null, null, null);
			for (SDFAttribute a : attrs) {
				if (a.getQualName().equals(graphVarAtt.getQualName())) {
					alreadyAdded = true;
				}
			}
			if (!alreadyAdded) {
				attrs.add(graphVarAtt);
			}
		}
		
		setOutputSchema(SDFSchemaFactory.createNewWithAttributes(attrs,getInputSchema(0)));
		return getOutputSchema();
	}


	public void initPredicate() {
		SDFSchema inputSchema = this.getInputSchema(0);
		System.out
				.println("TriplePatternMatching.initPredicate: inputSchema = "
						+ inputSchema);

		IAttributeResolver attrRes = new SPARQLDirectAttributeResolver(
				inputSchema);
		ArrayList<SDFExpression> exprs = new ArrayList<SDFExpression>();

		if (!this.triple.getSubject().isVariable()) {
			String exprStr = inputSchema.getAttribute(0).getURI() + " == '"
					+ this.triple.getSubject().getName() + "'";
			SDFExpression expr = new SDFExpression(null, exprStr, attrRes,
					MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
			exprs.add(expr);
		}

		if (!this.triple.getPredicate().isVariable()) {
			String exprStr = inputSchema.getAttribute(1).getURI() + " == '"
					+ this.triple.getPredicate().getName() + "'";
			SDFExpression expr = new SDFExpression(null, exprStr, attrRes,
					MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
			exprs.add(expr);
		}

		if (!this.triple.getObject().isVariable()) {
			String exprStr = inputSchema.getAttribute(2).getURI() + " == '"
					+ this.triple.getObject().getName() + "'";
			SDFExpression expr = new SDFExpression(null, exprStr, attrRes,
					MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
			exprs.add(expr);
		}

		IPredicate pred = null;

		if (exprs.size() > 1) {
			RelationalPredicate firstPredicate = new RelationalPredicate(
					exprs.get(0));
			firstPredicate.init(inputSchema, null);

			IPredicate left = firstPredicate;
			for (int i = 1; i < exprs.size(); i++) {
				RelationalPredicate right = new RelationalPredicate(
						exprs.get(i));
				right.init(inputSchema, null);
				IPredicate tempAnd = ComplexPredicateHelper.createAndPredicate(
						left, right);
				left = tempAnd;
			}

			pred = left;
		}

		// there is only one predicate
		else if (exprs.size() == 1) {
			RelationalPredicate firstRelational = new RelationalPredicate(
					exprs.get(0));
			firstRelational.init(inputSchema, null);
			pred = firstRelational;
		}

		// there is no predicate
		else {
			pred = TruePredicate.getInstance();
		}

		this.selectionPredicate = pred;
	}

	@Override
	public IPredicate getPredicate() {
		return this.selectionPredicate;
	}

	// public void calcOutIDSize(){
	// // the input ao might be null
	// // because the method calcOutElements
	// // will already be called in the
	// // constructor of BasicTripleSelectionAO
	// if(this.getInputAO() != null){
	// this.setInputIDSize(this.getInputAO().getOutputIDSize());
	// this.setOutputIDSize(this.getInputIDSize());
	// }
	// }

	public Variable getGraphVar() {
		return graphVar;
	}

	public void setGraphVar(Variable graphVar) {
		this.graphVar = graphVar;
	}

	public String getStream_name() {
		return stream_name;
	}

	public void setStream_name(String stream_name) {
		this.stream_name = stream_name;
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		// the source name must be a unique artificial name, since
		// every triple pattern needs its own source name.
		this.sourceName = "s" + TriplePatternMatchingAO.sourceNameCounter++;
		this.calcOutputSchema();
		return getOutputSchema();
	}


}
