package mg.dynaquest.queryexecution.po.streaming.relational;

import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.NAryPlanOperator;
import mg.dynaquest.queryexecution.po.base.UnaryPlanOperator;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;
import mg.dynaquest.queryexecution.po.streaming.object.StreamExchangeElement;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFDatatype;
import mg.dynaquest.sourcedescription.sdf.schema.SDFNumberConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFStringConstant;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFDatatypes;

import org.w3c.dom.NodeList;

public class RelationalStreamingSelectPO extends UnaryPlanOperator {

	transient HashMap<SDFAttribute, SDFConstant> attribMap = new HashMap<SDFAttribute, SDFConstant>();

	public RelationalStreamingSelectPO() {
		super();
	}

	public RelationalStreamingSelectPO(RelationalStreamingSelectPO operator) {
		super(operator);
	}

	public RelationalStreamingSelectPO(AlgebraPO algebraPO) {
		super(algebraPO);
	}

	@Override
	public String getInternalPOName() {
		return RelationalStreamingSelectPO.class.getSimpleName();
	}

	@Override
	protected void getInternalXMLRepresentation(String baseIndent,
			String indent, StringBuffer xmlRetValue) {

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
		// Vom Vorgnger solange Objekte anfordern, bis eines die Selektions-
		// bedingung erfllt
		StreamExchangeElement<RelationalTuple> retVal = getInputNext(this,
				-1);

		while (retVal != null) {
			// Aus dem RelationalTuple ein Element fr das SDFPredicate machen

			for (int i = 0; i < getInAttribs().getAttributeCount(); i++) {
				SDFAttribute attribute = getInAttribs().getAttribute(i);
				SDFDatatype datatype = attribute.getDatatype();
				if (datatype.equals(SDFDatatypes.String)) {
					attribMap.put(attribute, new SDFStringConstant("", retVal
							.getCargo().getAttribute(i)));
				} else {
					if (datatype.equals(SDFDatatypes.Number)) {
						attribMap.put(attribute, new SDFNumberConstant("",
								retVal.getCargo().getAttribute(i)));
					} else {
//						if (datatype.equals(SDFDatatypes.Intervall)) {
//							attribMap.put(attribute, new SDFIntervalConstant(
//									"", retVal.getValidity()));
//						}
					}
				}
			}

			// Wenn das Tupel das Prdikat erfllt, die Schleife verlassen
			if (this.getPredicate().evaluate(attribMap)) {
				break;
			}
			retVal = this.getInputNext(this, -1);
		}
		return retVal;
	}

	@Override
	protected boolean process_open() throws POException {
		return true;
	}

	public SupportsCloneMe cloneMe() {
		return new RelationalStreamingSelectPO(this);
	}

	private SDFAttributeList getInAttribs() {
		return ((NAryPlanOperator) this.getInputPO()).getOutElements();
	}

}
