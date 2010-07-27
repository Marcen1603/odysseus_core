
package de.uniol.inf.is.odysseus.scars.objecttracking;

import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

public class CovarianceInitiatorTest
{
  public static void main(String[] args)
  {
    TestFactory.create();
    System.out.println(TestFactory.schema);
    CovarianceInitiatorTest.tryIterator();
    //CovarianceInitiatorTest.test();
  }
  
  public static void tryIterator()
  {
    TupleIterator iterator = new TupleIterator(TestFactory.tuples, TestFactory.schema);
    
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
    // create CovarianceInitiator
    
    // Notiz: SDFDatatype.isXXX(SDFDatatype) sollte false liefern, falls übergebener
    // SDFDatatype null ist!
    CovarianceInitiator<IProbability> covInit = new CovarianceInitiator<IProbability>(TestFactory.schema);
    
    // initialize iprobability metadata of tuples with CovarianceInitiator
    
    covInit.updateMetadata(TestFactory.tuples);
  }
}
