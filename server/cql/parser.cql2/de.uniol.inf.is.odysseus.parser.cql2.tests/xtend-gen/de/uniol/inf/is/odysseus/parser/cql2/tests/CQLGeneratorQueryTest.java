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
    return s.replaceAll("\\s*[\\r\\n]+\\s*", "").trim().replace(" ", "");
  }
}
