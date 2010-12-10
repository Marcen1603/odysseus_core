package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

public class SDFMetaAttributeList extends SDFSchemaElementSet<SDFMetaAttribute> {

	private static final long serialVersionUID = -1194514879108835243L;	

	public SDFMetaAttributeList(){
	}
	
	public SDFMetaAttributeList(SDFMetaAttributeList attributes1) {
		super(attributes1);
	}

	public static SDFMetaAttributeList union(SDFMetaAttributeList attributes1, SDFMetaAttributeList attributes2) {		
		if (attributes1 == null || attributes1.size() == 0) {
			return attributes2;
		}
		if (attributes2 == null || attributes2.size() == 0) {
			return attributes1;
		}
		SDFMetaAttributeList newSet = new SDFMetaAttributeList(attributes1);
		for (int i = 0; i < attributes2.size(); i++) {
			if (!newSet.contains(attributes2.get(i))) {
				newSet.add(attributes2.get(i));
			}
		}
		return newSet;
	}
}
