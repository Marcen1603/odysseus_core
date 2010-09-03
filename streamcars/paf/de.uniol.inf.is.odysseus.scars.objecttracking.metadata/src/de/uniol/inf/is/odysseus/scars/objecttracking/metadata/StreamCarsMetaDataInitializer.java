
package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.base.AbstractMetadataUpdater;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.util.SchemaInfo;
import de.uniol.inf.is.odysseus.scars.util.SchemaIterator;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;
import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * Initializes tuple (of type {@link MVRelationalTuple}) meta data used by
 * project group StreamCars.
 * <p>
 * See public methods for details.
 * 
 * @author Hauke
 * @author Sven
 */
public class StreamCarsMetaDataInitializer<M extends IProbability & IConnectionContainer> extends
    AbstractMetadataUpdater<M, MVRelationalTuple<M>>
{
  // set by constructor/initMetadata
  // used by updateMetadata
  private SDFAttributeList schema;
  private double[][] covMatrix = null;

  /**
   * Creates a StreamCarsMetaDataInitializer object and initializes the meta data used by
   * {@link #updateMetaData(MVRelationalTuple)}.
   * <p>
   * NOTE: schema is used by {@link #updateMetaData(MVRelationalTuple)} as root
   * to navigate through the tuple given. So it has to represent the schema of
   * the tuples which shall be initialized by this object.
   * <p>
   * Probability Meta Data (type IProbability)
   * <p>
   * As basis for initializing the schema given is used. It is searched for
   * measurement value attributes by invoking
   * {@link SDFDatatypes#isMeasurementValue(de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype)}
   * for each attribute or subattribute.
   * <p>
   * Finally, each row of the covariance matrix used by
   * {@link #updateMetaData(MVRelationalTuple)} is set to the covariance list of
   * each measurement value attribute found in the schema respectively (see
   * {@link SDFAttribute#getCovariance()}).
   * 
   * @param schema
   *          the schema, used to initialize meta data, NOTE: has to be the
   *          schema of the tuples which shall be initialized by this object!
   */
  public StreamCarsMetaDataInitializer(SDFAttributeList schema)
  {
    this.schema = schema;
    this.initMetaData();
  }

  private void initMetaData()
  {
    this.initProbabilityMetaData();
    // invoke more StreamCars meta data initializers here
  }
  
  private void initProbabilityMetaData()
  {
    // 
    // get measurement attributes of schema given to constructor
    // 
    
    // go through all attributes in schema
    List<SDFAttribute> mAttrs = new ArrayList<SDFAttribute>();
    for( SchemaInfo info : new SchemaIterator(this.schema) ) {
    	     
      // get next schema attribute from iterator
      SDFAttribute attr = info.attribute;
      
      // if current attribute is measurement attribute
      SDFDatatype type = attr.getDatatype();
      // Notiz: SDFDatatype.isXXX(SDFDatatype) sollte false liefern, falls übergebener
      // SDFDatatype null ist!
      if (type != null && SDFDatatypes.isMeasurementValue(type))
      {
          
        // save attribute for later use
        mAttrs.add(attr);
      }
    }
    
    // 
    // initialize covariance matrix meta attribute
    // 
    
    // number_of_measurement_attributes * number_of_measurement_attributes
    this.covMatrix = new double[mAttrs.size()][mAttrs.size()];

    // current row of covariance matrix (corresponds to current measurement
    // attribute)
    int row = 0;

    // work through all measurement attributes saved before
    for (SDFAttribute mAttr : mAttrs)
    {

      // get covariance list out of attribute
      List<?> covarianceList = mAttr.getCovariance();

      // initialize current matrix row with covariance list
      for (int i = 0; i < covarianceList.size(); ++i)
      {
        // a ClassCastException being thrown by the following line
        // means that the covariance list of a measurement value attribute
        // does not entirely contain double objects (type Double)
        this.covMatrix[row][i] = (Double) covarianceList.get(i);
      }

      // next row
      row++;
    }
  }

  /**
   * Initializes tuple (of type {@link MVRelationalTuple}) meta data used by
   * project group StreamCars.
   * <p>
   * The meta data used to update tuple is being set by the constructor. See
   * {@link #StreamCarsMetaDataInitializer(SDFAttributeList)} for details.
   * <p>
   * NOTE: It is assumed that the schema used to initialize this object
   * represents the schema of the tuple initialized by this method.
   * 
   * @param tuple
   *          tuple of which meta data (M) should be initialized, NOTE: has to
   *          match schema given to constructor!
   */
  @Override
  public void updateMetadata(MVRelationalTuple<M> tuple)
  {
    this.initMetaDataOfTuple(tuple);
  }

  private void initMetaDataOfTuple(MVRelationalTuple<M> tupleGiven)
  {
    this.initProbabilityMetaDataOfTuple(tupleGiven);
    this.initAssoziationMetaData(tupleGiven);
    // invoke more tuple metadata initializers here
  }
  
  private void initAssoziationMetaData(MVRelationalTuple<M> tupleGiven) {
    tupleGiven.getMetadata().setConnectionList(new ConnectionList());
  }

  private void initProbabilityMetaDataOfTuple(MVRelationalTuple<M> tupleGiven)
  {
    // 
    // set covariance matrix of tuple given
    // (workaround since tuple iterator does not take tuple given itself as an
    // element)
    // 
    
    IProbability iProbabilityMetaData = tupleGiven.getMetadata();
    iProbabilityMetaData.setCovariance(this.covMatrix);
    
    // 
    // set covariance matrix of tuples in tuple given
    // 
    
    // navigate through all tuples in tuple given
    // use schema given to constructor as root schema
    for( TupleInfo info : new TupleIterator(tupleGiven, this.schema) ) {
    	     
      // get next tuple object from iterator
      Object tupleObject = info.tupleObject;
      
      // if it is a tuple (could also be a double, int, etc.)
      if (tupleObject instanceof MVRelationalTuple<?>)
      {
        MVRelationalTuple<?> tuple = (MVRelationalTuple<?>) tupleObject;
        
        // then get probability meta data of tuple
        Object metadataObject = tuple.getMetadata();
        // we know that the tuple cant't contain any meta data but probability
        // meta data (type IProbability), since it says so in declaration
        // of MVRelationalTuple
        iProbabilityMetaData = (IProbability) metadataObject;
        
        // set covariance matrix to the one set by initMetadataItself
        // a NullPointerException thrown by this line
        // means that meta data creation failed
        iProbabilityMetaData.setCovariance(this.covMatrix);
      }
    }
  }
}
