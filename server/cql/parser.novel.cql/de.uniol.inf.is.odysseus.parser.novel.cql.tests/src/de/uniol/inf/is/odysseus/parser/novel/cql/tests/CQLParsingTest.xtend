/*
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.novel.cql.tests

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Model
import de.uniol.inf.is.odysseus.parser.novel.cql.generator.CQLGenerator
import java.util.Set
import org.eclipse.xtext.generator.InMemoryFileSystemAccess
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test
import org.junit.runner.RunWith

import static extension org.junit.Assert.*

@RunWith(XtextRunner)
@InjectWith(CQLInjectorProvider)
class CQLParsingTest
{

	@Inject extension ValidationTestHelper
	@Inject extension CQLGenerator
	@Inject extension ParseHelper<Model>
	
	
	val keyword0 = CQLGenerator.getKeyword(0)
	val keyword1 = CQLGenerator.getKeyword(1)
	val keyword2 = CQLGenerator.getKeyword(2)
	val keyword3 = CQLGenerator.getKeyword(3)
	
	def void assertCorrectGenerated(String s, String t, CQLDictionaryDummy dictionary) 
	{
		
		s.parse.assertNoErrors
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
			"project_1 = PROJECT({attributes=['attr1', 'attr2']}, stream1)"
		, new CQLDictionaryDummy())
	}
	
	@Test def void SelectAllTest2() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1, stream2, stream3;"
			,
			"join_1 = JOIN(stream1, JOIN(stream2, stream3))"
		, new CQLDictionaryDummy())
	}
	
	@Test def void SelectAllTest3() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1, stream2, stream3 WHERE attr1 > 2 AND attr2 == 'Test';"
			,
			"select_1 = SELECT({predicate='attr1 > 2 && attr2 == 'Test''}, JOIN(stream1, JOIN(stream2, stream3)))"
		, new CQLDictionaryDummy())
	}
	
	@Test def void SelectAllTest4() 
	{ 
		assertCorrectGenerated
		(
			"SELECT * FROM stream1 WHERE attr1 > 2 AND attr2 == 'Test';"
			,
			"select_1 = SELECT({predicate='attr1 > 2 && attr2 == 'Test''}, stream1)"
		, new CQLDictionaryDummy())
	}
	
	@Test def void SelectAttr1Test1() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1 WHERE attr1 > 2;"
			,
			"select_1 = SELECT({predicate='attr1 > 2'}, stream1)"
		, new CQLDictionaryDummy())
	}
	
	@Test def void SelectAttr1Test2() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1, stream2, stream3 WHERE attr1 > 2;"
			,
			"select_1 = SELECT({predicate='attr1 > 2'},PROJECT({attributes=['attr1']},JOIN(stream1, JOIN(stream2, stream3))))"
		, new CQLDictionaryDummy())
	}
	
	@Test def void SelectAttr1Test3() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1;"
			,
			"project_1 = PROJECT({attributes=['attr1']}, stream1)"
		, new CQLDictionaryDummy())
	}
	
	@Test def void SelectAttr1Test4() 
	{ 
		assertCorrectGenerated
		(
			"SELECT attr1 FROM stream1, stream2, stream3;"
			,
			"project_1 = PROJECT({attributes=['attr1']},JOIN(stream1, JOIN(stream2, stream3)))"
		, new CQLDictionaryDummy())
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
			, new CQLDictionaryDummy	
		)
	}
	
	@Test def void CreateViewTest2()
	{
		assertCorrectGenerated
		(
			"CREATE VIEW view1 FROM (
				CREATE STREAM stream1 (attr1 INTEGER) CHANNEL localhost : 54321
			)"
			,
			"
			view1 := ACCESS
			(
				{ source = 'input_stream1', 
				  wrapper = 'GenericPush',
				  protocol = 'SizeByteBuffer',
				  transport = 'NonBlockingTcp',
				  dataHandler ='Tuple',
				  schema = [['attr1', 'INTEGER']],
				  options =[['port', '54321'],['host', 'localhost']]
				}
			)
			"
			, new CQLDictionaryDummy	
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
			"stream1 = ACCESS
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
			"stream1 = ACCESS
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

//	@Test def void CreateStream1Channel()
//	{
//		assertCorrectGenerated
//		(
//			"CREATE STREAM stream1 (attr1 INTEGER) CHANNEL localhost : 54321;"
//			,
//			"stream1 = ACCESS
//			(
//				{ source = 'input_stream1', 
//				  wrapper = 'GenericPush',
//				  schema = [['attr1', 'INTEGER']],
//				  transport = 'NonBlockingTcp',
//				  protocol = 'SizeByteBuffer',
//				  dataHandler ='Tuple',
//				  options =[['port', '54321'],['host', 'localhost']]
//				}
//			)"
//		, null)
//	}
//	
//	@Test def void CreateStream1AccessFramework()
//	{
//		assertCorrectGenerated
//		(
//			"CREATE STREAM stream1 (attr1 INTEGER) 
//    		WRAPPER 'GenericPush'
//    		PROTOCOL 'CSV'
//    		TRANSPORT 'File'
//    		DATAHANDLER 'Tuple'
//    		OPTIONS ('port' '54321', 'host' 'localhost')"
//			,
//			"stream1 = ACCESS
//			(
//				{ source = 'input_stream1', 
//				  wrapper = 'GenericPush',
//				  protocol = 'CSV',
//				  transport = 'File',
//				  dataHandler ='Tuple',
//				  schema = [['attr1', 'INTEGER']],
//				  options =[['port', '54321'],['host', 'localhost']]
//				}
//			)"
//		, null)
//	}
//	
//	@Test def void CreateStream2Channel()
//	{
//		assertCorrectGenerated
//		(
//			"CREATE STREAM stream1 (attr1 INTEGER, attr2 STRING, attr3 BOOLEAN) CHANNEL localhost : 54321;"
//			,
//			"stream1 = ACCESS
//			(
//				{ source = 'input_stream1', 
//				  wrapper = 'GenericPush',
//				  schema = [['attr1', 'INTEGER'],
//							['attr2', 'STRING'],
//							['attr3', 'BOOLEAN']],
//				transport = 'NonBlockingTcp',
//				protocol = 'SizeByteBuffer',
//				dataHandler ='Tuple',
//				options =[['port', '54321'],['host', 'localhost']]
//				}
//			)"
//		, null)
//	}

//	@Test def void CreateSink1()//TODO Input operator missing!
//	{
//		assertCorrectGenerated
//		(
//			"CREATE SINK stream1 (attr1 INTEGER, attr2 STRING, attr3 BOOLEAN) 
//    		WRAPPER 'GenericPush'
//    		PROTOCOL 'CSV'
//    		TRANSPORT 'File'
//    		DATAHANDLER 'Tuple'
//    		OPTIONS ('filename' 'E:\test')"
//			,
//			keyword2+"stream1 := SENDER
//			(
//				{ 
//					sink = 'stream1'
//			  		wrapper = 'GenericPush',
//					protocol = 'CSV',
//					transport = 'File',
//					dataHandler ='Tuple',
//					options =[['filename', 'E:\test']]
//				}
//			)"
//		, null)
//	}
	
//	@Test def void WindowUnbounded() 
//	{ 
//		assertCorrectGenerated
//		(
//			"SELECT * FROM stream1 [UNBOUNDED];"
//			,
//			"stream1 := ACCESS
//			(
//				{
//					source      = 'input_stream1',
//					wrapper     = 'GenericPush',
//					transport   = 'TCPClient',
//					dataHandler = 'Tuple',
//					schema = 
//					[
//						['attr1', 'Integer'],
//						['attr2', 'String']
//					]
//				}
//			)"
//		, new CQLDictionaryDummy())
//	}
	
//	@Test def void WindowTimeBased() 
//	{ 
//		assertCorrectGenerated
//		(
//			"SELECT * FROM stream1 [SIZE 5 MINUTES TIME]; "
//			,
//			keyword0+"stream1 := ACCESS
//			(
//				{
//					source      = 'stream1',
//					wrapper     = 'GenericPush',
//					transport   = 'TCPClient',
//					dataHandler = 'Tuple',
//					schema = 
//					[
//						['attr1', 'Integer'],
//						['attr2', 'String']
//					]
//				}
//			)"
//			+keyword1+"stream1 := TIMEWINDOW
//			(
//				{ size = [5, 'MINUTES']},"
//				+keyword0+"stream1
//			)"
//		, new CQLDictionaryDummy())
//	}
	
//	@Test def void WindowElementBased1() 
//	{ 
//		assertCorrectGenerated
//		(
//			"SELECT * FROM stream1 [SIZE 5 TUPLE]; "
//			,
//			keyword0+"stream1 := ACCESS
//			(
//				{
//					source      = 'stream1',
//					wrapper     = 'GenericPush',
//					transport   = 'TCPClient',
//					dataHandler = 'Tuple',
//					schema = 
//					[
//						['attr1', 'Integer'],
//						['attr2', 'String']
//					]
//				}
//			)"
//			+keyword1+"stream1 := ELEMENTWINDOW
//			(
//				{ size = 5,
//				  advance = 1},"
//				+keyword0+"stream1
//			)"
//		, new CQLDictionaryDummy())
//	}
	
//	@Test def void WindowElementBased2() 
//	{ 
//		assertCorrectGenerated
//		(
//			"SELECT * FROM stream1 [SIZE 5 TUPLE PARTITION BY attr1]; "
//			,
//			keyword0+"stream1 := ACCESS
//			(
//				{
//					source      = 'stream1',
//					wrapper     = 'GenericPush',
//					transport   = 'TCPClient',
//					dataHandler = 'Tuple',
//					schema = 
//					[
//						['attr1', 'Integer'],
//						['attr2', 'String']
//					]
//				}
//			)"
//			+keyword1+"stream1 := ELEMENTWINDOW
//			(
//				{ size = 5,
//				  advance = 1,
//				  partition = 'attr1'
//				},"
//				+keyword0+"stream1
//			)"
//		, new CQLDictionaryDummy())
//	}
	
//	@Test def void WindowElementTimeUnbounded() 
//	{ 
//		assertCorrectGenerated
//		(
//			"SELECT * FROM stream1 [SIZE 5 TUPLE], stream2 [SIZE 12 SECONDS TIME], stream3 [UNBOUNDED]; "
//			,
//			keyword0+"stream1 := ACCESS
//			(
//				{
//					source      = 'stream1',
//					wrapper     = 'GenericPush',
//					transport   = 'TCPClient',
//					dataHandler = 'Tuple',
//					schema = 
//					[
//						['attr1', 'Integer'],
//						['attr2', 'String']
//					]
//				}
//			)
//			"
//			+keyword1+"stream1 := ELEMENTWINDOW
//			(
//				{ size = 5,
//				  advance = 1},"
//				+keyword0+"stream1
//			)
//			"
//			+keyword0+"stream2 := ACCESS
//			(
//				{
//					source      = 'stream2',
//					wrapper     = 'GenericPush',
//					transport   = 'TCPClient',
//					dataHandler = 'Tuple',
//					schema = 
//					[
//						['attr4', 'String'],
//						['attr3', 'Integer']
//					]
//				}
//			)"
//			+keyword1+"stream2 := TIMEWINDOW
//			(
//				{ size = [12, 'SECONDS']},"
//				+keyword0+"stream2
//			)"
//			+keyword0+"stream3 := ACCESS
//			(
//				{
//					source      = 'stream3',
//					wrapper     = 'GenericPush',
//					transport   = 'TCPClient',
//					dataHandler = 'Tuple',
//					schema = 
//					[
//						['attr5', 'Integer'],
//						['attr6', 'String']
//					]
//				}
//			)"
//		, new CQLDictionaryDummy())
//	}
	
	

	
}
