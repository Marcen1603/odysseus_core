package de.uniol.inf.is.odysseus.parser.novel.cql.generator;

import java.util.List;

@SuppressWarnings("all")
public class AttributeStruct {
  public String attributename;
  
  public String sourcename;
  
  public String datatype;
  
  public List<String> aliases;
  
  @Override
  public boolean equals(final Object obj) {
    if ((obj instanceof AttributeStruct)) {
      if (((this.attributename.equals(((AttributeStruct) obj).attributename) && this.sourcename.equals(((AttributeStruct) obj).sourcename)) && this.datatype.equals(((AttributeStruct) obj).datatype))) {
        return true;
      }
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return 0;
  }
  
  @Override
  public String toString() {
    String _property = System.getProperty("line.separator");
    String _plus = (("attributename " + this.attributename) + _property);
    String _plus_1 = (_plus + " sourcename ");
    String _plus_2 = (_plus_1 + this.sourcename);
    String _property_1 = System.getProperty("line.separator");
    String _plus_3 = (_plus_2 + _property_1);
    String _plus_4 = (_plus_3 + " datatype ");
    String _plus_5 = (_plus_4 + this.datatype);
    String _property_2 = System.getProperty("line.separator");
    String _plus_6 = (_plus_5 + _property_2);
    String _plus_7 = (_plus_6 + " aliases ");
    String _string = this.aliases.toString();
    return (_plus_7 + _string);
  }
}
