package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractCSVHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.CSVProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SimpleCSVProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.FileHandler;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.FileNameParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.Option;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.util.Constants;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "CSVFileSink", category={LogicalOperatorCategory.SINK}, doc = "Allows to write tp a csv based file")
public class CSVFileSink extends AbstractSenderAO {

	private static final long serialVersionUID = -1229693373253532412L;
	private boolean textDelimiterSet = false;

	public CSVFileSink(){
		super();
		setTransportHandler(FileHandler.NAME);
		setDataHandler(new TupleDataHandler().getSupportedDataTypes().get(0));
		setWrapper(Constants.GENERIC_PUSH);
	}
	
	public CSVFileSink(CSVFileSink other){
		super(other);
	}
	
	@Override
	public CSVFileSink clone() {
		return new CSVFileSink(this);
	}
	
	@Override
	@Parameter(type = OptionParameter.class, name = "options", optional = true, isList = true, doc = "Additional options.")
	public void setOptions(List<Option> value) {
		for (Option o:value){
			addOption(o.getName(), o.getValue());
		}
	}
	
	@Parameter(type = FileNameParameter.class, name = "filename", optional = false)
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
		textDelimiterSet  = true;
	}
	
	@Parameter(type = StringParameter.class, name = AbstractCSVHandler.CSV_FLOATING_FORMATTER, optional = true, doc = "Formatter for floating numbers.")
	public void setFloatingFormatter(String value) {
		addOption(AbstractCSVHandler.CSV_FLOATING_FORMATTER, value);
	}
	
	@Parameter(type = StringParameter.class, name = AbstractCSVHandler.CSV_NUMBER_FORMATTER, optional = true, doc = "Formatter for integer numbers.")
	public void setNumberFormatter(String value) {
		addOption(AbstractCSVHandler.CSV_NUMBER_FORMATTER, value);
	}
	
	@Parameter(type = BooleanParameter.class, name = AbstractCSVHandler.CSV_WRITE_METADATA, optional = true, doc = "Write metadata.")
	public void writeMetadata(Boolean value) {
		addOption(AbstractCSVHandler.CSV_WRITE_METADATA, Boolean.toString(value));
	}
		
	@Override
	public String getProtocolHandler() {
		if (textDelimiterSet){
			return CSVProtocolHandler.NAME;
		}else{
			return SimpleCSVProtocolHandler.NAME;
		}
	}
		
}
