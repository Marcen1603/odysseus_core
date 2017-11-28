package de.uniol.inf.is.odysseus.server.generator;

import de.uniol.inf.is.odysseus.server.generator.StreamingSparqlTriplePatternMatching;
import org.eclipse.xtend.lib.annotations.Accessors;
import org.eclipse.xtend.lib.annotations.Data;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.util.ToStringBuilder;

@Accessors
@Data
@SuppressWarnings("all")
public class TripleToJoin {
  private final StreamingSparqlTriplePatternMatching pattern1;
  
  private final StreamingSparqlTriplePatternMatching pattern2;
  
  private final String variable;
  
  public TripleToJoin(final StreamingSparqlTriplePatternMatching pattern1, final StreamingSparqlTriplePatternMatching pattern2, final String variable) {
    this.pattern1 = pattern1;
    this.pattern2 = pattern2;
    this.variable = variable;
  }
  
  @Override
  @Pure
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((this.pattern1== null) ? 0 : this.pattern1.hashCode());
    result = prime * result + ((this.pattern2== null) ? 0 : this.pattern2.hashCode());
    result = prime * result + ((this.variable== null) ? 0 : this.variable.hashCode());
    return result;
  }
  
  @Override
  @Pure
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TripleToJoin other = (TripleToJoin) obj;
    if (this.pattern1 == null) {
      if (other.pattern1 != null)
        return false;
    } else if (!this.pattern1.equals(other.pattern1))
      return false;
    if (this.pattern2 == null) {
      if (other.pattern2 != null)
        return false;
    } else if (!this.pattern2.equals(other.pattern2))
      return false;
    if (this.variable == null) {
      if (other.variable != null)
        return false;
    } else if (!this.variable.equals(other.variable))
      return false;
    return true;
  }
  
  @Override
  @Pure
  public String toString() {
    ToStringBuilder b = new ToStringBuilder(this);
    b.add("pattern1", this.pattern1);
    b.add("pattern2", this.pattern2);
    b.add("variable", this.variable);
    return b.toString();
  }
  
  @Pure
  public StreamingSparqlTriplePatternMatching getPattern1() {
    return this.pattern1;
  }
  
  @Pure
  public StreamingSparqlTriplePatternMatching getPattern2() {
    return this.pattern2;
  }
  
  @Pure
  public String getVariable() {
    return this.variable;
  }
}
