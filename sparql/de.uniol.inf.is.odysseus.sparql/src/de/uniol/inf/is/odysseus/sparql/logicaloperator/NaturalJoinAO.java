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
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.AbstractLogicalOperator;
//import de.uniol.inf.is.odysseus.core.server.querytranslation.logicalops.JoinAO;
//import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFAttribute;
//import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFSchema;
//import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFSchemaElement;
//
//public class NaturalJoinAO extends JoinAO {
//
//	private boolean leftJoin;
//
//	public NaturalJoinAO() {
//		leftJoin = false;
//	}
//
//	// Needed, because SPARQL-Stream-LeftJoin extends this class
//	// an needs a special copy constructor
//	public NaturalJoinAO(NaturalJoinAO original) {
//		super(original);
//	}
//
//	public boolean isLeftJoin() {
//		return leftJoin;
//	}
//
//	public void setLeftJoin(boolean isLeftJoin) {
//		this.leftJoin = isLeftJoin;
//	}
//	
//	public void calcOutElements() {
//		AbstractLogicalOperator po1 = getLeftInput();
//		this.setInputSchema(0, po1.getOutputSchema());
//		
//		AbstractLogicalOperator po2 = getRightInput();
//		this.setInputSchema(1, po2.getOutputSchema());
//		
//		if (po1 != null && po2 != null) {
//			SDFSchema l1 = po1.getOutElements();
//			SDFSchema l2 = po2.getOutElements();
//
//			SDFSchema jList = new SDFSchema();
//			// Alle von links
//			jList.addAttributes(l1);
//			// ergaenzt um die fehlenden von rechts, die noch nicht links drin
//			// sind
//			for (SDFSchemaElement a : l2) {
//
//				if (!refersSameVar(jList, (SDFAttribute) a)) {
//					jList.add((SDFAttribute) a);
//				}
//			}
//			this.setOutputSchema(jList);
//		}
//		
//		// also calc out id size
//		this.calcOutIDSize();
//	}
//	
//	public void calcOutIDSize(){
//		this.setInputIDSize(0, this.getLeftInput().getOutputIDSize());
//		this.setInputIDSize(1, this.getRightInput().getOutputIDSize());
//		this.setOutputIDSize(this.getInputIDSize(0) + this.getInputIDSize(1));
//	}
//
//	private boolean refersSameVar(SDFAttribute a, SDFAttribute b) {
//		return a.getQualName().equals(b.getQualName());
//	}
//
//	private boolean refersSameVar(SDFSchema list, SDFAttribute a) {
//		for (SDFSchemaElement e : list) {
//			if (refersSameVar((SDFAttribute) e, a)) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//}
