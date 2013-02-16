package de.uniol.inf.is.odysseus.core.server.mep.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

public class ConcatFunction extends AbstractFunction<String> {

	  /**
	 * 
	 */
	private static final long serialVersionUID = -2667259091974125547L;
	private static final SDFDatatype[][] accTypes         = new SDFDatatype[][] {
          { SDFDatatype.STRING },
          { SDFDatatype.STRING } };

  @Override
  public int getArity() {
      return 2;
  }

  @Override
  public SDFDatatype[] getAcceptedTypes(int argPos) {
      if (argPos < 0) {
          throw new IllegalArgumentException("negative argument index not allowed");
      }
      if (argPos > this.getArity()) {
          throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity()
                  + " argument(s): two strings");
      }
      return accTypes[argPos];
  }

  @Override
  public String getSymbol() {
      return "concat";
  }

  @Override
  public String getValue() {
	  StringBuilder sb = new StringBuilder();
	  sb.append(getInputValue(0));
	  sb.append(getInputValue(1));
      return sb.toString();
  }

  @Override
  public SDFDatatype getReturnType() {
      return SDFDatatype.STRING;
  }

}
