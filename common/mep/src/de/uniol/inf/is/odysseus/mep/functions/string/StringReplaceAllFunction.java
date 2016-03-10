package de.uniol.inf.is.odysseus.mep.functions.string;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class StringReplaceAllFunction extends AbstractFunction<String> {

	private static final long serialVersionUID = -5970067521376213129L;
	public static SDFDatatype[][] accTypes = new SDFDatatype[][]{
		{SDFDatatype.STRING},{SDFDatatype.STRING},{SDFDatatype.STRING}};
		
	
	public StringReplaceAllFunction(){
		super("replaceAll",3,accTypes, SDFDatatype.STRING);
	}


	@Override
	public String getValue() {
		String in = getInputValue(0);
		if (in != null){
			String toFind = getInputValue(1);
			String replaceWith = getInputValue(2);
			return in.replaceAll(toFind, replaceWith);
		}
		return null;
	}
}
