package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractCSVHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.CSVProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SimpleCSVProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.FileHandler;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.util.Constants;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "CSVFileSource", category={LogicalOperatorCategory.SOURCE}, doc = "Allows to read input from a csv based file")
public class CSVFileSource extends AbstractAccessAO {

	private static final long serialVersionUID = -3200078517889579479L;
	boolean textDelimiterSet = false;

	public CSVFileSource() {
		super();
		setTransportHandler(FileHandler.NAME);
		setDataHandler(new TupleDataHandler().getSupportedDataTypes().get(0));
		setWrapper(Constants.GENERIC_PULL);
	}

	public CSVFileSource(CSVFileSource csvFileSource) {
		super(csvFileSource);
		textDelimiterSet = csvFileSource.textDelimiterSet;
	}


	@Parameter(type = StringParameter.class, name = "filename", optional = false)
	public void setFilename(String filename) {
		addOption(FileHandler.FILENAME,filename);
	}

	@Parameter(type = StringParameter.class, name = "delimiter", optional = true, doc = "Default delimiter is ';'")
	public void setDelimiter(String delimiter) {
		addOption(AbstractCSVHandler.CSV_DELIMITER, delimiter);
	}

	@Parameter(type = StringParameter.class, name = "textdelimiter", optional = true, doc = "Delimiter for Strings. No default.")
	public void setTextDelimiter(String textDelimiter) {
		addOption(AbstractCSVHandler.CSV_TEXT_DELIMITER, textDelimiter);
		textDelimiterSet = true;
	}

	@Parameter(type = BooleanParameter.class, name = "trim", optional = true, doc = "If set to true, for each element leading and trailing whitespaces are removed. Default false.")
	public void setTrim(boolean trim) {
		addOption(AbstractCSVHandler.CSV_TRIM, Boolean.toString(trim));
	}

	@Parameter(type = BooleanParameter.class, name = "readFirstLine", optional = true, doc = "If fist line contains header information, set to false. Default true.")
	public void setReadFirstLine(boolean readFirstLine) {
		addOption(AbstractCSVHandler.READFIRSTLINE, Boolean.toString(readFirstLine));
	}

	@Override
	public String getProtocolHandler() {
		if (textDelimiterSet == false){
			return SimpleCSVProtocolHandler.NAME;
		}else{
			return CSVProtocolHandler.NAME;
		}
	}

	
	@Override
	public AbstractAccessAO clone() {
		return new CSVFileSource(this);
	}
	
	@Override
	public boolean isValid() {
		if (getAttributes() == null || getAttributes().size() == 0){
			addError(new IllegalParameterException("Schema must be set!"));
			return false;
		}
		
		return super.isValid();
	}
	

}
