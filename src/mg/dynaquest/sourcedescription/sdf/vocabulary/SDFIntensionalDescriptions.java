package mg.dynaquest.sourcedescription.sdf.vocabulary;

public class SDFIntensionalDescriptions extends SDF {

	public static String baseURI = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_intensional_descriptions.sdf#";

	public static String filename = baseDir + "sdf_intensional_descriptions.sdf";

	public static String namespaceName = "sdfIntensionalDescriptions";

	public static String hasLocalSchema = baseURI + "hasLocalSchema";

	public static String hasSchemaMapping = baseURI + "hasSchemaMapping";

	public static String AccessPattern = baseURI + "AccessPattern";

	public static String hasAccessPattern = baseURI + "hasAccessPattern";

	public static String InputPattern = baseURI + "InputPattern";

	public static String OutputPattern = baseURI + "OutputPattern";

	public static String hasInputPattern = baseURI + "hasInputPattern";

	public static String hasOutputPattern = baseURI + "hasOutputPattern";

	public static String hasAttributeAttributeBindingPair = baseURI
			+ "hasAttributeAttributeBindingPair";

	public static String AttributeAttributeBindingPair = baseURI
			+ "AttributeAttributeBindingPair";

	public static String concernsAttribute = baseURI + "concernsAttribute";

	public static String NecessityState = baseURI + "NecessityState";

	public static String Optional = baseURI + "Optional";

	public static String Required = baseURI + "Required";

	public static String necessity = baseURI + "necessity";

	public static String hasCompareOperator = baseURI + "hasCompareOperator";

	public static String valueFromConstantSet = baseURI
			+ "valueFromConstantSet";

	public static String ConstantSetSelection = baseURI
			+ "ConstantSetSelection";

	public static String ConstantSelectionType = baseURI
			+ "ConstantSelectionType";

	public static String Single = baseURI + "Single";

	public static String And = baseURI + "And";

	public static String Or = baseURI + "Or";

	public static String hasConstantSelectionType = baseURI
			+ "hasConstantSelectionType";

	public static String hasConstantSet = baseURI + "hasConstantSet";

	public static String dependsOn = baseURI + "dependsOn";

	public static String OutputAttributeBinding = baseURI + "OutputAttributeBinding";

	public static String Id = baseURI + "Id";

	public static String SortAsc = baseURI + "SortAsc";

	public static String SortDesc = baseURI + "SortDesc";
    
    public static String None = baseURI + "e";

	public static String hasOutputAttributeBinding = baseURI + "hasOutputAttributeBinding";

	protected SDFIntensionalDescriptions() {
		super.baseUri = SDFIntensionalDescriptions.baseURI;
		super.namespaceName = SDFIntensionalDescriptions.namespaceName;
	}
}