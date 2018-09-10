package de.uniol.inf.is.odysseus.parser.cql2.tests;

import de.uniol.inf.is.odysseus.parser.cql2.tests.CQLParsingTest;
import java.util.List;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

@SuppressWarnings("all")
public class CQLParserTestSuite {
  public static void main(final String[] args) {
    InputOutput.<String>println("//CQLTestSuite started/////////////////////////////");
    Result result = JUnitCore.runClasses(CQLParsingTest.class);
    InputOutput.<String>println("-> Done");
    int failuresCount = result.getFailureCount();
    if ((failuresCount != 0)) {
      InputOutput.<String>println(("-> Failures " + Integer.valueOf(failuresCount)));
      InputOutput.println();
      for (int i = 0; (i < failuresCount); i++) {
        String _plus = (Integer.valueOf(i) + " : ");
        List<Failure> _failures = result.getFailures();
        Failure _get = _failures.get(i);
        String _string = _get.toString();
        String _plus_1 = (_plus + _string);
        InputOutput.<String>println(_plus_1);
      }
    } else {
      InputOutput.println();
      InputOutput.<String>println("-> Successful: All tests ran without any failures");
    }
    InputOutput.println();
    InputOutput.<String>println("//CQLTestSuite terminated///////////////////////////");
  }
}
