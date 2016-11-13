package de.uniol.inf.is.odysseus.incubation.graph.logicaloperator;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.incubation.graph.datamodel.IGraphDataStructure;
import de.uniol.inf.is.odysseus.incubation.graph.provider.GraphDataStructureProvider;
import de.uniol.inf.is.odysseus.incubation.graph.sdf.schema.SDFGraphDatatype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

/**
 * Logical GraphBuilder operator.
 * 
 * @author Kristian Bruns
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name="GRAPHBUILDER", doc = "Converts a graph stream to an adjacency matrix", category={LogicalOperatorCategory.TRANSFORM})
public class GraphBuilderAO extends UnaryLogicalOp {
	// Static
	private static final long serialVersionUID = -8568425936637625420L;

	public static Map<String, IGraphDataStructure<IMetaAttribute>> dataStructureTypes = new HashMap<String, IGraphDataStructure<IMetaAttribute>>();
	
	public static void bindGraphDataStructure(IGraphDataStructure<IMetaAttribute> structure) {
		dataStructureTypes.put(structure.getType(), structure);
	}
	
	public static void unbindGraphDataStructure(IGraphDataStructure<IMetaAttribute> structure) {
		dataStructureTypes.remove(structure.getType());
	}
	
	//Non-Static
	
	private String datatype;
	private String structure_name;
	
	private IGraphDataStructure<IMetaAttribute> structure;
	
	public GraphBuilderAO() {
	}
	
	public GraphBuilderAO (GraphBuilderAO graphBuilder) {
		super(graphBuilder);
		this.structure = graphBuilder.structure;
		this.structure_name = graphBuilder.structure_name;
		this.datatype = graphBuilder.datatype;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new GraphBuilderAO(this);
	}
	
	@Parameter(type = StringParameter.class, name="STRUCTURENAME", optional=false, isList=false, doc="matrix name")
	public void setStructureName (String structure_name) {
		if (!(GraphDataStructureProvider.getInstance().structureNameExists(structure_name))) {
			this.structure_name = structure_name;
		} else {
			throw new QueryParseException("Data structure with this name already exists.");
		}
	}
	
	@Parameter(type = StringParameter.class, name="DATATYPE", optional=false, isList=false, doc="datatype to save graph in")
	public void setDataType (String type) {
		if (dataStructureTypes.containsKey(type)) {
			this.datatype = type;
		} else {
			throw new QueryParseException("Graph data structure with this name doesn't exist.");
		}
	}
	
	@GetParameter(name="DATATYPE")
	public String getDataType() {
		return this.datatype;
	}
	
	@GetParameter(name="STRUCTURENAME")
	public String getStructureName() {
		return this.structure_name;
	}
	
	public IGraphDataStructure<IMetaAttribute> getGraphDataStructure() {
		return this.structure;
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		List<SDFAttribute> attributeList = new ArrayList<SDFAttribute>();
		SDFAttribute attr = new SDFAttribute("graph", "graph", SDFGraphDatatype.GRAPH);
		attributeList.add(attr);
		
		@SuppressWarnings("unchecked")
		SDFSchema schema = SDFSchemaFactory.createNewSchema("graphstructure", (Class<? extends IStreamObject<?>>) Tuple.class, attributeList, getInputSchema());
		return schema;
	}
	
}
