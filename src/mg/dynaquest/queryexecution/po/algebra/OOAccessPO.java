package mg.dynaquest.queryexecution.po.algebra;

import mg.dynaquest.sourcedescription.sdf.description.SDFSource;

public class OOAccessPO extends AccessPO {

//	public OOAccessPO(AlgebraPO po) {
//		super(po);
//	}

	private String asName;

	public OOAccessPO() {
		super();
	}

	public OOAccessPO(OOAccessPO po) {
		super(po);
		this.asName = po.asName;
	}

	public OOAccessPO(SDFSource source, String asName) {
		super(source);
		this.asName = asName;
	}

	public String getAsName() {
		return asName;
	}

	public void setAsName(String asName) {
		this.asName = asName;
	}

}
