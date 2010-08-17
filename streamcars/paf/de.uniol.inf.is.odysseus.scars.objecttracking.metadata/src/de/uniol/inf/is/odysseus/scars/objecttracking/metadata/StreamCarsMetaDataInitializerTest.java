
package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

// test StreamCarsMetaDataInitializer
// author: sven
public class StreamCarsMetaDataInitializerTest
{
  public static void main(String[] args)
  {
    StreamCarsMetaDataInitializerTest.doTest();
  }
  
  private static void doTest()
  {
    System.out.println("StreamCarsMetaDataInitializerTest: Testing StreamCarsMetaDataInitializer...");
    
    TestFactory.createTestObjects();
    System.out.println("Tuples before:");
    TestUtil.printTuple(TestFactory.testTuple, TestFactory.testSchema);
    
    // create StreamCarsMetaDataInitializer
    
    System.out.print("StreamCarsMetaDataInitializerTest: Create StreamCarsMetaDataInitializer...");
    StreamCarsMetaDataInitializer<StreamCarsMetaData<Object>> covInit = new StreamCarsMetaDataInitializer<StreamCarsMetaData<Object>>(TestFactory.testSchema);
    System.out.println(" done!");
    
    // initialize probability meta data of tuples with StreamCarsMetaDataInitializer
    
    System.out.print("StreamCarsMetaDataInitializerTest: Initialize probability meta data of tuples with StreamCarsMetaDataInitializer...");
    covInit.updateMetadata(TestFactory.testTuple);
    System.out.println(" done!");
    
    System.out.println("Tuples afterwards:");
    TestUtil.printTuple(TestFactory.testTuple, TestFactory.testSchema);
    
    System.out.println("StreamCarsMetaDataInitializerTest: Testing done!");
  }
}
