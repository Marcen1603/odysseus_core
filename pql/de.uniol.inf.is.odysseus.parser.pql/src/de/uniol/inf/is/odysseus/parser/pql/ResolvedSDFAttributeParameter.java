package de.uniol.inf.is.odysseus.parser.pql;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.AmgigiousAttributeException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.NoSuchAttributeException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class ResolvedSDFAttributeParameter extends AbstractParameter<SDFAttribute> {

	public ResolvedSDFAttributeParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);
	}

	@Override
	public void setValueOf(Object object) {
		SDFAttribute attribute;
		try {
			attribute = getAttributeResolver().getAttribute(
					(String) object);
		setValue(attribute);
		} catch (AmgigiousAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
