package de.uniol.inf.is.odysseus.parser.cql2.tests;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.Model;
import de.uniol.inf.is.odysseus.parser.cql2.generator.CQLGenerator;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
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
@Deprecated
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
    this.source = "stream1";
    this.aliases = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("a1", "a2", "aa1", "stream1X"));
    this.attributes = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("attr1", "attr2", "attr1", "attrX"));
    this.source2 = "stream2";
    this.aliases2 = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("b1", "b2", "bb2", "stream2X"));
    this.attributes2 = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("attr4", "attr5", "attr5", "attrX"));
    this.aggregates = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("AVG(attr1)", (("SUM(" + this.source2) + ".attr5)"), (("COUNT(" + this.source) + ".aa1)"), "MAX(b2)", (("MIN(" + this.source1Alias) + ".attr2)"), (("MIN(" + this.source2Alias) + ".b2)")));
    this.aggregationAliases = Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("avgAttr1", "sumAttr5", "countAa1", "maxB2", "minAttr2", "minB2"));
    String attributesWithAlias = "";
    for (int i = 0; (i < this.attributes.size()); i++) {
      String _attributesWithAlias = attributesWithAlias;
      String _get = this.attributes.get(i);
      String _plus = (_get + " AS ");
      String _get_1 = this.aliases.get(i);
      String _plus_1 = (_plus + _get_1);
      String _plus_2 = (_plus_1 + ",");
      attributesWithAlias = (_attributesWithAlias + _plus_2);
    }
    for (int i = 0; (i < this.attributes2.size()); i++) {
      String _attributesWithAlias = attributesWithAlias;
      String _get = this.attributes2.get(i);
      String _plus = (_get + " AS ");
      String _get_1 = this.aliases2.get(i);
      String _plus_1 = (_plus + _get_1);
      String _plus_2 = (_plus_1 + ",");
      attributesWithAlias = (_attributesWithAlias + _plus_2);
    }
    for (int i = 0; (i < (this.aggregates.size() - 1)); i++) {
      String _attributesWithAlias = attributesWithAlias;
      String _get = this.aggregates.get(i);
      String _plus = (_get + " AS ");
      String _get_1 = this.aggregationAliases.get(i);
      String _plus_1 = (_plus + _get_1);
      String _plus_2 = (_plus_1 + ",");
      attributesWithAlias = (_attributesWithAlias + _plus_2);
    }
    String _attributesWithAlias = attributesWithAlias;
    int _size = this.aggregates.size();
    int _minus = (_size - 1);
    String _get = this.aggregates.get(_minus);
    String _plus = (_get + " AS ");
    int _size_1 = this.aggregationAliases.size();
    int _minus_1 = (_size_1 - 1);
    String _get_1 = this.aggregationAliases.get(_minus_1);
    String _plus_1 = (_plus + _get_1);
    attributesWithAlias = (_attributesWithAlias + _plus_1);
    String query = ((((((((("SELECT " + attributesWithAlias) + " FROM ") + this.source) + " AS ") + this.source1Alias) + ",") + this.source2) + " AS ") + this.source2Alias);
    this.generateModel(query);
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
  }
  
  @Test
  public void getSourcenameFromAliasTest() {
    Assert.assertEquals(this.source, this._cQLGenerator.getSourcenameFromAlias(this.source1Alias));
    Assert.assertEquals(this.source2, this._cQLGenerator.getSourcenameFromAlias(this.source2Alias));
  }
  
  @Test
  public void getSourceAliasesTest() {
  }
  
  @Test
  public void getAttributeAliasesTest() {
  }
}
