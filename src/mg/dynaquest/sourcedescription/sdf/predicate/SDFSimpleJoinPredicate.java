package mg.dynaquest.sourcedescription.sdf.predicate;

import java.util.Map;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;

public class SDFSimpleJoinPredicate extends SDFSimplePredicate {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1508278076460306191L;
	
	private SDFCompareOperator compop;

	public SDFSimpleJoinPredicate(String URI, SDFAttribute attribute1, SDFCompareOperator compop, SDFAttribute attribute2) {
		super(URI, attribute1);
		setCompareOp(compop);
	}

	@Override
	public SDFCompareOperator getCompareOp() {
		return compop;
	}

	@Override
	public SDFConstant getValue() {
		return null;
	}

	@Override
	public void setCompareOp(SDFCompareOperator operator) {
		this.compop = operator;
	}

	@Override
	public boolean evaluate(Map<SDFAttribute, SDFConstant> attributeAssignment) {
		throw new NotImplementedException();
		// TODO:
	}

	@Override
	public boolean evaluateDirect(Map<SDFAttribute, Object> attributeAssignment) {		
		throw new NotImplementedException();
		// TODO:
	}

	@Override
	public void getXMLRepresentation(String indent, StringBuffer xmlRetValue) {
		// TODO Auto-generated method stub

	}

}
