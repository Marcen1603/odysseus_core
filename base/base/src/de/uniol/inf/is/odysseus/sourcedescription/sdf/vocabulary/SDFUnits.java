package de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDF;

public class SDFUnits extends SDF {

	public static String baseURI = "http://www-is.informatik.uni-oldenburg.de/~grawund/rdf/2003/04/sdf_units.sdf#";

	public static String filename = baseDir + "sdf_units.sdf";

	public static String namespaceName = "sdfUnits";

	public static String CanHaveUnit = baseURI + "CanHaveUnit";

	public static String SDFUnit = baseURI + "SDFUnit";

	public static String SDFCurrencyUnit = baseURI + "SDFCurrencyUnit";

	public static String Euro = baseURI + "Euro";

	public static String Dollar = baseURI + "Dollar";

	public static String SDFWeightUnit = baseURI + "SDFWeightUnit";

	public static String Gramm = baseURI + "Gramm";

	public static String Kilogramm = baseURI + "Kilogramm";

	public static String Tonne = baseURI + "Tonne";

	public static String SDFDistanceUnit = baseURI + "SDFDistanceUnit";

	public static String Millimeter = baseURI + "Millimeter";

	public static String Zentimeter = baseURI + "Zentimeter";

	public static String Decimeter = baseURI + "Decimeter";

	public static String Meter = baseURI + "Meter";

	public static String Kilometer = baseURI + "Kilometer";

	public static String SDFAreaUnit = baseURI + "SDFAreaUnit";

	public static String Quadratzentimeter = baseURI + "Quadratzentimeter";

	public static String Quadratmeter = baseURI + "Quadratmeter";

	public static String Hektar = baseURI + "Hektar";

	public static String Quadratkilometer = baseURI + "Quadratkilometer";

	public static String SDFVolumeUnit = baseURI + "SDFVolumeUnit";

	public static String Liter = baseURI + "Liter";

	public static String Kubikmeter = baseURI + "Kubikmeter";

	public static String Gallone = baseURI + "Gallone";

	public static String SDFTemperatureUnit = baseURI + "SDFTemperatureUnit";

	public static String GradCelsius = baseURI + "GradCelsius";

	public static String GradFahrenheid = baseURI + "GradFahrenheid";

	public static String GradKelvin = baseURI + "GradKelvin";

	public static String SDFTimeUnit = baseURI + "SDFTimeUnit";

	public static String Sekunden = baseURI + "Sekunden";

	public static String Minuten = baseURI + "Minuten";

	public static String Stunden = baseURI + "Stunden";

	public static String Tage = baseURI + "Tage";

	public static String Wochen = baseURI + "Wochen";

	public static String Jahre = baseURI + "Jahre";

	public static String SDFSpeedUnit = baseURI + "SDFSpeedUnit";

	public static String MeterProSekunde = baseURI + "MeterProSekunde";

	public static String KilometerProStunde = baseURI + "KilometerProStunde";

	public static String MeilenProStunde = baseURI + "MeilenProStunde";

	public static String hasUnit = baseURI + "hasUnit";

	protected SDFUnits() {
		super.baseUri = SDFUnits.baseURI;
		super.namespaceName = SDFUnits.namespaceName;
	}
}