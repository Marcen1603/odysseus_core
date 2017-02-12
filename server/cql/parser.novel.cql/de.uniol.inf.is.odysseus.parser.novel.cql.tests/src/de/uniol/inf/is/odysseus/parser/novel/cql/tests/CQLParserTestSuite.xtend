package de.uniol.inf.is.odysseus.parser.novel.cql.tests

import org.junit.runner.Result
import org.junit.runner.JUnitCore

class CQLParserTestSuite 
{
	def static void main(String[] args)
	{
		println("//CQLTestSuite started/////////////////////////////")
		var Result result = JUnitCore.runClasses(CQLParsingTest);
		println("-> Done")
		var failuresCount = result.failureCount
		if(failuresCount != 0)
		{
			println("-> Failures " + failuresCount)
			println()
			for (var i = 0; i < failuresCount; i++)
				println(i + " : " + result.getFailures().get(i).toString());
		}
		else
		{
			println()
			println("-> Successful: All tests ran without any failures");
		}
		println()
		println("//CQLTestSuite terminated///////////////////////////")
   }
}