package de.uniol.inf.is.odysseus.parser.novel.cql.tests;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Model;
import de.uniol.inf.is.odysseus.parser.novel.cql.tests.CQLInjectorProvider;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.eclipse.xtext.junit4.validation.ValidationTestHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(XtextRunner.class)
@InjectWith(CQLInjectorProvider.class)
@SuppressWarnings("all")
public class StatementsTest {
  @Inject
  @Extension
  private ParseHelper<Model> _parseHelper;
  
  @Inject
  @Extension
  private ValidationTestHelper _validationTestHelper;
  
  public final static String SELECT = "SELECT attr1 FROM R1 WHERE attr1 > 2";
  
  @Test
  public void selectStatement() {
    try {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("SELECT attr1");
      _builder.newLine();
      _builder.append("FROM R1");
      _builder.newLine();
      _builder.append("WHERE attr1 > 2;");
      _builder.newLine();
      Model _parse = this._parseHelper.parse(_builder);
      this._validationTestHelper.assertNoErrors(_parse);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
