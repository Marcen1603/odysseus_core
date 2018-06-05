package de.uniol.inf.is.odysseus.server.xml.logicaloperator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.FileParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "XMLTransform", doc = "Transform a given XML document with XSLT.", category = {LogicalOperatorCategory.TRANSFORM })
public class XMLTransformAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -4970136188613426865L;

	private OptionMap optionsMap = new OptionMap();
	private List<Option> optionsList = new ArrayList<>();
	private Path xsdFile;
	private String xsdString;
	private boolean dynamicXslt;
	
	public XMLTransformAO() {}

	public XMLTransformAO(XMLTransformAO ao) {
		super(ao);
		
		this.optionsList = ao.getOptionsList();
		this.optionsMap = ao.getOptionsMap();
		this.xsdFile = ao.getXsdFile();
		this.xsdString = ao.getXsdString();
		this.dynamicXslt = ao.isDynamic();
	}
	
	@Parameter(type = FileParameter.class, name = "xsltFile", optional = true)
	public void setXSDFile(File xsdFile) throws IOException {
		this.xsdFile = Paths.get(xsdFile.toURI());
	}
	
	@Parameter(type = StringParameter.class, name = "xsltString", optional = true)
	public void setXSDString(String xsdString) {
		this.xsdString = xsdString;
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
	
	@Parameter(type = BooleanParameter.class, name = "dynamic", optional = true) 
	public void setDynamicXslt(boolean b) {
		dynamicXslt = b;
	}
	
	public List<Option> getOptionsList() {
		return optionsList;
	}

	public OptionMap getOptionsMap() {
		return optionsMap;
	}

	public String getXsdString() {
		return xsdString;
	}

	public Path getXsdFile() {
		return xsdFile;
	}

	public boolean isDynamic() {
		return dynamicXslt;
	}

	public @Override XMLTransformAO clone() {
		return new XMLTransformAO(this);
	}

	public IMetaAttribute getLocalMetaAttribute() {
		return null;
	}

	public boolean readMetaData() {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		Collection<SDFAttribute> emptyAttributes = new ArrayList<>();
		SDFSchema newOutputSchema = SDFSchemaFactory.createNewSchema(getInputSchema(pos).getURI(),
				(Class<? extends IStreamObject<?>>) XMLStreamObject.class, emptyAttributes, getInputSchema());
		setOutputSchema(newOutputSchema);
		return newOutputSchema;
	}

}
