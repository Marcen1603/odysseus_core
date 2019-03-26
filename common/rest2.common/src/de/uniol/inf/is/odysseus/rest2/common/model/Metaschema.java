package de.uniol.inf.is.odysseus.rest2.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

public class Metaschema {
	
	@JsonProperty("metaschemauri")
	private String uri = null;

	@JsonProperty("metaschematypeClass")
	private String typeClass = null;

	@JsonProperty("metaschemaattributes")
	private List<Attribute> attributes = null;


	@JsonProperty("metaschemametaattributeClass")
	private String metaattributeClass = null;

	public Metaschema() {
	}

	public Metaschema(Schema schema, String metaAttributeClass) {
		this.attributes = schema.getAttributes();
		this.uri = schema.getUri();
		this.typeClass = schema.getTypeClass();
		this.metaattributeClass = metaAttributeClass;
	}

	public void setMetaattributeClass(String metaattributeClass) {
		this.metaattributeClass = metaattributeClass;
	}

	@ApiModelProperty(value = "")
	public String getMetaattributeClass() {
		return metaattributeClass;
	}
	
	public Metaschema uri(String uri) {
		this.uri = uri;
		return this;
	}

	/**
	 * Get uri
	 * 
	 * @return uri
	 **/
	@ApiModelProperty(value = "")
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Metaschema typeClass(String typeClass) {
		this.typeClass = typeClass;
		return this;
	}

	/**
	 * Get typeClass
	 * 
	 * @return typeClass
	 **/
	@ApiModelProperty(value = "")
	public String getTypeClass() {
		return typeClass;
	}

	public void setTypeClass(String typeClass) {
		this.typeClass = typeClass;
	}

	public Metaschema attributes(List<Attribute> attributes) {
		this.attributes = attributes;
		return this;
	}

	public Metaschema addAttributesItem(Attribute attributesItem) {
		if (this.attributes == null) {
			this.attributes = new ArrayList<Attribute>();
		}
		this.attributes.add(attributesItem);
		return this;
	}

	/**
	 * Get attributes
	 * 
	 * @return attributes
	 **/
	@ApiModelProperty(value = "")
	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(metaattributeClass);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Metaschema other = (Metaschema) obj;
		return Objects.equals(this, other);
	}

	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Metaschema {\n");

		sb.append("    uri: ").append(toIndentedString(getUri())).append("\n");
		sb.append("    metaattribute ").append(toIndentedString(metaattributeClass)).append("\n");
		sb.append("    typeClass: ").append(toIndentedString(getTypeClass())).append("\n");

		sb.append("    attributes: ").append(toIndentedString(getAttributes())).append("\n");

		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	protected String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
