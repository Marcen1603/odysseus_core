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
package de.uniol.inf.is.odysseus.securitypunctuation.sp;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class PredicateSecurityPunctuation extends AbstractSecurityPunctuation {

	private static final long serialVersionUID = -6847322746263006915L;
	/**
	 * Gibt an, ob die Attribute der SP leer sind - die SP also keinerlei Zugriff gew�hrt.
	 */
	private Boolean isEmpty;
	
	public PredicateSecurityPunctuation(Object[] objects, SDFSchema schema) {
		super((long)objects[3]);
		this.setAttribute("ts", (Long) objects[3]);
		setSchema(schema);
		setAttribute("predicate", createPredicate((String) objects[0]));
		this.setAttribute("sign", (Integer) objects[1]);
		this.setAttribute("mutable", (Integer) objects[2]);
	}
	
	public PredicateSecurityPunctuation(PredicateSecurityPunctuation sp) {
		super(sp);
		setSchema(sp.getSchema());
		setAttribute("predicate", sp.getPredicateAttribute("predicate"));
		this.setAttribute("sign", sp.getIntegerAttribute("sign"));
		this.setAttribute("mutable", sp.getIntegerAttribute("mutable"));
		this.setAttribute("ts", sp.getIntegerAttribute("ts"));
	}
	
	@Override
	public PredicateSecurityPunctuation clone() {
		return new PredicateSecurityPunctuation(this);
	}
	
	@Override
	public Boolean evaluate(Long tupleTS, List<String> userRoles,
			Tuple<?> tuple, SDFSchema schema) {
		Tuple<IMetaAttribute> newTuple = new Tuple<IMetaAttribute>(2, false);
		newTuple.setAttribute(0, tupleTS);
		
		KeyValueObject<IMetaAttribute> additional = new KeyValueObject<IMetaAttribute>();
//		additional.setAttribute("userRoles", userRoles);
		additional.setAttribute("streamname", schema.getURI());
		// Alle Rollen als Attribut hinzuf�gen. Wenn Benutzer sie besitzt dann Wert true, sonst false...???
		for(String userRole:userRoles) {
			additional.setAttribute("has_" + userRole, true);
		}		
		return ((RelationalPredicate)getPredicateAttribute("predicate")).evaluate(newTuple, additional);
	}
	
	public IPredicate<?> createPredicate(String exprString) {	
	       SDFSchema schema = new SDFSchema("tupleToEvaluate", 
	                new SDFAttribute("", "ts", new SDFDatatype("Long")));
		RelationalPredicate pred = new RelationalPredicate(new SDFExpression(
				exprString,  MEP.getInstance()));

		pred.init(schema, null, false);
		return pred;
	}

	@Override
	public ISecurityPunctuation union(ISecurityPunctuation sp2) {
		return null;
	}
	@Override
	public ISecurityPunctuation intersect(ISecurityPunctuation sp2) {	
		return null;		
	}

	@Override
	public ISecurityPunctuation processSP(ISecurityPunctuation sp2) {
		return null;
	}
	
	public Boolean isEmpty() {
		if(isEmpty == null) {
			// isEmpty �berpr�fen!!!
		} 
		return false;
	}

	@Override
	public ISecurityPunctuation intersect(ISecurityPunctuation sp2, Long newTS) {
		return null;
	}
}