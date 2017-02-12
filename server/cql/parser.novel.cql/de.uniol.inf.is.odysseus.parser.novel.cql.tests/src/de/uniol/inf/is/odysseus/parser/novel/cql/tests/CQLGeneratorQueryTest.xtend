package de.uniol.inf.is.odysseus.parser.novel.cql.tests

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Model
import de.uniol.inf.is.odysseus.parser.novel.cql.generator.CQLGenerator
import de.uniol.inf.is.odysseus.parser.novel.cql.tests.util.CQLDictionaryHelper
import java.util.Set
import org.eclipse.xtext.generator.InMemoryFileSystemAccess
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.junit.Test
import org.junit.runner.RunWith

import static extension org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(CQLInjectorProvider)
class CQLGeneratorQueryTest 
{

	@Inject extension CQLGenerator
	@Inject extension ParseHelper<Model>
	
//	val keyword0 = CQLGenerator.getKeyword(0)
//	val keyword1 = CQLGenerator.getKeyword(1)
//	val keyword2 = CQLGenerator.getKeyword(2)
//	val keyword3 = CQLGenerator.getKeyword(3)
	
	def void assertCorrectGenerated(String s, String t, CQLDictionaryHelper dictionary) 
	{
		
//		s.parse.assertNoErrors//TODO Validator not working because no CQLDictionary can be found
		var model = s.parse 
		val fsa = new InMemoryFileSystemAccess()
		if(dictionary != null)
		innerschema = dictionary.schema as Set<SDFSchema>
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
	
	//TODO \t should also be removed
	//TODO escape symbols should retrieved with System.getProperties(linesperator)
	def String format(String s) { s.replaceAll("\\s*[\\r\\n]+\\s*", "").trim().replace(" ","") }
	
	@Test def void SelectAllTest1() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1;"
			,
			"operator_1 = PROJECT({attributes=['stream1.attr1', 'stream1.attr2']}, stream1)"
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
			"
			s2 = stream2
			operator_1 = SELECT({predicate='stream1.attr1 > 2 && stream1.attr2 == 'Test''}, JOIN(stream1,JOIN(stream2,stream3)))
			"
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

	val aliasTestResult = "
		s1 = stream1
		operator_1 = PROJECT({attributes=['stream1.attr1']}, stream1)
		operator_2 = SELECT({predicate='stream1.attr1 > 2'}, operator_1)
		renamed_3=RENAME({aliases=['attr1','value1'],pairs='true'},operator_2)"

	@Test def void AliasTest1()
	{
		assertCorrectGenerated
		(
			"SELECT attr1 AS value1 FROM stream1 AS s1 WHERE value1 > 2;"
			,		
			aliasTestResult
			, new CQLDictionaryHelper()
		)
	}

	@Test def void AliasTest2()
	{
		assertCorrectGenerated
		(
			"SELECT attr1 AS value1 FROM stream1 AS s1 WHERE s1.value1 > 2;"
			,
			aliasTestResult
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void AliasTest3()
	{
		assertCorrectGenerated
		(
			"SELECT attr1 AS value1 FROM stream1 AS s1 WHERE s1.attr1 > 2;"
			,
			aliasTestResult
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void AliasTest4()
	{
		assertCorrectGenerated
		(
			"SELECT attr1 AS value1 FROM stream1 AS s1 WHERE stream1.value1 > 2;"
			,
			aliasTestResult
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void AliasTest5()
	{
		assertCorrectGenerated
		(
			"SELECT attr1 AS value1 FROM stream1 AS s1 WHERE attr1 > 2;"
			,
			aliasTestResult
			, new CQLDictionaryHelper()
		)
	}
	
//	@Test def void AliasTest6()
//	{
//		assertCorrectGenerated
//		(
//			"SELECT COUNT(stream1.attr1) AS value1 FROM stream1 AS s1 WHERE attr1 > 2;"
//			,
//			aliasTestResult
//			, new CQLDictionaryHelper()
//		)
//	}
//	
//	@Test def void AliasTest7()
//	{
//		assertCorrectGenerated
//		(
//			"SELECT attr1 FROM stream1 AS s1, stream1 AS s2 WHERE s1.attr1 > s2.attr1;"
//			,
//			aliasTestResult
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
			operator_1 = PROJECT({attributes=['stream1.attr1']}, stream1)
			operator_2 = SELECT({predicate='stream1.attr1 > 2'}, operator_1)"
		, new CQLDictionaryHelper())
	}
	
	@Test def void SelfJoinTest1() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1 AS s1, stream1 AS s2;"
			,
			"
			operator_1 = PROJECT({attributes=['stream1.attr1']}, stream1)
			operator_2 = SELECT({predicate='stream1.attr1 > 2'}, operator_1)"
		, new CQLDictionaryHelper())
	}
	
	@Test def void SelfJoinTest2() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1 AS s1, stream1 AS s2, stream1 AS s3;"
			,
			"
			operator_1 = PROJECT({attributes=['stream1.attr1']}, stream1)
			operator_2 = SELECT({predicate='stream1.attr1 > 2'}, operator_1)"
		, new CQLDictionaryHelper())
	}
	
	@Test def void SelectAttr1Test2() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1, stream2, stream3 WHERE attr1 > 2"
			,
			"
			operator_1 = PROJECT({attributes=['stream1.attr1']},JOIN(stream1, JOIN(stream2, stream3)))
			operator_2 = SELECT({predicate='stream1.attr1 > 2'}, operator_1)"
		, new CQLDictionaryHelper())
	}
	
	@Test def void SelectAttr1Test3() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1;"
			,
			"operator_1 = PROJECT({attributes=['stream1.attr1']}, stream1)"
		, new CQLDictionaryHelper())
	}
	
	@Test def void SelectAttr1Test4() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1, attr2, attr3 FROM stream1, stream2, stream3;"
			,
			"operator_1 = PROJECT({attributes=['stream1.attr1', 'stream1.attr2', 'stream2.attr3']},
							JOIN(stream1, 
								JOIN(stream2, stream3)))"
		, new CQLDictionaryHelper())
	}

	@Test def void SelectAttr1Test5() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1, AVG(attr1) AS avgAttr1, attr2, attr3 FROM stream1, stream2, stream3;"
			,
			"operator_1 = AGGREGATE({AGGREGATIONS=[['AVG','stream1.attr1','avgAttr1','Integer']]},
							JOIN(stream1,JOIN(stream2,stream3)))
			 operator_2 = PROJECT({attributes=['avgAttr1','stream1.attr1','stream1.attr2','stream2.attr3']},
							JOIN(operator_1,JOIN(stream1,JOIN(stream2,stream3))))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void SelectAttr1Test6() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1, attr2 FROM stream1 WHERE attr1=7 OR attr1=20 OR attr1=21 OR attr1=59 OR attr1=87;"
			,
			"operator_1 = PROJECT({attributes=['stream1.attr1','stream1.attr2']},stream1)
			 operator_2 = SELECT({predicate='stream1.attr1==7||stream1.attr1==20||stream1.attr1==21||stream1.attr1==59||stream1.attr1==87'},operator_1)"
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
			operator_1 = PROJECT({attributes=['stream1.attr1', 'stream1.attr2']}, stream1)
			view_2 := operator_1
			"
			, new CQLDictionaryHelper	
		)
	}
	
	@Test def void CreateStreamTest1()
	{
		assertCorrectGenerated
		(
			"CREATE STREAM stream1 (attr1 INTEGER) 
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
				  schema = [['attr1', 'INTEGER']],
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
			"CREATE STREAM stream1 (attr1 INTEGER) FILE 'this/is/a/filename.file' AS SimpleCSV;"
			,
			"stream1 := ACCESS
			(
				{ source = 'stream1', 
				  wrapper = 'GenericPull',
				  protocol = 'SimpleCSV',
				  transport = 'File',
				  dataHandler ='Tuple',
				  schema = [['attr1', 'INTEGER']],
				  options =[['filename', 'this/is/a/filename.file'],['delimiter',';'],['textDelimiter',\"'\"],['readfirstline','true']]
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
			"out1 := SENDER
			(
				{
					sink='out1',
					wrapper='GenericPush',
					protocol='CSV',
					transport='FILE',
					dataHandler='TUPLE',
					options=[['filename','outfile1']]
				},
				SELECT({predicate='stream1.attr2!=stream1.attr1'}, PROJECT({attributes=['stream2.attr1','stream1.attr2']},stream1)))"
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
			"out1 := SENDER
			(
				{
					sink='out1',
					wrapper='GenericPush',
					protocol='CSV',
					transport='FILE',
					dataHandler='TUPLE',
					options=[['filename','outfile1']]
				},
				stream1
			)"
			, new CQLDictionaryHelper()	
		)	
	}
	
	@Test def void WindowUnboundedTest1() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 [UNBOUNDED];"
			,
			"operator_1 = PROJECT({attributes=['stream1.attr1', 'stream1.attr2']}, stream1)"
		, new CQLDictionaryHelper())
	}
	
	@Test def void WindowTimeTest1() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 [SIZE 5 MINUTES TIME];"
			,
			"operator_1 = PROJECT({attributes=['stream1.attr1', 'stream1.attr2']},
							 TIMEWINDOW({size=[5, 'MINUTES'], advance=[1, 'MINUTES']}, stream1))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void WindowTimeTest2() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 [SIZE 5 MINUTES ADVANCE 1 SECONDS TIME];"
			,
			"operator_1 = PROJECT({attributes=['stream1.attr1', 'stream1.attr2']}, 
							TIMEWINDOW({size=[5, 'MINUTES'], advance=[1, 'SECONDS']}, stream1))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void WindowElementTest1() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 [SIZE 5 TUPLE];"
			,
			"operator_1 = PROJECT({attributes=['stream1.attr1', 'stream1.attr2']}, 
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
			"operator_1 = PROJECT({attributes=['stream1.attr1','stream1.attr2','stream2.attr4']}, 
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
			operator_1 = PROJECT({attributes=['stream1.attr1','stream1.attr2','stream2.attr4']},
							JOIN(ELEMENTWINDOW({size=5,advance=1},stream1),
								JOIN(TIMEWINDOW({size=[5,'MINUTES'],advance=[1,'MINUTES']},stream2),stream3)))
			operator_2 = SELECT({predicate='stream2.attr4 != stream1.attr1'}, operator_1)"
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
			"operator_1 = PROJECT({attributes=['stream1.attr1']}, ELEMENTWINDOW({size=5,advance=1}, stream1))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void AggregationTest1()
	{
		assertCorrectGenerated
		(
			"SELECT COUNT(attr1) AS Counter FROM stream1 GROUP BY attr1;"
			,
			"operator_1 = AGGREGATE
			(
				{
					AGGREGATIONS=[
									['COUNT','stream1.attr1','Counter','Integer']
								 ],
					GROUP_BY =['stream1.attr1']
				}, stream1)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void AggregationTest2()
	{
		assertCorrectGenerated
		(
			"SELECT COUNT(attr1) AS Counter, AVG(attr2) FROM stream1 GROUP BY attr1, attr2;"
			,
			"operator_1 = AGGREGATE
			(
				{
					AGGREGATIONS=[
									['COUNT','stream1.attr1','Counter','Integer'],
									['AVG','stream1.attr2','AVG_attr2','String']
								 ],
					GROUP_BY =['stream1.attr1', 'stream1.attr2']
				}, stream1)"
			, new CQLDictionaryHelper()
		)
	}	
	
	@Test def void AggregationTest3()
	{
		assertCorrectGenerated
		(
			"SELECT COUNT(attr1) AS Counter, AVG(attr2) FROM stream1 [SIZE 10 MINUTES TIME] AS s1 , stream2 GROUP BY attr1, attr2;"
			,
			"
			s1 = stream1
			operator_1 = AGGREGATE
			(
				{
					AGGREGATIONS=[
									['COUNT', 'stream1.attr1', 'Counter', 'Integer'],
									['AVG', 'stream1.attr2', 'AVG_attr2', 'String']
								 ],
					GROUP_BY =['stream1.attr1', 'stream1.attr2']
				}, JOIN(TIMEWINDOW({size=[10, 'MINUTES'], advance=[1, 'MINUTES']},stream1), stream2)
			)"
			, new CQLDictionaryHelper()
		)
	}

	@Test def void AggregationTest4()
	{
		assertCorrectGenerated
		(
			"SELECT COUNT(attr1) AS Counter, attr2 FROM stream1 [SIZE 5 MINUTES TIME];"
			,
			"
			operator_1 = AGGREGATE
			(
				{
					AGGREGATIONS=[
									['COUNT', 'stream1.attr1', 'Counter', 'Integer']
								 ]
				}, TIMEWINDOW({size=[5,'MINUTES'], advance=[1, 'MINUTES']}, stream1)
			)
			operator_2 = PROJECT
			({attributes=['Counter', 'stream1.attr2']}, 
				JOIN(TIMEWINDOW({size=[5,'MINUTES'], advance=[1, 'MINUTES']}, stream1), 
				operator_1
				)
			)"
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
			operator_2 = PROJECT
			({attributes=['Counter', 'stream2.attr3']}, JOIN(operator_1, JOIN(stream1, stream2)))"
			, new CQLDictionaryHelper()
		)
	}	
	
	@Test def void AggregationTest6()
	{
		assertCorrectGenerated
		(
			"SELECT COUNT(attr1) AS Counter, attr3 FROM stream1 , stream2 [SIZE 10 MINUTES TIME];"
			,
			"
			operator_1 = AGGREGATE
			(
				{
					AGGREGATIONS=[
									['COUNT', 'stream1.attr1', 'Counter', 'Integer']
								 ]
				}, JOIN(TIMEWINDOW({size=[10,'MINUTES'],advance=[1,'MINUTES']},stream2),stream1))

			operator_2=PROJECT({attributes=['Counter','stream2.attr3']},
						JOIN(TIMEWINDOW({size=[10,'MINUTES'],advance=[1,'MINUTES']},stream2),JOIN(operator_1,stream1)))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void AggregationTest7()
	{
		assertCorrectGenerated
		(
			"SELECT COUNT(attr1) AS Counter, attr3 FROM stream1 [SIZE 10 MINUTES ADVANCE 2 SECONDS TIME] , stream2 [SIZE 10 MINUTES TIME] WHERE attr3 > 100;"
			,
			"
			operator_1 = AGGREGATE
			(
				{
					AGGREGATIONS=[
									['COUNT', 'stream1.attr1', 'Counter', 'Integer']
								 ]
				}, JOIN(TIMEWINDOW({size=[10,'MINUTES'],advance=[1,'MINUTES']},stream2),TIMEWINDOW({size=[10,'MINUTES'],advance=[2,'SECONDS']},stream1))
			)
			operator_2 = PROJECT({attributes=['stream2.attr3','Counter']},
							JOIN(TIMEWINDOW({size=[10,'MINUTES'],advance=[1,'MINUTES']},stream2),
								JOIN(TIMEWINDOW({size=[10,'MINUTES'],advance=[2,'SECONDS']},stream1),operator_1)))
			operator_3 = SELECT({predicate='stream2.attr3 > 100'}, operator_2)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void AggregationTest8()
	{
		assertCorrectGenerated
		(
			"SELECT COUNT(attr1) AS Counter, attr3 FROM stream1 [SIZE 10 MINUTES ADVANCE 2 SECONDS TIME] , stream2 [SIZE 10 MINUTES TIME] WHERE attr3 > 100 HAVING Counter > 1000;"
			,
			"
			operator_1 = AGGREGATE({AGGREGATIONS=[['COUNT','stream1.attr1','Counter','Integer']]},
							JOIN(TIMEWINDOW({size=[10,'MINUTES'],advance=[1,'MINUTES']},stream2),
								TIMEWINDOW({size=[10,'MINUTES'],advance=[2,'SECONDS']},stream1)))
			operator_2 = PROJECT({attributes=['stream2.attr3','Counter']},
							JOIN(TIMEWINDOW({size=[10,'MINUTES'],advance=[1,'MINUTES']},stream2),
								JOIN(TIMEWINDOW({size=[10,'MINUTES'],advance=[2,'SECONDS']},stream1),operator_1)))
			operator_3 = SELECT({predicate='Counter>1000&&stream2.attr3>100'}, operator_2)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void NestedStatementTest1()
	{
		assertCorrectGenerated
		(
			"SELECT attr1 FROM (SELECT attr1 FROM stream1) AS str1"
			,
			"
			operator_1 = PROJECT({attributes=['stream1.attr1']}, stream1)
			str1 = operator_1 
			operator_2 = PROJECT({attributes=['stream1.attr1']}, str1)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void NestedStatementTest2()
	{
		assertCorrectGenerated
		(
			"SELECT attr1, attr3 FROM (SELECT attr1 FROM stream1) AS s1, (SELECT * FROM stream2) AS s2"
			,
			"
			operator_1 = PROJECT({attributes=['stream1.attr1']}, stream1)
			s1 = operator_1
			operator_2 = PROJECT({attributes=['stream2.attr3', 'stream2.attr4']}, stream2)
			s2 = operator_2
			operator_3 = PROJECT({attributes=['stream1.attr1', 'stream2.attr3']}, JOIN(s1, s2))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void NestedStatementTest3()
	{
		assertCorrectGenerated
		(
			"SELECT attr1, attr3 FROM stream1 WHERE attr1 IN (SELECT attr1 FROM stream1 WHERE attr1 > 1000)"
			,
			"
			operator_1 = PROJECT({attributes=['stream1.attr1','stream2.attr3']}, stream1)
			operator_2 = SELECT({predicate='stream1.attr1 > 1000'}, operator_1)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void NestedStatementTest4()
	{
		assertCorrectGenerated
		(
			"SELECT attr1, attr3 FROM stream1 WHERE attr1 IN (SELECT attr1 FROM stream1 WHERE attr1 > 1000) AND attr3 != 2"
			,
			"	
			operator_1=PROJECT({attributes=['stream1.attr1','stream2.attr3']},stream1)
			operator_2=SELECT({predicate='stream1.attr1>1000&&stream2.attr3!=2'},operator_1)

			"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void NestedStatementTest5()
	{
		assertCorrectGenerated
		(
			"SELECT attr1, attr3 FROM stream1 
			 WHERE  attr3 != 20 
					OR attr1 IN (SELECT attr1 FROM stream1 WHERE attr1 > 1000) 
					AND attr1 != 0 
					AND attr3 IN (SELECT attr1, attr3 FROM stream1 WHERE attr1 < 100) AND attr3 > 1.92"
			,
			"
			operator_1 = PROJECT({attributes=['stream1.attr1','stream2.attr3']}, stream1)
			operator_2 = SELECT({predicate='stream2.attr3 !=20 
							|| stream1.attr1 > 1000 
							&& stream1.attr1 != 0 
							&&  stream1.attr1 < 100 
							&& stream2.attr3 > 1.92'}, operator_1)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void NestedStatementTest6()
	{
		assertCorrectGenerated
		(
			"SELECT attr1, attr2 FROM stream1 
			 WHERE attr1 IN (SELECT attr1, attr2 FROM (SELECT * FROM stream1 WHERE attr1 < 1234) AS s1, (SELECT * FROM stream2) AS s2)"
			,
			"
			operator_1=SELECT({predicate='stream1.attr1<1234'},stream1)
			s1=operator_1
 			operator_2=PROJECT({attributes=['stream2.attr3','stream2.attr4']},stream2)
			s2=operator_2
			operator_3=PROJECT({attributes=['stream1.attr1','stream1.attr2']},JOIN(s1,JOIN(s2,stream1)))
			operator_4=SELECT({predicate='stream1.attr1<1234'},operator_3)"
			, new CQLDictionaryHelper()
		)
	}
	
	//SELECT * FROM nexmark:person2 WHERE ID IN (SELECT ID FROM (SELECT * FROM nexmark:person2) AS s1 WHERE ID < 1000);
	
	@Test def void DistinctTest()
	{
		assertCorrectGenerated
		(
			"SELECT DISTINCT * FROM stream1;"
			,
			"operator_1 = DISTINCT(PROJECT({attributes=['stream1.attr1', 'stream1.attr2']}, stream1))"
			, new CQLDictionaryHelper()
		)
	}
	
}
