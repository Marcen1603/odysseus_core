package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;


/**
 * @author Jonas Jacobi
 */
public class DirectAttributeResolver implements IAttributeResolver {

	private static final long serialVersionUID = 692060392529144987L;
	private SDFAttributeList schema;

	public DirectAttributeResolver(SDFAttributeList schema) {
		this.schema = schema;
	}

	public SDFAttribute getAttribute(String name) {
		String[] parts = name.split("\\.", 2);
		SDFAttribute found = null;
		for (SDFAttribute attr : schema) {
			if (parts.length == 1) {
				if(((SDFAttribute)attr).getAttributeName().equals(name)){
					if (found != null) {
						throw new IllegalArgumentException("amgigious attribute: " + name);
					}
					found = attr;
				}
			} else {
				if (attr.getURI(false).equals(name)) {
					return attr;
				}
			}
		}
		if (found == null) {
			throw new IllegalArgumentException("no such attribute: " + name);
		}
		return found;
	}

}
