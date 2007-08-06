package mg.dynaquest.sourcedescription.sdf.predicate;

import java.util.Map;

import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFIntervall;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@SuppressWarnings("serial")
public class SDFIntervalPredicate extends SDFSimplePredicate {

	SDFIntervall intervall = null;
	
	SDFIntervalPredicate(String uri, SDFAttribute attribute, SDFIntervall intervall){
		super(uri, attribute);
		this.intervall = intervall;
	}
	
	@Override
	public SDFCompareOperator getCompareOp() {
		return null;
	}

	@Override
	public SDFConstant getValue() {
		return null;
	}
	
	public Double getLeftValue(){
		return intervall.getLeftBorder();
	}
	
	public Double getRightValue(){
		return intervall.getRightBorder();
	}
	
	public boolean isLeftOpen(){
		return intervall.isLeftIsOpen();
	}
	
	public boolean isRightOpen(){
		return intervall.isRightIsOpen();
	}
	
	

	@Override
	public boolean evaluate(
			Map<SDFAttribute, SDFConstant> attributeAssignment) {
		throw new NotImplementedException();
	}

	@Override
	public void getXMLRepresentation(String indent, StringBuffer xmlRetValue) {
		throw new NotImplementedException();
	}
	
	@Override
	public boolean evaluateDirect(Map<SDFAttribute, Object> attributeAssignment) {
		throw new NotImplementedException();
	}
	
	@Override
	public void setCompareOp(SDFCompareOperator operator) {
		// gibt es nichts sinnvolles ...
	}

	@Override
	public SDFAttributeList getAllAttributesWithCompareOperator(SDFCompareOperator op) {
		// Keine Attribute mit =
		return null;
	}



}
