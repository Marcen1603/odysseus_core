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
package de.uniol.inf.is.odysseus.sparql.parser.helper;

public class Triple {
	private INode subject;
	private INode predicate;
	private INode object;
	
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
			throw new RuntimeException("No name for Subject in triple. Subject: " + this.subject);
		}
		if(predicate.getName() == null){
			throw new RuntimeException("No name for Predicate in triple. Predicate: " + this.predicate);
		}
		if(object.getName() == null){
			throw new RuntimeException("No name for Object in triple. Object: " + this.object);
		}
	}
	
	
	
	public INode getSubject() {
		return subject;
	}

	public void setSubject(INode subject) {
		this.subject = subject;
	}

	public INode getPredicate() {
		return predicate;
	}

	public void setPredicate(INode predicate) {
		this.predicate = predicate;
	}

	public INode getObject() {
		return object;
	}

	public void setObject(INode object) {
		this.object = object;
	}

}
