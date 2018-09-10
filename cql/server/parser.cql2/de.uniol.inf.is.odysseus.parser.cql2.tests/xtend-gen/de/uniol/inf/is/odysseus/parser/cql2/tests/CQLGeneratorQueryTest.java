package de.uniol.inf.is.odysseus.parser.cql2.tests;

import org.eclipse.xtext.testing.XtextRunner;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(XtextRunner.class)
@Deprecated
@SuppressWarnings("all")
public class CQLGeneratorQueryTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();
  
  public String format(final String s) {
    String _replaceAll = s.replaceAll("\\s*[\\r\\n]+\\s*", "");
    String _trim = _replaceAll.trim();
    return _trim.replace(" ", "");
  }
}
