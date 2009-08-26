package de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeConstraint;

public class SDFDatatypeConstraints extends SDF {

	public static String baseURI = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_datatype_constraints.sdf#";

	

	public static String filename = baseDir + "sdf_datatype_constraints.sdf";

	public static String namespaceName = "sdfDatatypeConstraints";

	public static String hasMaxLength = baseURI + "hasMaxLength";

	public static String hasRange = baseURI + "hasRange";

	public static String Granularity = baseURI + "Granularity";
	
	public static String hasCovarianceMatrix = baseURI + "hasCovarianceMatrix";
	
	public static String dateFormat = baseURI + "dateFormat";
	
	public static String IntegerNumbers = baseURI + "IntegerNumbers";

	public static String RationalNumbers = baseURI + "RationalNumbers";
	
	public static String MeasurementValues = baseURI + "MeasurementValues";

	public static String hasGranularity = baseURI + "hasGranularity";
	
	public static boolean isInteger(SDFDatatypeConstraint constraint) {
		return constraint.getURI(false).equals(IntegerNumbers);
	}
	
	public static boolean isRational(SDFDatatypeConstraint type) {
		return type.getURI(false).equals(RationalNumbers);
	}
	
	public static boolean isMeasurementValue(SDFDatatypeConstraint type){
		return type.getURI(false).equals(MeasurementValues);
	}
	
	public static boolean isDateFormat(SDFDatatypeConstraint type){
		return type.getURI(false).equals(dateFormat);
	}

	protected SDFDatatypeConstraints() {
		super.baseUri = SDFDatatypes.baseURI;
		super.namespaceName = SDFDatatypes.namespaceName;
	}
}
