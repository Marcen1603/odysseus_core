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

public class Variable implements INode{

	private String name;
	
	public Variable(String name){
		// remove ? and $ variable names
		if(name.startsWith("?") || name.startsWith("$")){
			this.name = name.substring(1);
		}
		else{
			this.name = name;
		}
	}
	
	@Override
	public boolean isVariable() {
		return true;
	}

	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		if(name.startsWith("?") || name.startsWith("$")){
			this.name = name.substring(1);
		}
		else{
			this.name = name;
		}
	}

	@Override
	public boolean isIRI() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBlankNode() {
		// TODO Auto-generated method stub
		return false;
	}

}
