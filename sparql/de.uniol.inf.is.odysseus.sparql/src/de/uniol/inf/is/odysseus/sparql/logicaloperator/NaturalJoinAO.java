//package de.uniol.inf.is.odysseus.sparql.logicaloperator;
//
//import de.uniol.inf.is.odysseus.querytranslation.logicalops.AbstractLogicalOperator;
//import de.uniol.inf.is.odysseus.querytranslation.logicalops.JoinAO;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchemaElement;
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
//			SDFAttributeList l1 = po1.getOutElements();
//			SDFAttributeList l2 = po2.getOutElements();
//
//			SDFAttributeList jList = new SDFAttributeList();
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
//	private boolean refersSameVar(SDFAttributeList list, SDFAttribute a) {
//		for (SDFSchemaElement e : list) {
//			if (refersSameVar((SDFAttribute) e, a)) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//}
