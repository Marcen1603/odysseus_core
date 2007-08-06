package mg.dynaquest.sourcedescription.sdf.vocabulary;

public class SDFDatatypeConstraints extends SDF {

	public static String baseURI = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_datatype_constraints.sdf#";

	

	public static String filename = baseDir + "sdf_datatype_constraints.sdf";

	public static String namespaceName = "sdfDatatypeConstraints";

	public static String hasMaxLength = baseURI + "hasMaxLength";

	public static String hasRange = baseURI + "hasRange";

	public static String Granularity = baseURI + "Granularity";

	public static String IntegerNumbers = baseURI + "IntegerNumbers";

	public static String RationalNumbers = baseURI + "RationalNumbers";

	public static String hasGranularity = baseURI + "hasGranularity";

	protected SDFDatatypeConstraints() {
		super.baseUri = SDFDatatypes.baseURI;
		super.namespaceName = SDFDatatypes.namespaceName;
	}
}
