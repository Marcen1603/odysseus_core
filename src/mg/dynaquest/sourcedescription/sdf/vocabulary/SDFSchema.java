package mg.dynaquest.sourcedescription.sdf.vocabulary;

import mg.dynaquest.sourcedescription.sdf.vocabulary.SDF;

public class SDFSchema extends SDF {

	public static String baseURI = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_schema.sdf#";

	public static String filename = baseDir + "sdf_schema.sdf";

	public static String namespaceName = "sdfSchema";

	public static String SchemaElement = baseURI + "SchemaElement";

	public static String Entity = baseURI + "Entity";

	public static String Dataschema = baseURI + "Dataschema";

	public static String Attribute = baseURI + "Attribute";

	public static String Constant = baseURI + "Constant";

	public static String SchemaElementSet = baseURI + "SchemaElementSet";

	public static String AttributeSet = baseURI + "AttributeSet";

	public static String ConstantSet = baseURI + "ConstantSet";

	public static String hasEntity = baseURI + "hasEntity";

	public static String hasAttribute = baseURI + "hasAttribute";

	public static String hasIdAttribute = baseURI + "hasIdAttribute";

	public static String hasAttributeSet = baseURI + "hasAttributeSet";

	public static String hasConstant = baseURI + "hasConstant";

	public static String hasConstantSet = baseURI + "hasConstantSet";

	public static String isA = baseURI + "isA";

	public static String hasValue = baseURI + "hasValue";

	protected SDFSchema() {
		super.baseUri = SDFSchema.baseURI;
		super.namespaceName = SDFSchema.namespaceName;
	}
}