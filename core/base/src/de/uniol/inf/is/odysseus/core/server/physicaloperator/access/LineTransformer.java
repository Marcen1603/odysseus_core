package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;


public class LineTransformer extends AbstractTransformer<String, String[]> implements IToStringArrayTransformer<String> {

	@Override
	public ITransformer<String, String[]> getInstance(
			Map<String, String> options, SDFSchema schema) {
		return new LineTransformer();
	}
	
	@Override
	public String[] transform(String input) {
		String[] splittedLine;
		splittedLine = new String[1];
		splittedLine[0] = input;
		return splittedLine;
	}
	
	@Override
	public String getName() {
		return "LineToString";
	}

}
