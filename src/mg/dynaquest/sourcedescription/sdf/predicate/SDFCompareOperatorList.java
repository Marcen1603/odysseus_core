package mg.dynaquest.sourcedescription.sdf.predicate;

import java.util.ArrayList;

public class SDFCompareOperatorList extends ArrayList<SDFCompareOperator> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 711373188536550923L;

	public SDFCompareOperatorList(SDFCompareOperatorList compareOps) {
		super(compareOps);
	}

	public SDFCompareOperatorList() {
		super();
	}

	public void getXMLRepresentation(String indent, StringBuffer xmlRetValue) {
		for (SDFCompareOperator c: this.toArray(new SDFCompareOperator[this.size()])){
			xmlRetValue.append(indent);
			xmlRetValue.append("<operator>");
			xmlRetValue.append(c.getXMLRepresentation());
			xmlRetValue.append("</operator>\n");
		}
	}
}
