package de.uniol.inf.is.odysseus.parser.novel.cql.tests;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Model;
import de.uniol.inf.is.odysseus.parser.novel.cql.generator.CQLGenerator;
import de.uniol.inf.is.odysseus.parser.novel.cql.generator.SourceStruct;
import de.uniol.inf.is.odysseus.parser.novel.cql.tests.CQLInjectorProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(XtextRunner.class)
@InjectWith(CQLInjectorProvider.class)
@SuppressWarnings("all")
public class CQLGeneratorMethodTest {
  @Inject
  @Extension
  private CQLGenerator _cQLGenerator;
  
  @Inject
  @Extension
  private ParseHelper<Model> _parseHelper;
  
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
    try {
      final InMemoryFileSystemAccess fsa = new InMemoryFileSystemAccess();
      Model model = this._parseHelper.parse(query);
      this._cQLGenerator.doGenerate(model.eResource(), fsa, null);
      String result = "";
      Set<Map.Entry<String, CharSequence>> _entrySet = fsa.getTextFiles().entrySet();
      for (final Map.Entry<String, CharSequence> e : _entrySet) {
        String _result = result;
        CharSequence _value = e.getValue();
        result = (_result + _value);
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Before
  public void setup() {
    throw new Error("Unresolved compilation problems:"
      + "\nThe method CQLSchemata(Set<SDFSchema>) is undefined"
      + "\nThe method CQLSchemata(Set<SDFSchema>) is undefined");
  }
  
  @Test
  public void getAttributenameFromAliasTest() {
    for (int i = 0; (i < this.attributes.size()); i++) {
      Assert.assertEquals(this.attributes.get(i), this._cQLGenerator.getAttributenameFromAlias(this.aliases.get(i)));
    }
    for (int i = 0; (i < this.attributes2.size()); i++) {
      Assert.assertEquals(this.attributes2.get(i), this._cQLGenerator.getAttributenameFromAlias(this.aliases2.get(i)));
    }
    for (int i = 0; (i < this.aggregates.size()); i++) {
      Assert.assertEquals(this.aggregationAliases.get(i), this._cQLGenerator.getAttributenameFromAlias(this.aggregationAliases.get(i)));
    }
    this.thrown.expect(IllegalArgumentException.class);
    this._cQLGenerator.getAttributenameFromAlias("");
    this.thrown.reportMissingExceptionWithMessage("No exception of %s thrown");
    this._cQLGenerator.getAttributenameFromAlias(null);
    this.thrown.reportMissingExceptionWithMessage("No exception of %s thrown");
    this._cQLGenerator.getAttributenameFromAlias("attr12");
    this.thrown.reportMissingExceptionWithMessage("No exception of %s thrown");
  }
  
  @Test
  public void getAttributenameTest() {
    Assert.assertEquals("stream1.attr1", this._cQLGenerator.getAttributename("attr1", null));
    Assert.assertEquals("stream1.attr1", this._cQLGenerator.getAttributename("a1", null));
    Assert.assertEquals("stream1.attr1", this._cQLGenerator.getAttributename("stream1.a1", null));
    Assert.assertEquals("stream1.attr1", this._cQLGenerator.getAttributename("aa1", null));
    Assert.assertEquals("stream1.attr1", this._cQLGenerator.getAttributename("stream1.aa1", null));
    Assert.assertEquals("stream2.attr5", this._cQLGenerator.getAttributename("attr5", null));
    Assert.assertEquals("stream2.attr5", this._cQLGenerator.getAttributename("b2", null));
    Assert.assertEquals("stream2.attr5", this._cQLGenerator.getAttributename("stream2.b2", null));
    Assert.assertEquals("stream2.attr5", this._cQLGenerator.getAttributename("bb2", null));
    Assert.assertEquals("stream2.attr5", this._cQLGenerator.getAttributename("stream2.bb2", null));
    Assert.assertEquals("avgAttr1", this._cQLGenerator.getAttributename("avgAttr1", null));
    Assert.assertEquals("sumAttr5", this._cQLGenerator.getAttributename("sumAttr5", null));
    this.thrown.expect(IllegalArgumentException.class);
    Assert.assertEquals("attr12", this._cQLGenerator.getAttributename("stream1.attr12", null));
    this.thrown.reportMissingExceptionWithMessage("No exception of %s thrown");
  }
  
  @Test
  public void getAttributenamesFromSource() {
    List<String> _attributeNamesFrom = this._cQLGenerator.getAttributeNamesFrom(this.source);
    for (final String attribute : _attributeNamesFrom) {
      Assert.assertTrue(this.attributes.contains(attribute));
    }
    List<String> _attributeNamesFrom_1 = this._cQLGenerator.getAttributeNamesFrom(this.source2);
    for (final String attribute_1 : _attributeNamesFrom_1) {
      Assert.assertTrue(this.attributes2.contains(attribute_1));
    }
  }
  
  @Test
  public void getSourcenameFromAliasTest() {
    Assert.assertEquals(this.source, this._cQLGenerator.getSourcenameFromAlias(this.source1Alias));
    Assert.assertEquals(this.source2, this._cQLGenerator.getSourcenameFromAlias(this.source2Alias));
  }
  
  @Test
  public void getSourceAliasesTest() {
    ArrayList<String> aliases = CollectionLiterals.<String>newArrayList();
    Set<Map.Entry<SourceStruct, List<String>>> _entrySet = this._cQLGenerator.getSourceAliases().entrySet();
    for (final Map.Entry<SourceStruct, List<String>> str : _entrySet) {
      aliases.addAll(str.getValue());
    }
    Assert.assertTrue(aliases.contains(this.source1Alias));
    Assert.assertTrue(aliases.contains(this.source2Alias));
  }
  
  @Test
  public void getAttributeAliasesTest() {
    ArrayList<String> aliases = CollectionLiterals.<String>newArrayList();
    aliases.addAll(this.aliases);
    aliases.addAll(this.aliases2);
    for (final String alias : aliases) {
      Assert.assertTrue(this._cQLGenerator.getAttributeAliasesAsList().contains(alias));
    }
  }
}
