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
package de.uniol.inf.is.odysseus.sparql.datamodel;

import java.util.Map;

public class Triple {
	final private INode subject;
	final private INode predicate;
	final private INode object;
	
	public Triple(){
		this.subject = null;
		this.object = null;
		this.predicate = null;
	}
	
	public Triple(INode subject, INode predicte, INode object){
		this.subject = subject;
		this.predicate = predicte;
		this.object = object;
		
		if(subject.getName() == null){
			throw new RuntimeException("No value for Subject in triple. Subject: " + this.subject);
		}
		if(predicate.getName() == null){
			throw new RuntimeException("No value for Predicate in triple. Predicate: " + this.predicate);
		}
		if(object.getName() == null){
			throw new RuntimeException("No value for Object in triple. Object: " + this.object);
		}
	}
	
	public Triple replacePrefixes(Map<String, String> prefixes){
		INode newSubject = subject.isVariable()?subject.replacePrefixes(prefixes):subject;
		INode newPredicate = predicate.isVariable()?predicate.replacePrefixes(prefixes):predicate;
		INode newObject = object.isVariable()?object.replacePrefixes(prefixes):object;
		return new Triple(newSubject, newPredicate, newObject);
	}
	
	public INode getSubject() {
		return subject;
	}
	
	public INode getPredicate() {
		return predicate;
	}

	public INode getObject() {
		return object;
	}
	
	@Override
	public String toString() {
		return subject+" "+predicate+" "+object;
	}

}
