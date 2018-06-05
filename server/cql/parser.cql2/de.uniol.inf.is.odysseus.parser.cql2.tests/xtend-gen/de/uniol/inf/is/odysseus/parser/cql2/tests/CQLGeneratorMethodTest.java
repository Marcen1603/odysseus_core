package de.uniol.inf.is.odysseus.parser.cql2.tests;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.parser.cql2.generator.CQLGenerator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.eclipse.xtext.xbase.lib.Extension;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

@Deprecated
@SuppressWarnings("all")
public class CQLGeneratorMethodTest {
  @Inject
  @Extension
  private CQLGenerator _cQLGenerator;
  
  @Rule
  public ExpectedException thrown = ExpectedException.none();
  
  private String source;
  
  private String source2;
  
  private List<String> attributes;
  
  private List<String> attributes2;
  
  private List<String> aliases;
  
  private List<String> aliases2;
  
  private List<String> aggregates;
  
  private List<String> aggregationAliases;
  
  private String source1Alias = "s1";
  
  private String source2Alias = "s2";
  
  public void generateModel(final String query) {
    final InMemoryFileSystemAccess fsa = new InMemoryFileSystemAccess();
    String result = "";
    Map<String, CharSequence> _textFiles = fsa.getTextFiles();
    Set<Map.Entry<String, CharSequence>> _entrySet = _textFiles.entrySet();
    for (final Map.Entry<String, CharSequence> e : _entrySet) {
      String _result = result;
      CharSequence _value = e.getValue();
      result = (_result + _value);
    }
  }
}
