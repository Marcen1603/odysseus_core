package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import java.util.Arrays;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.util.SchemaInfo;
import de.uniol.inf.is.odysseus.scars.util.SchemaIterator;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;
import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

// some test utils
// author: sven
public class TestUtil
{
  public static void printSchema(SDFAttributeList schema)
  {
    for( SchemaInfo info : new SchemaIterator(schema)) {
    	
      for (int i = 0; i < info.level; i++)
      {
        System.out.print("\t");
      }
      
      System.out.print("schema attribute \"" + info.attribute.getAttributeName() + "\" (of type " + info.attribute.getDatatype().getQualName());
      System.out.print(", path " + info.schemaIndexPath);
      System.out.println(", path name \"" + info.schemaIndexPath.getFullAttributeName() + "\", covariance list " + info.attribute.getCovariance() + ")");
    }
  }
  
  public static <M extends IProbability> void printTuple(MVRelationalTuple<M> tuple, SDFAttributeList schema)
  {
    for( TupleInfo info : new TupleIterator(tuple, schema) ) {
    	
      for (int i = 0; i < info.level; i++)
      {
        System.out.print("\t");
      }
      
      System.out.print("schema attribute \"" + info.attribute.getAttributeName() + "\" (of type " + info.attribute.getDatatype().getQualName());
      System.out.print(", path " + info.schemaIndexPath);
      System.out.println(", path name \"" + info.schemaIndexPath.getFullAttributeName() + "\", covariance list " + info.attribute.getCovariance() + ")");
      
      for (int i = 0; i < info.level; i++)
      {
        System.out.print("\t");
      }
      System.out.print("> ");
      
      Object obj = info.tupleObject;
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
      System.out.println(" (path " + info.tupleIndexPath + ", covariance matrix " + TestUtil.matrixToString(matrix) + ")");
    }
  }
  
  private static String matrixToString(double[][] matrix)
  {
	  if (matrix == null)
	  {
		  return null;
	  }
	  StringBuffer buf = new StringBuffer();
	  buf.append("[ ");
	  for (double[] array : matrix)
	  {
		  buf.append(Arrays.toString(array));
		  buf.append(" ");
	  }
	  buf.append("]");
	  return buf.toString();
  }
}
