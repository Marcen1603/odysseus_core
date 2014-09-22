package de.uniol.inf.is.odysseus.wrapper.shiproutes.conversion.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;
import de.uniol.inf.is.odysseus.wrapper.ivef.IVEFVersion;

@LogicalOperator(name = "SHIPROUTECONVERTER", minInputPorts = 1, maxInputPorts = 1, doc = "This operator is used to convert ship route messages into IEC messages and vice versa.", category = { LogicalOperatorCategory.PROCESSING })
public class ShipRouteConverterAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	private ConversionType conversionType;

	private IVEFVersion ivefVersion = IVEFVersion.V015;
	
	public ShipRouteConverterAO() {
		super();
	}

	public ShipRouteConverterAO(ShipRouteConverterAO converterAO) {
		super(converterAO);
		this.conversionType = converterAO.getConversionType();
		this.ivefVersion = converterAO.getIvefVersion();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ShipRouteConverterAO(this);
	}

	@Parameter(name = "conversionType", type = EnumParameter.class, isList = false, optional = false, doc = "The conversion type between shipRoute messages: JSON_TO_IEC, "
			+ "JSON_NMEA_TO_IVEF, IEC_TO_JSON_ROUTE, IEC_TO_JSON_MANOEUVRE, IEC_TO_JSON_PREDICTION, IEC_NMEA_TO_IVEF, IVEF_TO_JSON_ROUTE, IVEF_TO_JSON_MANOEUVRE, IVEF_TO_JSON_PREDICTION")
	public void setConversionType(ConversionType conversionType) {
		this.conversionType = conversionType;
	}
	
	@Parameter(name="ivefVersion", type=EnumParameter.class, isList=false, optional=true, doc="The version of IVEF elements: v015 (0.1.5), v025 (0.2.5)")
	public void setIVEFVersion(IVEFVersion ivefVersion) {
		this.ivefVersion= ivefVersion; 
	} 
	
	public ConversionType getConversionType() {
		return this.conversionType;
	}
	
	public IVEFVersion getIvefVersion() {
		return ivefVersion;
	}

}
