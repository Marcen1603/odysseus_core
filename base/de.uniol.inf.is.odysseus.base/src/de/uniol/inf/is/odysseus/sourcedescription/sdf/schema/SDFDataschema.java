package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.util.ArrayList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;

public class SDFDataschema extends SDFElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6456013184930683544L;
	/**
	 * @uml.property  name="entityList"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity"
	 */
    private ArrayList<SDFEntity> entityList = new ArrayList<SDFEntity>();

	public SDFDataschema(String URI) {
		super(URI);
	}

	public void addEntity(SDFEntity entity) {
		entityList.add(entity);
	}

	public SDFEntity getEntity(int index) {
		return entityList.get(index);
	}

	public int getNoOfEntities() {
		return entityList.size();
	}

	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString() + "\n");
		for (int i = 0; i < getNoOfEntities(); i++) {
			ret.append(getEntity(i).toString() + "\n");
		}
		return ret.toString();
	}

}