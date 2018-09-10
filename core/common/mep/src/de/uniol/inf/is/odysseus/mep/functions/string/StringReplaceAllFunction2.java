package de.uniol.inf.is.odysseus.mep.functions.string;

import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class StringReplaceAllFunction2 extends AbstractFunction<String> {

	private static final long serialVersionUID = -5970067521376213129L;
	public static SDFDatatype[][] accTypes = new SDFDatatype[][]{
		{SDFDatatype.STRING},{SDFDatatype.LIST_STRING},{SDFDatatype.LIST_STRING}};
		
	
	public StringReplaceAllFunction2(){
		super("replaceAll",3,accTypes, SDFDatatype.STRING);
	}


	@Override
	public String getValue() {
		String in = getInputValue(0);
		if (in != null){
			List<String> toFindList = getInputValue(1);
			List<String> replaceWithList = getInputValue(2);
			Iterator<String> iter = replaceWithList.iterator();
			String out=in;
			for (String toFind:toFindList){
				out = out.replaceAll(toFind, iter.next());
			}
			return out;
		}
		return null;
	}
}
	