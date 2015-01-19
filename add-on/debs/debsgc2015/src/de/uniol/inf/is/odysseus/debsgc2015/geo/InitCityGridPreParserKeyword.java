package de.uniol.inf.is.odysseus.debsgc2015.geo;

import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.memstore.mdastore.MDAStore;
import de.uniol.inf.is.odysseus.memstore.mdastore.MDAStoreManager;
import de.uniol.inf.is.odysseus.script.parser.AbstractPreParserKeyword;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptException;

public class InitCityGridPreParserKeyword extends AbstractPreParserKeyword {

	public static final String KEYWORD = "INIT_CITYGRID";

	private static final int ATTRIBUTE_COUNT = 7;

	private static final String PATTERN = "Name StartLatitude NumberOfLatitudes Distance(m) StartLatitude NumberOfLatitudes Distance(m)";

	@Override
	public void validate(Map<String, Object> variables, String parameter,
			ISession caller, Context context) throws OdysseusScriptException {
		if (Strings.isNullOrEmpty(parameter)) {
			throw new OdysseusScriptException("MDAStore name is missing!");
		}

		String[] splitted = parameter.trim().split(" ");
		if (splitted.length != ATTRIBUTE_COUNT) {
			throw new OdysseusScriptException(KEYWORD + " needs "
					+ ATTRIBUTE_COUNT + " attributes: " + PATTERN + "!");
		}
	}

	@Override
	public List<IExecutorCommand> execute(Map<String, Object> variables,
			String parameter, ISession caller, Context context)
			throws OdysseusScriptException {
		String[] splitted = parameter.trim().split(" ");
		String name = splitted[0];
		MDAStore<Double> store = MDAStoreManager.create(name);

		double startLatitude = Double.parseDouble(splitted[1]);
		int numLatitudes = Integer.parseInt(splitted[2]);
		double diffMetersLatitude = Double.parseDouble(splitted[3]);
		double startLongitude = Double.parseDouble(splitted[4]);
		int numLongitudes = Integer.parseInt(splitted[5]);
		double diffMetersLongitude = Double.parseDouble(splitted[6]);

		IPair<Double, Double> startPoint = new Pair<Double, Double>(
				startLatitude, startLongitude);
		List<Double> latitudes = Lists.newArrayList();
		List<Double> longitudes = Lists.newArrayList();
		int latCnt = 0;
		int lngCnt = 0;
		while (latCnt < numLatitudes || lngCnt < numLongitudes) {
			if (latCnt < numLatitudes) {
				latitudes.add(calcGegraphicPosition(startPoint,
						diffMetersLatitude / 1000 * (latCnt + 1), Math.PI / 2)
						.getE1());
				latCnt++;
			}
			if (lngCnt < numLongitudes) {
				longitudes.add(calcGegraphicPosition(startPoint,
						diffMetersLongitude / 1000 * (lngCnt + 1), Math.PI)
						.getE2());
				lngCnt++;
			}
		}

		List<List<Double>> values = Lists.newArrayList();
		values.add(latitudes);
		values.add(longitudes);
		store.initialize(values);
		return null;
	}

	private IPair<Double, Double> calcGegraphicPosition(
			IPair<Double, Double> startPoint, double diffInKm, double azimuth) {
		double earthRadius = 6371; // kilometers
		double lat1 = Math.toRadians(startPoint.getE1());
		double lng1 = Math.toRadians(startPoint.getE2());
		double d = Math.toRadians(diffInKm / earthRadius);
		double a = Math.toDegrees(azimuth);

		double lat2 = Math.asin(Math.sin(lat1) * Math.cos(d) + Math.cos(lat1)
				* Math.sin(d) * Math.cos(a));
		double dlon = Math.atan2(Math.sin(a) * Math.sin(d) * Math.cos(lat1),
				Math.cos(d) - Math.sin(lat1 * lat2));
		double lng2 = (lng1 - dlon + Math.PI) % (2 * Math.PI) - Math.PI;
		return new Pair<Double, Double>(Math.toDegrees(lat2),
				Math.toDegrees(lng2));
	}
}