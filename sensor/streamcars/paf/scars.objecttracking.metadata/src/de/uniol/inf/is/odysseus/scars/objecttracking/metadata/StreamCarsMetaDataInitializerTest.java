/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;


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
