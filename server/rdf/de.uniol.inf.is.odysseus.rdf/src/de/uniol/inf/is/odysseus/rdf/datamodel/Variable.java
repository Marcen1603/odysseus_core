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

public class Variable extends AbstractNode implements INode {

	public Variable(String name) {
		super(name.startsWith("?") || name.startsWith("$") ? name.substring(1) : name);
	}
	
	@Override
	public boolean isVariable() {
		return true;
	}
	
	@Override
	protected INode cloneWithNewName(String newName) {
		return new Variable(newName);
	}

}