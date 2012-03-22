package de.uniol.inf.is.odysseus.core.server.physicaloperator.access.pull;

public class LineTransformer extends AbstractTransformer<String, String[]> {

	@Override
	public String[] transform(String input) {
		String[] splittedLine;
		splittedLine = new String[1];
		splittedLine[0] = input;
		return splittedLine;
	}

}
