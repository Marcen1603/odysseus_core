/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
