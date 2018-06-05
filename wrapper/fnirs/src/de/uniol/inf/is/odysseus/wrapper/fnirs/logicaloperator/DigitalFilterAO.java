package de.uniol.inf.is.odysseus.wrapper.fnirs.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * 
 * @author Henrik Surm
 * 
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "DIGITALFILTER", doc = "This operator applies a digital filter to elements of a tuple.", category = { LogicalOperatorCategory.TRANSFORM })
public class DigitalFilterAO extends UnaryLogicalOp {

    private static final long serialVersionUID = -1992998023364461468L;
	private String filterType;
	private String passType;
	private double cornerFreq1;
	private double cornerFreq2;
	private double samplingFreq;
	private double rippleAttenuation;
	private int order;
	private int byteBufferSampleDepth;
	private List<SDFAttribute> attributes = new ArrayList<>();

    public DigitalFilterAO() {
        super();
    }

    public DigitalFilterAO(DigitalFilterAO other) {
        super(other);
        
        filterType = other.filterType;
        passType = other.passType;
        samplingFreq = other.samplingFreq;
        cornerFreq1 = other.cornerFreq1;
        cornerFreq2 = other.cornerFreq2;
        rippleAttenuation = other.rippleAttenuation;
        order = other.order;
        byteBufferSampleDepth = other.byteBufferSampleDepth;
        
        attributes = new ArrayList<>(other.attributes);
    }

    @Parameter(type = StringParameter.class, optional = false, name = "filtertype", doc = "The filter type of this digital filter. Can be Bessel, Butterworth or Chebychev")
	public void setFilterType(String filterType) 
	{
		this.filterType = filterType;
	}        
    
	public String getFilterType() 
	{
		return filterType;
	}	

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", optional = true, isList = true, doc = "A list of attributes that will be filtered.")
	public void setAttributes(List<SDFAttribute> attributes) 
	{
		this.attributes = attributes;
	}
	
	public List<SDFAttribute> getAttributes() {
		return attributes;
	}
	
    @Parameter(type = StringParameter.class, optional = false, name = "passtype", doc = "The pass type of this digital filter. Can be Lowpass, Highpass or Bandpass")
	public void setPassType(String passType) 
	{
		this.passType = passType;
	}        
    
	public String getPassType() 
	{
		return passType;
	}	
	
	@Parameter(type = IntegerParameter.class, optional = false, name = "order", doc = "The order of this digital filter")
	public void setOrder(int order) 
	{
		this.order = order;
	}        
    	
	public int getOrder() 
	{
		return order;
	}	
	
	@Parameter(type = DoubleParameter.class, optional = false, name = "samplingfreq", doc = "Sampling frequency of the filter")
	public void setSamplingFreq(double samplingFreq) 
	{
		this.samplingFreq = samplingFreq;
	}        
    
	public double getSamplingFreq() 
	{
		return samplingFreq;
	}			
	
	@Parameter(type = DoubleParameter.class, optional = false, name = "cornerfreq1", doc = "First corner frequency of the filter")
	public void setCornerFreq1(double cornerFreq1) 
	{
		this.cornerFreq1 = cornerFreq1;
	}        
    
	public double getCornerFreq1() 
	{
		return cornerFreq1;
	}		
	
	@Parameter(type = DoubleParameter.class, optional = true, name = "cornerfreq2", doc = "Second corner frequency of the filter. Must be set for band pass")
	public void setCornerFreq2(double cornerFreq2) 
	{
		this.cornerFreq2 = cornerFreq2;
	}        
    
	public double getCornerFreq2() 
	{
		return cornerFreq2;
	}			

	@Parameter(type = IntegerParameter.class, optional = true, name = "bytebuffersampledepth", doc = "Bits per sample when using a byte buffer as input. Allowed values: 16 or 32")
	public void setByteBufferSampleDepth(int byteBufferSampleDepth) 
	{
		this.byteBufferSampleDepth = byteBufferSampleDepth;
	}        
    
	public int getByteBufferSampleDepth() 
	{
		return byteBufferSampleDepth;
	}				
	
	@Parameter(type = DoubleParameter.class, optional = true, name = "rippleAttenuation", doc = "Ripple attenuation of the filter. Must be set to a negative value for chebychev filters")
	public void setRippleAttenuation(double rippleAttenuation) 
	{
		this.rippleAttenuation = rippleAttenuation;
	}        
    
	public double getRippleAttenuation() 
	{
		return rippleAttenuation;
	}
	
	public int[] getAttributePositions() {
		SDFSchema inputSchema = getInputSchema();
		if (attributes.size() > 0) {
			int[] ret = new int[attributes.size()];
			int i = 0;
			for (SDFAttribute a : attributes) {
				ret[i++] = inputSchema.indexOf(a);
			}
			return ret;
		} 
		return null;
	}	
	
    @Override
    public AbstractLogicalOperator clone() {
        return new DigitalFilterAO(this);
    }
}
