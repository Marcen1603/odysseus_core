package de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.enums.GeometryType;
import de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.helper.IECStringHelper;

public class IECLeg implements IIecElement {
	// Constants
	public static final String STARBOARD_XTD = "starboardXTD";
	public static final String PORTSIDE_XTD = "portsideXTD";
	public static final String SAFETY_CONTOUR = "safetyContour";
	public static final String SAFETY_DEPTH = "safetyDepth";
	public static final String GEOMETRY_TYPE = "geometryType";
	public static final String SPEED_MIN = "speedMin";
	public static final String SPEED_MAX = "speedMax";
	public static final String DRAUGHT = "draught";
	public static final String DRAUGHT_FORWARD = "draughtForward";
	public static final String DRAUGHT_AFT = "draughtAft";
	public static final String STATIC_UKC = "staticUKC";
	public static final String DYNAMIC_UKC = "dynamicUKC";
	public static final String MASTHEAD = "masthead";
	public static final String LEG_REPORT = "legReport";
	public static final String LEG_INFO = "legInfo";
	public static final String LEG_NOTE1 = "legNote1";
	public static final String LEG_NOTE2 = "legNote2";

	// map constants
	private static final String ELEMENT_PREFIX = "_leg";

	// XML Constants
	private static final String OPEN_TAG = "<leg";
	private static final String CLOSE_TAG = "</scheduleElement>";
	private static final String OPEN_EXTENSIONS_TAG = "<extensions>";
	private static final String CLOSE_EXTENSIONS_TAG = "</extensions>";

	// values
	private Double starboardXTD;
	private Double portsideXTD;
	private Double safetyContour;
	private Double safetyDepth;
	private GeometryType geometryType;
	private Double speedMin;
	private Double speedMax;
	private Double draught;
	private Double draughtForward;
	private Double draughtAft;
	private Double staticUKC;
	private Double dynamicUKC;
	private Double masthead;
	private String legReport;
	private String legInfo;
	private String legNote1;
	private String legNote2;
	private List<IECExtension> extensions;

	@Override
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map,
			String prefix) {
		prefix += ELEMENT_PREFIX;
		String elementPrefix = prefix + "_";
		
		if (starboardXTD != null)
			map.addAttributeValue(elementPrefix+STARBOARD_XTD, starboardXTD);
		if (portsideXTD != null)
			map.addAttributeValue(elementPrefix+PORTSIDE_XTD, portsideXTD);
		if (safetyContour != null)
			map.addAttributeValue(elementPrefix+SAFETY_CONTOUR, safetyContour);
		if (safetyDepth != null)
			map.addAttributeValue(elementPrefix+SAFETY_DEPTH, safetyDepth);
		if (geometryType != null)
			map.addAttributeValue(elementPrefix+GEOMETRY_TYPE, geometryType.toString());
		if (speedMin != null)
			map.addAttributeValue(elementPrefix+SPEED_MIN, speedMin);
		if (speedMax != null)
			map.addAttributeValue(elementPrefix+SPEED_MAX, speedMax);
		if (draught != null)
			map.addAttributeValue(elementPrefix+DRAUGHT, draught);
		if (draughtForward != null)
			map.addAttributeValue(elementPrefix+DRAUGHT_FORWARD, draughtForward);
		if (draughtAft != null)
			map.addAttributeValue(elementPrefix+DRAUGHT_AFT, draughtAft);
		if (staticUKC != null)
			map.addAttributeValue(elementPrefix+STATIC_UKC, staticUKC);
		if (dynamicUKC != null)
			map.addAttributeValue(elementPrefix+DYNAMIC_UKC, dynamicUKC);
		if (masthead != null)
			map.addAttributeValue(elementPrefix+MASTHEAD, masthead);
		if (legReport != null)
			map.addAttributeValue(elementPrefix+LEG_REPORT, legReport);
		if (legInfo != null)
			map.addAttributeValue(elementPrefix+LEG_INFO, legInfo);
		if (legNote1 != null)
			map.addAttributeValue(elementPrefix+LEG_NOTE1, legNote1);
		if (legNote2 != null)
			map.addAttributeValue(elementPrefix+LEG_NOTE2, legNote2);
	}

	@Override
	public String toXML() {
		StringBuilder builder = new StringBuilder(OPEN_TAG);
		if (starboardXTD != null)
			IECStringHelper.appendDoubleElement(builder, STARBOARD_XTD,
					starboardXTD);
		if (portsideXTD != null)
			IECStringHelper.appendDoubleElement(builder, PORTSIDE_XTD,
					portsideXTD);
		if (safetyContour != null)
			IECStringHelper.appendDoubleElement(builder, SAFETY_CONTOUR,
					safetyContour);
		if (safetyDepth != null)
			IECStringHelper.appendDoubleElement(builder, SAFETY_DEPTH,
					safetyDepth);
		if (geometryType != null)
			IECStringHelper.appendStringElement(builder, GEOMETRY_TYPE,
					geometryType.toString());
		if (speedMin != null)
			IECStringHelper.appendDoubleElement(builder, SPEED_MIN, speedMin);
		if (speedMax != null)
			IECStringHelper.appendDoubleElement(builder, SPEED_MAX, speedMax);
		if (draught != null)
			IECStringHelper.appendDoubleElement(builder, DRAUGHT, draught);
		if (draughtForward != null)
			IECStringHelper.appendDoubleElement(builder, DRAUGHT_FORWARD,
					draughtForward);
		if (draughtAft != null)
			IECStringHelper.appendDoubleElement(builder, DRAUGHT_AFT,
					draughtAft);
		if (staticUKC != null)
			IECStringHelper.appendDoubleElement(builder, STATIC_UKC, staticUKC);
		if (dynamicUKC != null)
			IECStringHelper.appendDoubleElement(builder, DYNAMIC_UKC,
					dynamicUKC);
		if (masthead != null)
			IECStringHelper.appendDoubleElement(builder, MASTHEAD, masthead);
		if (legReport != null)
			IECStringHelper.appendStringElement(builder, LEG_REPORT, legReport);
		if (legInfo != null)
			IECStringHelper.appendStringElement(builder, LEG_INFO, legInfo);
		if (legNote1 != null)
			IECStringHelper.appendStringElement(builder, LEG_NOTE1, legNote1);
		if (legNote2 != null)
			IECStringHelper.appendStringElement(builder, LEG_NOTE2, legNote2);

		if (!hasSubelements()) {
			builder.append("/>");
		} else {
			builder.append(">");
			if (extensions != null) {
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
		if (extensions == null || extensions.isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public void addExtension(IECExtension extension) {
		if (extensions == null)
			extensions = new ArrayList<IECExtension>();
		extensions.add(extension);
	}

	public Double getStarboardXTD() {
		return starboardXTD;
	}

	public void setStarboardXTD(Double starboardXTD) {
		this.starboardXTD = starboardXTD;
	}

	public Double getPortsideXTD() {
		return portsideXTD;
	}

	public void setPortsideXTD(Double portsideXTD) {
		this.portsideXTD = portsideXTD;
	}

	public Double getSafetyContour() {
		return safetyContour;
	}

	public void setSafetyContour(Double safetyContour) {
		this.safetyContour = safetyContour;
	}

	public Double getSafetyDepth() {
		return safetyDepth;
	}

	public void setSafetyDepth(Double safetyDepth) {
		this.safetyDepth = safetyDepth;
	}

	public GeometryType getGeometryType() {
		return geometryType;
	}

	public void setGeometryType(GeometryType geometryType) {
		this.geometryType = geometryType;
	}

	public Double getSpeedMin() {
		return speedMin;
	}

	public void setSpeedMin(Double speedMin) {
		this.speedMin = speedMin;
	}

	public Double getSpeedMax() {
		return speedMax;
	}

	public void setSpeedMax(Double speedMax) {
		this.speedMax = speedMax;
	}

	public Double getDraught() {
		return draught;
	}

	public void setDraught(Double draught) {
		this.draught = draught;
	}

	public Double getDraughtForward() {
		return draughtForward;
	}

	public void setDraughtForward(Double draughtForward) {
		this.draughtForward = draughtForward;
	}

	public Double getDraughtAft() {
		return draughtAft;
	}

	public void setDraughtAft(Double draughtAft) {
		this.draughtAft = draughtAft;
	}

	public Double getStaticUKC() {
		return staticUKC;
	}

	public void setStaticUKC(Double staticUKC) {
		this.staticUKC = staticUKC;
	}

	public Double getDynamicUKC() {
		return dynamicUKC;
	}

	public void setDynamicUKC(Double dynamicUKC) {
		this.dynamicUKC = dynamicUKC;
	}

	public Double getMasthead() {
		return masthead;
	}

	public void setMasthead(Double masthead) {
		this.masthead = masthead;
	}

	public String getLegReport() {
		return legReport;
	}

	public void setLegReport(String legReport) {
		this.legReport = legReport;
	}

	public String getLegInfo() {
		return legInfo;
	}

	public void setLegInfo(String legInfo) {
		this.legInfo = legInfo;
	}

	public String getLegNote1() {
		return legNote1;
	}

	public void setLegNote1(String legNote1) {
		this.legNote1 = legNote1;
	}

	public String getLegNote2() {
		return legNote2;
	}

	public void setLegNote2(String legNote2) {
		this.legNote2 = legNote2;
	}

	@Override
	public List<IECExtension> getExtensions() {
		return extensions;
	}

	public void setExtensions(List<IECExtension> extensions) {
		this.extensions = extensions;
	}

}
