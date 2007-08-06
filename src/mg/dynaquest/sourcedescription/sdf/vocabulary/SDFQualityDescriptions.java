package mg.dynaquest.sourcedescription.sdf.vocabulary;

import mg.dynaquest.sourcedescription.sdf.vocabulary.SDF;

public class SDFQualityDescriptions extends SDF {

	public static String baseURI = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_quality_descriptions.sdf#";

	public static String filename = baseDir + "sdf_quality_descriptions.sdf";

	public static String namespaceName = "sdfQualityDescriptions";

	public static String hasSourceQualityDescription = baseURI
			+ "hasSourceQualityDescription";

	public static String hasQueryQualityDescription = baseURI
			+ "hasQueryQualityDescription";

	public static String QualityPair = baseURI + "QualityPair";

	public static String qualityProperty = baseURI + "qualityProperty";

	public static String qualityValue = baseURI + "qualityValue";

	protected SDFQualityDescriptions() {
		super.baseUri = SDFQualityDescriptions.baseURI;
		super.namespaceName = SDFQualityDescriptions.namespaceName;
	}
}