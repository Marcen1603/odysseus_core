package de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDF;

public class SDFDescriptions extends SDF {

	public static String baseURI = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_descriptions.sdf#";

	public static String filename = baseDir + "sdf_descriptions.sdf";

	public static String namespaceName = "sdfDescriptions";

	public static String SourceDescription = baseURI + "SourceDescription";

	public static String aboutSource = baseURI + "aboutSource";

	public static String alternateURI = baseURI + "alternateURI";

	public static String IntensionalDescription = baseURI
			+ "IntensionalDescription";

	public static String ExtensionalDescription = baseURI
			+ "ExtensionalDescription";

	public static String QualityDescription = baseURI + "QualityDescription";

	public static String hasIntensionalDesc = baseURI + "hasIntensionalDesc";

	public static String hasExtensionalDesc = baseURI + "hasExtensionalDesc";

	public static String hasQualityDesc = baseURI + "hasQualityDesc";

	protected SDFDescriptions() {
		super.baseUri = SDFDescriptions.baseURI;
		super.namespaceName = SDFDescriptions.namespaceName;

	}
}