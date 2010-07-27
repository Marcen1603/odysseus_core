package de.uniol.inf.is.odysseus.scars.objecttracking;

import java.util.ArrayList;
import java.util.Arrays;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

public class TestFactory
{
  public static SDFAttributeList schema = null;
  public static MVRelationalTuple<IProbability> tuples = null;
  public static double[][] covMatrix = null;
  
  public static void create()
  {
    // create covariance matrix
    
    TestFactory.covMatrix = new double[][]
    {
      { 0.1, 0.0, 0.0 },
      { 0.0, 0.2, 0.0 },
      { 0.0, 0.0, 0.3 }
    };
    
    // create covariance lists for each measurement attribute
    
    ArrayList<Double> maList0 = new ArrayList<Double>();
    for (double wert : TestFactory.covMatrix[0])
    {
      maList0.add(new Double(wert));
    }
    ArrayList<Double> maList1 = new ArrayList<Double>();
    for (double wert : TestFactory.covMatrix[1])
    {
      maList1.add(new Double(wert));
    }
    ArrayList<Double> maList2 = new ArrayList<Double>();
    for (double wert : TestFactory.covMatrix[2])
    {
      maList2.add(new Double(wert));
    }
    
    // create datatypes
    
    final SDFDatatype TYPE_MV = new SDFDatatype(SDFDatatypes.MeasurementValue);
    final SDFDatatype TYPE_DEFAULT = new SDFDatatype(SDFDatatypes.SDFDatatype);
    
    // create schema
    // 
    // src0
    // > attr0                      (index: 0    )
    // > attr1                      (index: 1    )
    //   > attr1:attr0         (ma) (index: 1-0  )
    //   > attr1:attr1              (index: 1-1  )
    // > attr2                 (ma) (index: 2    )
    // > attr3                      (index: 3    )
    // src1
    // > attr4                      (index: 4    )
    // > attr5                      (index: 5    )
    // > attr6                      (index: 6    )
    //   > attr6:attr0              (index: 6-0  )
    //     > attr6:attr0:attr0      (index: 6-0-0)
    //     > attr6:attr0:attr1 (ma) (index: 6-0-1)
    
    // create schema attributes
    SDFAttribute attr0 = new SDFAttribute("src0.attr0");
    SDFAttribute attr1 = new SDFAttribute("src0.attr1");
    SDFAttribute attr10 = new SDFAttribute("src0.attr1:attr0");
    SDFAttribute attr11 = new SDFAttribute("src0.attr1:attr1");
    SDFAttribute attr2 = new SDFAttribute("src0.attr2");
    SDFAttribute attr3 = new SDFAttribute("src0.attr3");
    SDFAttribute attr4 = new SDFAttribute("src1.attr4");
    SDFAttribute attr5 = new SDFAttribute("src1.attr5");
    SDFAttribute attr6 = new SDFAttribute("src1.attr6");
    SDFAttribute attr60 = new SDFAttribute("src1.attr6:attr0");
    SDFAttribute attr600 = new SDFAttribute("src1.attr6:attr0:attr0");
    SDFAttribute attr601 = new SDFAttribute("src1.attr6:attr0:attr1");
    
    // add datatypes to schema attributes
    attr0.setDatatype(TYPE_DEFAULT);
    attr1.setDatatype(TYPE_DEFAULT);
    attr10.setDatatype(TYPE_MV);
    attr11.setDatatype(TYPE_DEFAULT);
    attr2.setDatatype(TYPE_MV);
    attr3.setDatatype(TYPE_DEFAULT);
    attr4.setDatatype(TYPE_DEFAULT);
    attr5.setDatatype(TYPE_DEFAULT);
    attr6.setDatatype(TYPE_DEFAULT);
    attr60.setDatatype(TYPE_DEFAULT);
    attr600.setDatatype(TYPE_DEFAULT);
    attr601.setDatatype(TYPE_MV);
    
    // add covariance lists to measurement attributes
    attr10.setCovariance(maList0);
    attr2.setCovariance(maList1);
    attr601.setCovariance(maList2);
    
    // create schema and add schema attributes
    TestFactory.schema = new SDFAttributeList();
    TestFactory.schema.addAttribute(attr0);
    TestFactory.schema.addAttribute(attr1);
    TestFactory.schema.getAttribute(1).addSubattribute(attr10);
    TestFactory.schema.getAttribute(1).addSubattribute(attr11);
    TestFactory.schema.add(attr2);
    TestFactory.schema.add(attr3);
    TestFactory.schema.add(attr4);
    TestFactory.schema.add(attr5);
    TestFactory.schema.add(attr6);
    TestFactory.schema.getAttribute(6).addSubattribute(attr60);
    TestFactory.schema.getAttribute(6).getSubattribute(0).addSubattribute(attr600);
    TestFactory.schema.getAttribute(6).getSubattribute(0).addSubattribute(attr601);
    
    // create tuples
    
    Double value = new Double(42.0);
    
    Object[] objects60 = new Object[2];
    Arrays.fill(objects60, value);
    MVRelationalTuple<IProbability> tuples60 = new MVRelationalTuple<IProbability>(objects60);
    
    Object[] objects6 = new Object[1];
    objects6[0] = tuples60;
    MVRelationalTuple<IProbability> tuples6 = new MVRelationalTuple<IProbability>(objects6);
    
    Object[] objects1 = new Object[2];
    Arrays.fill(objects1, value);
    MVRelationalTuple<IProbability> tuples1 = new MVRelationalTuple<IProbability>(objects1);
    
    Object[] objects = new Object[7];
    Arrays.fill(objects, value);
    objects[1] = tuples1;
    objects[6] = tuples6;
    TestFactory.tuples = new MVRelationalTuple<IProbability>(objects);
  }
}
