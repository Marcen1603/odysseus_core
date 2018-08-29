package de.uniol.inf.is.odysseus.wrapper.ncsa_hdf.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.NoProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractFileHandler;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.FileNameParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.util.Constants;
import de.uniol.inf.is.odysseus.wrapper.ncsa_hdf.NcsaHDFTransportHandler;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "HDFSource", category={LogicalOperatorCategory.SOURCE}, doc = "Allows to read input from a nsca hdf(5) based file")
public class HDFSource extends AbstractAccessAO {

	private static final long serialVersionUID = 1169102243961150444L;

	public HDFSource(HDFSource po) {
		super(po);
	}

	public HDFSource() {
		setTransportHandler(NcsaHDFTransportHandler.NAME);
		setDataHandler(new TupleDataHandler().getSupportedDataTypes().get(0));
		setWrapper(Constants.GENERIC_PULL);
		setProtocolHandler(NoProtocolHandler.NAME);
	}

	public HDFSource(Resource name, String wrapper, String transportHandler,
			String protocolHandler, String dataHandler,
			OptionMap optionsMap) {
		super(name, wrapper, transportHandler, protocolHandler, dataHandler,
				optionsMap);
	}

	@Parameter(type = FileNameParameter.class, name = "filename", optional = false)
	public void setFilename(String filename) {
		addOption(AbstractFileHandler.FILENAME,filename);
	}
	
	@Parameter(type=StringParameter.class, name="paths", optional=false, isList = true)
	public void setPaths(List<String> paths){
		addOption(NcsaHDFTransportHandler.PATH, buildString(paths, ";"));
	}
	
	@Override
	public AbstractAccessAO clone() {
		return new HDFSource(this);
	}

}
