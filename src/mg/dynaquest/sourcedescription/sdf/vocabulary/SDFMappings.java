package mg.dynaquest.sourcedescription.sdf.vocabulary;

import mg.dynaquest.sourcedescription.sdf.vocabulary.SDF;

public class SDFMappings extends SDF {

	public static String baseURI = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_mappings.sdf#";

	public static String filename = baseDir + "sdf_mappings.sdf";

	public static String namespaceName = "sdfMappings";

	public static String SDFMapping = baseURI + "SDFMapping";

	public static String OneToOne = baseURI + "OneToOne";

	public static String OneToMany = baseURI + "OneToMany";

	public static String ManyToOne = baseURI + "ManyToOne";

	public static String Conditional = baseURI + "Conditional";

	public static String Equivalence = baseURI + "Equivalence";

	public static String Superset = baseURI + "Superset";

	public static String Subset = baseURI + "Subset";

	public static String Common = baseURI + "Common";

	public static String hasIn = baseURI + "hasIn";

	public static String hasUnitIn = baseURI + "hasUnitIn";

	public static String hasOut = baseURI + "hasOut";

	public static String hasUnitOut = baseURI + "hasUnitOut";

	public static String usesFunction = baseURI + "usesFunction";

	public static String usesOneToOneFunction = baseURI
			+ "usesOneToOneFunction";

	public static String usesOneToManyFunction = baseURI
			+ "usesOneToManyFunction";

	public static String usesManyToOneFunction = baseURI
			+ "usesManyToOneFunction";

	public static String hasCond = baseURI + "hasCond";

	protected SDFMappings() {
		super.baseUri = SDFMappings.baseURI;
		super.namespaceName = SDFMappings.namespaceName;
	}
}