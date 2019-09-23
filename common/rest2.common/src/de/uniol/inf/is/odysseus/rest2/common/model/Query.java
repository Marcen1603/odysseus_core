package de.uniol.inf.is.odysseus.rest2.common.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.uniol.inf.is.odysseus.rest2.common.model.Operator;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * Query
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-03-27T10:38:43.789+01:00[Europe/Berlin]")
public class Query   {
  @JsonProperty("id")
  private Integer id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("parser")
  private String parser;

  @JsonProperty("queryText")
  private String queryText;

  @JsonProperty("state")
  private String state;

  @JsonProperty("user")
  private String user;

  @JsonProperty("rootOperators")
  private List<Operator> rootOperators = null;

  public Query id(Integer id) {
    this.id = id;
    return this;
  }

   /**
   * The ID of the query.
   * @return id
  **/
  @ApiModelProperty(value = "The ID of the query.")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Query name(String name) {
    this.name = name;
    return this;
  }

   /**
   * The name of the query.
   * @return name
  **/
  @ApiModelProperty(value = "The name of the query.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Query parser(String parser) {
    this.parser = parser;
    return this;
  }

   /**
   * The parser that should be used to parse the query text.
   * @return parser
  **/
  @ApiModelProperty(value = "The parser that should be used to parse the query text.")
  public String getParser() {
    return parser;
  }

  public void setParser(String parser) {
    this.parser = parser;
  }

  public Query queryText(String queryText) {
    this.queryText = queryText;
    return this;
  }

   /**
   * The query text.
   * @return queryText
  **/
  @ApiModelProperty(value = "The query text.")
  public String getQueryText() {
    return queryText;
  }

  public void setQueryText(String queryText) {
    this.queryText = queryText;
  }

  public Query state(String state) {
    this.state = state;
    return this;
  }

   /**
   * The state of the query.
   * @return state
  **/
  @ApiModelProperty(value = "The state of the query.")
  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Query user(String user) {
    this.user = user;
    return this;
  }

   /**
   * The user that created the query.
   * @return user
  **/
  @ApiModelProperty(value = "The user that created the query.")
  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public Query rootOperators(List<Operator> rootOperators) {
    this.rootOperators = rootOperators;
    return this;
  }

  public Query addRootOperatorsItem(Operator rootOperatorsItem) {
    if (this.rootOperators == null) {
      this.rootOperators = new ArrayList<Operator>();
    }
    this.rootOperators.add(rootOperatorsItem);
    return this;
  }

   /**
   * Get rootOperators
   * @return rootOperators
  **/
  @ApiModelProperty(value = "")
  public List<Operator> getRootOperators() {
    return rootOperators;
  }

  public void setRootOperators(List<Operator> rootOperators) {
    this.rootOperators = rootOperators;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Query query = (Query) o;
    return Objects.equals(this.id, query.id) &&
        Objects.equals(this.name, query.name) &&
        Objects.equals(this.parser, query.parser) &&
        Objects.equals(this.queryText, query.queryText) &&
        Objects.equals(this.state, query.state) &&
        Objects.equals(this.user, query.user) &&
        Objects.equals(this.rootOperators, query.rootOperators);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, parser, queryText, state, user, rootOperators);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Query {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    parser: ").append(toIndentedString(parser)).append("\n");
    sb.append("    queryText: ").append(toIndentedString(queryText)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
    sb.append("    rootOperators: ").append(toIndentedString(rootOperators)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

