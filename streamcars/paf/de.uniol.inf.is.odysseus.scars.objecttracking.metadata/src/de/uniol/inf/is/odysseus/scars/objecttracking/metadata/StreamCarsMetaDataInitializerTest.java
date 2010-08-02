
package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

public class StreamCarsMetaDataInitializerTest
{
  public static void main(String[] args)
  {
    StreamCarsMetaDataInitializerTest.doTest();
  }
  
  private static void doTest()
  {
    System.out.println("StreamCarsMetaDataInitializerTest: Testing StreamCarsMetaDataInitializer...");
    
    TestFactory.create();
    
    // create StreamCarsMetaDataInitializer
    
    System.out.print("StreamCarsMetaDataInitializerTest: Create StreamCarsMetaDataInitializer...");
    StreamCarsMetaDataInitializer<IProbability> covInit = new StreamCarsMetaDataInitializer<IProbability>(TestFactory.schema);
    System.out.println(" done!");
    
    // initialize probability meta data of tuples with StreamCarsMetaDataInitializer
    
    System.out.print("StreamCarsMetaDataInitializerTest: Initialize probability meta data of tuples with StreamCarsMetaDataInitializer...");
    covInit.updateMetadata(TestFactory.tuple);
    System.out.println(" done!");
    
    System.out.println("Tuples:");
    TestUtil.printTuple(TestFactory.tuple, TestFactory.schema);
    
    System.out.println("StreamCarsMetaDataInitializerTest: Testing done!");
  }
}
