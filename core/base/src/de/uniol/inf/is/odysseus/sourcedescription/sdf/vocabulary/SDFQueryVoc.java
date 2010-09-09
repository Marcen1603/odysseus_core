package de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary;

public class SDFQueryVoc extends SDF {

	public static String baseURI = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_query.sdf#";

	public static String filename = baseDir + "sdf_query.sdf";

	public static String namespaceName = "sdfQuery";

	public static String Query = baseURI + "Query";

	public static String Weightable = baseURI + "Weightable";

	public static String Boundable = baseURI + "Boundable";

	public static String QueryAttribute = baseURI + "QueryAttribute";

	public static String hasQueryAttribute = baseURI + "hasQueryAttribute";

	public static String concernsAttribute = baseURI + "concernsAttribute";

	public static String weighting = baseURI + "weighting";

	public static String hasQueryPredicate = baseURI + "hasQueryPredicate";

	public static String QueryPredicate = baseURI + "QueryPredicate";

	public static String hasPredicate = baseURI + "hasPredicate";

	public static String respectQuality = baseURI + "respectQuality";

	public static String QueryQualityRequirement = baseURI
			+ "QueryQualityRequirement";

	public static String qualityAttribut = baseURI + "qualityAttribut";

	public static String lowerBound = baseURI + "lowerBound";

	public static String upperBound = baseURI + "upperBound";

	public static String respectUserSourceRating = baseURI
			+ "respectUserSourceRating";

	public static String UserSourceRating = baseURI + "UserSourceRating";

	public static String concernsSource = baseURI + "concernsSource";

	public static String concernsQualityAttribute = baseURI
			+ "concernsQualityAttribute";

	public static String hasValue = baseURI + "hasValue";

	public static String respectUserQualityRating = baseURI
			+ "respectUserQualityRating";

	public static String UserQualityRating = baseURI + "UserQualityRating";

	public static String overwriteQualityLevel = baseURI
			+ "overwriteQualityLevel";

	public static String OverwrittenQualityLevel = baseURI
			+ "OverwrittenQualityLevel";

	public static String concernsQualityLevel = baseURI
			+ "concernsQualityLevel";

	public static String correspondingValue = baseURI + "correspondingValue";

	public static String ImportanceLevel = baseURI + "ImportanceLevel";

	public static String VeryImportant = baseURI + "VeryImportant";

	public static String Important = baseURI + "Important";

	public static String Neutral = baseURI + "Neutral";

	public static String NiceToHave = baseURI + "NiceToHave";

	public static String Dispensable = baseURI + "Dispensable";
	
	public static String orderBy = baseURI+"orderBy";
	public static String OrderSet = baseURI+"OrderSet";
	public static String hasOrderAttribute = baseURI+"hasOrderAttribute";
	public static String hasSortOrder = baseURI+"hasSortOrder";
	public static String SortOrderAsc = baseURI+"SortOrderAsc";
	public static String SortOrderDesc = baseURI+"SortOrderDesc";

	protected SDFQueryVoc() {
		super.baseUri = SDFQueryVoc.baseURI;
		super.namespaceName = SDFQueryVoc.namespaceName;
	}
}