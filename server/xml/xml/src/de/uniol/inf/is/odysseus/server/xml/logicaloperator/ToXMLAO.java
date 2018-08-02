package de.uniol.inf.is.odysseus.server.xml.logicaloperator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.FileParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "ToXML", doc = "Constructs an XML object from a tuple.", category = {LogicalOperatorCategory.TRANSFORM })
public class ToXMLAO extends UnaryLogicalOp {
	
	private static final long serialVersionUID = -7878339457198812181L;
	
	private OptionMap optionsMap = new OptionMap();
	private List<Option> optionsList = new ArrayList<>();
	private Path xsdFile;
	private String xsdString;
	private String xsdAttribute;
	private String rootElement;
	private String rootElementNamespaceURI;
	private String rootAttribute;
	private Collection<String> xpathAttributes = new ArrayList<>(); 

	public ToXMLAO() {
		super();
	}

	public ToXMLAO(ToXMLAO ao) {
		super(ao);
		
		this.optionsList = ao.getOptionsList();
		this.optionsMap = ao.getOptionsMap();
		this.xsdAttribute = ao.getXsdAttribute();
		this.xsdFile = ao.getXsdFile();
		this.xsdString = ao.getXsdString();
		this.rootElement = ao.getRootElement();
		this.rootElementNamespaceURI = ao.getRootElementNamespaceURI();
		this.rootAttribute = ao.getRootAttribute();
		this.xpathAttributes = ao.getXPathAttributes();
		
	}

	public @Override ToXMLAO clone() {
		return new ToXMLAO(this);
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		
		Collection<SDFAttribute> emptyAttributes = new ArrayList<>();

		@SuppressWarnings("unchecked")
		SDFSchema newOutputSchema = SDFSchemaFactory.createNewSchema(
				getInputSchema(pos).getURI(),
				(Class<? extends IStreamObject<?>>) XMLStreamObject.class, 
				emptyAttributes, 
				getInputSchema()
		);
		
		setOutputSchema(newOutputSchema);
		return newOutputSchema;
	}
	
	public IMetaAttribute getLocalMetaAttribute() {
		return null;
	}

	public boolean readMetaData() {
		return false;
	}
	
	
	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "xPathAttributes", isList = true, optional = true)
	public void setXPathAttributes(List<SDFAttribute> attributes) {
		this.xpathAttributes = attributes.stream().map(e -> e.getAttributeName()).collect(Collectors.toList());
	}
	
	@Parameter(type = FileParameter.class, name = "xsdFile", optional = true)
	public void setXSDFile(File xsdFile) throws IOException {
		this.xsdFile = Paths.get(xsdFile.toURI());
	}
	
	@Parameter(type = StringParameter.class, name = "xsdString", optional = true)
	public void setXSDString(String xsdString) {
		this.xsdString = xsdString;
	}
	
	@Parameter(type = StringParameter.class, name = "xsdAttribute", optional = true)
	public void setXSDAttribute(String xsdAttribute) {
		this.xsdAttribute = xsdAttribute;
	}
	
	@Parameter(type = StringParameter.class, name = "rootElementNamespaceURI", optional = true)
	public void setRootElementNamespaceURI(String rootElementNamespaceURI) {
		this.rootElementNamespaceURI = rootElementNamespaceURI;
	}
	
	@Parameter(type = StringParameter.class, name = "rootElement", optional = true)
	public void setRootElement(String root) {
		this.rootElement = root;
	}
	
	@Parameter(type = StringParameter.class, name = "rootAttribute", optional = true)
	public void setRootAttribute(String root) {
		this.rootAttribute = root;
	}
	
	@Parameter(type = OptionParameter.class, name = "options", optional = true, isList = true, doc = "Additional options.")
	public void setOptions(List<Option> value) {
		if (value != null) {
			for (Option option : value) {
				if (value != null) {
					optionsMap.setOption(option.getName(), option.getValue());
				}
			}
			optionsList = value;
		}
	}

	public OptionMap getOptionsMap() {
		return optionsMap;
	}

	public List<Option> getOptionsList() {
		return optionsList;
	}

	public Path getXsdFile() {
		return xsdFile;
	}

	public String getXsdString() {
		return xsdString;
	}	
	
	public String getXsdAttribute() {
		return xsdAttribute;
	}
	
	public String getRootElement() {
		return rootElement;
	}
	
	public String getRootElementNamespaceURI() {
		return rootElementNamespaceURI;
	}
	
	public String getRootAttribute() {
		return rootAttribute;
	}
	
	public Collection<String> getXPathAttributes() {
		return xpathAttributes;
	}
	
}
