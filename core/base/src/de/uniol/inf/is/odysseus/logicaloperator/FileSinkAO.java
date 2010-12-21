package de.uniol.inf.is.odysseus.logicaloperator;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class FileSinkAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -5468128562016704956L;
	final String filename;
	final String sinkType;
	final long writeAfterElements;
	
	public FileSinkAO(String filename, String sinkType, long writeAfterElements) {
		this.filename = filename;
		this.sinkType = sinkType;
		this.writeAfterElements = writeAfterElements;
	}

	public FileSinkAO(FileSinkAO fileSinkAO) {
		super(fileSinkAO);
		this.filename = fileSinkAO.filename;
		this.sinkType = fileSinkAO.sinkType;
		this.writeAfterElements = fileSinkAO.writeAfterElements;
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

	public String getSinkType() {
		return sinkType;
	}
	
	public long getWriteAfterElements() {
		return writeAfterElements;
	}


}
