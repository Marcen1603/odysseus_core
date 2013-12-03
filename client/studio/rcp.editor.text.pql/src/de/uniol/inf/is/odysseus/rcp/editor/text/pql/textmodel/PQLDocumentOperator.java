/********************************************************************************** 
  * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.rcp.editor.text.pql.textmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dennis Geesen
 *
 */
public class PQLDocumentOperator{

	private List<PQLDocumentOperator> childs = new ArrayList<>();
	private List<PQLDocumentAttribute> attributes = new ArrayList<>();
	private String name;	

	public PQLDocumentOperator(String name) {		
		this.setName(name);		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PQLDocumentOperator> getChilds() {
		return childs;
	}

	public void addChild(PQLDocumentOperator child){
		this.childs.add(child);
	}
	
	public List<PQLDocumentAttribute> getAttributes() {
		return attributes;
	}

	public void addAttribute(PQLDocumentAttribute attribute) {
		this.attributes.add(attribute);
	}



}
