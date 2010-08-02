package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

// test TestFactory :)
// author: sven
public class TestFactoryTest
{
  public static void main(String[] args)
  {
    TestFactoryTest.doTest();
  }
  
  private static void doTest()
  {
    System.out.println("TestFactoryTest: Testing TestFactory...");
    
    TestFactory.createTestObjects();
    
    System.out.println("Schema: ");
    TestUtil.printSchema(TestFactory.testSchema);
    System.out.println("Tuples: ");
    TestUtil.printTuple(TestFactory.testTuple, TestFactory.testSchema);
    
    System.out.println("TestFactoryTest: Testing TestFactory done!");
  }
}
