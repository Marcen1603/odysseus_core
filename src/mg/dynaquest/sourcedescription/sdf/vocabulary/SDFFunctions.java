package mg.dynaquest.sourcedescription.sdf.vocabulary;

import mg.dynaquest.sourcedescription.sdf.vocabulary.SDF;

public class SDFFunctions extends SDF {

	public static String baseURI = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_functions.sdf#";

	public static String filename = baseDir + "sdf_functions.sdf";

	public static String namespaceName = "sdfFunctions";

	public static String SDFFunction = baseURI + "SDFFunction";

	public static String OneToOne = baseURI + "OneToOne";

	public static String OneToMany = baseURI + "OneToMany";

	public static String ManyToOne = baseURI + "ManyToOne";

	public static String Identity = baseURI + "Identity";

	public static String MathCalc = baseURI + "MathCalc";

	public static String Translate = baseURI + "Translate";

	public static String ToString = baseURI + "ToString";

	public static String ToNumber = baseURI + "ToNumber";

	public static String TSplittReg = baseURI + "TSplittReg";

	public static String TSplitt = baseURI + "TSplitt";

	public static String MSplitt = baseURI + "MSplitt";

	public static String Merge = baseURI + "Merge";

	public static String hasReverse = baseURI + "hasReverse";

	protected SDFFunctions() {
		super.baseUri = SDFFunctions.baseURI;
		super.namespaceName = SDFFunctions.namespaceName;
	}
}