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

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

// creates objects for test purposes
// author: sven
public class TestFactory
{
  // test objects, set by create
  public static SDFAttributeList testSchema = null;
  public static MVRelationalTuple<StreamCarsMetaData<Object>> testTuple = null;
  public static double[][] testCovMatrix = null;
  
//  public static void createTestObjects()
//  {
//    System.out.println("TestFactory: Creating...");
//    
//    // create covariance matrix
//    
//    TestFactory.testCovMatrix = new double[][]
//    {
//      { 0.1, 0.0 },
//      { 0.0, 0.2 }
//    };
//    
//    // create covariance lists for each measurement attribute
//    
//    ArrayList<String> maList0 = new ArrayList<String>();
//    for (double wert : TestFactory.testCovMatrix[0])
//    {
//      maList0.add(String.valueOf(wert));
//    }
//    ArrayList<String> maList1 = new ArrayList<String>();
//    for (double wert : TestFactory.testCovMatrix[1])
//    {
//      maList1.add(String.valueOf(wert));
//    }
//    
//    // create datatypes
//    
//    final SDFDatatype TYPE_MV_FLOAT = SDFDatatypeFactory.createAndReturnDatatype("MVFloat");
//    final SDFDatatype TYPE_DOUBLE = SDFDatatypeFactory.createAndReturnDatatype("Double");
//    final SDFDatatype TYPE_RECORD = SDFDatatypeFactory.createAndReturnDatatype("Record");
//    
//    // create schema
//    // 
//    // --> src0
//    // 
//    // baseAttr
//    // > attr0
//    // > attr1       (ma)
//    //   > attr10
//    //   > attr11
//    // > attr2
//    // > attr3
//    // > attr4
//    // > attr5
//    // > attr6
//    //   > attr60    (ma)
//    //     > attr600
//    //     > attr601
//    
//    // create schema attributes
//    SDFAttribute baseAttr = new SDFAttribute("src0.baseAttr");
//    SDFAttribute attr0 = new SDFAttribute("src0.attr0");
//    SDFAttribute attr1 = new SDFAttribute("src0.attr1");
//    SDFAttribute attr10 = new SDFAttribute("src0.attr10");
//    SDFAttribute attr11 = new SDFAttribute("src0.attr11");
//    SDFAttribute attr2 = new SDFAttribute("src0.attr2");
//    SDFAttribute attr3 = new SDFAttribute("src0.attr3");
//    SDFAttribute attr4 = new SDFAttribute("src0.attr4");
//    SDFAttribute attr5 = new SDFAttribute("src0.attr5");
//    SDFAttribute attr6 = new SDFAttribute("src0.attr6");
//    SDFAttribute attr60 = new SDFAttribute("src0.attr60");
//    SDFAttribute attr600 = new SDFAttribute("src0.attr600");
//    SDFAttribute attr601 = new SDFAttribute("src0.attr601");
//    
//    // add datatypes to schema attributes
//    baseAttr.setDatatype(TYPE_RECORD);
//    attr0.setDatatype(TYPE_DOUBLE);
//    attr1.setDatatype(TYPE_RECORD);
//    attr10.setDatatype(TYPE_DOUBLE);
//    attr11.setDatatype(TYPE_MV_FLOAT);
//    attr2.setDatatype(TYPE_DOUBLE);
//    attr3.setDatatype(TYPE_DOUBLE);
//    attr4.setDatatype(TYPE_DOUBLE);
//    attr5.setDatatype(TYPE_DOUBLE);
//    attr6.setDatatype(TYPE_RECORD);
//    attr60.setDatatype(TYPE_RECORD);
//    attr600.setDatatype(TYPE_MV_FLOAT);
//    attr601.setDatatype(TYPE_DOUBLE);
//    
//    // add covariance lists to measurement attributes
//    attr11.setCovariance(maList0);
//    attr600.setCovariance(maList1);
//    
//    // create schema and add schema attributes
//    TestFactory.testSchema = new SDFAttributeList();
//    TestFactory.testSchema.addAttribute(baseAttr);
//    baseAttr.addSubattribute(attr0);
//    baseAttr.addSubattribute(attr1);
//    attr1.addSubattribute(attr10);
//    attr1.addSubattribute(attr11);
//    baseAttr.addSubattribute(attr2);
//    baseAttr.addSubattribute(attr3);
//    baseAttr.addSubattribute(attr4);
//    baseAttr.addSubattribute(attr5);
//    baseAttr.addSubattribute(attr6);
//    attr6.addSubattribute(attr60);
//    attr60.addSubattribute(attr600);
//    attr60.addSubattribute(attr601);
//    
//    // create tuples
//    
//    Double value = new Double(42.0);
//    
//    Object[] objects60 = new Object[2];
//    Arrays.fill(objects60, value);
//    MVRelationalTuple<IProbability> tuple60 = new MVRelationalTuple<IProbability>(objects60);
//    
//    Object[] objects6 = new Object[1];
//    objects6[0] = tuple60;
//    MVRelationalTuple<IProbability> tuple6 = new MVRelationalTuple<IProbability>(objects6);
//    
//    Object[] objects1 = new Object[2];
//    Arrays.fill(objects1, value);
//    MVRelationalTuple<IProbability> tuple1 = new MVRelationalTuple<IProbability>(objects1);
//    
//    Object[] objects = new Object[7];
//    Arrays.fill(objects, value);
//    objects[1] = tuple1;
//    objects[6] = tuple6;
//    MVRelationalTuple<IProbability> tuple = new MVRelationalTuple<IProbability>(objects);
//    
//    Object[] baseObjects = new Object[1];
//    baseObjects[0] = tuple;
//    MVRelationalTuple<StreamCarsMetaData<Object>> baseTuple = new MVRelationalTuple<StreamCarsMetaData<Object>>(baseObjects);
//    
//    TestFactory.testTuple = baseTuple;
//    
//    // create empty probability metadata
//    
//    baseTuple.setMetadata(new StreamCarsMetaData<Object>());
//    tuple.setMetadata(new Probability());
//    tuple1.setMetadata(new Probability());
//    tuple6.setMetadata(new Probability());
//    tuple60.setMetadata(new Probability());
//    
//    System.out.println("TestFactory: Creating done!");
//  }
}
