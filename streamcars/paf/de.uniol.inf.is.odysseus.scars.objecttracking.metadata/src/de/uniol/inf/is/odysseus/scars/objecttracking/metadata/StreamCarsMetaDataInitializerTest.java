
package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

public class StreamCarsMetaDataInitializerTest
{
  public static void main(String[] args)
  {
    TestFactory.create();
    System.out.println(TestFactory.schema);
    //StreamCarsMetaDataInitializerTest.tryIterator();
    StreamCarsMetaDataInitializerTest.test();
  }
  
  public static void tryIterator()
  {
    TupleIterator iterator = new TupleIterator(TestFactory.tuple, TestFactory.schema);
    
    while( !iterator.isFinished() ) {
      
      // Tabs für Hierarchie
      for( int i = 0; i < iterator.getLevel(); i++ ) 
        System.out.print("\t");
      
      // Infos ausgeben
      Object obj = iterator.getTupleObject();
      if( obj instanceof MVRelationalTuple<?> ) {
        System.out.print("TUPLE");
      } else {
        System.out.print(obj);
      }
      System.out.print(" (" + iterator.getAttribute().getAttributeName() + " : " + iterator.getAttribute().getDatatype().getQualName() + ")");
      System.out.print(iterator.getTupleIndexPath());
      System.out.print(iterator.getSchemaIndexPath());
      System.out.println(iterator.getSchemaIndexPath().getFullAttributeName());

      iterator.next();
    }
  }
  
  public static void test()
  {
    // create StreamCarsMetaDataInitializer
    
    // Notiz: SDFDatatype.isXXX(SDFDatatype) sollte false liefern, falls übergebener
    // SDFDatatype null ist!
    StreamCarsMetaDataInitializer<IProbability> covInit = new StreamCarsMetaDataInitializer<IProbability>(TestFactory.schema);
    
    // initialize iprobability metadata of tuples with StreamCarsMetaDataInitializer
    
    covInit.updateMetadata(TestFactory.tuple);
  }
}
