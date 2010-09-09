package de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary;

public class SDFExtensionalDescriptions extends SDF {

	public static String baseURI = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_extensional_descriptions.sdf#";

	public static String filename = baseDir + "sdf_extensional_descriptions.sdf";

	public static String namespaceName = "sdfExtensionalDescriptions";

	public static String hasDescriptionPredicate = baseURI
			+ "hasDescriptionPredicate";

	public static String hasSimpleDescriptionPredicate = baseURI
			+ "hasSimpleDescriptionPredicate";

	public static String hasComplexDescriptionPredicate = baseURI
			+ "hasComplexDescriptionPredicate";

	protected SDFExtensionalDescriptions() {
		super.baseUri = SDFExtensionalDescriptions.baseURI;
		super.namespaceName = SDFExtensionalDescriptions.namespaceName;
	}
}