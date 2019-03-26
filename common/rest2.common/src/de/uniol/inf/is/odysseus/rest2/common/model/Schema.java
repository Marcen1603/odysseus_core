/*
 * 
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package de.uniol.inf.is.odysseus.rest2.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * Schema
 */
public class Schema {
	@JsonProperty("uri")
	private String uri = null;

	@JsonProperty("typeClass")
	private String typeClass = null;

	@JsonProperty("attributes")
	private List<Attribute> attributes = null;

	@JsonProperty("metaschema")
	private List<Metaschema> metaschema = null;

	public Schema uri(String uri) {
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

	public Schema typeClass(String typeClass) {
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

	public Schema attributes(List<Attribute> attributes) {
		this.attributes = attributes;
		return this;
	}

	public Schema addAttributesItem(Attribute attributesItem) {
		if (this.attributes == null) {
			this.attributes = new ArrayList<>();
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

	@ApiModelProperty(value = "")
	public void setMetaschema(List<Metaschema> metaschema) {
		this.metaschema = metaschema;
	}

	public List<Metaschema> getMetaschema() {
		return metaschema;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Schema schema = (Schema) o;
		return Objects.equals(this.uri, schema.uri) && Objects.equals(this.typeClass, schema.typeClass)
				&& Objects.equals(this.attributes, schema.attributes)
				&& Objects.equals(this.metaschema, schema.metaschema);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uri, typeClass, attributes);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Schema {\n");

		sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
		sb.append("    typeClass: ").append(toIndentedString(typeClass)).append("\n");
		sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
		sb.append("    meta schemes: ").append(toIndentedString(metaschema)).append("\n");
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
