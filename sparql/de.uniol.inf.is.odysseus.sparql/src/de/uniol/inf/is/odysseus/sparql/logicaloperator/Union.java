//package de.uniol.inf.is.odysseus.sparql.logicaloperator;
//
//import de.uniol.inf.is.odysseus.querytranslation.logicalops.UnionAO;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
//import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchemaElement;
//
///**
// * This is a union operator for sparql solutions. This operator
// * overrides the method calcOutElements of UnionAO. The out elements
// * will a list of attributes containing the attributes from left input operator
// * followed by the attributes of the left input operator.
// * 
// * Sparql Solutions that do not own all the attributes will
// * get a special value into the attributes that do not belong to them.
// * 
// * @author Andre Bolles <andre.bolles@informatik.uni-oldenburg.de>
// *
// */
//public class Union extends UnionAO{
//	
////	private SDFAttributeList leftAttrs;
////	private SDFAttributeList rightAttrs;
//
//	public Union(){
//		super();
//	}
//	
//	public Union(UnionAO original){
//		super(original);
//	}
//	
//	@Override
//	public Union clone(){
//		return new Union(this);
//	}
//	
//	/**
//	 * This method calcs the out attributes
//	 * in the same way as a natural join does.
//	 * A physical union operator has than to 
//	 * fill the extra attributes with a special node.
//	 */
//	@Override
//	public void calcOutElements() {
//		SDFAttributeList l1 = getLeftInputSchema();
//		SDFAttributeList l2 = getRightInputSchema();
//		
//		if (l1 != null && l2 != null) {
//			setLeftInputSchema(l1);
//			setRightInputSchema(l2);
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
//		// also calc the out id size
//		this.calcOutIDSize();
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
//	public void calcOutIDSize(){
//		this.setInputIDSize(0, this.getInputAO(0).getOutputIDSize());
//		this.setInputIDSize(1, this.getInputAO(1).getOutputIDSize());
//		this.setOutputIDSize(this.getInputIDSize(0) + this.getInputIDSize(1));
//	}
//
//}
