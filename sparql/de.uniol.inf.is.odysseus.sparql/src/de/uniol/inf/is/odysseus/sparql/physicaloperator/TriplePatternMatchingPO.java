package de.uniol.inf.is.odysseus.sparql.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sparql.logicaloperator.TriplePatternMatching;
import de.uniol.inf.is.odysseus.sparql.parser.helper.Triple;
import de.uniol.inf.is.odysseus.sparql.parser.helper.Variable;


/**
 * This triple pattern matching operator works as follows.
 * Each incoming tuple has three attributes subject, predicate, object.
 * Now the triple pattern matching operator checks for each attribute that
 * is not defined as variable, if the corresponding value is correct. So, for
 * a triple pattern ?x :myName ?y the attribute predicate will be checked
 * (predicate == :myName). If this is okay, a new tuple will be generated,
 * that only contains the attribes that are variable, so in the above case
 * only the attributes subject and object will be in the output tuple. However,
 * these attributes will be renamed to ?x and ?y.
 * 
 * After that there will be a check for a graph variable. If the triple pattern
 * is in a graph var term of the query an additional attribute for the graph
 * variable will be generated and filled with the name of the stream this triple
 * pattern stands for. Remember, each graph var term in a query will be generated
 * multiples time, one for each possible named stream. So there will be a triple
 * pattern matching for each named stream. This named stream is stored in the
 * triple pattern matching and used as value for the graph variable attribute.
 * 
 * In the following operators the graph variable attribute will also be checked
 * in the predicates (e. g. a join prediate).
 * 
 * @author André Bolles <andre.bolles@uni-oldenburg.de>
 * 
 */
public class TriplePatternMatchingPO<M extends IMetaAttribute> extends AbstractPipe<RelationalTuple<M>, RelationalTuple<M>>{

	private Triple queryTriple;
	
	private Variable graphVar;
	
	private String stream_name;
	
	private IPredicate predicate;
	
	private SDFAttributeList outputSchema;
	
	public TriplePatternMatchingPO(TriplePatternMatching tpm){
		this.queryTriple = tpm.getTriple();
		this.graphVar = tpm.getGraphVar();
		this.stream_name = tpm.getStream_name();
		this.predicate = tpm.getPredicate();
		this.outputSchema = tpm.getOutputSchema();
	}
	
	public TriplePatternMatchingPO(TriplePatternMatchingPO original){
		this.queryTriple = original.queryTriple;
		this.graphVar = original.graphVar;
		this.stream_name = original.stream_name;
		this.predicate = original.predicate;
		this.outputSchema = original.outputSchema;
	}
	
	protected synchronized void process_next(RelationalTuple<M> object, int port) {
		
		// TODO: Entfernen des Tests auf selectPredicate --> Restrukturierung!!
		if (this.predicate.evaluate(object)) {
			this.transfer(this.transform(object));
		}

	}
	
	
	private RelationalTuple<M> transform(RelationalTuple<M> element){
		
		RelationalTuple<M> newTuple = new RelationalTuple<M>(this.outputSchema.size());
		Object[] attrs = new Object[this.outputSchema.size()];
		
		int curI = 0;
		if(this.queryTriple.getSubject().isVariable()){
			attrs[curI++] = element.getAttribute(0);
		}
		
		if(this.queryTriple.getPredicate().isVariable()){
			attrs[curI++] = element.getAttribute(1);
		}
		
		if(this.queryTriple.getObject().isVariable()){
			attrs[curI++] = element.getAttribute(2);
		}
		
		if(this.graphVar != null){
			attrs[curI++] = this.stream_name;
		}
		
		newTuple.setAttributes(attrs);
		
		return newTuple;
	}
	
	
	public void process_close(){
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe.OutputMode.NEW_ELEMENT;
	}

	@Override
	public AbstractPipe<RelationalTuple<M>, RelationalTuple<M>> clone() {
		// TODO Auto-generated method stub
		return new TriplePatternMatchingPO(this);
	}
}
