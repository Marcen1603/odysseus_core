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
package de.uniol.inf.is.odysseus.relational.base.predicate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.relational.base.Tuple;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleHelper;

public class ObjectRelationalPredicate extends AbstractPredicate<Tuple<?>> implements IRelationalPredicate {

	private static final long serialVersionUID = 1222104352250883947L;

	protected SDFExpression expression;

	// stores which attributes are needed at which position for
	// variable bindings
	protected int[][] attributePaths;

	// fromRightChannel[i] stores if the getAttribute(attributePositions[i])
	// should be called on the left or on the right input tuple
	protected boolean[] fromRightChannel;

	private Map<SDFAttribute, SDFAttribute> replacementMap = new HashMap<SDFAttribute, SDFAttribute>();

	public ObjectRelationalPredicate(SDFExpression expression) {
		this.expression = expression;
	}

	@Override
	public void init(SDFSchema leftSchema, SDFSchema rightSchema) {
		List<SDFAttribute> neededAttributes = expression.getAllAttributes();
		this.attributePaths = new int[neededAttributes.size()][];
		this.fromRightChannel = new boolean[neededAttributes.size()];

		int i = 0;
		for (SDFAttribute curAttribute : neededAttributes) {

			int[] pos = indexOf(leftSchema, curAttribute);
			if (pos == null || pos.length == 0) {
				if (rightSchema == null){
					throw new IllegalArgumentException("Attribute "+curAttribute+" not in "+leftSchema+" and rightSchema is null!");
				}
				// if you get here, there is an attribute
				// in the predicate that does not exist
				// in the left schema, so there must also be
				// a right schema
				pos = indexOf(rightSchema, curAttribute);
				if (pos == null){
					throw new IllegalArgumentException("Attribute "+curAttribute+" not in "+rightSchema);
				}
				this.fromRightChannel[i] = true;
			}
			this.attributePaths[i++] = pos;
		}
	}

	private int[] indexOf(SDFSchema schema, SDFAttribute attr) {
		SDFAttribute cqlAttr = getReplacement(attr);
		
		ArrayList<Integer> path = new ArrayList<Integer>();
		
		findAttribute(schema, cqlAttr, path);

		int[] p = new int[path.size()];
		for( int i = 0; i < path.size(); i++ ) 
			p[i] = path.get(i);
		return p;
	}
	
	private boolean findAttribute( SDFSchema list, SDFAttribute attr, ArrayList<Integer> path ) {
		for( int i = 0; i < list.size(); i++ ) {
			SDFAttribute a = list.get(i);
			path.add(i);
			if(a.getSourceName().equals(attr.getSourceName()) && a.getAttributeName().equals(attr.getAttributeName())) {
				return true;
			}
            boolean found = false;
            if(a.getDatatype().hasSchema()){
            	found = findAttribute(a.getDatatype().getSchema(), attr, path);
            }
            if(!found){
            	path.remove(path.size() - 1); // remove the last entry, because it is wrong
            }
            else{
            	return true;
            }
		}
		return false;
	}

	private SDFAttribute getReplacement(SDFAttribute a) {
		SDFAttribute ret = a;
		SDFAttribute tmp = null;
		while ((tmp=replacementMap.get(ret))!=null){
			ret = tmp;
		}
		return ret;
	}

	public ObjectRelationalPredicate(ObjectRelationalPredicate predicate) {
	    this.attributePaths = predicate.attributePaths == null ? null:(int[][])predicate.attributePaths.clone();
	    this.fromRightChannel = predicate.fromRightChannel == null ? null:(boolean[])predicate.fromRightChannel.clone();
		this.expression = predicate.expression == null ? null:predicate.expression.clone();
		this.replacementMap = new HashMap<SDFAttribute, SDFAttribute>(predicate.replacementMap);
	}

	@Override
	public boolean evaluate(Tuple<?> input) {
		Object[] values = new Object[this.attributePaths.length];
		TupleHelper th = new TupleHelper(input);
		for (int i = 0; i < values.length; ++i) {
//			values[i] = input.getORAttribute(this.attributePaths[i]);
			values[i] = th.getObject(this.attributePaths[i]);
		}
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	@Override
	public boolean evaluate(Tuple<?> left, Tuple<?> right) {
		Object[] values = new Object[this.attributePaths.length];
		for (int i = 0; i < values.length; ++i) {
			Tuple<?> r = fromRightChannel[i] ? right : left;
			TupleHelper th = new TupleHelper(r);
//			values[i] = r.getORAttribute(this.attributePaths[i]);
			values[i] = th.getObject(this.attributePaths[i]);
		}
		this.expression.bindVariables(values);
		return (Boolean) this.expression.getValue();
	}

	@Override
	public ObjectRelationalPredicate clone() {
		return new ObjectRelationalPredicate(this);
	}

	@Override
	public String toString() {
		return this.expression.toString();
	}

	@Override
	public List<SDFAttribute> getAttributes() {
		return Collections.unmodifiableList(this.expression.getAllAttributes());
	}
	
	@Override
	public boolean equals(Object other){
		if(!(other instanceof ObjectRelationalPredicate)){
			return false;
		}
        return this.expression.equals(((ObjectRelationalPredicate)other).expression);
	}
	
	@Override
	public int hashCode(){
		return 23 * this.expression.hashCode();
	}
	
	@Override
	public void replaceAttribute(SDFAttribute curAttr, SDFAttribute newAttr) {
		replacementMap.put(curAttr, newAttr);
	}
}
