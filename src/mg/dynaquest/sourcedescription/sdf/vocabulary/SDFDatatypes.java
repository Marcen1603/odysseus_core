package mg.dynaquest.sourcedescription.sdf.vocabulary;

import mg.dynaquest.sourcedescription.sdf.vocabulary.SDF;

public class SDFDatatypes extends SDF {

	public static String baseURI = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_datatypes.sdf#";

	public static String filename = baseDir + "sdf_datatypes.sdf";

	public static String namespaceName = "sdfDatatypes";

	public static String SDFDatatype = baseURI + "SDFDatatype";

	public static String SDFBaseDatatype = baseURI + "SDFBaseDatatype";

	public static String String = baseURI + "String";

	public static String Number = baseURI + "Number";

	public static String Intervall = baseURI + "Intervall";

	public static String List = baseURI + "List";

	public static String hasBaseDatatype = baseURI + "hasBaseDatatype";
	public static String hasIntervallStartOpen = baseURI + "hasIntervallStartOpen";
	public static String hasIntervallStartClose = baseURI + "hasIntervallStartClose";
	public static String hasIntervallEndOpen = baseURI + "hasIntervallEndOpen";
	public static String hasIntervallEndClose = baseURI + "hasIntervallEndClose";
	

	protected SDFDatatypes() {
		super.baseUri = SDFDatatypes.baseURI;
		super.namespaceName = SDFDatatypes.namespaceName;
	}
}