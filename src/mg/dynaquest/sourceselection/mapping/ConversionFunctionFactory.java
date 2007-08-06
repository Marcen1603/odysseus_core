package mg.dynaquest.sourceselection.mapping;

import mg.dynaquest.sourcedescription.sdf.function.SDFFunction;

public class ConversionFunctionFactory {

	static public ConversionFunction getFunction(SDFFunction function) {
		if (function != null) {
			return getFunction(function.getURI(false));
		} else {
			return getFunction((String) null);
		}
	}

	static public ConversionFunction getFunction(String function) {
		// Erst mal zum testen hart codiert, später Auslagern in DB oder
		// Config-Datei!!

		// Wenn keine Funktion angegeben worden ist, ist es immer die
		// Identität
		if (function == null
				|| function
						.equals("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_functions.sdf#Identity")) {
			return Identity.getInstance();
		}
		
		if (function.equals("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2004/08/Automarkt2004.sdf#Identity")){
		    return Identity.getInstance();
		}

		if (function
				.equals("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_unit_mappings.sdf#EuroDollarTrafo")) {
			return EuroDollarTrafo.getInstance();
		}
		if (function
				.equals("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_unit_mappings.sdf#DollarEuroTrafo")) {
			return DollarEuroTrafo.getInstance();
		}
		if (function
				.equals("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_functions.sdf#ToString")) {
			return ToString.getInstance();
		}
		if (function
				.equals("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_functions.sdf#ToNumber")) {
			return ToNumber.getInstance();
		}
		if (function
				.equals("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Source_CarVendor_1.sdf#MileToKm")) {
			return MileKmTrafo.getInstance();
		}
		if (function
				.equals("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/Source_CarVendor_1.sdf#KmToMile")) {
			return KmMileTrafo.getInstance();
		}
		if (function
				.equals("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_conversion_functions.sdf#BruttoToNettoProzent")) {
			return BruttoToNettoProzent.getInstance();
		}
		if (function
				.equals("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_conversion_functions.sdf#NettoToBruttoProzent")) {
			return NettoToBruttoProzent.getInstance();
		}
		if (function
				.equals("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_conversion_functions.sdf#StringConcat")) {
			return StringConcat.getInstance();
		}
		if (function
				.equals("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_conversion_functions.sdf#StringConcatWithBlank")) {
			return StringConcatWithBlank.getInstance();
		}	
		if (function
				.equals("http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_conversion_functions.sdf#BlankSplitter")) {
			return BlankSplitter.getInstance();
		}

		System.err
				.println("ConversionFunction: Leider keine passende Function gefunden für");
		System.err.println(function);
		// Throws FunctionNotFoundException ...
		return null;
	}

}