package de.uniol.inf.is.odysseus.wrapper.opcda.logicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.NoProtocolHandler;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IllegalParameterException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.wrapper.opcda.physicaloperator.access.OPCDATransportHandler;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "OPCDASource", category={LogicalOperatorCategory.SOURCE}, doc = "Allows to read input from a OPC-DA connections.")
public class OPCDASource extends AbstractAccessAO {

	private static final long serialVersionUID = 1366670017348220400L;
	private List<String> paths;

	public OPCDASource(AbstractLogicalOperator po) {
		super(po);
	}

	public OPCDASource() {
		setTransportHandler(OPCDATransportHandler.NAME);
		setDataHandler(new TupleDataHandler().getSupportedDataTypes().get(0));
		setWrapper(Constants.GENERIC_PUSH);
		setProtocolHandler(NoProtocolHandler.NAME);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new OPCDASource(this);
	}

	@Parameter(type = StringParameter.class, name = OPCDATransportHandler.HOST, optional = false)
	public void setHost(String host) {
		addOption(OPCDATransportHandler.HOST,host);
	}
	
	@Parameter(type = StringParameter.class, name = OPCDATransportHandler.DOMAIN, optional = false)
	public void setDomain(String domain) {
		addOption(OPCDATransportHandler.DOMAIN,domain);
	}

	@Parameter(type = StringParameter.class, name = OPCDATransportHandler.USERNAME, optional = false)
	public void setUsername(String username) {
		addOption(OPCDATransportHandler.USERNAME, username);
	}

	@Parameter(type = StringParameter.class, name = OPCDATransportHandler.PASSWORD, optional = false)
	public void setPassword(String username) {
		addOption(OPCDATransportHandler.PASSWORD, username);
	}
	
	@Parameter(type = StringParameter.class, name = OPCDATransportHandler.PROG_ID, optional = false)
	public void setProgId(String progId) {
		addOption(OPCDATransportHandler.PROG_ID, progId);
	}
	
	@Parameter(type = StringParameter.class, name = OPCDATransportHandler.CLS_ID, optional = false)
	public void setClsId(String clsId) {
		addOption(OPCDATransportHandler.CLS_ID, clsId);
	}
	
	@Parameter(type=StringParameter.class, name="paths", optional=false, isList = true)
	public void setPaths(List<String> paths){
		addOption(OPCDATransportHandler.PATH, buildString(paths, ";"));
		this.paths = paths;
	}
	
	@Override
	public boolean isValid() {
		boolean valid = super.isValid();
		if (paths.size() != getOutputSchema().size()){
			valid = false;
			addError(new IllegalParameterException("number of elements in path must be the same as in output schema"));
		}
		
		return valid;
	}
	
	
}
