package de.uniol.inf.is.odysseus.mep.functions.command;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.command.TargetedCommand;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public abstract class AbstractTargetedCommandFunction extends AbstractFunction<TargetedCommand<?>> 
{
	private static final long serialVersionUID = 4994770787017052258L;

	// Puts {STRING, LIST_STRING} in front of parameters
	private static SDFDatatype[][] generateParameters(SDFDatatype[][] additionalParameters)
	{
		if (additionalParameters == null) {
			return new SDFDatatype[][] {{SDFDatatype.OBJECT}};
		} else {
			SDFDatatype[][] parameters = new SDFDatatype[additionalParameters.length+1][];
			parameters[0] = new SDFDatatype[] {SDFDatatype.OBJECT};
			System.arraycopy(additionalParameters, 0, parameters, 1, additionalParameters.length);
			return parameters;
		}
	}
	
	public AbstractTargetedCommandFunction(String name, SDFDatatype[][] additionalParameters) {
		super(name, 1 + (additionalParameters == null ? 0 : additionalParameters.length), generateParameters(additionalParameters), SDFDatatype.COMMAND);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getTargets()
	{
		Object target = getInputValue(0);
		if (target instanceof List) return (List<Object>) target;
		
		List<Object> result = new ArrayList<>(1);
		result.add(target);
		return result;
	}
}