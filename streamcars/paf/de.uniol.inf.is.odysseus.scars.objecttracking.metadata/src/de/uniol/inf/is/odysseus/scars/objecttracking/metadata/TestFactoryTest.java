package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;

public class TestFactoryTest
{
  public static void main(String[] args)
  {
    TestFactoryTest.doTest();
  }
  
  private static void doTest()
  {
    System.out.println("TestFactoryTest: Testing TestFactory...");
    
    TestFactory.create();
    
    // @Timo: An dieser Stelle ist ein Double, der Tupeliterator hält es aber für
    // ein Tupel und versucht es zu "öffnen" > IndexOutOfBoundsException
    // Einfach mal diese Klasse laufen lassen
    MVRelationalTuple<?> bla = (MVRelationalTuple<?>) TestFactory.tuple.getAttribute(0);
    System.out.println(bla.getAttribute(0));
    
    System.out.println("Schema: ");
    TestUtil.printSchema(TestFactory.schema);
    System.out.println("Tuples: ");
    TestUtil.printTuple(TestFactory.tuple, TestFactory.schema);
    
    System.out.println("TestFactoryTest: Testing TestFactory done!");
  }
}
