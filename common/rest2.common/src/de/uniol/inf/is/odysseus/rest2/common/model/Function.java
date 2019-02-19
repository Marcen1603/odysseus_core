package de.uniol.inf.is.odysseus.rest2.common.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import de.uniol.inf.is.odysseus.rest2.common.model.Datatype;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * A [functions or operations (MEP)](https://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/MEP%3A+Functions+and+Operators) that can be used in operators like [MAP](https://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/Map+operator).
 */
@ApiModel(description = "A [functions or operations (MEP)](https://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/MEP%3A+Functions+and+Operators) that can be used in operators like [MAP](https://wiki.odysseus.informatik.uni-oldenburg.de/display/ODYSSEUS/Map+operator).")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-19T10:53:43.202Z[GMT]")
public class Function   {
  @JsonProperty("symbol")
  private String symbol;

  @JsonProperty("parameters")
  private List<List<Datatype>> parameters = null;

  public Function symbol(String symbol) {
    this.symbol = symbol;
    return this;
  }

   /**
   * The symbol or name of the function.
   * @return symbol
  **/
  @ApiModelProperty(value = "The symbol or name of the function.")
  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public Function parameters(List<List<Datatype>> parameters) {
    this.parameters = parameters;
    return this;
  }

  public Function addParametersItem(List<Datatype> parametersItem) {
    if (this.parameters == null) {
      this.parameters = new ArrayList<List<Datatype>>();
    }
    this.parameters.add(parametersItem);
    return this;
  }

   /**
   * A list of parameters with the allowed datatypes.
   * @return parameters
  **/
  @ApiModelProperty(value = "A list of parameters with the allowed datatypes.")
  public List<List<Datatype>> getParameters() {
    return parameters;
  }

  public void setParameters(List<List<Datatype>> parameters) {
    this.parameters = parameters;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Function function = (Function) o;
    return Objects.equals(this.symbol, function.symbol) &&
        Objects.equals(this.parameters, function.parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symbol, parameters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Function {\n");
    
    sb.append("    symbol: ").append(toIndentedString(symbol)).append("\n");
    sb.append("    parameters: ").append(toIndentedString(parameters)).append("\n");
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

