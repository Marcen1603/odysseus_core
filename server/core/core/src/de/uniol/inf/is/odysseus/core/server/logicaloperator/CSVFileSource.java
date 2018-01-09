package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.ConversionOptions;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractCSVHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.CSVProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.LineProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SimpleCSVProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.FileHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.FileNameParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.util.Constants;

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


	@Parameter(type = FileNameParameter.class, name = "filename", optional = false)
	public void setFilename(String filename) {
		addOption(FileHandler.FILENAME,filename);
	}
	
	public String getFilename() {
		return getOption(FileHandler.FILENAME);
	}
	
	@Override
	@Parameter(type = CreateSDFAttributeParameter.class, name = "Schema", isList = true, optional = false, doc = "The output schema.")
	public void setAttributes(List<SDFAttribute> attributes) {
		super.setAttributes(attributes);
	}


	@Parameter(type = StringParameter.class, name = "delimiter", optional = true, doc = "Default delimiter is ','")
	public void setDelimiter(String delimiter) {
		addOption(ConversionOptions.CSV_DELIMITER, delimiter);
	}
	
	public String getDelimiter() {
		return getOption(ConversionOptions.CSV_DELIMITER);
	}

	@Parameter(type = StringParameter.class, name = "textdelimiter", optional = true, doc = "Delimiter for Strings. No default.")
	public void setTextDelimiter(String textDelimiter) {
		addOption(ConversionOptions.CSV_TEXT_DELIMITER, textDelimiter);
		textDelimiterSet = true;
	}
	
	public String getTextDelimiter() {
		return getOption(ConversionOptions.CSV_TEXT_DELIMITER);
	}

	@Parameter(type = BooleanParameter.class, name = "trim", optional = true, doc = "If set to true, for each element leading and trailing whitespaces are removed. Default false.")
	public void setTrim(boolean trim) {
		addOption(AbstractCSVHandler.CSV_TRIM, Boolean.toString(trim));
	}
	
	public boolean isTrim() {
		return Boolean.valueOf(getOption(AbstractCSVHandler.CSV_TRIM));
	}

	@Parameter(type = BooleanParameter.class, name = "readFirstLine", optional = true, doc = "If fist line contains header information, set to false. Default true.")
	public void setReadFirstLine(boolean readFirstLine) {
		addOption(LineProtocolHandler.READFIRSTLINE, Boolean.toString(readFirstLine));
	}
	
	public boolean isReadFirstLine() {
		return Boolean.valueOf(getOption(LineProtocolHandler.READFIRSTLINE));
	}

	@Override
	public String getProtocolHandler() {
		if (textDelimiterSet == false){
			return SimpleCSVProtocolHandler.NAME;
		}
		return CSVProtocolHandler.NAME;
	}

	
	@Override
	public AbstractAccessAO clone() {
		return new CSVFileSource(this);
	}
	
	@Override
	public boolean isValid() {
		if (getAttributes() == null || getAttributes().size() == 0){
			addError("Schema must be set!");
			return false;
		}
		
		return super.isValid();
	}
	

}
