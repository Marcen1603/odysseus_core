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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author  Marco Grawunder
 */
public abstract class SDFPredicate extends SDFElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4940130236222593530L;
	public SDFPredicate(String URI) {
		super(URI);
	}
	
	public SDFPredicate(SDFPredicate other){
		super(other);
	}
	
	/**
	 * @uml.property  name="allAttributes"
	 * @uml.associationEnd  readOnly="true"
	 */
	public abstract SDFAttributeList getAllAttributes();
	public abstract SDFAttributeList getAllAttributesWithCompareOperator(SDFCompareOperator op);
	public abstract void getAllPredicatesWithCompareOperator(SDFCompareOperator op, List<SDFSimplePredicate> resultList);
	public abstract boolean evaluate(Map<SDFAttribute, Object> attributeAssignment);

	public abstract void getXMLRepresentation(String indent, StringBuffer xmlRetValue);
	public abstract String toSQL();
	public abstract boolean isNegatived();
	public abstract void negate();
	
	public abstract SDFPredicate clone();
}