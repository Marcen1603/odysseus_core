package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.util.SchemaIterator;
import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class TestUtil
{
  public static void printSchema(SDFAttributeList schema)
  {
    SchemaIterator iterator = new SchemaIterator(schema);
    
    while(!iterator.isFinished())
    {
      for (int i = 0; i < iterator.getLevel(); i++)
      {
        System.out.print("\t");
      }
      
      System.out.print("schema attribute \"" + iterator.getAttribute().getAttributeName() + "\" (of type " + iterator.getAttribute().getDatatype().getQualName());
      System.out.print(", path " + iterator.getSchemaIndexPath());
      System.out.println(", path name \"" + iterator.getSchemaIndexPath().getFullAttributeName() + "\", covariance list " + iterator.getAttribute().getCovariance() + ")");

      iterator.next();
    }
  }
  
  public static <M extends IProbability> void printTuple(MVRelationalTuple<M> tuple, SDFAttributeList schema)
  {
    TupleIterator iterator = new TupleIterator(tuple, schema);
    
    while(!iterator.isFinished())
    {
      for (int i = 0; i < iterator.getLevel(); i++)
      {
        System.out.print("\t");
      }
      
      System.out.print("schema attribute \"" + iterator.getAttribute().getAttributeName() + "\" (of type " + iterator.getAttribute().getDatatype().getQualName());
      System.out.print(", path " + iterator.getSchemaIndexPath());
      System.out.println(", path name \"" + iterator.getSchemaIndexPath().getFullAttributeName() + "\", covariance list " + iterator.getAttribute().getCovariance() + ")");
      
      for (int i = 0; i < iterator.getLevel(); i++)
      {
        System.out.print("\t");
      }
      System.out.print("> ");
      
      Object obj = iterator.getTupleObject();
      MVRelationalTuple<?> currTuple = null;
      IProbability meta = null;
      double[][] matrix = null;
      if (obj instanceof MVRelationalTuple<?>)
      {
        currTuple = (MVRelationalTuple<?>) obj;
        meta = (IProbability) currTuple.getMetadata();
        if (meta != null)
        {
          matrix = meta.getCovariance();
        }
        System.out.print("tuple");
      }
      else
      {
        System.out.print("object: " + obj);
      }
      System.out.println(" (path " + iterator.getTupleIndexPath() + ", covariance matrix " + matrix + ")");

      iterator.next();
    }
  }
}
