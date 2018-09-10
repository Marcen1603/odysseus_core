package de.uniol.inf.is.odysseus.recommendation.lod.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author Christopher Schwarz
 */
@LogicalOperator(name = "LOD_ENRICH", minInputPorts = 1, maxInputPorts = 1, category = {LogicalOperatorCategory.ENRICH}, doc = "Enriches a stream with linked open data.")
public class LODEnrichAO extends UnaryLogicalOp {
	private static final long serialVersionUID = -5480581520336543686L;

	private String attribute = null;
	private String predicate = null;
	private String title = null;
	private String type = "XML";
	private NamedExpression url = null;

	/**
	 * Default constructor.
	 */
	public LODEnrichAO() {
		super();
	}

	/**
	 * Copy constructor.
	 */
	public LODEnrichAO(LODEnrichAO operator) {
		super(operator);
		attribute = operator.getAttribute();
		predicate = operator.getPredicate();
		title = operator.getTitle();
		type = operator.getType();
		url = operator.getURL();
	}
	
	@Override
	public LODEnrichAO clone() {
		return new LODEnrichAO(this);
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFAttribute sttribute = new SDFAttribute("lod", (title == null ? "undefined" : title), SDFDatatype.STRING);
		SDFSchema inputSchema = getInputSchema();

		SDFSchema outputSchema = SDFSchemaFactory.createNewAddAttribute(sttribute, inputSchema);
		
		setOutputSchema(outputSchema);
		return outputSchema;
	}

	/**
	 * Defines the attribute, which contains the required information in an xml-file.
	 * Example: <dbp:author rdf:resource="http://dbpedia.org/resource/J._K._Rowling"/>
	 * If 'attribute' is not defined, the content of the tag will be used.
	 * Example: <dbp:language xml:lang="en">English</dbp:language>
	 * @param attribute The attribute, which contains the required information.
	 */
	@Parameter(type = StringParameter.class, name = "attribute", optional = true, doc = "The attribute, which contains the required information in an xml-file.")
	public void setAttribute(String attribute) {
	    this.attribute = attribute;
	}

	/**
	 * Defines the predicate that refers to the required information.
	 * @param predicate The predicate, that refers to the required information.
	 */
	@Parameter(type = StringParameter.class, name = "predicate", optional = false, doc = "The predicate, that refers to the required information.")
	public void setPredicate(String predicate) {
	    this.predicate = predicate;
	}

	/**
	 * Defines the attribute name.
	 * @param title The attribute name.
	 */
	@Parameter(type = StringParameter.class, name = "title", optional = true, doc = "The name of the attribute, that will be added.")
	public void setTitle(String title) {
	    this.title = title;
	}

	/**
	 * Defines the type of the source (currently only XML).
	 * @param type The source type.
	 */
	@Parameter(type = StringParameter.class, name = "type", optional = true, doc = "The type of the source (currently only XML).")
	public void setType(String type) {
	    this.type = type;
	}

	/**
	 * Defines the attribute name, in which the URL is stored.
	 * @param url The attribute name storing the URL.
	 */
	@Parameter(type = NamedExpressionParameter.class, name = "url", optional = false, doc = "The attribute name in which the url is stored.")
	public void setURL(NamedExpression url) {
	    this.url = url;
	}

	public String getAttribute() {
		return attribute;
	}

	public String getPredicate() {
		return predicate;
	}

	public String getTitle() {
		return title;
	}

	public String getType() {
		return type;
	}

	public NamedExpression getURL() {
		return url;
	}
}
