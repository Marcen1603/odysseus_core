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
package de.uniol.inf.is.odysseus.rdf.datamodel;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class Triple<T extends IMetaAttribute> extends Tuple<T>{

	private static final long serialVersionUID = 599012678300032084L;
	final static int subject = 0;
	final static int predicate = 1;
	final static int object = 2;
	
	public Triple(){
		super(3,false);
	}
	
	public Triple(INode subject, INode predicate, INode object){
		super(3,false);
		
		if(subject.getName() == null){
			throw new RuntimeException("No value for Subject in triple. Subject: " + Triple.subject);
		}
		if(predicate.getName() == null){
			throw new RuntimeException("No value for Predicate in triple. Predicate: " + Triple.predicate);
		}
		if(object.getName() == null && object.getValue() == null){
			throw new RuntimeException("No value for Object in triple. Object: " + Triple.object);
		}
		
		setAttribute(Triple.subject, subject);
		setAttribute(Triple.predicate, predicate);
		setAttribute(Triple.object, object);

	}

	public Triple(String subject, String predicate, String  object){
		this(new Literal(subject), new Literal(predicate), new Literal(object));
	}
		
	public Triple<T> replacePrefixes(Map<String, String> prefixes){
		INode newSubject = !getSubject().isVariable()?getSubject().replacePrefixes(prefixes):getSubject();
		INode newPredicate = !getPredicate().isVariable()?getPredicate().replacePrefixes(prefixes):getPredicate();
		INode newObject = !getObject().isVariable()?getObject().replacePrefixes(prefixes):getObject();
		return new Triple<T>(newSubject, newPredicate, newObject);
	}
	
	public INode getSubject() {
		return getAttribute(Triple.subject);
	}
	
	public INode getPredicate() {
		return getAttribute(Triple.predicate);
	}

	public INode getObject() {
		return getAttribute(Triple.object);
	}
	
	@Override
	public String toString() {
		return getSubject()+" "+getPredicate()+" "+getObject();
	}

	@Override
	public Tuple<T> clone() {
		return this; // No need to clone
	}

	@Override
	public AbstractStreamObject<T> newInstance() {
		return this; // immutable objects
	}

	public INode get(int i) {
		switch(i){
		case 0:
			return getSubject();
		case 1:
			return getPredicate();
		case 2:
			return getObject();
		default:
			throw new IllegalArgumentException("Position "+i+" not available in triples!");
		}
	}

}
