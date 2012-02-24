//package de.uniol.inf.is.odysseus.core.server.sparql.logicaloperator;
//
//import java.util.List;
//
//import com.hp.hpl.jena.graph.Node_Variable;
//
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.AccessAO;
//import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFAttribute;
//import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFSchema;
//
//public class RDFDatasetAO extends AccessAO {
//
//	private Node_Variable sourceVar;
//	private List<String> possSources;
//	
//	
//	public RDFDatasetAO(RDFDatasetAO datasetPO) {
//		super(datasetPO);
//		this.sourceVar = datasetPO.sourceVar;
//		this.possSources = datasetPO.possSources; 
//	}
//
//	
//	public RDFDatasetAO() {
//		super();
//	}
//
//
//	@Override
//	public RDFDatasetAO clone() {
//		return new RDFDatasetAO(this);
//	}
//
//
//	public Node_Variable getSourceVar() {
//		return sourceVar;		
//	}
//
//
//	public void setSourceVar(Node_Variable sourceVar, List<String> possSources) {
//		this.sourceVar = sourceVar;
//		this.possSources = possSources;
//		calcOutputVars();
//	}
//	
//	private void calcOutputVars(){
//		if (sourceVar != null){
//			SDFSchema out = new SDFSchema();
//			out.add(new SDFAttribute(sourceVar.getName()));
//			this.setOutputSchema(out);
//		}
//	}
//
//
//	public List<String> getPossSources() {
//		return possSources;
//	}
//
//}
