package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.internal.RegistryBinder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts=1, minInputPorts=1, name="GroupSplitFileWriter", doc="GroupSplitFileWriter", category={LogicalOperatorCategory.SINK})
public class GroupSplitFileWriterAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -1863186263887878541L;
	
	List<SDFAttribute> groupAttributes = new ArrayList<>();
	String path = "";
	String dataHandler = "";

	public GroupSplitFileWriterAO(GroupSplitFileWriterAO po) {
		super(po);
		this.groupAttributes = new ArrayList<>(po.groupAttributes);
		this.path = po.path;
		this.dataHandler = po.dataHandler;
	}

	public GroupSplitFileWriterAO() {
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new GroupSplitFileWriterAO(this);
	}

	@Parameter(type = StringParameter.class, name = "DataHandler", optional = false, possibleValues="getDataHandlerValues", doc = "The name of the datahandler to use, e.g. Tuple or Document.")
	public void setDataHandler(String dataHandler) {
		this.dataHandler = dataHandler;
	}
	
	public List<String> getDataHandlerValues() {
		return RegistryBinder.getDataHandlerRegistry().getStreamableDataHandlerNames();
	}
	
	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "groupAttributes", optional = false, doc = "", isList = true)
	public void setGroupAttributes(List<SDFAttribute> groupAttributes) {
		this.groupAttributes = groupAttributes;
	}
	
	public List<SDFAttribute> getGroupAttributes() {
		return groupAttributes;
	}
	
	public String getDataHandler() {
		return dataHandler;
	}
	
	@Parameter(type = StringParameter.class, name = "path", optional = false, doc = "Outputfolder")
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}
}
