/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;

/**
 * @author  Marco Grawunder
 */
public class SDFEntity extends SDFSchemaElement {
	
	private static final long serialVersionUID = -5891495267668181672L;	
    private SDFAttributeList  idAttributes = new SDFAttributeList();
    private SDFAttributeList attributes = new SDFAttributeList();
    private SDFConstantList constants = new SDFConstantList();
    
    private ArrayList<SDFEntity> subClassOf = new ArrayList<SDFEntity>();

	public SDFEntity(String URI) {
		super(URI);
	}

	public SDFEntity(SDFEntity sdfEntity) {
		super(sdfEntity);
		this.idAttributes.addAll(sdfEntity.idAttributes);
		this.attributes.addAll(sdfEntity.attributes);
		this.constants.addAll(sdfEntity.constants);
	}

	public boolean isIdentifiying(SDFAttribute attribute){
		return idAttributes.contains(attribute);
	}
	
	public SDFAttributeList getIdAttributes(){
		return idAttributes;
	}
	
	public void addAttribute(SDFAttribute attribute, boolean isIdentifying) {
		attributes.add(attribute);
		if (isIdentifying) idAttributes.add(attribute);
	}

	public SDFAttribute getAttribute(int index) {
		return (SDFAttribute) attributes.get(index);
	}
	
	public SDFAttributeList getAttributes() {
		return this.attributes;
	}

	public int attribizeSize() {
		return attributes.size();
	}

	public void addConstant(SDFConstant constant) {
		constants.add(constant);
	}

	public SDFConstant getConstant(int index) {
		return (SDFConstant) constants.get(index);
	}

	public int constantSize() {
		return constants.size();
	}

	public void addSuperclass(SDFEntity superclass) {
		subClassOf.add(superclass);
	}

	public SDFEntity getSuperclass(int index) {
		return (SDFEntity) subClassOf.get(index);
	}

	public int getNoOfSuperclasses() {
		return subClassOf.size();
	}
	
	public void setAttributes(SDFAttributeList attrs) {
		this.attributes = attrs;
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString() + "\n");
		for (int i = 0; i < getNoOfSuperclasses(); i++) {
			// Hier nicht die To-String der Oberklasse aufrufen, da dann
			// alle Attribute, Konstanten etc. mit ausgedruckt werden
			ret.append(" isA " + getSuperclass(i).getURI(true) + "\n");
		}
		ret.append("\t\t Attributes \n");
		for (SDFAttribute a: attributes) {
			ret.append("\t\t").append(a);
			if (isIdentifiying(a)) ret.append(" identifying ");
			ret.append("\n");
		}
		ret.append("\t\t Constants \n");
		for (SDFConstant c:constants) {
			ret.append("\t\t").append(c.toString()).append("\n");
		}
		return ret.toString();
	}

	@Override
	public SDFElement clone() {
		return new SDFEntity(this);
	}
}