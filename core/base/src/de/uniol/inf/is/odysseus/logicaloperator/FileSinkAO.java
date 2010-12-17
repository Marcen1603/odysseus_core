package de.uniol.inf.is.odysseus.logicaloperator;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FileSinkAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -5468128562016704956L;
	final String filename;

	public FileSinkAO(String filename) {
		this.filename = filename;
	}

	public FileSinkAO(FileSinkAO fileSinkAO) {
		super(fileSinkAO);
		this.filename = fileSinkAO.filename;
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return null;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new FileSinkAO(this);
	}

	public String getFilename() {
		return filename;
	}
	


}
