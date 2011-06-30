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
package de.uniol.inf.is.odysseus.objecttracking.util;

import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.IRangePredicate;
import de.uniol.inf.is.odysseus.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

@SuppressWarnings("unchecked")
public class ObjectTrackingPredicateInitializer {

	public static void visitPredicates(Map<IPredicate, IRangePredicate> predicates,
			ILogicalOperator operator) {
		
		for(Entry<IPredicate, IRangePredicate> entry: predicates.entrySet()){
			visitPredicates(entry.getKey(), operator.getInputSchema(0), operator.getInputSchema(1));
			entry.getValue().init(operator.getInputSchema(0), operator.getInputSchema(1));	
		}
	}
	
	public static void visitPredicates(IPredicate predicate, SDFAttributeList leftSchema, SDFAttributeList rightSchema){
		Stack<IPredicate> predicateStack = new Stack<IPredicate>();
		predicateStack.push(predicate);
		
		while (!predicateStack.isEmpty()) {
			IPredicate curPred = predicateStack.pop();
			if (curPred instanceof ComplexPredicate<?>) {
				predicateStack.push(((ComplexPredicate) curPred).getLeft());
				predicateStack.push(((ComplexPredicate) curPred).getRight());
			} else if(ComplexPredicateHelper.isNotPredicate(curPred)){
				predicateStack.push(ComplexPredicateHelper.getChild(curPred));
			}
			else {
				init(curPred, leftSchema, rightSchema);
			}
		}
	}
	
	private static void init(IPredicate predicate, SDFAttributeList leftSchema, SDFAttributeList rightSchema){
		if(predicate instanceof IRelationalPredicate) {
			// We can always init with the left and right input schema.
			// There are two cases:
			// 1. The relational predicate is used as a key to get the correct
			//    prediction function. In this case, if the predicate only
			//    uses attributes from the left. The field fromRightChannel
			//    will be false for each attribute. If it uses only attributes
			//    from the right fromRightChannel will be true, for each predicate.
			// 2. The relational predicate is used as a condition predicate
			//    for a maple solution. In this case, if the predicate
			//    only uses attributes from the left, the field fromRightChannel
			//    will be false for every attribute. If the predicate only uses
			//    attributes from the right fromRightChannel will be true for
			//    every attribute.
			((IRelationalPredicate)predicate).init(leftSchema, rightSchema);				
		}
	}
	
}
