package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;


public class CSVTransformer extends LineTransformer {

	private String delimitter;

	public CSVTransformer(String delimitter) {
		this.delimitter = delimitter;
	}
	
	public CSVTransformer() {
	}
	
	@Override
	public ITransformer<String, String[]> getInstance(
			Map<String, String> options, SDFSchema schema) {
		return new CSVTransformer(options.get("delimitter"));
	}
	

	@Override
	public String[] transform(String line) {
		String[] splittedLine;
		splittedLine = line.split(delimitter);
		return splittedLine;
	}

	@Override
	public String getName() {
		return "CSV2String";
	}
	
}
