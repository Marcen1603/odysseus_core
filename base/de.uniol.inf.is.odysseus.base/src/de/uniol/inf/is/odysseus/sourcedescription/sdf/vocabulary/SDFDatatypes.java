package de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class SDFDatatypes extends SDF {

	public static String baseURI = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_datatypes.sdf#";

	public static String filename = baseDir + "sdf_datatypes.sdf";

	public static String namespaceName = "sdfDatatypes";

	public static String SDFDatatype = baseURI + "SDFDatatype";

	public static String SDFBaseDatatype = baseURI + "SDFBaseDatatype";

	public static String String = baseURI + "String";

	public static String Number = baseURI + "Number";

	public static String Intervall = baseURI + "Intervall";
	
	public static String MeasurementValue = baseURI + "MeasurementValue";

	public static String List = baseURI + "List";

	public static String hasBaseDatatype = baseURI + "hasBaseDatatype";

	public static String hasIntervallStartOpen = baseURI
			+ "hasIntervallStartOpen";

	public static String hasIntervallStartClose = baseURI
			+ "hasIntervallStartClose";

	public static String hasIntervallEndOpen = baseURI + "hasIntervallEndOpen";

	public static String hasIntervallEndClose = baseURI
			+ "hasIntervallEndClose";

	public static boolean isNumerical(
			de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype type) {
		return type.getURI(false).equals("Long")
				|| type.getURI(false).equals("Integer")
				|| type.getURI(false).equals("Double")
				|| type.getURI(false).equals("MV");
	}
	
	public static boolean isInteger(SDFDatatype type){
		return type.getURI(false).equals("Integer");
	}
	
	public static boolean isLong(SDFDatatype type){
		return type.getURI(false).equals("Long");
	}
	
	public static boolean isDouble(SDFDatatype type){
		return type.getURI(false).equals("Double");
	}
	
	public static boolean isMeasurementValue(
			de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype type) {
		return type.getURI(false).equals("MV");
	}

	public static boolean isString(
			de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype type) {
		return type.getURI(false).equals(String);
	}

	public static boolean isDate(SDFDatatype type){
		return type.getURI(false).equals("Date");
	}
	
	protected SDFDatatypes() {
		super.baseUri = SDFDatatypes.baseURI;
		super.namespaceName = SDFDatatypes.namespaceName;
	}
}