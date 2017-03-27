package de.uniol.inf.is.odysseus.parser.novel.cql.tests;

import com.google.common.base.Objects;
import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Model;
import de.uniol.inf.is.odysseus.parser.novel.cql.generator.CQLGenerator;
import de.uniol.inf.is.odysseus.parser.novel.cql.tests.CQLInjectorProvider;
import de.uniol.inf.is.odysseus.parser.novel.cql.tests.util.CQLDictionaryHelper;
import de.uniol.inf.is.odysseus.parser.novel.cql.tests.util.NameProviderHelper;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.generator.InMemoryFileSystemAccess;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.XtextRunner;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(XtextRunner.class)
@InjectWith(CQLInjectorProvider.class)
@SuppressWarnings("all")
public class CQLGeneratorQueryTest {
  @Inject
  @Extension
  private CQLGenerator _cQLGenerator;
  
  @Inject
  @Extension
  private ParseHelper<Model> _parseHelper;
  
  @Rule
  public ExpectedException thrown = ExpectedException.none();
  
  public void assertCorrectGenerated(final String s, final String t, final CQLDictionaryHelper dictionary) {
    try {
      Model model = this._parseHelper.parse(s);
      final InMemoryFileSystemAccess fsa = new InMemoryFileSystemAccess();
      boolean _notEquals = (!Objects.equal(dictionary, null));
      if (_notEquals) {
        Collection<SDFSchema> _schema = dictionary.getSchema();
        this._cQLGenerator.setInnerschema(((Set<SDFSchema>) _schema));
      }
      NameProviderHelper _nameProviderHelper = new NameProviderHelper();
      this._cQLGenerator.setNameProvider(_nameProviderHelper);
      Resource _eResource = model.eResource();
      this._cQLGenerator.doGenerate(_eResource, fsa, null);
      this._cQLGenerator.clear();
      String query = "";
      Map<String, CharSequence> _textFiles = fsa.getTextFiles();
      Set<Map.Entry<String, CharSequence>> _entrySet = _textFiles.entrySet();
      for (final Map.Entry<String, CharSequence> e : _entrySet) {
        String _query = query;
        CharSequence _value = e.getValue();
        query = (_query + _value);
      }
      String _format = this.format(t);
      String _format_1 = this.format(query);
      Assert.assertEquals(_format, _format_1);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public String format(final String s) {
    String _replaceAll = s.replaceAll("\\s*[\\r\\n]+\\s*", "");
    String _trim = _replaceAll.trim();
    return _trim.replace(" ", "");
  }
  
  @Test
  public void SelectAllTest1() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT * FROM stream1;", 
      "operator_1 = MAP({expressions=[\'stream1.attr1\', \'stream1.attr2\']}, stream1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectAllTest2() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT * FROM stream1, stream2, stream3;", 
      "operator_1 = JOIN(stream1, JOIN(stream2, stream3))", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectAllTest3() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT * FROM stream1, stream2 AS s2, stream3 WHERE attr1 > 2 AND attr2 = \'Test\';", 
      "\n\t\t\toperator_1 = SELECT({predicate=\'stream1.attr1 > 2 && stream1.attr2 == \'Test\'\'}, JOIN(stream1,JOIN(stream2,stream3)))\n\t\t\t", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectAllTest4() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT * FROM stream1 WHERE attr1 > 2 AND attr2 = \'Test\';", 
      "operator_1 = SELECT({predicate=\'stream1.attr1 > 2 && stream1.attr2 == \'Test\'\'}, stream1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectAllTest5() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT s1.* FROM stream1 AS s1 WHERE attr1 > 2 AND attr2 = \'Test\';", 
      "operator_1 = SELECT({predicate=\'stream1.attr1>2&&stream1.attr2==\'Test\'\'}, stream1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectAllTest6() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT stream1.* FROM stream1", 
      "operator_1 = MAP({expressions=[\'attr1\', \'attr2\']}, stream1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void RenameTest1() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr1 AS a1, attr2 AS a2 FROM stream1", 
      "\n\t\t\t operator_1 = RENAME({aliases=[\'attr1\',\'a1\',\'attr2\',\'a2\'], pairs=\'true\'}, stream1)\n\t\t\t operator_2 = MAP({expressions=[\'a1\', \'a2\']}, operator_1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void RenameTest2() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT a.attr1 AS a1, b.attr1 AS b1 FROM stream1 AS a, stream1 AS b", 
      "\n\t\t\t operator_1 = RENAME({aliases=[\'attr1\',\'a1\'], pairs=\'true\'}, stream1)\n\t\t\t operator_2 = RENAME({aliases=[\'attr1\',\'b1\',\'attr2\',\'stream1.attr2#1\'], pairs=\'true\'}, stream1)\n\t\t\t operator_3 = MAP({expressions=[\'a1\', \'b1\']}, JOIN(operator_1, operator_2))", _cQLDictionaryHelper);
  }
  
  @Test
  public void RenameTest3() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT a.attr1 AS a1, attr2 AS b1 FROM stream1 AS a", 
      "\n\t\t\t operator_1 = RENAME({aliases=[\'attr1\',\'a1\', \'attr2\', \'b1\'], pairs=\'true\'}, stream1)\n\t\t\t operator_2 = MAP({expressions=[\'a1\', \'b1\']}, operator_1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void RenameTest7() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT a.attr1 FROM stream1 AS a", 
      "\n\t\t\t operator_1 = RENAME({aliases=[\'attr1\',\'a.attr1\'], pairs=\'true\'}, stream1)\n\t\t\t operator_2 = MAP({expressions=[\'a.attr1\']}, operator_1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void RenameTest4() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT a.attr1 AS a1, stream1.attr2 AS b1 FROM stream1 AS a", 
      "\n\t\t\t operator_1 = RENAME({aliases=[\'attr1\',\'a1\', \'attr2\', \'b1\'], pairs=\'true\'}, stream1)\n\t\t\t operator_2 = MAP({expressions=[\'a1\', \'b1\']}, operator_1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void RenameTest5() {
    this.thrown.expect(IllegalArgumentException.class);
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT a.attr1 AS a1, stream1.attr2 AS b1 FROM stream1 AS a, stream1 AS b", 
      "", _cQLDictionaryHelper);
    this.thrown.reportMissingExceptionWithMessage("No exception of %s thrown");
  }
  
  @Test
  public void RenameTest6() {
    this.thrown.expect(IllegalArgumentException.class);
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT a.attr1 AS a1, attr1 AS b1 FROM stream1 AS a", 
      "", _cQLDictionaryHelper);
    this.thrown.reportMissingExceptionWithMessage("No exception of %s thrown");
  }
  
  @Test
  public void AmbiguouseAttributeTest1() {
    this.thrown.expect(IllegalArgumentException.class);
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr1 AS a1, attr1 AS a2 FROM stream1, stream4", 
      "", _cQLDictionaryHelper);
    this.thrown.reportMissingExceptionWithMessage("No exception of %s thrown");
  }
  
  @Test
  public void AmbiguouseAttributeTest2() {
    this.thrown.expect(IllegalArgumentException.class);
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT stream1.attr1 AS a1, attr1 AS a2 FROM stream1, stream4", 
      "", _cQLDictionaryHelper);
    this.thrown.reportMissingExceptionWithMessage("No exception of %s thrown");
  }
  
  @Test
  public void AmbiguouseAttributeTest3() {
    this.thrown.expect(IllegalArgumentException.class);
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr1 AS a1, stream4.attr1 AS a2 FROM stream1, stream4", 
      "", _cQLDictionaryHelper);
    this.thrown.reportMissingExceptionWithMessage("No exception of %s thrown");
  }
  
  @Test
  public void AmbiguouseAttributeTest4() {
    this.thrown.expect(IllegalArgumentException.class);
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr1 FROM stream1 AS s1, stream1 AS s2;", 
      "", _cQLDictionaryHelper);
    this.thrown.reportMissingExceptionWithMessage("No exception of %s thrown");
  }
  
  @Test
  public void AmbiguouseAttributeTest5() {
    this.thrown.expect(IllegalArgumentException.class);
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr1 FROM stream1 AS s1, stream1 AS s2", 
      "", _cQLDictionaryHelper);
    this.thrown.reportMissingExceptionWithMessage("No exception of %s thrown");
  }
  
  @Test
  public void AmbiguouseAttributeTest6() {
    this.thrown.expect(IllegalArgumentException.class);
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr1 FROM stream1 AS s1, stream1 AS s2, stream1 AS s3;", 
      "", _cQLDictionaryHelper);
    this.thrown.reportMissingExceptionWithMessage("No exception of %s thrown");
  }
  
  private final String aliasTestResult = "\n\t\toperator_1 = SELECT({predicate=\'stream1.value1 > 2\'}, stream1)\n\t\toperator_2 = MAP({expressions=[\'stream1.attr1\']}, operator_1)\n\t\trenamed_3 = RENAME({aliases=[\'attr1\',\'value1\'],pairs=\'true\'}, operator_2)";
  
  @Test
  public void AliasTest1() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr1 AS value1 FROM stream1;", 
      "operator_1  = RENAME({aliases=[\'attr1\',\'value1\'],pairs=\'true\'}, stream1)\n\t\t\t operator_2 = MAP({expressions=[\'value1\']}, operator_1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectAttr1Test1() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr1 FROM stream1 WHERE attr1 > 2;", 
      "\n\t\t\toperator_1 = SELECT({predicate=\'stream1.attr1>2\'},stream1)\n\t\t\toperator_2 = MAP({expressions=[\'stream1.attr1\']},operator_1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectAttr1Test2() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT stream1.attr1 FROM stream1, stream2, stream3 WHERE attr1 > 2", 
      "\n\t\t\toperator_1 = SELECT({predicate=\'stream1.attr1>2\'},JOIN(stream1,JOIN(stream2,stream3)))\n\t\t\toperator_2 = MAP({expressions=[\'stream1.attr1\']},operator_1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectAttr1Test3() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr1 FROM stream1;", 
      "operator_1 = MAP({expressions=[\'stream1.attr1\']}, stream1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectAttr1Test4() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT stream1.attr1, stream1.attr2, attr3 FROM stream1, stream2, stream3;", 
      "operator_1 = MAP({expressions=[\'stream1.attr1\', \'stream1.attr2\', \'stream2.attr3\']},\n\t\t\t\t\t\t\tJOIN(stream1, \n\t\t\t\t\t\t\t\tJOIN(stream2, stream3)))", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectAttr1Test5() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT stream1.attr1, AVG(attr1) AS avgAttr1, stream1.attr2, attr3 FROM stream1, stream2, stream3;", 
      "operator_1 = AGGREGATE({AGGREGATIONS=[[\'AVG\',\'stream1.attr1\',\'avgAttr1\',\'Integer\']]},JOIN(stream1,JOIN(stream2,stream3)))\n\t\t\t operator_2 = MAP({expressions=[\'stream1.attr1\',\'avgAttr1\',\'stream1.attr2\',\'stream2.attr3\']}, operator_1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectAttr1Test6() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr1, attr2 FROM stream1 WHERE attr1=7 OR attr1=20 OR attr1=21 OR attr1=59 OR attr1=87;", 
      "operator_1 = SELECT({predicate=\'stream1.attr1==7||stream1.attr1==20||stream1.attr1==21||stream1.attr1==59||stream1.attr1==87\'},stream1)\n\t\t\t operator_2 = MAP({expressions=[\'stream1.attr1\',\'stream1.attr2\']},operator_1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectAttr1Test7() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT a.attr1 AS aid, b.attr1 AS d, b.attr1 FROM stream1 AS a, stream1 AS b WHERE aid=b.attr1;", 
      "operator_1=RENAME({aliases=[\'attr1\',\'aid\'],pairs=\'true\'},stream1)\n \t\t\t operator_2=RENAME({aliases=[\'attr1\',\'d\',\'attr2\',\'stream1.attr2#1\'],pairs=\'true\'},stream1)\n \t\t\t operator_3=RENAME({aliases=[\'attr1\',\'b.attr1\',\'attr2\',\'stream1.attr2#2\'],pairs=\'true\'},stream1)\n\t\t\t operator_4=SELECT({predicate=\'aid==b.attr1\'},JOIN(operator_1,JOIN(operator_2,operator_3)))\n\t\t\t operator_5=MAP({expressions=[\'aid\',\'d\',\'b.attr1\']},operator_4)", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectAttr1Test8() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT b.attr1 FROM stream1 AS b WHERE b.attr1 < 150.0;", 
      "operator_1=RENAME({aliases=[\'attr1\',\'b.attr1\'],pairs=\'true\'},stream1)\n\t\t\t operator_2=SELECT({predicate=\'b.attr1<150.0\'},operator_1)\n\t\t\t operator_3=MAP({expressions=[\'b.attr1\']},operator_2)", _cQLDictionaryHelper);
  }
  
  @Test
  public void CreateViewTest1() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "CREATE VIEW view1 FROM (\n\t\t\t\tSELECT * FROM stream1\n\t\t\t)", 
      "\n\t\t\toperator_1 = MAP({expressions=[\'stream1.attr1\', \'stream1.attr2\']}, stream1)\n\t\t\tview_2 := operator_1\n\t\t\t", _cQLDictionaryHelper);
  }
  
  @Test
  public void CreateStreamTest1() {
    this.assertCorrectGenerated(
      "CREATE STREAM stream1 (attr1 Integer) \n    \t\tWRAPPER \'GenericPush\'\n    \t\tPROTOCOL \'CSV\'\n    \t\tTRANSPORT \'File\'\n    \t\tDATAHANDLER \'Tuple\'\n    \t\tOPTIONS (\'port\' \'54321\', \'host\' \'localhost\')", 
      "stream1 := ACCESS\n\t\t\t(\n\t\t\t\t{ source = \'stream1\', \n\t\t\t\t  wrapper = \'GenericPush\',\n\t\t\t\t  protocol = \'CSV\',\n\t\t\t\t  transport = \'File\',\n\t\t\t\t  dataHandler =\'Tuple\',\n\t\t\t\t  schema = [[\'attr1\', \'Integer\']],\n\t\t\t\t  options =[[\'port\', \'54321\'],[\'host\', \'localhost\']]\n\t\t\t\t}\n\t\t\t)", null);
  }
  
  @Test
  public void CreateStreamTest2() {
    this.assertCorrectGenerated(
      "ATTACH STREAM stream1 (attr1 INTEGER) CHANNEL localhost : 54321;", 
      "stream1 := ACCESS\n\t\t\t(\n\t\t\t\t{ source = \'stream1\', \n\t\t\t\t  wrapper = \'GenericPush\',\n\t\t\t\t  protocol = \'SizeByteBuffer\',\n\t\t\t\t  transport = \'NonBlockingTcp\',\n\t\t\t\t  dataHandler =\'Tuple\',\n\t\t\t\t  schema = [[\'attr1\', \'INTEGER\']],\n\t\t\t\t  options =[[\'port\', \'54321\'],[\'host\', \'localhost\']]\n\t\t\t\t}\n\t\t\t)", null);
  }
  
  @Test
  public void CreateStreamTest3() {
    this.assertCorrectGenerated(
      "CREATE STREAM stream1 (attr1 Integer) FILE \'this/is/a/filename.file\' AS SimpleCSV;", 
      "stream1 := ACCESS\n\t\t\t(\n\t\t\t\t{ source = \'stream1\', \n\t\t\t\t  wrapper = \'GenericPull\',\n\t\t\t\t  protocol = \'SimpleCSV\',\n\t\t\t\t  transport = \'File\',\n\t\t\t\t  dataHandler =\'Tuple\',\n\t\t\t\t  schema = [[\'attr1\', \'Integer\']],\n\t\t\t\t  options =[[\'filename\', \'this/is/a/filename.file\'],[\'delimiter\',\';\'],[\'textDelimiter\',\"\'\"]]\n\t\t\t\t}\n\t\t\t)", null);
  }
  
  @Test
  public void StreamtoTest1() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "STREAM TO out1 SELECT * FROM stream1;", 
      "", _cQLDictionaryHelper);
  }
  
  @Test
  public void StreamtoTest2() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "STREAM TO out1 input1;", 
      "", _cQLDictionaryHelper);
  }
  
  @Test
  public void StreamtoTest3() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "\n\t\t\tCREATE SINK out1 (attr1 INTEGER, attr2 STRING)\n\t\t\tWRAPPER \'GenericPush\'\n\t\t\tPROTOCOL \'CSV\'\n\t\t\tTRANSPORT \'FILE\'\n\t\t\tDATAHANDLER \'TUPLE\'\n\t\t\tOPTIONS(\'filename\' \'outfile1\') \t\t\t\n\n\t\t\tSTREAM TO out1 SELECT attr1, attr2 FROM stream1 WHERE attr2 != attr1;", 
      "operator_1 = SELECT({predicate=\'stream1.attr2!=stream1.attr1\'},stream1) \n\t\t\t operator_2 = MAP({expressions=[\'stream1.attr1\',\'stream1.attr2\']},operator_1)\n\t\t\t out1 := SENDER\n\t\t\t\t  (\n\t\t\t\t\t{\t  \n\t\t\t\t\t\tsink      = \'out1\', \n\t\t\t\t\t\twrapper     = \'GenericPush\',\n\t\t\t\t\t\tprotocol    = \'CSV\',\n\t\t\t\t\t\ttransport   = \'FILE\',\n\t\t\t\t\t\tdataHandler = \'TUPLE\',\n\t\t\t\t\t\toptions =[[\'filename\',\'outfile1\']]\n\t\t\t\t\t }\n\t\t\t\t\t ,operator_2\n\t\t\t\t   )", _cQLDictionaryHelper);
  }
  
  @Test
  public void StreamtoTest4() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "\n\t\t\tCREATE SINK out1 (attr1 INTEGER, attr2 STRING)\n\t\t\tWRAPPER \'GenericPush\'\n\t\t\tPROTOCOL \'CSV\'\n\t\t\tTRANSPORT \'FILE\'\n\t\t\tDATAHANDLER \'TUPLE\'\n\t\t\tOPTIONS(\'filename\' \'outfile1\') \t\t\t\n\n\t\t\tSTREAM TO out1 stream1;", 
      "out1:=SENDER({sink=\'out1\',wrapper=\'GenericPush\',protocol=\'CSV\',transport=\'FILE\',dataHandler=\'TUPLE\',options=[[\'filename\',\'outfile1\']]}, out1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void WindowUnboundedTest1() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT * FROM stream1 [UNBOUNDED];", 
      "operator_1 = MAP({expressions=[\'stream1.attr1\', \'stream1.attr2\']}, stream1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void WindowTimeTest1() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT * FROM stream1 [SIZE 5 MINUTES TIME];", 
      "operator_1 = MAP({expressions=[\'stream1.attr1\', \'stream1.attr2\']},\n\t\t\t\t\t\t\t TIMEWINDOW({size=[5, \'MINUTES\'], advance=[1, \'MINUTES\']}, stream1))", _cQLDictionaryHelper);
  }
  
  @Test
  public void WindowTimeTest2() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT * FROM stream1 [SIZE 5 MINUTES ADVANCE 1 SECONDS TIME];", 
      "operator_1 = MAP({expressions=[\'stream1.attr1\', \'stream1.attr2\']}, \n\t\t\t\t\t\t\tTIMEWINDOW({size=[5, \'MINUTES\'], advance=[1, \'SECONDS\']}, stream1))", _cQLDictionaryHelper);
  }
  
  @Test
  public void WindowElementTest1() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT * FROM stream1 [SIZE 5 TUPLE];", 
      "operator_1 = MAP({expressions=[\'stream1.attr1\', \'stream1.attr2\']}, \n\t\t\t\t\t\t\tELEMENTWINDOW({size=5,advance=1}, stream1))", _cQLDictionaryHelper);
  }
  
  @Test
  public void WindowUnboundedTest2() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT * FROM stream1 [UNBOUNDED], stream2 [UNBOUNDED];", 
      "operator_1 = JOIN(stream1, stream2)", _cQLDictionaryHelper);
  }
  
  @Test
  public void WindowTimeWindowElementTest1() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT * FROM stream1 [SIZE 5 TUPLE], stream2 [SIZE 5 MINUTES TIME];", 
      "operator_1 = JOIN(ELEMENTWINDOW({size=5,advance=1}, stream1), \n\t\t\t\t\t\t\tTIMEWINDOW({size=[5, \'MINUTES\'], advance=[1, \'MINUTES\']}, stream2))", _cQLDictionaryHelper);
  }
  
  @Test
  public void WindowTimeWindowElementTest2() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr1, attr2, attr4 FROM stream1 [SIZE 5 TUPLE], stream2 [SIZE 5 MINUTES TIME], stream3;", 
      "operator_1 = MAP({expressions=[\'stream1.attr1\',\'stream1.attr2\',\'stream2.attr4\']}, \n\t\t\t\t\t\t\tJOIN(ELEMENTWINDOW({size=5,advance=1}, stream1), \n\t\t\t\t\t\t\t\tJOIN(TIMEWINDOW({size=[5, \'MINUTES\'], advance=[1, \'MINUTES\']}, stream2), stream3)))", _cQLDictionaryHelper);
  }
  
  @Test
  public void WindowTimeWindowElementTest3() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr1, attr2, attr4 FROM stream1 [SIZE 5 TUPLE], stream2 [SIZE 5 MINUTES TIME], stream3 WHERE attr4 != attr1;", 
      "\n\t\t\toperator_1 = SELECT({predicate=\'stream2.attr4 != stream1.attr1\'}, JOIN(ELEMENTWINDOW({size=5,advance=1},stream1),\n\t\t\t\t\t\t\t\tJOIN(TIMEWINDOW({size=[5,\'MINUTES\'],advance=[1,\'MINUTES\']},stream2),stream3)))\n\t\t\toperator_2 = MAP({expressions=[\'stream1.attr1\',\'stream1.attr2\',\'stream2.attr4\']}, operator_1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void WindowTimeWindowElementTest4() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT * FROM stream1 [SIZE 5 TUPLE], stream2 [SIZE 5 MINUTES TIME] WHERE attr4 != attr1;", 
      "operator_1 = SELECT({predicate=\'stream2.attr4 != stream1.attr1\'}, \n\t\t\t\t\t\t\tJOIN(ELEMENTWINDOW({size=5,advance=1}, stream1),\n\t\t\t\t\t\t\t\tTIMEWINDOW({size=[5, \'MINUTES\'], advance=[1, \'MINUTES\']}, stream2)))", _cQLDictionaryHelper);
  }
  
  @Test
  public void WindowElement2() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr1 FROM stream1 [SIZE 5 TUPLE];", 
      "operator_1 = MAP({expressions=[\'stream1.attr1\']}, ELEMENTWINDOW({size=5,advance=1}, stream1))", _cQLDictionaryHelper);
  }
  
  @Test
  public void AggregationTest1() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT COUNT(attr1) AS Counter FROM stream1 GROUP BY attr1;", 
      "operator_1 = MAP({expressions=[\'Counter\']},AGGREGATE({AGGREGATIONS=[[\'COUNT\',\'stream1.attr1\',\'Counter\',\'Integer\']],GROUP_BY=[\'stream1.attr1\']},stream1))", _cQLDictionaryHelper);
  }
  
  @Test
  public void AggregationTest2() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT COUNT(attr1) AS Counter, AVG(attr2) FROM stream1 GROUP BY attr1, attr2;", 
      "operator_1=MAP({expressions=[\'Counter\',\'AVG_0\']},AGGREGATE({AGGREGATIONS=[[\'COUNT\',\'stream1.attr1\',\'Counter\',\'Integer\'],[\'AVG\',\'stream1.attr2\',\'AVG_0\',\'String\']],GROUP_BY=[\'stream1.attr1\',\'stream1.attr2\']},stream1))", _cQLDictionaryHelper);
  }
  
  @Test
  public void AggregationTest3() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT COUNT(attr1) AS Counter, AVG(attr2) FROM stream1 [SIZE 10 MINUTES TIME] AS s1 , stream2 GROUP BY attr1, attr2;", 
      "\n\t\t\toperator_1=MAP({expressions=[\'Counter\',\'AVG_0\']},AGGREGATE({AGGREGATIONS=[[\'COUNT\',\'stream1.attr1\',\'Counter\',\'Integer\'],[\'AVG\',\'stream1.attr2\',\'AVG_0\',\'String\']],GROUP_BY=[\'stream1.attr1\',\'stream1.attr2\']},JOIN(TIMEWINDOW({size=[10,\'MINUTES\'],advance=[1,\'MINUTES\']},stream1),stream2)))", _cQLDictionaryHelper);
  }
  
  @Test
  public void AggregationTest4() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr2, COUNT(attr1) AS Counter FROM stream1 [SIZE 5 MINUTES TIME];", 
      "\n\t\t\toperator_1 = AGGREGATE\n\t\t\t(\n\t\t\t\t{\n\t\t\t\t\tAGGREGATIONS=[[\'COUNT\', \'stream1.attr1\', \'Counter\', \'Integer\']]\n\t\t\t\t}, TIMEWINDOW({size=[5,\'MINUTES\'], advance=[1, \'MINUTES\']}, stream1)\n\t\t\t)\n\t\t\toperator_2 = MAP({expressions=[\'stream1.attr2\', \'Counter\']}, operator_1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void AggregationTest5() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT COUNT(attr1) AS Counter, attr3 FROM stream1, stream2;", 
      "\n\t\t\toperator_1 = AGGREGATE\n\t\t\t(\n\t\t\t\t{\n\t\t\t\t\tAGGREGATIONS=[\n\t\t\t\t\t\t\t\t\t[\'COUNT\', \'stream1.attr1\', \'Counter\', \'Integer\']\n\t\t\t\t\t\t\t\t ]\n\t\t\t\t}, JOIN(stream1, stream2)\n\t\t\t)\n\t\t\toperator_2 = MAP({expressions=[\'Counter\', \'stream2.attr3\']}, operator_1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void AggregationTest6() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT COUNT(attr1) AS Counter, attr3 FROM stream1 , stream2 [SIZE 10 MINUTES TIME];", 
      "\n\t\t\toperator_1=AGGREGATE({AGGREGATIONS=[[\'COUNT\',\'stream1.attr1\',\'Counter\',\'Integer\']]},JOIN(stream1,TIMEWINDOW({size=[10,\'MINUTES\'],advance=[1,\'MINUTES\']},stream2)))\n\t\t\toperator_2=MAP({expressions=[\'Counter\',\'stream2.attr3\']},operator_1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void AggregationTest7() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT COUNT(attr1) AS Counter, attr3 FROM stream1 [SIZE 10 MINUTES ADVANCE 2 SECONDS TIME] , stream2 [SIZE 10 MINUTES TIME] WHERE attr3 > 100;", 
      "\n\t\t\toperator_1=AGGREGATE({AGGREGATIONS=[[\'COUNT\',\'stream1.attr1\',\'Counter\',\'Integer\']]},JOIN(TIMEWINDOW({size=[10,\'MINUTES\'],advance=[2,\'SECONDS\']},stream1),TIMEWINDOW({size=[10,\'MINUTES\'],advance=[1,\'MINUTES\']},stream2)))\n\t\t\toperator_2=SELECT({predicate=\'stream2.attr3>100\'},operator_1)\n\t\t\toperator_3=MAP({expressions=[\'Counter\',\'stream2.attr3\']},operator_2)", _cQLDictionaryHelper);
  }
  
  @Test
  public void AggregationTest8() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT COUNT(attr1) AS Counter, attr3 FROM stream1 [SIZE 10 MINUTES ADVANCE 2 SECONDS TIME] , stream2 [SIZE 10 MINUTES TIME] WHERE attr3 > 100 HAVING Counter > 1000;", 
      "\n\t\t\toperator_1=AGGREGATE({AGGREGATIONS=[[\'COUNT\',\'stream1.attr1\',\'Counter\',\'Integer\']]},JOIN(TIMEWINDOW({size=[10,\'MINUTES\'],advance=[2,\'SECONDS\']},stream1),TIMEWINDOW({size=[10,\'MINUTES\'],advance=[1,\'MINUTES\']},stream2)))\n\t\t\toperator_2=SELECT({predicate=\'Counter>1000&&stream2.attr3>100\'},operator_1)\n\t\t\toperator_3=MAP({expressions=[\'Counter\',\'stream2.attr3\']},operator_2)", _cQLDictionaryHelper);
  }
  
  @Test
  public void NestedStatementTest1() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr1 FROM (SELECT attr1 FROM stream1) AS str1", 
      "\n\t\t\toperator_1 = MAP({expressions=[\'stream1.attr1\']}, stream1)\n\t\t\toperator_2 = MAP({expressions=[\'stream1.attr1\']}, operator_1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void NestedStatementTest2() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr1, attr3 FROM (SELECT attr1 FROM stream1) AS s1, (SELECT * FROM stream2) AS s2", 
      "\n\t\t\toperator_1 = MAP({expressions=[\'stream1.attr1\']}, stream1)\n\t\t\toperator_2 = MAP({expressions=[\'stream2.attr3\', \'stream2.attr4\']}, stream2)\n\t\t\toperator_3 = MAP({expressions=[\'stream1.attr1\', \'stream2.attr3\']}, JOIN(operator_1, operator_2))", _cQLDictionaryHelper);
  }
  
  @Test
  public void NestedStatementTest3() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr1 FROM stream1 WHERE attr1 IN (SELECT attr1 FROM stream1 WHERE attr1 > 1000)", 
      "\n\t\t\toperator_1 = SELECT({predicate=\'stream1.attr1 > 1000\'}, stream1)\n\t\t\toperator_2 = MAP({expressions=[\'stream1.attr1\']}, operator_1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void NestedStatementTest4() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr1 FROM stream1 WHERE attr1 IN (SELECT attr1 FROM stream1 WHERE attr1 > 1000) AND attr2 != 2", 
      "\t\n\t\t\toperator_1 = SELECT({predicate=\'stream1.attr1>1000&&stream1.attr2!=2\'}, stream1)\n\t\t\toperator_2 = MAP({expressions=[\'stream1.attr1\']}, operator_1)\n\n\t\t\t", _cQLDictionaryHelper);
  }
  
  @Test
  public void NestedStatementTest5() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr1, attr2 FROM stream1 \n\t\t\t WHERE  attr2 != 20 \n\t\t\t\t\tOR attr1 IN (SELECT attr1 FROM stream1 WHERE attr1 > 1000) \n\t\t\t\t\tAND attr1 != 0 \n\t\t\t\t\tAND attr2 IN (SELECT attr1, attr2 FROM stream1 WHERE attr1 < 100) AND attr2 > 1.92", 
      "\n\t\t\toperator_1 = SELECT({predicate=\'stream1.attr2 !=20 \n\t\t\t\t\t\t\t|| stream1.attr1 > 1000 \n\t\t\t\t\t\t\t&& stream1.attr1 != 0 \n\t\t\t\t\t\t\t&&  stream1.attr1 < 100 \n\t\t\t\t\t\t\t&& stream1.attr2 > 1.92\'}, stream1)\n\t\t\toperator_2 = MAP({expressions=[\'stream1.attr1\',\'stream1.attr2\']}, operator_1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void NestedStatementTest6() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr1, attr2 FROM stream1 \n\t\t\t WHERE attr1 IN (SELECT attr1, attr2 FROM (SELECT * FROM stream1 WHERE attr1 < 1234) AS s1, (SELECT * FROM stream2) AS s2)", 
      "\n\t\t\toperator_1 = SELECT({predicate=\'stream1.attr1<1234\'},stream1)\n\t\t\toperator_2 = MAP({expressions=[\'stream1.attr1\',\'stream1.attr2\']},operator_1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectExpressionTest1() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT DolToEur(attr1) FROM stream1;", 
      "operator_1=MAP({expressions=[[\'DolToEur(stream1.attr1)\',\'expression_0\']]},stream1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectExpressionTest2() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT attr1 + attr1 AS attr1PlusAttr1 FROM stream1;", 
      "operator_1=MAP({expressions=[[\'stream1.attr1 + stream1.attr1\',\'attr1PlusAttr1\']]},stream1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectExpressionTest3() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT DolToEur(attr1 * attr2) + 10.0 - attr2 AS exp1, DolToEur(attr1) AS exp2 FROM stream1;", 
      "operator_1=MAP({expressions=[[\'DolToEur(stream1.attr1*stream1.attr2)+10.0-stream1.attr2\',\'exp1\'], [\'DolToEur(stream1.attr1)\',\'exp2\']]},stream1)", _cQLDictionaryHelper);
  }
  
  public void SelectExpressionTest4() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT DolToEur(DolToEur(attr1 + 20.25)) FROM stream1;", 
      "operator_1=MAP({expressions=[[\'DolToEur(DolToEur(stream1.attr1 + 20.25))\',\'expression_0\']]},stream1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectExpressionTest5() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT \'hello\' + \'world\' FROM stream1;", 
      "operator_1=MAP({expressions=[[\'\"hello\"+\"world\"\',\'expression_0\']]},stream1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectExpressionTest6() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT DolToEur(attr1), attr1 FROM stream1;", 
      "operator_1 = MAP({expressions=[[\'DolToEur(stream1.attr1)\',\'expression_0\'],\'stream1.attr1\']}, stream1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectExpressionTest7() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT b.attr1, DolToEur(b.attr2) AS euroPrice FROM stream1 [UNBOUNDED] AS b;", 
      "operator_1=RENAME({aliases=[\'attr1\',\'b.attr1\',\'attr2\',\'b.attr2\'],pairs=\'true\'},stream1)\n\t\t\t operator_2=MAP({expressions=[\'b.attr1\',[\'DolToEur(b.attr2)\',\'euroPrice\']]}, operator_1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectExpressionTest8() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT DolToEur(stream1.attr1), AVG(attr1) FROM stream1;", 
      "operator_1=MAP({expressions=[[\'DolToEur(stream1.attr1)\',\'expression_0\'],\'AVG_0\']},AGGREGATE({AGGREGATIONS=[[\'AVG\',\'stream1.attr1\',\'AVG_0\',\'Integer\']]},stream1))", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectExpressionTest9() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT DolToEur(stream1.attr1), AVG(attr1), attr1 AS a1 FROM stream1;", 
      "operator_1=RENAME({aliases=[\'attr1\',\'a1\'],pairs=\'true\'},stream1)\n\t\t\t operator_2=AGGREGATE({AGGREGATIONS=[[\'AVG\',\'stream1.attr1\',\'AVG_0\',\'Integer\']]},operator_1)\n\t\t\t operator_3=MAP({expressions=[[\'DolToEur(stream1.attr1)\',\'expression_0\'],\'AVG_0\',\'a1\']},operator_2)", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectExpressionTest10() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT DolToEur(stream1.attr1), AVG(attr1), attr1 FROM stream1 WHERE attr1 > 10;", 
      "operator_1 = AGGREGATE({AGGREGATIONS=[[\'AVG\',\'stream1.attr1\',\'AVG_0\',\'Integer\']]},stream1)\n             operator_2 = SELECT({predicate=\'stream1.attr1>10\'}, operator_1)\n             operator_3 = MAP({expressions=[[\'DolToEur(stream1.attr1)\',\'expression_0\'],\'AVG_0\', \'stream1.attr1\']}, operator_2)", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectExpressionTest11() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT DolToEur(stream1.attr1), AVG(attr1) FROM stream1 WHERE attr1 > 10;", 
      "operator_1 = AGGREGATE({AGGREGATIONS=[[\'AVG\',\'stream1.attr1\',\'AVG_0\',\'Integer\']]},stream1)\n\t\t\t operator_2 = SELECT({predicate=\'stream1.attr1>10\'}, operator_1)\n\t\t\t operator_3 = MAP({expressions=[[\'DolToEur(stream1.attr1)\',\'expression_0\'],\'AVG_0\']}, operator_2)", _cQLDictionaryHelper);
  }
  
  @Test
  public void SelectExpressionTest12() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT DolToEur(stream1.attr1) FROM stream1 WHERE attr1 > 10;", 
      "operator_1 = SELECT({predicate=\'stream1.attr1>10\'}, stream1)\n\t\t\t operator_2 = MAP({expressions=[[\'DolToEur(stream1.attr1)\',\'expression_0\']]}, operator_1)", _cQLDictionaryHelper);
  }
  
  @Test
  public void DistinctTest() {
    CQLDictionaryHelper _cQLDictionaryHelper = new CQLDictionaryHelper();
    this.assertCorrectGenerated(
      "SELECT DISTINCT * FROM stream1;", 
      "operator_1 = DISTINCT(MAP({expressions=[\'stream1.attr1\', \'stream1.attr2\']}, stream1))", _cQLDictionaryHelper);
  }
}
