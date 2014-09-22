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

public class Literal implements INode{

	private String name;
	
	private String language;
	private String datatype;
	
	public Literal(String name){
		this.name = name;
	}
	
	public Literal(){
	}
	
	@Override
	public boolean isVariable() {
		return false;
	}

	@Override
	public boolean isLiteral() {
		return true;
	}
	
	@Override
	public String getName(){
		return this.name;
	}
	
	@Override
	public void setName(String name){
		this.name = name;
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
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
