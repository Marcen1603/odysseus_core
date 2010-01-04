package de.uniol.inf.is.odysseus.action.operator;

import de.uniol.inf.is.odysseus.action.exception.OperatorException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * Class providing static methods for extracting data from
 * data stream elements
 * @author Simon Flandergan
 *
 */
public class AttributeExtractor {
	/**
	 * List of all avaiable datatypes
	 * @author Simon Flandergan
	 *
	 */
	public enum Datatype {
		Relational("relational");
		
		private String name;
		
		private Datatype(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	};
	
	/**
	 * Extracts the value of given attribute
	 * @param element data stream element
	 * @param attributeIdentifier identifier for attribute
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object extractAttribute(Object element, Object attributeIdentifier, Datatype type){
		Object attributeValue = null;
		switch (type){
		case Relational:
			attributeValue = ((RelationalTuple)element).getAttribute(((Number)attributeIdentifier).intValue());
			break;
		}
		return attributeValue;
	}
	
	/**
	 * Determine datatype through evaluating given name
	 * @param datatypeName
	 * @return
	 * @throws OperatorException
	 */
	public static Datatype determineDataType (String datatypeName) throws OperatorException{
		datatypeName = datatypeName.toLowerCase();
		for (Datatype type : Datatype.values()){
			if (type.getName().equals(datatypeName)){
				return type;
			}
		}
		throw new OperatorException("Transformation error: Unknown datatype: " +datatypeName);
	}

}
