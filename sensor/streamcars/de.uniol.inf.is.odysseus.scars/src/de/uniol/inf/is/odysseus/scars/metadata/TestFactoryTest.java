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
package de.uniol.inf.is.odysseus.scars.metadata;

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
