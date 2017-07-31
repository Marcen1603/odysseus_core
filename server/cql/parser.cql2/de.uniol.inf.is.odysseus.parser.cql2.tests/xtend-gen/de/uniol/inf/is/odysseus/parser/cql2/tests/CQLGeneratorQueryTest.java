package de.uniol.inf.is.odysseus.parser.cql2.tests;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Model;
import de.uniol.inf.is.odysseus.parser.cql2.generator.CQLGenerator;
import de.uniol.inf.is.odysseus.parser.cql2.tests.CQLInjectorProvider;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.eclipse.xtext.junit4.validation.ValidationTestHelper;
import org.eclipse.xtext.xbase.lib.Extension;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(XtextRunner.class)
@InjectWith(CQLInjectorProvider.class)
@Deprecated
@SuppressWarnings("all")
public class CQLGeneratorQueryTest {
  @Inject
  @Extension
  private CQLGenerator _cQLGenerator;
  
  @Inject
  @Extension
  private ParseHelper<Model> _parseHelper;
  
  @Inject
  @Extension
  private ValidationTestHelper _validationTestHelper;
  
  @Rule
  public ExpectedException thrown = ExpectedException.none();
  
  public String format(final String s) {
    return s.replaceAll("\\s*[\\r\\n]+\\s*", "").trim().replace(" ", "");
  }
}
