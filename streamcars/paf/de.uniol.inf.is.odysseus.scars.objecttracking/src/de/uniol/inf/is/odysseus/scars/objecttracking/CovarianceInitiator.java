
package de.uniol.inf.is.odysseus.scars.objecttracking;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.base.AbstractMetadataUpdater;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;
import de.uniol.inf.is.odysseus.scars.util.SchemaIterator;
import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * Initializes tuple (of type {@link MVRelationalTuple}) metadata used by
 * project group StreamCars.
 * <p>
 * See public methods for details.
 * 
 * @author Hauke
 * @author Sven
 */
public class CovarianceInitiator<M extends IProbability> extends
    AbstractMetadataUpdater<M, MVRelationalTuple<M>>
{
  // set by constructor/initMetadata
  // used by updateMetadata
  private double[][] covMatrix = null;
  private SDFAttributeList schema;

  /**
   * Creates a CovarianceInitiator object and initializes the metadata used by
   * {@link #updateMetadata(MVRelationalTuple)}.
   * <p>
   * NOTE: schema is used by {@link #updateMetadata(MVRelationalTuple)} as root
   * to navigate through the tuple given. So it has to represent the schema of
   * the tuples which shall be initialized by this object.
   * <p>
   * Probability Metadata (type IProbability)
   * <p>
   * As basis for initializing the schema given is used. It is searched for
   * measurement value attributes by invoking
   * {@link SDFDatatypes#isMeasurementValue(de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype)}
   * for each attribute or subattribute.
   * <p>
   * Finally, each row of the covariance matrix used by
   * {@link #updateMetadata(MVRelationalTuple)} is set to the covariance list of
   * each measurement value attribute found in the schema respectively (see
   * {@link SDFAttribute#getCovariance()}).
   * 
   * @param schema
   *          the schema, used to initialize metadata, NOTE: has to be the
   *          schema of the tuples which shall be initialized by this object!
   */
  public CovarianceInitiator(SDFAttributeList schema)
  {
    this.schema = schema;
    this.initMetadata();
  }

  private void initMetadata()
  {
    this.initProbabilityMetadata();
    // invoke more metadata initializers here
  }
  
  private void initProbabilityMetadata()
  {
    // get measurement attributes of schema given to constructor
    
    // go through all attributes in schema
    List<SDFAttribute> mAttrs = new ArrayList<SDFAttribute>();
    SchemaIterator iterator = new SchemaIterator(this.schema);
    while (!iterator.isFinished())
    {
      SDFAttribute attr = iterator.getAttribute();
      
      // if current attribute is measurement attribute
      SDFDatatype type = attr.getDatatype();
      if (type != null)
      {
        if (SDFDatatypes.isMeasurementValue(type))
        {
          
          // save attribute for later use
          mAttrs.add(attr);
        }
      }
      else
      {
        // TODO
      }
      
      // get next attribute in schema
      iterator.next();
    }

    // initialize covariance matrix:
    // number_of_measurement_attributes * number_of_measurement_attributes

    this.covMatrix = new double[mAttrs.size()][mAttrs.size()];

    // current row of covariance matrix (corresponds to current measurement
    // value attribute)

    int row = 0;

    // work through all measurement attributes saved before

    for (SDFAttribute mAttr : mAttrs)
    {

      // get covariance list out of attribute

      ArrayList<?> covarianceList = mAttr.getCovariance();

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
   * Initializes tuple (of type {@link MVRelationalTuple}) metadata used by
   * project group StreamCars.
   * <p>
   * The metadata used to update tuple is being set by the constructor. See
   * {@link #CovarianceInitiator(SDFAttributeList)} for details.
   * <p>
   * NOTE: It is assumed that the schema used to initialize this object
   * represents the schema of the tuple initialized by this method.
   * 
   * @param tuple
   *          tuple of which metadata (M) should be initialized, NOTE: has to
   *          match schema given to constructor!
   */
  @Override
  public void updateMetadata(MVRelationalTuple<M> tuple)
  {
    this.initMetadataOfTuple(tuple);
  }

  private void initMetadataOfTuple(MVRelationalTuple<M> tupleGiven)
  {
    this.initProbabilityMetadataOfTuple(tupleGiven);
    // invoke more tuple metadata initializers here
  }
  
  private void initProbabilityMetadataOfTuple(MVRelationalTuple<M> tupleGiven)
  {
    Object rootObject = tupleGiven.getAttribute(0);
    if (rootObject instanceof MVRelationalTuple<?>)
    {
      MVRelationalTuple<?> root = (MVRelationalTuple<?>) rootObject;
      IProbability prob = ((IProbability) root.getMetadata());
      prob.setCovariance(this.covMatrix);
    }
    else
    {
      // TODO
    }
    
    // navigate through all tuples in tupleGiven
    // use schema given to constructor as root schema

    TupleIterator iterator = new TupleIterator(tupleGiven, this.schema);

    // while there are more tuples

    while (!iterator.isFinished())
    {

      // get next tuple object

      Object tupleObject = iterator.getTupleObject();
      
      // if it is a tuple (could also be a double, int, etc.)
      
      if (tupleObject instanceof MVRelationalTuple<?>)
      {
        
        // then get probability metadata of tuple
        
        MVRelationalTuple<?> tuple = (MVRelationalTuple<?>) tupleObject;
        Object metadataObject = tuple.getMetadata();
        // we know that the tuple cant't contain any metadata but probability
        // metadata (type IProbability), since it says so in declaration
        // of MVRelationalTuple
        IProbability iProbabilityMetadata = (IProbability) metadataObject;
        
        // set covariance matrix to the one set by initMetadataItself
        
        if (iProbabilityMetadata != null)
        {
          iProbabilityMetadata.setCovariance(this.covMatrix);
        }
        else
        {
          // TODO
        }
      }
      
      // tell iterator to move to next tuple

      iterator.next();
    }
  }
}
