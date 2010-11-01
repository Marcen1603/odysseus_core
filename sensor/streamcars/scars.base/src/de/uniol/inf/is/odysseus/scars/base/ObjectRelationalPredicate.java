package de.uniol.inf.is.odysseus.scars.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public class ObjectRelationalPredicate extends AbstractPredicate<MVRelationalTuple<?>> implements IRelationalPredicate {

	private static final long serialVersionUID = 1222104352250883947L;

	protected SDFExpression expression;

	// stores which attributes are needed at which position for
	// variable bindings
	protected int[][] attributePaths;

	// fromRightChannel[i] stores if the getAttribute(attributePositions[i])
	// should be called on the left or on the right input tuple
	protected boolean[] fromRightChannel;

	private Map<SDFAttribute, SDFAttribute> replacementMap = new HashMap<SDFAttribute, SDFAttribute>();

	public ObjectRelationalPredicate(SDFObjectRelationalExpression expression) {
		this.expression = expression;
	}

	@Override
	public void init(SDFAttributeList leftSchema, SDFAttributeList rightSchema) {
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

	private int[] indexOf(SDFAttributeList schema, SDFAttribute attr) {
		SDFAttribute cqlAttr = getReplacement(attr);
		
		ArrayList<Integer> path = new ArrayList<Integer>();
		
		findAttribute(schema, cqlAttr, path);

		int[] p = new int[path.size()];
		for( int i = 0; i < path.size(); i++ ) 
			p[i] = path.get(i);
		return p;
	}
	
	private boolean findAttribute( SDFAttributeList list, SDFAttribute attr, ArrayList<Integer> path ) {
		for( int i = 0; i < list.size(); i++ ) {
			SDFAttribute a = list.get(i);
			path.add(i);
			if(a.getSourceName().equals(attr.getSourceName()) && a.getAttributeName().equals(attr.getAttributeName())) {
				return true;
			}
			else{
				boolean found = findAttribute(a.getSubattributes(), attr, path);
				if(!found){
					path.remove(path.size() - 1); // remove the last entry, because it is wrong
				}
				else{
					return true;
				}
			}
			// FIXME: Diese Methode muss rekursiv funktionieren.
			// Dazu ist jedoch die Kenntnis der OR-Umsetzung der PG
			// notwendig.
//			boolean found = findAttribute(a.getSubattributes(), attr, path);
//			if( found == true ) 
//				return true;
//			else {
//				path.remove((Integer)i);
//			}

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
	public boolean evaluate(MVRelationalTuple<?> input) {
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
	public boolean evaluate(MVRelationalTuple<?> left, MVRelationalTuple<?> right) {
		Object[] values = new Object[this.attributePaths.length];
		for (int i = 0; i < values.length; ++i) {
			MVRelationalTuple<?> r = fromRightChannel[i] ? right : left;
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
		else{
			return this.expression.equals(((ObjectRelationalPredicate)other).expression);
		}
	}
	
	@Override
	public int hashCode(){
		return 23 * this.expression.hashCode();
	}
	
	@Override
	public void updateAfterClone(Map<ILogicalOperator, ILogicalOperator> updated) {
		expression.updateAfterClone(updated);
	}

	@Override
	public void replaceAttribute(SDFAttribute curAttr, SDFAttribute newAttr) {
		replacementMap.put(curAttr, newAttr);
	}
}
