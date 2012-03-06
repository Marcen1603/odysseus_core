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

import java.util.Arrays;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaInfo;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaIterator;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleInfo;
import de.uniol.inf.is.odysseus.relational.base.schema.TupleIterator;

// some test utils
// author: sven
public class TestUtil
{
  public static void printSchema(SDFSchema schema)
  {
    for( SchemaInfo info : new SchemaIterator(schema)) {
    	
      for (int i = 0; i < info.level; i++)
      {
        System.out.print("\t");
      }
      
      System.out.print("schema attribute \"" + info.attribute.getAttributeName() + "\" (of type " + info.attribute.getDatatype().getQualName());
      System.out.print(", path " + info.schemaIndexPath);
      System.out.println(", path name \"" + info.schemaIndexPath.getFullAttributeName() + "\", covariance list " + info.attribute.getAddInfo() + ")");
    }
  }
  
  public static <M extends IProbability> void printTuple(MVRelationalTuple<M> tuple, SDFSchema schema)
  {
    for( TupleInfo info : new TupleIterator(tuple, schema) ) {
    	
      for (int i = 0; i < info.level; i++)
      {
        System.out.print("\t");
      }
      
      System.out.print("schema attribute \"" + info.attribute.getAttributeName() + "\" (of type " + info.attribute.getDatatype().getQualName());
      System.out.print(", path " + info.schemaIndexPath);
      System.out.println(", path name \"" + info.schemaIndexPath.getFullAttributeName() + "\", covariance list " + info.attribute.getAddInfo() + ")");
      
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
