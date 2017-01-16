/*
 * generated by Xtext 2.10.0
 */
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
class CQLParsingTest
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
			"operator_1 = PROJECT({attributes=['attr1', 'attr2']}, stream1)"
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
			"SELECT * FROM stream1, stream2, stream3 WHERE attr1 > 2 AND attr2 == 'Test';"
			,
			"operator_1 = SELECT({predicate='attr1 > 2 && attr2 == 'Test''}, JOIN(stream1,JOIN(stream2,stream3)))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void SelectAllTest4() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 WHERE attr1 > 2 AND attr2 == 'Test';"
			,
			"operator_1 = SELECT({predicate='attr1 > 2 && attr2 == 'Test''}, stream1)"
		, new CQLDictionaryHelper())
	}
	
	@Test def void SelectAttr1Test1() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1 WHERE attr1 > 2;"
			,
			"operator_1 = SELECT({predicate='attr1 > 2'}, PROJECT({attributes=['attr1']},stream1))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void SelectAttr1Test2() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1, stream2, stream3 WHERE attr1 > 2"
			,
			"operator_1 = SELECT({predicate='attr1 > 2'},PROJECT({attributes=['attr1']},JOIN(stream1, JOIN(stream2, stream3))))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void SelectAttr1Test3() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1;"
			,
			"operator_1 = PROJECT({attributes=['attr1']}, stream1)"
		, new CQLDictionaryHelper())
	}
	
	@Test def void SelectAttr1Test4() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1, attr2, attr3 FROM stream1, stream2, stream3;"
			,
			"operator_1 = PROJECT({attributes=['attr1', 'attr2', 'attr3']},JOIN(stream1, JOIN(stream2, stream3)))"
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
			view1 := PROJECT({attributes=['attr1', 'attr2']}, stream1)
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
				{ source = 'input_stream1', 
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
			"CREATE STREAM stream1 (attr1 INTEGER) CHANNEL localhost : 54321;"
			,
			"stream1 := ACCESS
			(
				{ source = 'input_stream1', 
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
				{ source = 'input_stream1', 
				  wrapper = 'GenericPush',
				  protocol = 'SimpleCSV',
				  transport = 'File',
				  dataHandler ='Tuple',
				  schema = [['attr1', 'INTEGER']],
				  options =[['filename', 'this/is/a/filename.file']]
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
					sink='input_out1',
					wrapper='GenericPush',
					protocol='CSV',
					transport='FILE',
					dataHandler='TUPLE',
					options=[['filename','outfile1']]
				},
				SELECT({predicate='attr2!=attr1'}, PROJECT({attributes=['attr1','attr2']},stream1)))"
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
					sink='input_out1',
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
			"operator_1 = PROJECT({attributes=['attr1', 'attr2']}, stream1)"
		, new CQLDictionaryHelper())
	}
	
	@Test def void WindowTimeTest1() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 [SIZE 5 MINUTES TIME];"
			,
			"operator_1 = PROJECT({attributes=['attr1', 'attr2']}, TIMEWINDOW({size=[5, 'MINUTES'], advance=[1, 'MINUTES']}, stream1))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void WindowTimeTest2() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 [SIZE 5 MINUTES ADVANCE 1 SECONDS TIME];"
			,
			"operator_1 = PROJECT({attributes=['attr1', 'attr2']}, TIMEWINDOW({size=[5, 'MINUTES'], advance=[1, 'SECONDS']}, stream1))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void WindowElementTest1() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 [SIZE 5 TUPLE];"
			,
			"operator_1 = PROJECT({attributes=['attr1', 'attr2']}, ELEMENTWINDOW({size=5,advance=1}, stream1))"
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
			"operator_1 = JOIN(ELEMENTWINDOW({size=5,advance=1}, stream1), TIMEWINDOW({size=[5, 'MINUTES'], advance=[1, 'MINUTES']}, stream2))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void WindowTimeWindowElementTest2() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1, attr2, attr4 FROM stream1 [SIZE 5 TUPLE], stream2 [SIZE 5 MINUTES TIME], stream3;"
			,
			"operator_1 = PROJECT({attributes=['attr1','attr2','attr4']}, JOIN(ELEMENTWINDOW({size=5,advance=1}, stream1), 
														 				 JOIN(TIMEWINDOW({size=[5, 'MINUTES'], advance=[1, 'MINUTES']}, stream2), stream3)))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void WindowTimeWindowElementTest3() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1, attr2, attr4 FROM stream1 [SIZE 5 TUPLE], stream2 [SIZE 5 MINUTES TIME], stream3 WHERE attr4 != attr1;"
			,
			"operator_1 = SELECT({predicate='attr4 != attr1'}, PROJECT({attributes=['attr1','attr2','attr4']},JOIN(ELEMENTWINDOW({size=5,advance=1},stream1),JOIN(TIMEWINDOW({size=[5,'MINUTES'],advance=[1,'MINUTES']},stream2),stream3))))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void WindowTimeWindowElementTest4() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 [SIZE 5 TUPLE], stream2 [SIZE 5 MINUTES TIME] WHERE attr4 != attr1;"
			,
			"operator_1 = SELECT({predicate='attr4 != attr1'}, JOIN(ELEMENTWINDOW({size=5,advance=1}, stream1),
																  TIMEWINDOW({size=[5, 'MINUTES'], advance=[1, 'MINUTES']}, stream2)))"
		, new CQLDictionaryHelper())
	}
	
	@Test def void WindowElement2() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1 [SIZE 5 TUPLE];"
			,
			"operator_1 = PROJECT({attributes=['attr1']}, ELEMENTWINDOW({size=5,advance=1}, stream1))"
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
									['COUNT','attr1','Counter','Integer']
								 ],
					GROUP_BY =['attr1']
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
									['COUNT','attr1','Counter','Integer'],
									['AVG','attr2','AVG_attr2','String']
								 ],
					GROUP_BY =['attr1', 'attr2']
				}, stream1)"
			, new CQLDictionaryHelper()
		)
	}	
	
	@Test def void AggregationTest3()
	{
		assertCorrectGenerated
		(
			"SELECT COUNT(attr1) AS Counter, AVG(attr2) FROM stream1 [SIZE 10 MINUTES TIME] , stream2 GROUP BY attr1, attr2;"
			,
			"operator_1 = AGGREGATE
			(
				{
					AGGREGATIONS=[
									['COUNT', 'attr1', 'Counter', 'Integer'],
									['AVG', 'attr2', 'AVG_attr2', 'String']
								 ],
					GROUP_BY =['attr1', 'attr2']
				}, JOIN(TIMEWINDOW({size=[10, 'MINUTES'], advance=[1, 'MINUTES']},stream1), stream2)
			)"
			, new CQLDictionaryHelper()
		)
	}

	@Test def void AggregationTest4()
	{
		assertCorrectGenerated
		(
			"SELECT COUNT(attr1) AS Counter, attr3 FROM stream1 [SIZE 5 MINUTES TIME];"
			,
			"operator_1 = PROJECT
			({attributes=['Counter', 'attr3']}, 
				JOIN(TIMEWINDOW({size=[5,'MINUTES'], advance=[1, 'MINUTES']}, stream1), 
					AGGREGATE
					(
						{
							AGGREGATIONS=[
											['COUNT', 'attr1', 'Counter', 'Integer']
										 ]
						}, TIMEWINDOW({size=[5,'MINUTES'], advance=[1, 'MINUTES']}, stream1)
					)
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
			"operator_1 = PROJECT
			({attributes=['Counter', 'attr3']}, JOIN(stream1, JOIN(stream2, 
				AGGREGATE
				(
					{
						AGGREGATIONS=[
										['COUNT', 'attr1', 'Counter', 'Integer']
									 ]
					}, JOIN(stream1, stream2))))
			)"
			, new CQLDictionaryHelper()
		)
	}	
	
	@Test def void AggregationTest6()
	{
		assertCorrectGenerated
		(
			"SELECT COUNT(attr1) AS Counter, attr3 FROM stream1 , stream2 [SIZE 10 MINUTES TIME];"
			,
			"operator_1 = PROJECT
			({attributes=['Counter', 'attr3']}, JOIN(stream1, JOIN(TIMEWINDOW({size=[10, 'MINUTES'], advance=[1, 'MINUTES']},stream2), 
				AGGREGATE
				(
					{
						AGGREGATIONS=[
										['COUNT', 'attr1', 'Counter', 'Integer']
									 ]
					}, JOIN(stream1, TIMEWINDOW({size=[10, 'MINUTES'], advance=[1, 'MINUTES']},stream2)))))
			)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void AggregationTest7()
	{
		assertCorrectGenerated
		(
			"SELECT COUNT(attr1) AS Counter, attr3 FROM stream1 [SIZE 10 MINUTES ADVANCE 2 SECONDS TIME] , stream2 [SIZE 10 MINUTES TIME] WHERE attr3 > 100;"
			,
			"operator_1 = SELECT({predicate='attr3 > 100'}, PROJECT
			({attributes=['Counter', 'attr3']}, JOIN(TIMEWINDOW({size=[10, 'MINUTES'], advance=[2, 'SECONDS']},stream1), JOIN(TIMEWINDOW({size=[10, 'MINUTES'], advance=[1, 'MINUTES']},stream2), 
				AGGREGATE
				(
					{
						AGGREGATIONS=[
										['COUNT', 'attr1', 'Counter', 'Integer']
									 ]
					}, JOIN(TIMEWINDOW({size=[10, 'MINUTES'], advance=[2, 'SECONDS']},stream1), TIMEWINDOW({size=[10, 'MINUTES'], advance=[1, 'MINUTES']},stream2)))))
			))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void AggregationTest8()
	{
		assertCorrectGenerated
		(
			"SELECT COUNT(attr1) AS Counter, attr3 FROM stream1 [SIZE 10 MINUTES ADVANCE 2 SECONDS TIME] , stream2 [SIZE 10 MINUTES TIME] WHERE attr3 > 100 HAVING Counter > 1000;"
			,
			"operator_1=SELECT({predicate='Counter>1000&&attr3>100'},
				PROJECT({attributes=['Counter','attr3']},
					JOIN(TIMEWINDOW({size=[10,'MINUTES'],advance=[2,'SECONDS']},stream1),
						 JOIN(TIMEWINDOW({size=[10,'MINUTES'],advance=[1,'MINUTES']},stream2),
						 	AGGREGATE({AGGREGATIONS=[['COUNT','attr1','Counter','Integer']]},
								JOIN(TIMEWINDOW({size=[10,'MINUTES'],advance=[2,'SECONDS']},stream1),
									 TIMEWINDOW({size=[10,'MINUTES'],advance=[1,'MINUTES']},stream2)
								)
							)
						)
					)
				)
			 )"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void NestedStatementTest1()
	{
		assertCorrectGenerated
		(
			"SELECT attr1 FROM (SELECT attr1 FROM stream1)"
			,
			"operator_1 = PROJECT({attributes=['attr1']}, PROJECT({attributes=['attr1']}, stream1))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void NestedStatementTest2()
	{
		assertCorrectGenerated
		(
			"SELECT attr1, attr3 FROM (SELECT attr1 FROM stream1) AS s1, (SELECT * FROM stream2)"
			,
			"operator_1 = PROJECT({attributes=['attr1', 'attr3']}, 
				JOIN(
						PROJECT({attributes=['attr1']}, stream1),
						PROJECT({attributes=['attr4', 'attr3']}, stream2)
				    )
			)"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void NestedStatementTest3()
	{
		assertCorrectGenerated
		(
			"SELECT attr1, attr3 FROM stream1 WHERE attr1 IN (SELECT attr1 FROM stream1 WHERE attr1 > 1000)"
			,
			"operator_1 = SELECT({predicate='attr1 > 1000'}, PROJECT({attributes=['attr1','attr3']}, stream1))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void NestedStatementTest4()
	{
		assertCorrectGenerated
		(
			"SELECT attr1, attr3 FROM stream1 WHERE attr1 IN (SELECT attr1 FROM stream1 WHERE attr1 > 1000) AND attr3 != 2"
			,
			"operator_1 = SELECT({predicate='attr1 > 1000 && attr3 !=  2'}, PROJECT({attributes=['attr1','attr3']}, stream1))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void NestedStatementTest5()
	{
		assertCorrectGenerated
		(
			"SELECT attr1, attr3 FROM stream1 WHERE attr3 != 20 OR attr1 IN (SELECT attr1 FROM stream1 WHERE attr1 > 1000) AND attr1 != 0 AND attr3 IN (SELECT attr1, attr3 FROM stream1 WHERE attr1 < 100) AND attr3 > 1.92"
			,
			"operator_1 = SELECT({predicate='attr3 !=20 || attr1 > 1000 && attr1 != 0 &&  attr1 < 100 && attr3 > 1.92'}, PROJECT({attributes=['attr1','attr3']}, stream1))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void NestedStatementTest6()
	{
		assertCorrectGenerated
		(
			"SELECT attr1, attr2 FROM stream1 WHERE attr1 IN (SELECT attr1, attr2 FROM (SELECT * FROM stream1 WHERE attr1 < 1234), (SELECT * FROM stream2))"
			,
			"operator_1 = SELECT({predicate='attr1 < 1234'}, PROJECT({attributes=['attr1','attr2']}, stream1))"
			, new CQLDictionaryHelper()
		)
	}
	
	@Test def void DistinctTest()
	{
		assertCorrectGenerated
		(
			"SELECT DISTINCT * FROM stream1;"
			,
			"operator_1 = DISTINCT(PROJECT({attributes=['attr1', 'attr2']}, stream1))"
			, new CQLDictionaryHelper()
		)
	}
	
}
