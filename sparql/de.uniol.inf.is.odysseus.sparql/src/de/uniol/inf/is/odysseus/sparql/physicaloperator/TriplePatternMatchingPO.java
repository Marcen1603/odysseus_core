/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.sparql.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
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
@SuppressWarnings({"rawtypes","unchecked"})
public class TriplePatternMatchingPO<M extends IMetaAttribute> extends AbstractPipe<Tuple<M>, Tuple<M>>{

	private Triple queryTriple;
	
	private Variable graphVar;
	
	private String stream_name;
	
	private IPredicate predicate;
		
	public TriplePatternMatchingPO(TriplePatternMatching tpm){
		this.queryTriple = tpm.getTriple();
		this.graphVar = tpm.getGraphVar();
		this.stream_name = tpm.getStream_name();
		this.predicate = tpm.getPredicate();
		this.setName(super.getName() + ": " + this.predicate.toString());
	}
	
	public TriplePatternMatchingPO(TriplePatternMatchingPO original){
		super(original);
		this.queryTriple = original.queryTriple;
		this.graphVar = original.graphVar;
		this.stream_name = original.stream_name;
		this.predicate = original.predicate;
	}
	
	@Override
	protected synchronized void process_next(Tuple<M> object, int port) {
		
		// first the object has to be transformed
		// things like "xyz"^^http://...#string must be
		// xyz	
		Tuple<M> preprocessed = preprocess(object);
		if (this.predicate.evaluate(preprocessed)) {
			this.transfer(this.transform(preprocessed));
		}

	}
	
	private Tuple<M> preprocess(Tuple<M> element){
		// first clone the element
		Tuple<M> newElem = element.clone();
		
//		if(!this.queryTriple.getSubject().isVariable()){
			newElem = preprocess(newElem, 0);
//		}
		
//		if(!this.queryTriple.getPredicate().isVariable()){
			newElem = preprocess(newElem, 1);
//		}
		
//		if(!this.queryTriple.getObject().isVariable()){
			newElem = preprocess(newElem, 2);
//		}
		
		return newElem;
	}
	
	@SuppressWarnings("static-method")
    private Tuple<M> preprocess(Tuple<M> element, int attrPos){
		// remove datatype information
		int hatPos = ((String)element.getAttribute(attrPos)).indexOf("^^");
		if(hatPos != -1){
			String modified = ((String)element.getAttribute(attrPos)).substring(0, hatPos-1);
			element.setAttribute(attrPos, modified);
		}
		
		// remove quotes
		String withoutQuotes = ((String)element.getAttribute(attrPos)).replace("\"", "");
		element.setAttribute(attrPos, withoutQuotes);

		return element;
	}
	
	private Tuple<M> transform(Tuple<M> element){
		
		Tuple<M> newTuple = new Tuple<M>(this.getOutputSchema().size(), false);
		Object[] attrs = new Object[this.getOutputSchema().size()];
		
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
		newTuple.setMetadata(element.getMetadata());
		
		return newTuple;
	}
	
	
	@Override
	public void process_close(){
	}
	
	@Override
	public void process_done(){
		System.out.println("TPM (" + this.hashCode() + ").processDone()");
		super.process_done();
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode.NEW_ELEMENT;
	}

	@Override
	protected boolean canHandleOutOfOrder() {
		return true;
	}

	
	@Override
	public AbstractPipe<Tuple<M>, Tuple<M>> clone() {
		return new TriplePatternMatchingPO(this);
	}
	
	@Override
	public String toString(){
		return "TriplePatternMatchingPO (" + this.hashCode() + "): " + this.predicate;
	}
}
