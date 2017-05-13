package de.uniol.inf.is.odysseus.parser.novel.cql.tests

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Model
import de.uniol.inf.is.odysseus.parser.novel.cql.generator.CQLGenerator
import de.uniol.inf.is.odysseus.parser.novel.cql.tests.util.CQLDictionaryHelper
import de.uniol.inf.is.odysseus.parser.novel.cql.tests.util.NameProviderHelper
import java.util.Set
import org.eclipse.xtext.generator.InMemoryFileSystemAccess
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith

import static extension org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(CQLInjectorProvider)
class CQLGeneratorQueryTest 
{

	@Inject extension CQLGenerator
	@Inject extension ParseHelper<Model>
	@Inject extension ValidationTestHelper
	
	@Rule public ExpectedException thrown = ExpectedException.none()
	
	def void assertCorrectGenerated(String s, String t, CQLDictionaryHelper dictionary) 
	{
		
//		s.parse.assertNoErrors//TODO Validator not working because no CQLDictionary can be found
		var model = s.parse 
//		validate(model)
		val fsa = new InMemoryFileSystemAccess()
		if(dictionary !== null)
			CQLSchemata = dictionary.schema as Set<SDFSchema>
		nameProvider = new NameProviderHelper()
//		outerschema = dictionary.schema as Set<SDFSchema>
        doGenerate(model.eResource(), fsa, null)
        clear
        var query = ''
		for(e : fsa.textFiles.entrySet)
		{
			query += e.value
		}
//		println("result: " + query)
		format(t).assertEquals(format(query))
	} 
	
	//TODO escape symbols should retrieved with System.getProperties(linesperator)
	def String format(String s) { s.replaceAll("\\s*[\\r\\n]+\\s*", "").trim().replace(" ","") }
	
	@Test def void SelectAllTest1() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1;"
			,
			"operator_1 = MAP({expressions=['stream1.attr1', 'stream1.attr2']}, stream1)"
		, new CQLDictionaryHelper())
	}
	
	@Test def void SelectAllTest2() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1, stream2, stream3;"
			,
			"operator_1 = JOIN(stream1, JOIN(stream2, stream3))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void SelectAllTest3() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1, stream2 AS s2, stream3 WHERE attr1 > 2 AND attr2 = 'Test';"
			,
			"operator_1=RENAME({aliases=['attr3','s2.attr3','attr4','s2.attr4'],pairs='true'},stream2)
			 operator_2=SELECT({predicate='stream1.attr1>2&&stream1.attr2=='Test''},JOIN(stream1,JOIN(operator_1,stream3)))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void SelectAllTest4() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 WHERE attr1 > 2 AND attr2 = 'Test';"
			,
			"operator_1 = SELECT({predicate='stream1.attr1 > 2 && stream1.attr2 == 'Test''}, stream1)"
		, new CQLDictionaryHelper())
	}
	
	@Test def void SelectAllTest5() 
	{ 
		assertCorrectGenerated
		(
			"SELECT s1.* FROM stream1 AS s1 WHERE attr1 > 2 AND attr2 = 'Test';"
			,
			"operator_1=RENAME({aliases=['attr1','s1.attr1','attr2','s1.attr2'],pairs='true'},stream1)
			 operator_2=SELECT({predicate='s1.attr1>2&&s1.attr2=='Test''},operator_1)"
		, new CQLDictionaryHelper())
	}


    @Test def void SelectAllTest6()
    {
        assertCorrectGenerated
        (
            "SELECT stream1.* FROM stream1"
            ,
            "operator_1 = MAP({expressions=['stream1.attr1', 'stream1.attr2']}, stream1)"
        , new CQLDictionaryHelper()) 
    }
	
    @Test def void SelectAllTest7()
    {
        assertCorrectGenerated
        (
            "SELECT b.* FROM stream1 as b"
            ,
            "operator_1=RENAME({aliases=['attr1','b.attr1','attr2','b.attr2'],pairs='true'},stream1)
			 operator_2=MAP({expressions=['b.attr1','b.attr2']},operator_1)"
        , new CQLDictionaryHelper()) 
    }

    @Test def void SelectAllTest8()
    {
        assertCorrectGenerated
        (
            "SELECT b.* FROM stream1 as b WHERE b.attr1 > 100"
            ,
            "operator_1=RENAME({aliases=['attr1','b.attr1','attr2','b.attr2'],pairs='true'},stream1)
			 operator_2=SELECT({predicate='b.attr1>100'},operator_1)"
        , new CQLDictionaryHelper()) 
    }

    @Test def void SelectAllTest9()
    {
        assertCorrectGenerated
        (
            "SELECT b.*, d.* FROM stream1 AS b, stream2 AS d"
            ,
            "operator_1=RENAME({aliases=['attr1','b.attr1','attr2','b.attr2'],pairs='true'},stream1)
			 operator_2=RENAME({aliases=['attr3','d.attr3','attr4','d.attr4'],pairs='true'},stream2)
			 operator_3=MAP({expressions=['b.attr1','b.attr2','d.attr3','d.attr4']},JOIN(operator_1,operator_2))"
        , new CQLDictionaryHelper()) 
    }

    @Test def void SelectAllTest10()
    {
        assertCorrectGenerated
        (
            "SELECT * FROM stream1 AS b"
            ,
            "operator_1=RENAME({aliases=['attr1','b.attr1','attr2','b.attr2'],pairs='true'},stream1)
			 operator_2=MAP({expressions=['b.attr1','b.attr2']},operator_1)"
        , new CQLDictionaryHelper()) 
    }
    
    @Test def void SelectAllTest11()
    {
        assertCorrectGenerated
        (
            "SELECT * FROM stream1 AS b WHERE b.attr1 > 10"
            ,
            "operator_1=RENAME({aliases=['attr1','b.attr1','attr2','b.attr2'],pairs='true'},stream1)
			 operator_2=SELECT({predicate='b.attr1>10'},operator_1)"
        , new CQLDictionaryHelper()) 
    }
	

    @Test def void RenameTest1()
    {
        assertCorrectGenerated
        (
            "SELECT attr1 AS a1, attr2 AS a2 FROM stream1"
            ,
            "
			 operator_1 = RENAME({aliases=['attr1','a1','attr2','a2'], pairs='true'}, stream1)
			 operator_2 = MAP({expressions=['a1', 'a2']}, operator_1)"
        , new CQLDictionaryHelper()) 
    }

    @Test def void RenameTest2()
    {
        assertCorrectGenerated
        (
            "SELECT a.attr1 AS a1, b.attr1 AS b1 FROM stream1 AS a, stream1 AS b"
            ,
            "operator_1=RENAME({aliases=['attr1','a1'],pairs='true'},stream1)
			 operator_2=RENAME({aliases=['attr1','b1','attr2','stream1.attr2#1'],pairs='true'},stream1)
			 operator_3=MAP({expressions=['a1','b1']},JOIN(operator_1, operator_2))"
        , new CQLDictionaryHelper()) 
    }

	@Test def void RenameTest3()
    {
        assertCorrectGenerated
        (
            "SELECT a.attr1 AS a1, attr2 AS b1 FROM stream1 AS a"
            ,
            "
			 operator_1 = RENAME({aliases=['attr1','a1', 'attr2', 'b1'], pairs='true'}, stream1)
			 operator_2 = MAP({expressions=['a1', 'b1']}, operator_1)"
        , new CQLDictionaryHelper()) 
    }
    
	@Test def void RenameTest5()
    {
    	thrown.expect(IllegalArgumentException)
        assertCorrectGenerated
        (
            "SELECT a.attr1 AS a1, stream1.attr2 AS b1 FROM stream1 AS a, stream1 AS b"
            ,
            ""
        , new CQLDictionaryHelper()) 
        thrown.reportMissingExceptionWithMessage("No exception of %s thrown")
    }

    @Test def void RenameTest6()
    {
    	thrown.expect(IllegalArgumentException)
        assertCorrectGenerated
        (
            "SELECT a.attr1 AS a1, attr1 AS b1 FROM stream1 AS a"
            ,
            ""
        , new CQLDictionaryHelper())
        thrown.reportMissingExceptionWithMessage("No exception of %s thrown") 
    }

    @Test def void RenameTest7()
    {
        assertCorrectGenerated
        (
            "SELECT b.attr1, b.attr2 FROM stream1 AS b"
            ,
            "operator_1=RENAME({aliases=['attr1','b.attr1','attr2','b.attr2'],pairs='true'},stream1)
			 operator_2=MAP({expressions=['b.attr1','b.attr2']},operator_1)"
        , new CQLDictionaryHelper())
    }


    @Test def void AmbiguouseAttributeTest1()
    {
        thrown.expect(IllegalArgumentException)
        assertCorrectGenerated
        (
            "SELECT attr1 AS a1, attr1 AS a2 FROM stream1, stream4"
            ,
            ""
        , new CQLDictionaryHelper()
        ) 
        thrown.reportMissingExceptionWithMessage("No exception of %s thrown")
    }

    @Test def void AmbiguouseAttributeTest2()
    {
        thrown.expect(IllegalArgumentException)
        assertCorrectGenerated
        (
            "SELECT stream1.attr1 AS a1, attr1 AS a2 FROM stream1, stream4"
            ,
            ""
        , new CQLDictionaryHelper()
        ) 
        thrown.reportMissingExceptionWithMessage("No exception of %s thrown")
    }
	
    @Test def void AmbiguouseAttributeTest3()
    {
        thrown.expect(IllegalArgumentException)
        assertCorrectGenerated
        (
            "SELECT attr1 AS a1, stream4.attr1 AS a2 FROM stream1, stream4"
            ,
            ""
        , new CQLDictionaryHelper()
        ) 
        thrown.reportMissingExceptionWithMessage("No exception of %s thrown")
    }

    @Test def void AmbiguouseAttributeTest4()
    {
        thrown.expect(IllegalArgumentException)
        assertCorrectGenerated
        (
            "SELECT attr1 FROM stream1 AS s1, stream1 AS s2;"
            ,
            ""
        , new CQLDictionaryHelper()
        ) 
        thrown.reportMissingExceptionWithMessage("No exception of %s thrown")
    }

    @Test def void AmbiguouseAttributeTest5()
    {
        thrown.expect(IllegalArgumentException)
        assertCorrectGenerated
        (
            "SELECT attr1 FROM stream1 AS s1, stream1 AS s2"
            ,
            ""
        , new CQLDictionaryHelper()
        ) 
        thrown.reportMissingExceptionWithMessage("No exception of %s thrown")
    }

    @Test def void AmbiguouseAttributeTest6()
    {
        thrown.expect(IllegalArgumentException)
        assertCorrectGenerated
        (
            "SELECT attr1 FROM stream1 AS s1, stream1 AS s2, stream1 AS s3;"
            ,
            ""
        , new CQLDictionaryHelper()
        ) 
        thrown.reportMissingExceptionWithMessage("No exception of %s thrown")
    }

	@Test def void AliasTest1()
	{
		assertCorrectGenerated
		(
			"SELECT attr1 AS value1 FROM stream1;"
			,
			"operator_1  = RENAME({aliases=['attr1','value1'],pairs='true'}, stream1)
			 operator_2 = MAP({expressions=['value1']}, operator_1)"		
			, new CQLDictionaryHelper()
		)
	}

	@Test def void AliasTest2()
	{
		assertCorrectGenerated
		(
			"SELECT attr1 AS value1 FROM stream1 AS s1 WHERE s1.value1 > 2;"
			,
			"operator_1=RENAME({aliases=['attr1','value1'],pairs='true'},stream1)
			 operator_2=SELECT({predicate='value1>2'},operator_1)
			 operator_3=MAP({expressions=['value1']},operator_2)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void AliasTest3()
	{
		assertCorrectGenerated
		(
			"SELECT s1.attr1 AS value1 FROM stream1 AS s1 WHERE s1.value1 > 2;"
			,
			"operator_1=RENAME({aliases=['attr1','value1'],pairs='true'},stream1)
			 operator_2=SELECT({predicate='value1>2'},operator_1) 
			 operator_3=MAP({expressions=['value1']},operator_2)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void AliasTest4()
	{
		assertCorrectGenerated
		(
			"SELECT s1.attr1 AS value1 FROM stream1 AS s1 WHERE s1.attr1 > 2;"
			,
			"operator_1=RENAME({aliases=['attr1','value1'],pairs='true'},stream1)
			 operator_2=SELECT({predicate='value1>2'},operator_1) 
			 operator_3=MAP({expressions=['value1']},operator_2)"
			, new CQLDictionaryHelper()
		)
	}
	
//	
//	@Test def void AliasTest4()
//	{
//		assertCorrectGenerated
//		(
//			"SELECT attr1 AS value1 FROM stream1 AS s1 WHERE stream1.value1 > 2;"
//			,
//			aliasTestResult
//			, new CQLDictionaryHelper()
//		)
//	}
//	
//	@Test def void AliasTest5()
//	{
//		assertCorrectGenerated
//		(
//			"SELECT attr1 AS value1 FROM stream1 AS s1 WHERE attr1 > 2;"
//			,
//			aliasTestResult
//			, new CQLDictionaryHelper()
//		)
//	}
//	
//	@Test def void AliasTest6()
//	{
//		assertCorrectGenerated
//		(
//			"SELECT a.attr1 AS aid FROM stream1 AS a, stream1 AS b WHERE aid=stream1.attr1",
//			"operator_1=SELECT({predicate='stream1.attr1==stream1.attr1'},JOIN(RENAME({aliases=['attr1','stream1.attr1-1','attr2','stream1.attr2-1'],pairs='true'},stream1),stream1))
//			 operator_2=MAP({expressions=['stream1.attr1']},operator_1)
//			 renamed_3=RENAME({aliases=['attr1','aid'],pairs='true'},operator_2)"
//			, new CQLDictionaryHelper()
//		)
//		
//	}
//	
//	@Test def void AliasTest7()
//	{
//		assertCorrectGenerated
//		(
//			"SELECT COUNT(stream1.attr1) AS value1 FROM stream1 AS s1 WHERE attr1 > 2;"
//			,
//			"operator_1 = AGGREGATE({AGGREGATIONS=[['COUNT','stream1.attr1','value1','Integer']]}, stream1)
//			 operator_2 = SELECT({predicate='stream1.attr1>2'}, operator_1)
//			 operator_3 = MAP({expressions=['value1']}, operator_2)"   
//			, new CQLDictionaryHelper()
//		)
//	}
	
	@Test def void SelectAttr1Test1() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1 WHERE attr1 > 2;"
			,
			"
			operator_1 = SELECT({predicate='stream1.attr1>2'},stream1)
			operator_2 = MAP({expressions=['stream1.attr1']},operator_1)"
		, new CQLDictionaryHelper())
	}
	
	@Test def void SelectAttr1Test2() 
	{ 
		assertCorrectGenerated
		(
			"SELECT stream1.attr1 FROM stream1, stream2, stream3 WHERE attr1 > 2"
			,
			"
			operator_1 = SELECT({predicate='stream1.attr1>2'},JOIN(stream1,JOIN(stream2,stream3)))
			operator_2 = MAP({expressions=['stream1.attr1']},operator_1)"
		, new CQLDictionaryHelper())
	}
	
	@Test def void SelectAttr1Test3() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1;"
			,
			"operator_1 = MAP({expressions=['stream1.attr1']}, stream1)"
		, new CQLDictionaryHelper())
	}
	
	@Test def void SelectAttr1Test4() 
	{ 
		assertCorrectGenerated
		(
			"SELECT stream1.attr1, stream1.attr2, attr3 FROM stream1, stream2, stream3;"
			,
			"operator_1 = MAP({expressions=['stream1.attr1', 'stream1.attr2', 'stream2.attr3']},
							JOIN(stream1, 
								JOIN(stream2, stream3)))"
		, new CQLDictionaryHelper())
	}

	@Test def void SelectAttr1Test5() 
	{ 
		assertCorrectGenerated
		(
			"SELECT stream1.attr1, AVG(attr1) AS avgAttr1, stream1.attr2, attr3 FROM stream1, stream2, stream3;"
			,
			"operator_1 = AGGREGATE({AGGREGATIONS=[['AVG','stream1.attr1','avgAttr1','Integer']]}, JOIN(stream1,JOIN(stream2,stream3)))
			 operator_2 = MAP({expressions=['stream1.attr1','avgAttr1','stream1.attr2','stream2.attr3']}, JOIN(operator_1, JOIN(stream1, JOIN(stream2, stream3))))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void SelectAttr1Test6() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1, attr2 FROM stream1 WHERE attr1=7 OR attr1=20 OR attr1=21 OR attr1=59 OR attr1=87;"
			,
			"operator_1 = SELECT({predicate='stream1.attr1==7||stream1.attr1==20||stream1.attr1==21||stream1.attr1==59||stream1.attr1==87'},stream1)
			 operator_2 = MAP({expressions=['stream1.attr1','stream1.attr2']},operator_1)"
		, new CQLDictionaryHelper())
	}
	
    @Test def void SelectAttr1Test7() 
    { 
        assertCorrectGenerated
        (
            "SELECT a.attr1 AS aid, b.attr1 AS d, b.attr1 FROM stream1 AS a, stream1 AS b WHERE aid=b.attr1;"
            ,
            "operator_1=RENAME({aliases=['attr1','aid'],pairs='true'},stream1)
 			 operator_2=RENAME({aliases=['attr1','d','attr2','stream1.attr2#1'],pairs='true'},stream1)
 			 operator_3=RENAME({aliases=['attr1','b.attr1','attr2','stream1.attr2#2'],pairs='true'},stream1)
			 operator_4=SELECT({predicate='aid==b.attr1'},JOIN(operator_1,JOIN(operator_2,operator_3)))
			 operator_5=MAP({expressions=['aid','d','b.attr1']},operator_4)"
        , new CQLDictionaryHelper())
    }
	
 	@Test def void SelectAttr1Test8() 
    { 
        assertCorrectGenerated
        (
            "SELECT b.attr1 FROM stream1 AS b WHERE b.attr1 < 150.0;"
            ,
            "operator_1=RENAME({aliases=['attr1','b.attr1'],pairs='true'},stream1)
			 operator_2=SELECT({predicate='b.attr1<150.0'},operator_1)
			 operator_3=MAP({expressions=['b.attr1']},operator_2)"
        , new CQLDictionaryHelper())
    }
    
 	@Test def void SelectAttr1Test9() 
    { 
        assertCorrectGenerated
        (
            "SELECT attr1 FROM stream1 AS b, stream2 AS p WHERE b.attr2 = p.attr3;"
            ,
            "operator_1=SELECT({predicate='stream1.attr2==stream2.attr3'},JOIN(stream1,stream2))
			 operator_2=MAP({expressions=['stream1.attr1']},operator_1)"
        , new CQLDictionaryHelper())
    }
	
	@Test def void CreateViewTest1()
	{
		assertCorrectGenerated
		(
			"CREATE VIEW view1 FROM (
				SELECT * FROM stream1
			)"
			,
			"
			view1 := MAP({expressions=['stream1.attr1', 'stream1.attr2']}, stream1)
			"
			, new CQLDictionaryHelper	
		)
	}
	
	@Test def void CreateStreamTest1()
	{
		assertCorrectGenerated
		(
			"CREATE STREAM stream1 (attr1 Integer) 
    		WRAPPER 'GenericPush'
    		PROTOCOL 'CSV'
    		TRANSPORT 'File'
    		DATAHANDLER 'Tuple'
    		OPTIONS ('port' '54321', 'host' 'localhost')"
			,
			"stream1 := ACCESS
			(
				{ source = 'stream1', 
				  wrapper = 'GenericPush',
				  protocol = 'CSV',
				  transport = 'File',
				  dataHandler ='Tuple',
				  schema = [['attr1', 'Integer']],
				  options =[['port', '54321'],['host', 'localhost']]
				}
			)"
			, null
		)
	}
	
	@Test def void CreateStreamTest2()
	{
		assertCorrectGenerated
		(
			"ATTACH STREAM stream1 (attr1 INTEGER) CHANNEL localhost : 54321;"
			,
			"stream1 := ACCESS
			(
				{ source = 'stream1', 
				  wrapper = 'GenericPush',
				  protocol = 'SizeByteBuffer',
				  transport = 'NonBlockingTcp',
				  dataHandler ='Tuple',
				  schema = [['attr1', 'INTEGER']],
				  options =[['port', '54321'],['host', 'localhost']]
				}
			)"
			, null
		)
	}

	@Test def void CreateStreamTest3()
	{
		assertCorrectGenerated
		(
			"CREATE STREAM stream1 (attr1 Integer) FILE 'this/is/a/filename.file' AS SimpleCSV;"
			,
			"stream1 := ACCESS
			(
				{ source = 'stream1', 
				  wrapper = 'GenericPull',
				  protocol = 'SimpleCSV',
				  transport = 'File',
				  dataHandler ='Tuple',
				  schema = [['attr1', 'Integer']],
				  options =[['filename', 'this/is/a/filename.file'],['delimiter',';'],['textDelimiter',\"'\"]]
				}
			)"
			, null
		)
	}

	@Test def void StreamtoTest1()
	{
		assertCorrectGenerated
		(
			"STREAM TO out1 SELECT * FROM stream1;"
			,
			""
			, new CQLDictionaryHelper()	
		)	
	}
	
	@Test def void StreamtoTest2()
	{
		assertCorrectGenerated
		(
			"STREAM TO out1 input1;"
			,
			""
			, new CQLDictionaryHelper()	
		)	
	}

	@Test def void StreamtoTest3()
	{
		assertCorrectGenerated
		(
			"
			CREATE SINK out1 (attr1 INTEGER, attr2 STRING)
			WRAPPER 'GenericPush'
			PROTOCOL 'CSV'
			TRANSPORT 'FILE'
			DATAHANDLER 'TUPLE'
			OPTIONS('filename' 'outfile1') 			

			STREAM TO out1 SELECT attr1, attr2 FROM stream1 WHERE attr2 != attr1;"
			,
			"operator_1 = SELECT({predicate='stream1.attr2!=stream1.attr1'},stream1) 
			 operator_2 = MAP({expressions=['stream1.attr1','stream1.attr2']},operator_1)
			 out1 := SENDER
				  (
					{	  
						sink      = 'out1', 
						wrapper     = 'GenericPush',
						protocol    = 'CSV',
						transport   = 'FILE',
						dataHandler = 'TUPLE',
						options =[['filename','outfile1']]
					 }
					 ,operator_2
				   )"
			, new CQLDictionaryHelper()	
		)	
	}

	@Test def void StreamtoTest4()
	{
		assertCorrectGenerated
		(
			"
			CREATE SINK out1 (attr1 INTEGER, attr2 STRING)
			WRAPPER 'GenericPush'
			PROTOCOL 'CSV'
			TRANSPORT 'FILE'
			DATAHANDLER 'TUPLE'
			OPTIONS('filename' 'outfile1') 			

			STREAM TO out1 stream1;"
			,
			"out1:=SENDER({sink='out1',wrapper='GenericPush',protocol='CSV',transport='FILE',dataHandler='TUPLE',options=[['filename','outfile1']]}, out1)"
			, new CQLDictionaryHelper()	
		)	
	}
	
	@Test def void WindowUnboundedTest1() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 [UNBOUNDED];"
			,
			"operator_1 = MAP({expressions=['stream1.attr1', 'stream1.attr2']}, stream1)"
		, new CQLDictionaryHelper())
	}
	
	@Test def void WindowTimeTest1() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 [SIZE 5 MINUTES TIME];"
			,
			"operator_1 = MAP({expressions=['stream1.attr1', 'stream1.attr2']},
							 TIMEWINDOW({size=[5, 'MINUTES'], advance=[1, 'MINUTES']}, stream1))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void WindowTimeTest2() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 [SIZE 5 MINUTES ADVANCE 1 SECONDS TIME];"
			,
			"operator_1 = MAP({expressions=['stream1.attr1', 'stream1.attr2']}, 
							TIMEWINDOW({size=[5, 'MINUTES'], advance=[1, 'SECONDS']}, stream1))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void WindowElementTest1() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 [SIZE 5 TUPLE];"
			,
			"operator_1 = MAP({expressions=['stream1.attr1', 'stream1.attr2']}, 
							ELEMENTWINDOW({size=5,advance=1}, stream1))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void WindowUnboundedTest2() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 [UNBOUNDED], stream2 [UNBOUNDED];"
			,
			"operator_1 = JOIN(stream1, stream2)"
		, new CQLDictionaryHelper())
	}
	
	@Test def void WindowTimeWindowElementTest1() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 [SIZE 5 TUPLE], stream2 [SIZE 5 MINUTES TIME];"
			,
			"operator_1 = JOIN(ELEMENTWINDOW({size=5,advance=1}, stream1), 
							TIMEWINDOW({size=[5, 'MINUTES'], advance=[1, 'MINUTES']}, stream2))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void WindowTimeWindowElementTest2() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1, attr2, attr4 FROM stream1 [SIZE 5 TUPLE], stream2 [SIZE 5 MINUTES TIME], stream3;"
			,
			"operator_1 = MAP({expressions=['stream1.attr1','stream1.attr2','stream2.attr4']}, 
							JOIN(ELEMENTWINDOW({size=5,advance=1}, stream1), 
								JOIN(TIMEWINDOW({size=[5, 'MINUTES'], advance=[1, 'MINUTES']}, stream2), stream3)))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void WindowTimeWindowElementTest3() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1, attr2, attr4 FROM stream1 [SIZE 5 TUPLE], stream2 [SIZE 5 MINUTES TIME], stream3 WHERE attr4 != attr1;"
			,
			"
			operator_1 = SELECT({predicate='stream2.attr4 != stream1.attr1'}, JOIN(ELEMENTWINDOW({size=5,advance=1},stream1),
								JOIN(TIMEWINDOW({size=[5,'MINUTES'],advance=[1,'MINUTES']},stream2),stream3)))
			operator_2 = MAP({expressions=['stream1.attr1','stream1.attr2','stream2.attr4']}, operator_1)"
		, new CQLDictionaryHelper())
	}
	
	@Test def void WindowTimeWindowElementTest4() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 [SIZE 5 TUPLE], stream2 [SIZE 5 MINUTES TIME] WHERE attr4 != attr1;"
			,
			"operator_1 = SELECT({predicate='stream2.attr4 != stream1.attr1'}, 
							JOIN(ELEMENTWINDOW({size=5,advance=1}, stream1),
								TIMEWINDOW({size=[5, 'MINUTES'], advance=[1, 'MINUTES']}, stream2)))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void WindowElement2() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1 [SIZE 5 TUPLE];"
			,
			"operator_1 = MAP({expressions=['stream1.attr1']}, ELEMENTWINDOW({size=5,advance=1}, stream1))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void AggregationTest1()
	{
		assertCorrectGenerated
		(
			"SELECT COUNT(attr1) AS Counter FROM stream1 GROUP BY attr1;"
			,
			"operator_1=AGGREGATE({AGGREGATIONS=[['COUNT','stream1.attr1','Counter','Integer']],GROUP_BY=['stream1.attr1']},stream1)
			 operator_2=MAP({expressions=['Counter']},operator_1)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void AggregationTest2()
	{
		assertCorrectGenerated
		(
			"SELECT COUNT(attr1) AS Counter, AVG(attr2) FROM stream1 GROUP BY attr1, attr2;"
			,
			"operator_1=AGGREGATE({AGGREGATIONS=[['COUNT','stream1.attr1','Counter','Integer'],['AVG','stream1.attr2','AVG_0','String']],GROUP_BY=['stream1.attr1','stream1.attr2']},stream1)
			 operator_2=MAP({expressions=['Counter','AVG_0']},operator_1)"
			, new CQLDictionaryHelper()
		)
	}	
	
	@Test def void AggregationTest3()
	{
		assertCorrectGenerated
		(
			"SELECT COUNT(attr1) AS Counter, AVG(attr2) FROM stream1 [SIZE 10 MINUTES TIME] AS s1 , stream2 GROUP BY attr1, attr2;"
			,
			"operator_1=AGGREGATE({AGGREGATIONS=[['COUNT','stream1.attr1','Counter','Integer'],['AVG','stream1.attr2','AVG_0','String']],GROUP_BY=['stream1.attr1','stream1.attr2']},JOIN(TIMEWINDOW({size=[10,'MINUTES'],advance=[1,'MINUTES']},stream1),stream2))
		     operator_2=MAP({expressions=['Counter','AVG_0']},operator_1)"
			, new CQLDictionaryHelper()
		)
	}

	@Test def void AggregationTest4()//TODO JOIN is missing 
	{
		assertCorrectGenerated
		(
			"SELECT attr1 as a1, COUNT(attr1) AS Counter FROM stream1 [SIZE 5 MINUTES TIME];"
			,
			"operator_1=RENAME({aliases=['attr1','a1'],pairs='true'},TIMEWINDOW({size=[5,'MINUTES'],advance=[1,'MINUTES']},stream1))
			 operator_2=AGGREGATE({AGGREGATIONS=[['COUNT','a1','Counter','Integer']]},operator_1)
			 operator_3=RENAME({aliases=['attr1','a1'],pairs='true'},TIMEWINDOW({size=[5,'MINUTES'],advance=[1,'MINUTES']},stream1))
			 operator_4=MAP({expressions=['a1','Counter']},JOIN(operator_2,operator_3))"
			, new CQLDictionaryHelper()
		)
	}	
	
	@Test def void AggregationTest5()
	{
		assertCorrectGenerated
		(
			"SELECT COUNT(attr1) AS Counter, attr3 FROM stream1, stream2;"
			,
			"
			operator_1 = AGGREGATE
			(
				{
					AGGREGATIONS=[
									['COUNT', 'stream1.attr1', 'Counter', 'Integer']
								 ]
				}, JOIN(stream1, stream2)
			)
			operator_2 = MAP({expressions=['Counter', 'stream2.attr3']}, JOIN(operator_1, JOIN(stream1, stream2)))"
			, new CQLDictionaryHelper()
		)
	}	
	
	@Test def void AggregationTest6()
	{
		assertCorrectGenerated
		(
			"SELECT COUNT(attr1) AS Counter, attr3 FROM stream1 , stream2 [SIZE 10 MINUTES TIME];"
			,
			"operator_1=AGGREGATE({AGGREGATIONS=[['COUNT','stream1.attr1','Counter','Integer']]},JOIN(stream1,TIMEWINDOW({size=[10,'MINUTES'],advance=[1,'MINUTES']},stream2)))
			 operator_2=MAP({expressions=['Counter','stream2.attr3']},JOIN(operator_1,JOIN(stream1,TIMEWINDOW({size=[10,'MINUTES'],advance=[1,'MINUTES']},stream2))))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void AggregationTest7()
	{
		assertCorrectGenerated
		(
			"SELECT COUNT(attr1) AS Counter, attr3 FROM stream1 [SIZE 10 MINUTES ADVANCE 2 SECONDS TIME] , stream2 [SIZE 10 MINUTES TIME] WHERE attr3 > 100;"
			,
			"operator_1=AGGREGATE({AGGREGATIONS=[['COUNT','stream1.attr1','Counter','Integer']]},JOIN(TIMEWINDOW({size=[10,'MINUTES'],advance=[2,'SECONDS']},stream1),TIMEWINDOW({size=[10,'MINUTES'],advance=[1,'MINUTES']},stream2)))
			 operator_2=SELECT({predicate='stream2.attr3>100'},JOIN(operator_1,JOIN(TIMEWINDOW({size=[10,'MINUTES'],advance=[2,'SECONDS']},stream1),TIMEWINDOW({size=[10,'MINUTES'],advance=[1,'MINUTES']},stream2))))	
			 operator_3=MAP({expressions=['Counter','stream2.attr3']},operator_2)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void AggregationTest8()
	{
		assertCorrectGenerated
		(
			"SELECT COUNT(attr1) AS Counter, attr3 FROM stream1 [SIZE 10 MINUTES ADVANCE 2 SECONDS TIME] , stream2 [SIZE 10 MINUTES TIME] WHERE attr3 > 100 HAVING Counter > 1000;"
			,
			"operator_1=AGGREGATE({AGGREGATIONS=[['COUNT','stream1.attr1','Counter','Integer']]},JOIN(TIMEWINDOW({size=[10,'MINUTES'],advance=[2,'SECONDS']},stream1),TIMEWINDOW({size=[10,'MINUTES'],advance=[1,'MINUTES']},stream2)))
			 operator_2=SELECT({predicate='Counter>1000Counter>1000&&'},JOIN(operator_1,JOIN(TIMEWINDOW({size=[10,'MINUTES'],advance=[2,'SECONDS']},stream1),TIMEWINDOW({size=[10,'MINUTES'],advance=[1,'MINUTES']},stream2))))
			 operator_3=MAP({expressions=['Counter','stream2.attr3']},operator_2)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void AggregationTest9()
	{
		assertCorrectGenerated 
		(
			"SELECT COUNT(attr1 + 20) AS Counter FROM stream1;"
			,
			"operator_1=MAP({expressions=[['stream1.attr1+20','expression_0']]},stream1)
			 operator_2=AGGREGATE({AGGREGATIONS=[['COUNT','expression_0','Counter','DOUBLE']]},operator_1)
			 operator_3=MAP({expressions=['Counter']},operator_2)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void AggregationTest10()//TODO Join is not necessary
	{
		assertCorrectGenerated
		(
			"SELECT COUNT(*) AS count FROM stream1;"
			,
			"operator_1=AGGREGATE({AGGREGATIONS=[['COUNT','*','count']]},stream1)
			 operator_2=MAP({expressions=['count']}, JOIN(operator_1, stream1))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void AggregationTest11()//TODO Join is not necessary
	{
		assertCorrectGenerated
		(
			"SELECT attr1, AVG(attr2) AS aprice FROM stream1 GROUP BY attr1;"
			,
			"operator_1=AGGREGATE({AGGREGATIONS=[['AVG','stream1.attr2','aprice','String']],GROUP_BY=['stream1.attr1']},stream1)
			 operator_2=MAP({expressions=['stream1.attr1','aprice']},JOIN(RENAME({aliases=['attr1','attr1_groupAttribute#0'],pairs='true'},operator_1),stream1))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void NestedStatementTest1()
	{
		assertCorrectGenerated
		(
			"SELECT str1.attr1 FROM (SELECT s1.attr1 FROM stream1 AS s1) AS str1"
			,
			"operator_1=RENAME({aliases=['attr1','s1.attr1'],pairs='true'},stream1)
			 operator_2=MAP({expressions=['s1.attr1']},operator_1)
			 operator_3=RENAME({aliases=['s1_attr1','str1.attr1'],pairs='true'},operator_2)
			 operator_4=MAP({expressions=['str1.attr1']},operator_3)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void NestedStatementTest2()
	{
		assertCorrectGenerated
		(
			"SELECT s1.attr1 AS a1, s1.attr2 AS a3 FROM (SELECT s2.attr1 AS a11, s2.attr2 AS a12 FROM stream1 AS s2) AS s1"
			,
			"
			operator_1=RENAME({aliases=['attr1','a11','attr2','a12'],pairs='true'},stream1)
			operator_2=MAP({expressions=['a11','a12']},operator_1)
			operator_3=RENAME({aliases=['a11','a1','a12','a3'],pairs='true'},operator_2)
			operator_4=MAP({expressions=['a1','a3']},operator_3)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void SelectExpressionTest1()
	{
		assertCorrectGenerated
		(
			"SELECT DolToEur(attr1) FROM stream1;"
			,
			"operator_1=MAP({expressions=[['DolToEur(stream1.attr1)','expression_0']]},stream1)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void SelectExpressionTest2()
	{
		assertCorrectGenerated
		(
			"SELECT attr1 + attr1 AS attr1PlusAttr1 FROM stream1;"
			,
			"operator_1=MAP({expressions=[['stream1.attr1 + stream1.attr1','attr1PlusAttr1']]},stream1)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void SelectExpressionTest3()
	{
		assertCorrectGenerated
		(
			"SELECT DolToEur(attr1 * attr2) + 10.0 - attr2 AS exp1, DolToEur(attr1) AS exp2 FROM stream1;"
			,
			"operator_1=MAP({expressions=[['DolToEur(stream1.attr1*stream1.attr2)+10.0-stream1.attr2','exp1'], ['DolToEur(stream1.attr1)','exp2']]},stream1)"
			, new CQLDictionaryHelper()
		)
	}
	
	def void SelectExpressionTest4()
	{
		assertCorrectGenerated
		(
			"SELECT DolToEur(DolToEur(attr1 + 20.25)) FROM stream1;"
			,
			"operator_1=MAP({expressions=[['DolToEur(DolToEur(stream1.attr1 + 20.25))','expression_0']]},stream1)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void SelectExpressionTest5()
	{
		assertCorrectGenerated
		(
			"SELECT 'hello' + 'world' FROM stream1;"
			,
			"operator_1=MAP({expressions=[['\"hello\"+\"world\"','expression_0']]},stream1)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void SelectExpressionTest6()
	{
		assertCorrectGenerated
		(
			"SELECT DolToEur(attr1), attr1 FROM stream1;"
			,
			"operator_1 = MAP({expressions=[['DolToEur(stream1.attr1)','expression_0'],'stream1.attr1']}, stream1)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void SelectExpressionTest7()
	{
		assertCorrectGenerated
		(
			"SELECT b.attr1, DolToEur(b.attr2) AS euroPrice FROM stream1 [UNBOUNDED] AS b;"
			,
			"operator_1=RENAME({aliases=['attr1','b.attr1','attr2','b.attr2'],pairs='true'},stream1)
			 operator_2=MAP({expressions=['b.attr1',['DolToEur(b.attr2)','euroPrice']]}, operator_1)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void SelectExpressionTest8()
	{
		assertCorrectGenerated
		(
			"SELECT DolToEur(stream1.attr1), AVG(attr1) FROM stream1;"
			,
			"operator_1=AGGREGATE({AGGREGATIONS=[['AVG','stream1.attr1','AVG_0','Integer']]},stream1)
			 operator_2=MAP({expressions=[['DolToEur(stream1.attr1)','expression_0'],'AVG_0']},JOIN(operator_1, stream1))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void SelectExpressionTest9() 
	{
		assertCorrectGenerated
		(
			"SELECT attr1 AS a1, DolToEur(attr1), AVG(attr1) FROM stream1;"
			,
			"operator_1=RENAME({aliases=['attr1','a1'],pairs='true'},stream1)
			 operator_2=AGGREGATE({AGGREGATIONS=[['AVG','a1','AVG_0','Integer']]},operator_1)
			 operator_3=RENAME({aliases=['attr1','a1'],pairs='true'},stream1)
			 operator_4=MAP({expressions=['a1',['DolToEur(a1)','expression_0'],'AVG_0']},JOIN(operator_2,operator_3))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void SelectExpressionTest10()
	{
		assertCorrectGenerated
		(
			"SELECT DolToEur(stream1.attr1), AVG(attr1), attr1 FROM stream1 WHERE attr1 > 10;"
			,
			"operator_1 = AGGREGATE({AGGREGATIONS=[['AVG','stream1.attr1','AVG_0','Integer']]},stream1)
             operator_2 = SELECT({predicate='stream1.attr1>10'}, JOIN(operator_1, stream1))
             operator_3 = MAP({expressions=[['DolToEur(stream1.attr1)','expression_0'],'AVG_0', 'stream1.attr1']}, operator_2)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void SelectExpressionTest11()
	{
		assertCorrectGenerated
		(
			"SELECT DolToEur(stream1.attr1), AVG(attr1) FROM stream1 WHERE attr1 > 10;"
			,
			"operator_1 = AGGREGATE({AGGREGATIONS=[['AVG','stream1.attr1','AVG_0','Integer']]},stream1)
			 operator_2 = SELECT({predicate='stream1.attr1>10'}, JOIN(operator_1, stream1))
			 operator_3 = MAP({expressions=[['DolToEur(stream1.attr1)','expression_0'],'AVG_0']}, operator_2)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void SelectExpressionTest12()
	{
		assertCorrectGenerated
		(
			"SELECT DolToEur(stream1.attr1) FROM stream1 WHERE attr1 > 10;"
			,
			"operator_1 = SELECT({predicate='stream1.attr1>10'}, stream1)
			 operator_2 = MAP({expressions=[['DolToEur(stream1.attr1)','expression_0']]}, operator_1)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void SelectExpressionTest13()
	{
		assertCorrectGenerated
		(
			"SELECT 123.4 AS d FROM stream1;"
			,
			"operator_1=MAP({expressions=[['123.4','d']]},stream1)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void DistinctTest()
	{
		assertCorrectGenerated
		(
			"SELECT DISTINCT * FROM stream1;"
			,
			"operator_1 = DISTINCT(MAP({expressions=['stream1.attr1', 'stream1.attr2']}, stream1))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void MatrixTest1()
	{
		assertCorrectGenerated
		(
			"SELECT [1.0,2.0,3.0;3.0,4.0,6.0] + [2.0,4.0,5.0;12.0,5.0,1.0] FROM stream1;"
			,
			"operator_1=MAP({expressions=[['[1.0,2.0,3.0;3.0,4.0,6.0]+[2.0,4.0,5.0;12.0,5.0,1.0]','expression_0']]},stream1)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void MatrixTest2()
	{
		assertCorrectGenerated
		(
			"SELECT Max([1.0,2.0,3.0;3.0,4.0,6.0]) AS maxElement FROM stream1;"
			,
			"operator_1=MAP({expressions=[['Max([1.0,2.0,3.0;3.0,4.0,6.0])','maxElement']]},stream1)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void MatrixTest3()
	{
		assertCorrectGenerated
		(
			"SELECT attr1 AS a1, Max([1.0,2.0,3.0;3.0,4.0,6.0]) + 7.5 * a1 AS maxElement FROM stream1;"
			,
			"operator_1=RENAME({aliases=['attr1','a1'],pairs='true'},stream1)
			 operator_2=MAP({expressions=['a1',['Max([1.0,2.0,3.0;3.0,4.0,6.0])+7.5*a1','maxElement']]},operator_1)"
			, new CQLDictionaryHelper()
		)
	}
	//TODO price is missing in input source
	//SELECT price, price AS p, Max([1.0,2.3;2.0,1.3]) * price AS result FROM bid
	//operator_1 = RENAME({aliases=['price','p'],pairs='true'},bid)
	//operator_2 = MAP({expressions=['bid.price','p',['Max([1.0,2.3;2.0,1.3])*bid.price','result']]},operator_1)
	
	@Test def void SetOperatorTest1()
	{
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1
			UNION
			SELECT attr3 FROM stream2"
			,
			"operator_1=MAP({expressions=['stream1.attr1']},stream1)
			 operator_2=MAP({expressions=['stream2.attr3']},stream2)
			 operator_3=UNION(operator_1,operator_2)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void CreateDatabaseSourceTest1()
	{
		assertCorrectGenerated
		(
			"CREATE STREAM datastream(id INTEGER, value STRING) DATABASE con1 TABLE main"
			,
			"datastream:=DATABASESOURCE({connection='con1',table='main',attributes=[['id','INTEGER'],['value','STRING']]})"
			, null
		)
	}
	
	@Test def void CreateDatabaseSourceTest2()
	{
		assertCorrectGenerated
		(
			"CREATE STREAM datastream(id INTEGER, value STRING) DATABASE con1 TABLE main EACH 1 SECOND"
			,
			"datastream:=DATABASESOURCE({connection='con1',table='main',attributes=[['id','INTEGER'],['value','STRING']],waiteach=1000.0})"
			, null
		)
	}
	
	@Test def void CreateDatabaseSinkTest1()
	{
		assertCorrectGenerated
		(
			"CREATE SINK dbsink AS DATABASE con1 TABLE example;
			 STREAM TO dbsink SELECT * FROM stream1; 
			"
			,
			"operator_1 = MAP({expressions=['stream1.attr1','stream1.attr2']},stream1)
			 dbsink := DATABASESINK({connection='con1',table='example'},operator_1)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void CreateDatabaseSinkTest2()
	{
		assertCorrectGenerated
		(
			"CREATE SINK dbsink AS DATABASE con1 TABLE example AND DROP;
			 STREAM TO dbsink SELECT * FROM stream1; 
			"
			,
			"operator_1 = MAP({expressions=['stream1.attr1','stream1.attr2']},stream1)
			 dbsink := DATABASESINK({connection='con1',table='example', drop='true'},operator_1)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void ExistsTest1()
	{
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 WHERE EXISTS(SELECT attr2 FROM stream1 WHERE attr2 != 10);"
			,
			"operator_1=MAP({expressions=['stream1.attr2']},stream1)
			 operator_2=EXISTENCE({type='EXISTS',predicate='stream1.attr2!=10'},operator_1,stream1)
			 operator_3=MAP({expressions=['stream1.attr1', 'stream1.attr2']}, JOIN(operator_2, stream1))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void ExistsTest2()
	{
		assertCorrectGenerated
		(
			"SELECT attr1 as b FROM stream1 WHERE EXISTS(SELECT attr3 FROM stream2 WHERE b != attr3) AND b != 50 OR b > 100;"
			,
			"operator_1=MAP({expressions=['stream2.attr3']},stream2)
			 operator_2=RENAME({aliases=['attr1','b'],pairs='true'},stream1)
			 operator_4=EXISTENCE({type='EXISTS',predicate='b!=stream2.attr3'},operator_1,operator_2)
			 operator_3=SELECT({predicate='b!=50||b>100'},JOIN(operator_4,operator_2))
			 operator_5=MAP({expressions=['b']},operator_3)
			"	
			, new CQLDictionaryHelper()
		)
	}

	@Test def void ExistsTest3()
	{
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 WHERE attr1 != 50 AND EXISTS(SELECT attr2 FROM stream1 WHERE attr2 != 10);"
			,
			"operator_1=MAP({expressions=['stream1.attr2']},stream1)
			 operator_3=EXISTENCE({type='EXISTS',predicate='stream1.attr2!=10'},operator_1,stream1)
			 operator_2=SELECT({predicate='stream1.attr1!=50'},JOIN(operator_3,stream1))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void ExistsTest4()
	{
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 WHERE attr1 != 50 AND EXISTS(SELECT attr2 FROM stream1 WHERE attr2 != 10) OR attr1 > 100;"
			,
			"operator_1=MAP({expressions=['stream1.attr2']},stream1)
  			 operator_3=EXISTENCE({type='EXISTS',predicate='stream1.attr2!=10'},operator_1,stream1)
			 operator_2=SELECT({predicate='stream1.attr1!=50||stream1.attr1>100'},JOIN(operator_3,stream1))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void QuantificationPredicateTest1()
	{
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1 WHERE attr1 > ALL (SELECT COUNT(attr2) AS num FROM stream1);"
			,
			"operator_1=AGGREGATE({AGGREGATIONS=[['COUNT','stream1.attr2','num','String']]},stream1)
			 operator_2=MAP({expressions=['num']},operator_1)
			 operator_3=EXISTENCE({type='NOT_EXISTS',predicate='attr1<=num'},operator_2,stream1)
			 operator_4=MAP({expressions=['stream1.attr1']}, JOIN(operator_3, stream1))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void QuantificationPredicateTest2()
	{
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1 WHERE attr1 > ANY (SELECT COUNT(attr2) AS num FROM stream1);"
			,
			"operator_1=AGGREGATE({AGGREGATIONS=[['COUNT','stream1.attr2','num','String']]},stream1)
			 operator_2=MAP({expressions=['num']},operator_1)
			 operator_3=EXISTENCE({type='EXISTS',predicate='attr1>num'},operator_2,stream1)
			 operator_4=MAP({expressions=['stream1.attr1']}, JOIN(operator_3, stream1))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void QuantificationPredicateTest3()
	{
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1 WHERE attr1 > SOME (SELECT COUNT(attr2) AS num FROM stream1 WHERE attr1 < attr2);"
			,
			"operator_1=AGGREGATE({AGGREGATIONS=[['COUNT','stream1.attr2','num','String']]},stream1)
			 operator_2=SELECT({predicate='stream1.attr1<stream1.attr2'},operator_1)
			 operator_3=MAP({expressions=['num']},operator_2)
			 operator_4=EXISTENCE({type='EXISTS',predicate='attr1>num'},operator_3,stream1)
			 operator_5=MAP({expressions=['stream1.attr1']},JOIN(operator_4,stream1))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void QuantificationPredicateTest4()
	{
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1 WHERE attr1 > SOME (SELECT stream1.attr2, COUNT(stream1.attr2) AS num FROM stream1 WHERE attr2 < attr1);"
			,
			"operator_1=AGGREGATE({AGGREGATIONS=[['COUNT','stream1.attr2','num','String']]},stream1)
			 operator_2=SELECT({predicate='stream1.attr2<stream1.attr1'},JOIN(operator_1,stream1))
			 operator_3=MAP({expressions=['stream1.attr2','num']},operator_2)
		     operator_4=EXISTENCE({type='EXISTS',predicate='attr1>stream1.attr2&&attr1>num'},operator_3,stream1)
			 operator_5=MAP({expressions=['stream1.attr1']},JOIN(operator_4,stream1))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void QuantificationPredicateTest5()
	{
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1 WHERE attr1 > SOME (SELECT stream1.attr2 FROM stream1 WHERE attr2 < attr1);"
			,
			"operator_1=SELECT({predicate='stream1.attr2<stream1.attr1'},stream1)
			 operator_2=MAP({expressions=['stream1.attr2']},operator_1)
			 operator_3=EXISTENCE({type='EXISTS',predicate='attr1>stream1.attr2'},operator_2,stream1)
			 operator_4=MAP({expressions=['stream1.attr1']},JOIN(operator_3,stream1))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void InPredicateTest1()
	{
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1 WHERE attr1 IN (SELECT COUNT(attr2) AS num FROM stream1);"
			,
			"operator_1=AGGREGATE({AGGREGATIONS=[['COUNT','stream1.attr2','num','String']]},stream1)
			 operator_2=MAP({expressions=['num']},operator_1)
			 operator_3=EXISTENCE({type='EXISTS',predicate='attr1==num'},operator_2,stream1)
			 operator_4=MAP({expressions=['stream1.attr1']}, JOIN(operator_3,stream1))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void SubQueryTest1()
	{
		assertCorrectGenerated//TODO MINUTE must be mapped to MINUTES//TODO b1.attr1 not registered as alias!//TODO duplicated rename operator during join!
		(
		   "SELECT b2.attr1 as b
			FROM   (SELECT b1.attr1, COUNT(b1.attr1) AS num
			        FROM stream1 [SIZE 60 MINUTES ADVANCE 1 MINUTES TIME] AS b1) AS b2
			WHERE num >= 200"
			,
			"operator_1=RENAME({aliases=['attr1','b1.attr1'],pairs='true'},TIMEWINDOW({size=[60,'MINUTES'],advance=[1,'MINUTES']},stream1))
			 operator_2=AGGREGATE({AGGREGATIONS=[['COUNT','b1.attr1','num','Integer']]},operator_1)
			 operator_3=RENAME({aliases=['attr1','b1.attr1'],pairs='true'},TIMEWINDOW({size=[60,'MINUTES'],advance=[1,'MINUTES']},stream1))
			 operator_4=MAP({expressions=['b1.attr1','num']},JOIN(operator_2,operator_3))
			 operator_5=RENAME({aliases=['b1_attr1','b'],pairs='true'},operator_4)
			 operator_6=SELECT({predicate='num>=200'},operator_5)
			 operator_7=MAP({expressions=['b']},operator_6)"
			, new CQLDictionaryHelper()
		)
	}
	
//TODO MINUTE must be mapped to MINUTES	
	@Test def void SubQueryTest2()
	{
		assertCorrectGenerated
		(
		   "SELECT b2.attr1, b3.attr3
			FROM   (SELECT b1.attr1, COUNT(b1.attr1) AS num
			        FROM stream1 [SIZE 60 MINUTES ADVANCE 1 MINUTES TIME] AS b1
			        GROUP BY b1.attr1
			        ) AS b2, 
					(SELECT c1.attr3 FROM stream2 AS c1
					) AS b3 
			WHERE num >= 200"
			,
			"operator_1=RENAME({aliases=['attr1','b1.attr1'],pairs='true'},TIMEWINDOW({size=[60,'MINUTES'],advance=[1,'MINUTES']},stream1))
			 operator_2=AGGREGATE({AGGREGATIONS=[['COUNT','b1.attr1','num','Integer']],GROUP_BY=['b1.attr1']},operator_1)
			 operator_3=RENAME({aliases=['attr1','b1.attr1'],pairs='true'},TIMEWINDOW({size=[60,'MINUTES'],advance=[1,'MINUTES']},stream1))
			 operator_4=MAP({expressions=['b1.attr1','num']},JOIN(RENAME({aliases=['b1.attr1','b1.attr1_groupAttribute#0'],pairs='true'},operator_2),operator_3))
			 operator_5=RENAME({aliases=['attr3','c1.attr3'],pairs='true'},stream2)
			 operator_6=MAP({expressions=['c1.attr3']},operator_5)
			 operator_7=RENAME({aliases=['b1_attr1','b2.attr1'],pairs='true'},operator_4)
			 operator_8=RENAME({aliases=['c1_attr3','b3.attr3'],pairs='true'},operator_6)
			 operator_9=SELECT({predicate='num>=200'},JOIN(operator_7,operator_8))
             operator_10=MAP({expressions=['b2.attr1','b3.attr3']},operator_9)"
			, new CQLDictionaryHelper()
		)
	}

	@Test def void SubQueryTest3()
	{
		assertCorrectGenerated
		(
		   "SELECT b2.attr1, b3.attr3
			FROM   (SELECT b1.attr1, COUNT(b1.attr1) AS num
			        FROM stream1 [SIZE 60 MINUTES ADVANCE 1 MINUTES TIME] AS b1
			        GROUP BY b1.attr1
			        ) AS b2, 
					(SELECT c1.attr3 FROM stream2 AS c1
					) AS b3 
			WHERE num >= ALL (SELECT d1.attr2 FROM stream1 as d1)"
			,
			"operator_1=RENAME({aliases=['attr1','b1.attr1'],pairs='true'},TIMEWINDOW({size=[60,'MINUTES'],advance=[1,'MINUTES']},stream1))
			 operator_2=AGGREGATE({AGGREGATIONS=[['COUNT','b1.attr1','num','Integer']],GROUP_BY=['b1.attr1']},operator_1)
			 operator_3=RENAME({aliases=['attr1','b1.attr1'],pairs='true'},TIMEWINDOW({size=[60,'MINUTES'],advance=[1,'MINUTES']},stream1))
			 operator_4=MAP({expressions=['b1.attr1','num']},JOIN(operator_2,operator_3))
			 operator_5=RENAME({aliases=['attr3','c1.attr3'],pairs='true'},stream2)
			 operator_6=MAP({expressions=['c1.attr3']},operator_5)
			 operator_7=RENAME({aliases=['b1_attr1','b2.attr1'],pairs='true'},operator_4)
			 operator_8=RENAME({aliases=['c1_attr3','b3.attr3'],pairs='true'},operator_6)
			 operator_9=SELECT({predicate='num>=200'},JOIN(operator_7,operator_8))operator_10=MAP({expressions=['b2.attr1','b3.attr3']},operator_9)"
			, new CQLDictionaryHelper()
		)
	}
}