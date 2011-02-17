package de.uniol.inf.is.odysseus.datamining.state.logicaloperator;

import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class RecallAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1034411520230158353L;

	public SDFAttributeList schemaToCheck;

	public RecallAO(RecallAO recallAO) {
		super(recallAO);
		this.schemaToCheck = recallAO.schemaToCheck;
	}

	public RecallAO() {
		super();
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		SDFAttributeList schema = getInputSchema().clone();
		SDFAttribute a = new SDFAttribute("hits");
		a.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
		schema.add(a);

		SDFAttribute b = new SDFAttribute("misses");
		b.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
		schema.add(b);
		return schema;
	}

	@Override
	public RecallAO clone() {
		return new RecallAO(this);
	}

	public void setSchemaToCheck(SDFAttributeList schemaToCheck) {
		this.schemaToCheck = schemaToCheck;
	}

	public SDFAttributeList getSchemaToCheck() {
		return this.schemaToCheck;
	}

	public int[] getCheckList() {
		int[] liste = new int[this.schemaToCheck.size()];
		for (int s = 0; s < this.schemaToCheck.size(); s++) {
			for (int i = 0; i < this.getInputSchema().size(); i++) {
				if (this.getInputSchema().get(i).getAttributeName().equals(this.schemaToCheck.get(s).getAttributeName())) {
					liste[s] = i;
				}
			}
		}
		return liste;
	}

}
