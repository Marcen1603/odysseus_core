package mg.dynaquest.queryexecution.po.streaming.oo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.streaming.object.StreamExchangeElement;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;

import org.w3c.dom.NodeList;

/**
 * @author Jonas Jacobi
 *
 */
public class OOStreamingSelectPO extends OOStreamingBasePO {

	public OOStreamingSelectPO(OOStreamingBasePO operator) {
		super(operator);
	}

	public OOStreamingSelectPO() {
	}

	public OOStreamingSelectPO(AlgebraPO algebraPO) {
		super(algebraPO);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void getInternalXMLRepresentation(String baseIndent,
			String indent, StringBuffer xmlRetValue) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initInternalBaseValues(NodeList childNodes) {
	}

	@Override
	protected boolean process_close() throws POException {
		return true;
	}

	@Override
	protected Object process_next() throws POException, TimeoutException {
		StreamExchangeElement<Map<String, Object>> retVal = null;
		while (true) {
			retVal = getInputNext(this, -1);
			Map<SDFAttribute, SDFConstant> attributeAssignment = new HashMap<SDFAttribute, SDFConstant>();
			SDFAttributeList attribs = this.getPredicate().getAllAttributes();
			for (int i = 0; i < attribs.getAttributeCount(); ++i) {
				SDFAttribute curAttribute = (SDFAttribute) attribs.get(i);
				try {
					attributeAssignment.put(curAttribute, getSDFAttributeValue(
							curAttribute, retVal.getCargo()));
				} catch (Exception e) {
					throw new POException(e);
				}
			}

			if (this.getPredicate().evaluate(attributeAssignment)) {
				return retVal;
			}
		}
	}

	@Override
	protected boolean process_open() throws POException {
		return true;
	}

	public SupportsCloneMe cloneMe() {
		return new OOStreamingSelectPO(this);
	}

}
