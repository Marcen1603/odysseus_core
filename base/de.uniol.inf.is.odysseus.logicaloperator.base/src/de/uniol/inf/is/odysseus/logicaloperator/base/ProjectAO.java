/*
 * Created on 07.12.2004
 *
 */
package de.uniol.inf.is.odysseus.logicaloperator.base;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Marco Grawunder
 */
public class ProjectAO extends UnaryLogicalOp {
	private static final long serialVersionUID = 5487345119018834806L;

	public ProjectAO() {
		super();
	}

	public ProjectAO(ProjectAO ao){
		super(ao);
	}
	

	public @Override
	ProjectAO clone() {
		return new ProjectAO(this);
	}
	
	public int[] determineRestrictList(){
		return calcRestrictList(this.getInputSchema(), this.getOutputSchema());
	}

	
	public static int[] calcRestrictList(SDFAttributeList in, SDFAttributeList out){
		int[] ret = new int[out.size()];
		int i=0;
		for (SDFAttribute a:out){
			ret[i++] = in.indexOf(a);
		}
		return ret;
	}

}
