package de.uniol.inf.is.odysseus.server.generator;

import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtext.xbase.lib.Pure;

@Accessors
@SuppressWarnings("all")
public class StreamingSparqlTriplePatternMatching {
  private String subject;
  
  private String property;
  
  private String object;
  
  private String id;
  
  private String stmt;
  
  public StreamingSparqlTriplePatternMatching(final String subject, final String property, final String object, final String source, final String id) {
    this.subject = subject;
    this.property = property;
    this.object = object;
    this.id = id;
    this.stmt = (((((((((((id + "=TRIPLEPATTERNMATCHING({condition=[\'") + subject) + "\',\'") + property) + "\',\'") + object) + "\'],type = \'") + id) + "\'},") + source) + ")");
  }
  
  @Pure
  public String getSubject() {
    return this.subject;
  }
  
  public void setSubject(final String subject) {
    this.subject = subject;
  }
  
  @Pure
  public String getProperty() {
    return this.property;
  }
  
  public void setProperty(final String property) {
    this.property = property;
  }
  
  @Pure
  public String getObject() {
    return this.object;
  }
  
  public void setObject(final String object) {
    this.object = object;
  }
  
  @Pure
  public String getId() {
    return this.id;
  }
  
  public void setId(final String id) {
    this.id = id;
  }
  
  @Pure
  public String getStmt() {
    return this.stmt;
  }
  
  public void setStmt(final String stmt) {
    this.stmt = stmt;
  }
}
