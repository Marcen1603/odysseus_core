package de.uniol.inf.is.odysseus.mep.functions.tuple;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepVariable;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

abstract public class AbstractToTupleFunction extends AbstractFunction<Tuple<IMetaAttribute>> {
	
	private static final long serialVersionUID = -2571631849135423566L;
	
	public AbstractToTupleFunction(int size){
		super("toTuple",size, null, SDFDatatype.TUPLE);
		
	}

	@Override
	public Tuple<IMetaAttribute> getValue() {
		Tuple<IMetaAttribute> ret = new Tuple<>(getArity(), false);
		for (int i=0;i<getArity();i++){
			ret.setAttribute(i, getInputValue(i));
		}
		return ret;
	}
	
	 @Override
    public boolean determineTypeFromInput() {
    	return true;
    }
	
	@Override
	public SDFDatatype determineType(IMepExpression<?>[] args) {
		List<SDFAttribute> attr = new ArrayList<>();
		int counter = 0;
		for (IMepExpression<?> arg : args) {
			SDFDatatype retType = arg.getReturnType();
			String name;
			if(arg.isVariable()) {
				name = ((IMepVariable) arg).getIdentifier();
			} else {
				name = "x" + counter++;
			}
			attr.add(new SDFAttribute("", name, retType));
		}
		
		SDFSchema subschema = SDFSchemaFactory.createNewSchema("", Tuple.class, attr);
		SDFDatatype type = SDFDatatype.createTypeWithSubSchema(SDFDatatype.TUPLE, subschema);
		
		return type;
	}

}
