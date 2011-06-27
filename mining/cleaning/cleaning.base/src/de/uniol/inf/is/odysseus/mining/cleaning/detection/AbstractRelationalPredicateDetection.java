/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.mining.cleaning.detection;

import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.NoSuchAttributeException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/**
 * 
 * @author Dennis Geesen
 * Created at: 23.06.2011
 */
public abstract class AbstractRelationalPredicateDetection extends AbstractDetection {

	private RelationalPredicate predicate;
	
	public AbstractRelationalPredicateDetection(String attributeName, SDFAttributeList schema) {
		super(attributeName, schema);		
	}
	
	protected RelationalPredicate buildPredicate(String predicateString) {
		try {
			IAttributeResolver attributeResolver = new DirectAttributeResolver(super.getInputschema());			
			// build the predicate			
			SDFExpression expression = new SDFExpression("", predicateString, attributeResolver);
			predicate = new RelationalPredicate(expression);
			return predicate;
		} catch (NoSuchAttributeException ex) {
			System.err.println("Could not found the attribute \"" + ex.getAttribute() + "\" in schema ");
			throw ex;
		}
	}	
	
	public void init(){		
		String predicateString = createPredicate();
		buildPredicate(predicateString);
		this.predicate.init(getInputschema(), null);
	}
	
	public abstract String createPredicate();
	
	@Override
	public IPredicate<?> getPredicate() {		
		return predicate;
	}

}
