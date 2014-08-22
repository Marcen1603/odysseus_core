package de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.helper.IECStringHelper;

public class IECRouteInfo implements IIecElement {
	// Constants
	public static final String ROUTE_NAME = "routeName";
	public static final String ROUTE_AUTHOR = "routeAuthor";
	public static final String ROUTE_STATUS = "routeStatus";
	public static final String VALIDITY_PERIOD_START = "validityPeriodStart";
	public static final String VALIDITY_PERIOD_STOP = "validityPeriodStop";
	public static final String VESSEL_NAME = "vesselName";
	public static final String VESSEL_MMSI = "vesselMMSI";
	public static final String VESSEL_IMO = "vesselIMO";
	public static final String VESSEL_VOYAGE = "vesselVoyage";
	public static final String VESSEL_DISPLACEMENT = "vesselDisplacement";
	public static final String VESSEL_CARGO = "vesselCargo";
	public static final String VESSEL_GM = "vesselGM";
	public static final String OPTIMIZATION_METHOD = "optimizationMethod";
	public static final String VESSEL_MAX_ROLL = "vesselMaxRoll";
	public static final String VESSEL_MAX_WAVE = "vesselMaxWave";
	public static final String VESSEL_MAX_WIND = "vesselMaxWind";
	public static final String VESSEL_SPEED_MAX = "vesselSpeedMax";
	public static final String VESSEL_SERVICE_MIN = "vesselServiceMin";
	public static final String VESSEL_SERVICE_MAX = "vesselServiceMax";
	public static final String ROUTE_CHANGES_HISTORY = "routeChangesHistory";

	// map constants
	private static final String ELEMENT_PREFIX = "_rInfo";

	// XML Constants
	private static final String OPEN_TAG = "<routeInfo";
	private static final String CLOSE_TAG = "</routeInfo>";
	private static final String OPEN_EXTENSIONS_TAG = "<extensions>";
	private static final String CLOSE_EXTENSIONS_TAG = "</extensions>";

	// values
	private String routeName; // mandatory
	private String routeAuthor;
	private String routeStatus;
	private Date validityPeriodStart;
	private Date validityPeriodStop;
	private String vesselName;
	private Integer vesselMMSI;
	private Integer vesselIMO;
	private Integer vesselVoyage;
	private Integer vesselDisplacement;
	private Integer vesselCargo;
	private Double vesselGM;
	private String optimizationMethod;
	private Integer vesselMaxRoll;
	private Double vesselMaxWave;
	private Double vesselMaxWind;
	private Double vesselSpeedMax;
	private Double vesselServiceMin;
	private Double vesselServiceMax;
	private String routeChangesHistory;
	private List<IECExtension> extensions;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";
		
		if (routeName != null)
			map.addAttributeValue(elementPrefix+ROUTE_NAME, routeName);
		if (routeName != null)
			map.addAttributeValue(elementPrefix+ROUTE_AUTHOR, routeAuthor);
		if (routeStatus != null)
			map.addAttributeValue(elementPrefix+ROUTE_STATUS, routeStatus);
		if (validityPeriodStart != null)
			map.addAttributeValue(elementPrefix+VALIDITY_PERIOD_START, validityPeriodStart);
		if (validityPeriodStop != null)
			map.addAttributeValue(elementPrefix+VALIDITY_PERIOD_STOP, validityPeriodStop);
		if (vesselName != null)
			map.addAttributeValue(elementPrefix+VESSEL_NAME, vesselName);
		if (vesselMMSI != null)
			map.addAttributeValue(elementPrefix+VESSEL_MMSI, vesselMMSI);
		if (vesselIMO != null)
			map.addAttributeValue(elementPrefix+VESSEL_IMO, vesselIMO);
		if (vesselVoyage != null)
			map.addAttributeValue(elementPrefix+VESSEL_VOYAGE, vesselVoyage);
		if (vesselDisplacement != null)
			map.addAttributeValue(elementPrefix+VESSEL_DISPLACEMENT, vesselDisplacement);
		if (vesselCargo != null)
			map.addAttributeValue(elementPrefix+VESSEL_CARGO, vesselCargo);
		if (vesselGM != null)
			map.addAttributeValue(elementPrefix+VESSEL_GM, vesselGM);
		if (optimizationMethod != null)
			map.addAttributeValue(elementPrefix+OPTIMIZATION_METHOD, optimizationMethod);
		if (vesselMaxRoll != null)
			map.addAttributeValue(elementPrefix+VESSEL_MAX_ROLL, vesselMaxRoll);
		if (vesselMaxWave != null)
			map.addAttributeValue(elementPrefix+VESSEL_MAX_WAVE, vesselMaxWave);
		if (vesselMaxWind != null)
			map.addAttributeValue(elementPrefix+VESSEL_MAX_WIND, vesselMaxWind);
		if (vesselSpeedMax != null)
			map.addAttributeValue(elementPrefix+VESSEL_SPEED_MAX, vesselSpeedMax);
		if (vesselServiceMin != null)
			map.addAttributeValue(elementPrefix+VESSEL_SERVICE_MIN, vesselServiceMin);
		if (vesselServiceMax != null)
			map.addAttributeValue(elementPrefix+VESSEL_SERVICE_MAX, vesselServiceMax);
		if (routeChangesHistory != null)
			map.addAttributeValue(elementPrefix+ROUTE_CHANGES_HISTORY, routeChangesHistory);

		if (extensions != null) {
			for (IECExtension extension : extensions) {
				extension.fillMap(map, prefix);
			}
		}
	}

	@Override
	public String toXML() {
		StringBuilder builder = new StringBuilder(OPEN_TAG);

		if (routeName != null)
			IECStringHelper.appendStringElement(builder, ROUTE_NAME, routeName);
		if (routeAuthor != null)
			IECStringHelper.appendStringElement(builder, ROUTE_AUTHOR,
					routeAuthor);
		if (routeStatus != null)
			IECStringHelper.appendStringElement(builder, ROUTE_STATUS,
					routeStatus);
		if (validityPeriodStart != null)
			IECStringHelper.appendDateElement(builder, VALIDITY_PERIOD_START,
					validityPeriodStart);
		if (validityPeriodStop != null)
			IECStringHelper.appendDateElement(builder, VALIDITY_PERIOD_STOP,
					validityPeriodStop);
		if (vesselName != null)
			IECStringHelper.appendStringElement(builder, VESSEL_NAME,
					vesselName);
		if (vesselMMSI != null)
			IECStringHelper.appendIntElement(builder, VESSEL_MMSI, vesselMMSI);
		if (vesselIMO != null)
			IECStringHelper.appendIntElement(builder, VESSEL_IMO, vesselIMO);
		if (vesselVoyage != null)
			IECStringHelper.appendIntElement(builder, VESSEL_VOYAGE,
					vesselVoyage);
		if (vesselDisplacement != null)
			IECStringHelper.appendIntElement(builder, VESSEL_DISPLACEMENT,
					vesselDisplacement);
		if (vesselCargo != null)
			IECStringHelper
					.appendIntElement(builder, VESSEL_CARGO, vesselCargo);
		if (vesselGM != null)
			IECStringHelper.appendDoubleElement(builder, VESSEL_GM, vesselGM);
		if (optimizationMethod != null)
			IECStringHelper.appendStringElement(builder, OPTIMIZATION_METHOD,
					optimizationMethod);
		if (vesselMaxRoll != null)
			IECStringHelper.appendIntElement(builder, VESSEL_MAX_ROLL,
					vesselMaxRoll);
		if (vesselMaxWave != null)
			IECStringHelper.appendDoubleElement(builder, VESSEL_MAX_WAVE,
					vesselMaxWave);
		if (vesselMaxWind != null)
			IECStringHelper.appendDoubleElement(builder, VESSEL_MAX_WIND,
					vesselMaxWind);
		if (vesselSpeedMax != null)
			IECStringHelper.appendDoubleElement(builder, VESSEL_SPEED_MAX,
					vesselSpeedMax);
		if (vesselServiceMin != null)
			IECStringHelper.appendDoubleElement(builder, VESSEL_SERVICE_MIN,
					vesselServiceMin);
		if (vesselServiceMax != null)
			IECStringHelper.appendDoubleElement(builder, VESSEL_SERVICE_MAX,
					vesselServiceMax);
		if (routeChangesHistory != null)
			IECStringHelper.appendStringElement(builder, ROUTE_CHANGES_HISTORY,
					routeChangesHistory);

		if (!hasSubelements()) {
			builder.append("/>");
		} else {
			builder.append(">");
			if (extensions != null && !extensions.isEmpty()) {
				builder.append(OPEN_EXTENSIONS_TAG);
				for (IECExtension extension : extensions) {
					builder.append(extension.toXML());
				}
				builder.append(CLOSE_EXTENSIONS_TAG);
			}
			builder.append(CLOSE_TAG);
		}
		return builder.toString();
	}

	@Override
	public boolean hasSubelements() {
		if (extensions != null && !extensions.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isValid() {
		if (routeName != null && !routeName.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public void addExtension(IECExtension extension) {
		if (extensions == null)
			extensions = new ArrayList<IECExtension>();
		extensions.add(extension);
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public String getRouteAuthor() {
		return routeAuthor;
	}

	public void setRouteAuthor(String routeAuthor) {
		this.routeAuthor = routeAuthor;
	}

	public String getRouteStatus() {
		return routeStatus;
	}

	public void setRouteStatus(String routeStatus) {
		this.routeStatus = routeStatus;
	}

	public Date getValidityPeriodStart() {
		return validityPeriodStart;
	}

	public void setValidityPeriodStart(Date validityPeriodStart) {
		this.validityPeriodStart = validityPeriodStart;
	}

	public Date getValidityPeriodStop() {
		return validityPeriodStop;
	}

	public void setValidityPeriodStop(Date validityPeriodStop) {
		this.validityPeriodStop = validityPeriodStop;
	}

	public String getVesselName() {
		return vesselName;
	}

	public void setVesselName(String vesselName) {
		this.vesselName = vesselName;
	}

	public int getVesselMMSI() {
		return vesselMMSI;
	}

	public void setVesselMMSI(int vesselMMSI) {
		this.vesselMMSI = vesselMMSI;
	}

	public int getVesselIMO() {
		return vesselIMO;
	}

	public void setVesselIMO(int vesselIMO) {
		this.vesselIMO = vesselIMO;
	}

	public int getVesselVoyage() {
		return vesselVoyage;
	}

	public void setVesselVoyage(int vesselVoyage) {
		this.vesselVoyage = vesselVoyage;
	}

	public int getVesselDisplacement() {
		return vesselDisplacement;
	}

	public void setVesselDisplacement(int vesselDisplacement) {
		this.vesselDisplacement = vesselDisplacement;
	}

	public int getVesselCargo() {
		return vesselCargo;
	}

	public void setVesselCargo(int vesselCargo) {
		this.vesselCargo = vesselCargo;
	}

	public double getVesselGM() {
		return vesselGM;
	}

	public void setVesselGM(double vesselGM) {
		this.vesselGM = vesselGM;
	}

	public String getOptimizationMethod() {
		return optimizationMethod;
	}

	public void setOptimizationMethod(String optimizationMethod) {
		this.optimizationMethod = optimizationMethod;
	}

	public int getVesselMaxRoll() {
		return vesselMaxRoll;
	}

	public void setVesselMaxRoll(int vesselMaxRoll) {
		this.vesselMaxRoll = vesselMaxRoll;
	}

	public double getVesselMaxWave() {
		return vesselMaxWave;
	}

	public void setVesselMaxWave(double vesselMaxWave) {
		this.vesselMaxWave = vesselMaxWave;
	}

	public double getVesselMaxWind() {
		return vesselMaxWind;
	}

	public void setVesselMaxWind(double vesselMaxWind) {
		this.vesselMaxWind = vesselMaxWind;
	}

	public double getVesselSpeedMax() {
		return vesselSpeedMax;
	}

	public void setVesselSpeedMax(double vesselSpeedMax) {
		this.vesselSpeedMax = vesselSpeedMax;
	}

	public double getVesselServiceMin() {
		return vesselServiceMin;
	}

	public void setVesselServiceMin(double vesselServiceMin) {
		this.vesselServiceMin = vesselServiceMin;
	}

	public double getVesselServiceMax() {
		return vesselServiceMax;
	}

	public void setVesselServiceMax(double vesselServiceMax) {
		this.vesselServiceMax = vesselServiceMax;
	}

	public String getRouteChangesHistory() {
		return routeChangesHistory;
	}

	public void setRouteChangesHistory(String routeChangesHistory) {
		this.routeChangesHistory = routeChangesHistory;
	}

	@Override
	public List<IECExtension> getExtensions() {
		return extensions;
	}

	public void setExtensions(List<IECExtension> extensions) {
		this.extensions = extensions;
	}
}
