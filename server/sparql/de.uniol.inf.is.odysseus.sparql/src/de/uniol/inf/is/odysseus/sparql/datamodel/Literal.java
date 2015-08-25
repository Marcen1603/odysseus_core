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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class Literal extends AbstractNode implements INode {

	private String language;
	private SDFDatatype datatype;

	public Literal(String name, String language, SDFDatatype datatype) {
		super(name);
		this.language = language;
		this.datatype = datatype;
	}

	public Literal(String name) {
		super(name);
	}

	public void setDatatype(SDFDatatype datatype) {
		if (this.datatype != null) {
			this.datatype = datatype;
		} else {
			throw new IllegalArgumentException("Cannot change value of datatype");
		}
	}

	public void setLanguage(String language) {
		if (this.language != null) {
			this.language = language;
		} else {
			throw new IllegalArgumentException("Cannot change value of language");
		}
	}

	@Override
	protected INode cloneWithNewName(String newName) {
		return new Literal(newName, language, datatype);
	}

	@Override
	public boolean isLiteral() {
		return true;
	}

	public String getLanguage() {
		return language;
	}

	public SDFDatatype getDatatype() {
		return datatype;
	}

}
