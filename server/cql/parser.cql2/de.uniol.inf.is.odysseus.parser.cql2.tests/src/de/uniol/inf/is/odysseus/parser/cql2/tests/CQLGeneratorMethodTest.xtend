package de.uniol.inf.is.odysseus.parser.cql2.tests

import com.google.inject.Inject
import de.uniol.inf.is.odysseus.parser.cql2.generator.CQLGenerator
import java.util.List
import org.eclipse.xtext.generator.InMemoryFileSystemAccess
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

import static extension org.junit.Assert.*

//@RunWith(XtextRunner)
@Deprecated
class CQLGeneratorMethodTest 
{


	@Inject extension CQLGenerator
//	@Inject extension ParseHelper<Model>
	
	@Rule public ExpectedException thrown = ExpectedException.none()
	
	var String source
	var String source2
	var List<String> attributes
	var List<String> attributes2
	var List<String> aliases
	var List<String> aliases2
	var List<String> aggregates
	var List<String> aggregationAliases
	var String source1Alias = "s1"
	var String source2Alias = "s2"
		
	def generateModel(String query)
	{
		val fsa = new InMemoryFileSystemAccess()
//		var model = query.parse
//		doGenerate(model.eResource(), fsa, null)
        var result = ''
		for(e : fsa.textFiles.entrySet)
		{
			result += e.value
		}
//		println(result)
	}
	
//	@Before def void setup() 
//	{ 
//		//Define attributes, their aliases and sources for the query
//		source      = "stream1"
//		aliases     = #["a1", "a2", "aa1", "stream1X"]
//		attributes  = #["attr1", "attr2", "attr1","attrX"]
//		source2     = "stream2"
//		aliases2    = #["b1", "b2", "bb2", "stream2X"]
//		attributes2 = #["attr4", "attr5", "attr5", "attrX"]
//		aggregates  = #["AVG(attr1)", "SUM("+source2+".attr5)", "COUNT("+source+".aa1)", "MAX(b2)", 
//						"MIN("+source1Alias+".attr2)", "MIN("+source2Alias+".b2)"]
//		aggregationAliases    = #["avgAttr1", "sumAttr5", "countAa1", "maxB2", "minAttr2", "minB2"]
//		
//		//Add the definitions from above as schema to the generator
////		CQLSchemata = new CQLDictionaryHelper(source, attributes).schema
////		CQLSchemata = new CQLDictionaryHelper(source2, attributes2).schema
//		
//		var attributesWithAlias =  ''
//		//Construct attribute arguments
//		for(var i = 0; i < attributes.size; i++)
//			attributesWithAlias += attributes.get(i) + " AS " + aliases.get(i) + ","
//		for(var i = 0; i < attributes2.size; i++)
//			attributesWithAlias += attributes2.get(i) + " AS " + aliases2.get(i) + ","
//		for(var i = 0; i < aggregates.size - 1; i++)
//			attributesWithAlias += aggregates.get(i) + " AS " + aggregationAliases.get(i) + ","
//		attributesWithAlias += aggregates.get(aggregates.size - 1) + " AS " + aggregationAliases.get(aggregationAliases.size - 1)
//		//Add attributes and sources to the query
//		var query = "SELECT "+attributesWithAlias+" FROM "+ source +" AS " + source1Alias + "," + source2 + " AS " + source2Alias
////		println(query)
//		generateModel(query)
//	}
//	
//	@Test def void getAttributenameFromAliasTest()
//	{
//		//Check if all aliases can be found 
//		for(var i = 0 ; i < attributes.size; i++)
//			attributes.get(i).assertEquals(getAttributenameFromAlias(aliases.get(i)))
//		for(var i = 0 ; i < attributes2.size; i++)		
//			attributes2.get(i).assertEquals(getAttributenameFromAlias(aliases2.get(i)))
//		for(var i = 0 ; i < aggregates.size; i++)
//			aggregationAliases.get(i).assertEquals(getAttributenameFromAlias(aggregationAliases.get(i)))
//		//Check for invalid arguments that must throw an exception 
//		thrown.expect(IllegalArgumentException)
//		getAttributenameFromAlias("")
//		thrown.reportMissingExceptionWithMessage("No exception of %s thrown")
//		getAttributenameFromAlias(null)
//        thrown.reportMissingExceptionWithMessage("No exception of %s thrown")
//		getAttributenameFromAlias("attr12")
//        thrown.reportMissingExceptionWithMessage("No exception of %s thrown")
//	}
//	
//	@Test def void getAttributenameTest()
//	{
//		"stream1.attr1".assertEquals(getAttributename("attr1", null))
//		"stream1.attr1".assertEquals(getAttributename("a1", null))
//		"stream1.attr1".assertEquals(getAttributename("stream1.a1", null))
//		"stream1.attr1".assertEquals(getAttributename("aa1", null))
//		"stream1.attr1".assertEquals(getAttributename("stream1.aa1", null))
//		
//		"stream2.attr5".assertEquals(getAttributename("attr5", null))
//		"stream2.attr5".assertEquals(getAttributename("b2", null))
//		"stream2.attr5".assertEquals(getAttributename("stream2.b2", null))
//		"stream2.attr5".assertEquals(getAttributename("bb2", null))
//		"stream2.attr5".assertEquals(getAttributename("stream2.bb2", null))
//		
//		"avgAttr1".assertEquals(getAttributename("avgAttr1", null))
//		"sumAttr5".assertEquals(getAttributename("sumAttr5", null))
//				
//		thrown.expect(IllegalArgumentException)
//		"attr12".assertEquals(getAttributename("stream1.attr12", null))
//		thrown.reportMissingExceptionWithMessage("No exception of %s thrown")
//		
//	}
//	
//	@Test def void getAttributenamesFromSource()
//	{
////		for(String attribute : getAttributeNamesFrom(source))
////			assertTrue(attributes.contains(attribute))
////		for(String attribute : getAttributeNamesFrom(source2))
////			assertTrue(attributes2.contains(attribute))
//	}
//	
//	@Test def void getSourcenameFromAliasTest()
//	{
//		source.assertEquals(getSourcenameFromAlias(source1Alias))
//		source2.assertEquals(getSourcenameFromAlias(source2Alias))
//	}
//	
//	@Test def void getSourceAliasesTest()
//	{
////		var aliases = newArrayList
////		for(Entry<SourceStruct, List<String>> str : sourceAliases.entrySet)
////			aliases.addAll(str.value)
////		assertTrue(aliases.contains(source1Alias))
////		assertTrue(aliases.contains(source2Alias))
//	}
//	
//	@Test def void getAttributeAliasesTest()
//	{
////		var aliases = newArrayList
////		aliases.addAll(this.aliases)
////		aliases.addAll(this.aliases2)
////		for(String alias : aliases)
////			assertTrue(getAttributeAliasesAsList.contains(alias))
//	}
	
}
		