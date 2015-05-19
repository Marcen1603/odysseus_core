package de.uniol.inf.is.odysseus.relational.base.predicate;

import java.util.LinkedList;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.expression.VarHelper;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.relational.IProvidesMaxHistoryElements;

public class RelationalStateExpression<T extends IMetaAttribute> extends RelationalExpression<T> implements IProvidesMaxHistoryElements{

	private static final long serialVersionUID = -6372795234437022273L;
	private int maxHistoryElements = 0;
	
	public RelationalStateExpression(SDFExpression expression) {
		super(expression);
	}

	@Override
	public VarHelper initAttribute(SDFSchema schema, SDFAttribute curAttribute) {
		if (curAttribute.getNumber() > 0) {
			int pos = curAttribute.getNumber();
			if (pos > maxHistoryElements) {
				maxHistoryElements = pos + 1;
			}
			int index = schema.indexOf(curAttribute);
			if (index >= 0) {
				return new VarHelper(index, pos);
			} else { // Attribute is (potentially) part of meta data;
				Pair<Integer, Integer> posi = schema.indexOfMetaAttribute(curAttribute);
				if (posi != null){
					return new VarHelper(posi.getE1(), posi.getE2(), pos);
				}
			}
			return new VarHelper(index, pos);
		} else {
			return super.initAttribute(schema, curAttribute);
		}
	}
	
	@Override
	public Tuple<T> determineObjectForExpression(Tuple<T> object,
			LinkedList<Tuple<T>> lastObjects, int j) {
		Tuple<T> obj = null;
		if (lastObjects.size() > this.variables[j].getObjectPosToUse()) {
			obj = lastObjects.get(this.variables[j].getObjectPosToUse());
		}
		return obj;
	}
	
	@Override
	public int getMaxHistoryElements() {
		return maxHistoryElements;
	}
	
}
