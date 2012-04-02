package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;


public class CSVTransformer extends LineTransformer {

	final private String separator;

	public CSVTransformer(String separator) {
		this.separator = separator;
	}

	@Override
	public String[] transform(String line) {
		String[] splittedLine;
		splittedLine = line.split(separator);
		return splittedLine;
	}

}
