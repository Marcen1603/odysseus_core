package de.uniol.inf.is.odysseus.incubation.graph.physicaloperator;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.incubation.graph.datamodel.IGraphDataStructure;
import de.uniol.inf.is.odysseus.incubation.graph.datatype.Graph;
import de.uniol.inf.is.odysseus.incubation.graph.logicaloperator.GraphBuilderAO;
import de.uniol.inf.is.odysseus.incubation.graph.provider.GraphDataStructureProvider;

public class GraphBuilderPO<M extends ITimeInterval> extends AbstractPipe<KeyValueObject<M>, Tuple<M>> {

	private GraphBuilderAO graphBuilderAo;
	private String actualDataStructure;
	
	public GraphBuilderPO(GraphBuilderAO graphBuilderAo) {
		super();
		this.graphBuilderAo = graphBuilderAo;
	}
	
	public GraphBuilderPO (GraphBuilderPO<M> graphBuilder) {
		super(graphBuilder);
		this.graphBuilderAo = graphBuilder.graphBuilderAo;
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}
	
	@Override
	public void processPunctuation(IPunctuation timestamp, int port) {
		sendPunctuation(timestamp);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(KeyValueObject<M> object, int port) {
		IGraphDataStructure<IMetaAttribute> structure;
		
		// Existiert bereits eine Datenstruktur für diesen Namen, wird die aktuelle Datenstruktur geklont, ansonsten wird eine neue Datenstruktur erstellt.
		if (this.actualDataStructure != null) {
			structure = GraphDataStructureProvider.getInstance().getGraphDataStructure(this.actualDataStructure).cloneDataStructure();
		} else {
			structure = GraphBuilderAO.dataStructureTypes.get(graphBuilderAo.getDataType()).newInstance(graphBuilderAo.getStructureName());
		}
		
		if (object.getAttribute("method").equals("+")) {
			structure.addDataSet((KeyValueObject<IMetaAttribute>) object);
		} else if (object.getAttribute("method").equals("-")) {
			structure.deleteDataSet((KeyValueObject<IMetaAttribute>) object);
		} else if (object.getAttribute("method").equals("c")) {
			structure.editDataSet((KeyValueObject<IMetaAttribute>) object);
		}
		
		String name = GraphDataStructureProvider.getInstance().addGraphDataStructure(structure, object.getMetadata().getStart());
		
		this.actualDataStructure = name;
		
		Tuple<M> newTuple = new Tuple<M>(1, false);
		Graph graph = new Graph(name);
		newTuple.setAttribute(0, graph);
		newTuple.setMetadata((M) object.getMetadata().clone());
	
		transfer(newTuple);
	}
}
