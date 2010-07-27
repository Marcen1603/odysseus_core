
package de.uniol.inf.is.odysseus.scars.objecttracking;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.base.AbstractMetadataUpdater;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;
import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * Initializes metadata (of type {@link IProbability}) of a tuple (of type
 * {@link MVRelationalTuple}).
 * <p>
 * See public methods for details.
 * 
 * @author Hauke
 * @author Sven
 */
public class CovarianceInitiator<M extends IProbability> extends
    AbstractMetadataUpdater<M, MVRelationalTuple<M>>
{
  // set by constructor/initMetadataItself
  // used by updateMetadata
  private double[][] covMatrix = null;
  private SDFAttributeList schema = null;

  /**
   * Creates a CovarianceInitiator object and initializes the metadata used by
   * {@link #updateMetadata(MVRelationalTuple)}.
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
   * <p>
   * NOTE: schema is used by {@link #updateMetadata(MVRelationalTuple)} as root
   * to navigate through the tuple given. So it has to represent the schema of
   * the tuples which shall be initialized by this object.
   * 
   * @param schema
   *          the schema, used to initialize metadata, NOTE: has to be the
   *          schema of the tuples which shall be initialized by this object!
   */
  public CovarianceInitiator(SDFAttributeList schema)
  {
    this.schema = schema;
    this.initMetadataItself();
  }

  private void initMetadataItself()
  {
    // get measurement value attribute paths of schema given to constructor

    List<int[]> mvAttrPaths = OrAttributeResolver
        .getPathsOfMeasurements(this.schema);

    // initialize covariance matrix:
    // number of mv attributes * number of mv attributes

    this.covMatrix = new double[mvAttrPaths.size()][mvAttrPaths.size()];

    // current row of covariance matrix (corresponds to current measurement
    // value attribute)

    int row = 0;

    // work through all measurement value paths

    for (int[] mvAttrPath : mvAttrPaths)
    {

      // and get the corresponding measurement value attribute

      Object mvObject = OrAttributeResolver.getSubSchema(this.schema,
          mvAttrPath);
      // a ClassCastException being thrown by the following line
      // means that the object returned by OrAttributeResolver
      // for a measurement value attribute path returned by
      // the same class is no schema attribute (type SDFAttribute)
      SDFAttribute mvAttribute = (SDFAttribute) mvObject;

      // get covariance list out of attribute

      ArrayList<?> covariances = mvAttribute.getCovariance();

      // initialize current matrix row with covariance list

      for (int i = 0; i < covariances.size(); ++i)
      {
        // a ClassCastException being thrown by the following line
        // means that the covariance list of a measurement value attribute
        // does not entirely contain double objects (type Double)
        this.covMatrix[row][i] = (Double) covariances.get(i);
      }

      // next row

      row++;
    }
  }

  /**
   * Initializes metadata (of type {@link IProbability}) of a tuple (of type
   * {@link MVRelationalTuple}).
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
    this.initMetadataOfTuples(tuple);
  }

  private void initMetadataOfTuples(MVRelationalTuple<M> tupleGiven)
  {
    // navigate through all tuples in tupleGiven
    // use schema given to constructor as root schema

    TupleIterator iterator = new TupleIterator(tupleGiven, this.schema);

    // while there are more tuples

    while (!iterator.isFinished())
    {

      // get next tuple object

      Object tupleObject = iterator.getTupleObject();
      
      // if it is a tuple
      
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

        iProbabilityMetadata.setCovariance(this.covMatrix);
      }
      
      // tell iterator to move to next tuple

      iterator.next();
    }
  }
}
