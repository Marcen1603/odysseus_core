package de.uniol.inf.is.odysseus.debsgc2015.geo;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

// from http://www.ibm.com/developerworks/library/j-coordconvert/

public class LatLongToUtm extends AbstractFunction<List<Double>> {

	private static final long serialVersionUID = 2721221748368566272L;

	private static final Logger LOG = LoggerFactory
			.getLogger(LatLongToUtm.class);

	private static final SDFDatatype[][] acceptedTypes = new SDFDatatype[][] {
			{ SDFDatatype.DOUBLE }, { SDFDatatype.DOUBLE } };

	public LatLongToUtm() {
		super("latLongToUTM", 2, acceptedTypes, SDFDatatype.LIST_DOUBLE);
	}

	@Override
	public List<Double> getValue() {
		double lat = getNumericalInputValue(0);
		double lng = getNumericalInputValue(1);
		try {
			return Arrays.asList(latLongToUtm(lat, lng));
		} catch (IllegalArgumentException e) {
			// Illegal lat/long values
			LOG.error(e.getMessage());
			return Arrays.asList(new Double[] { 0.0, 0.0 });
		}
	}

	private static class ConversionVariables {

		public static double equatorialRadius = 6378137;

		public static double polarRadius = 6356752.314;

		public static double k0 = 0.9996;

		public static double e = Math.sqrt(1 - Math.pow(polarRadius
				/ equatorialRadius, 2));

		public static double e1sq = e * e / (1 - e * e);

		public static double A0 = 6367449.146;

		public static double B0 = 16038.42955;

		public static double C0 = 16.83261333;

		public static double D0 = 0.021984404;

		public static double E0 = 0.000312705;

		public static double sin1 = 4.84814E-06;

		double nu, p, S, K1, K2, K3, K4, K5;

	}

	public static Double[] latLongToUtm(double latitude, double longitude) {
		if (latitude < -90.0 || latitude > 90.0 || longitude < -180.0
				|| longitude >= 180.0) {
			return null;
		}
		ConversionVariables vars = setVariables(latitude, longitude);
		double easting = getEasting(vars);
		double northing = getNorthing(latitude, vars);
		return new Double[] { easting, northing };
	}

	private static ConversionVariables setVariables(double latitude,
			double longitude) {
		ConversionVariables vars = new ConversionVariables();
		double lat_rad = Math.toRadians(latitude);
		vars.nu = ConversionVariables.equatorialRadius
				/ Math.pow(1 - Math.pow(
						ConversionVariables.e * Math.sin(lat_rad), 2),
						(1 / 2.0));
		double var1;
		if (longitude < 0.0) {
			var1 = ((int) ((180 + longitude) / 6.0)) + 1;
		} else {
			var1 = ((int) (longitude / 6)) + 31;
		}
		double var2 = (6 * var1) - 183;
		double var3 = longitude - var2;
		vars.p = var3 * 3600 / 10000;
		vars.S = ConversionVariables.A0 * lat_rad - ConversionVariables.B0
				* Math.sin(2 * lat_rad) + ConversionVariables.C0
				* Math.sin(4 * lat_rad) - ConversionVariables.D0
				* Math.sin(6 * lat_rad) + ConversionVariables.E0
				* Math.sin(8 * lat_rad);

		vars.K1 = vars.S * ConversionVariables.k0;
		vars.K2 = vars.nu * Math.sin(lat_rad) * Math.cos(lat_rad)
				* Math.pow(ConversionVariables.sin1, 2)
				* ConversionVariables.k0 * (100000000) / 2;
		vars.K3 = ((Math.pow(ConversionVariables.sin1, 4) * vars.nu
				* Math.sin(lat_rad) * Math.pow(Math.cos(lat_rad), 3)) / 24)
				* (5 - Math.pow(Math.tan(lat_rad), 2) + 9
						* ConversionVariables.e1sq
						* Math.pow(Math.cos(lat_rad), 2) + 4
						* Math.pow(ConversionVariables.e1sq, 2)
						* Math.pow(Math.cos(lat_rad), 4))
				* ConversionVariables.k0 * (10000000000000000L);

		vars.K4 = vars.nu * Math.cos(lat_rad) * ConversionVariables.sin1
				* ConversionVariables.k0 * 10000;

		vars.K5 = Math.pow(ConversionVariables.sin1 * Math.cos(lat_rad), 3)
				* (vars.nu / 6)
				* (1 - Math.pow(Math.tan(lat_rad), 2) + ConversionVariables.e1sq
						* Math.pow(Math.cos(lat_rad), 2))
				* ConversionVariables.k0 * 1000000000000L;
		return vars;
	}

	private static double getEasting(ConversionVariables vars) {
		return 500000 + (vars.K4 * vars.p + vars.K5 * Math.pow(vars.p, 3));
	}

	private static double getNorthing(double latitude, ConversionVariables vars) {
		double northing = vars.K1 + vars.K2 * vars.p * vars.p + vars.K3
				* Math.pow(vars.p, 4);
		if (latitude < 0.0) {
			northing = 10000000 + northing;
		}
		return northing;
	}

}