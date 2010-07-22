
package de.uniol.inf.is.odysseus.scars.objecttracking;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.base.AbstractMetadataUpdater;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.scars.util.OrAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * Initializes metadata (of type {@link IProbability}) of a scan tuple (of type
 * {@link MVRelationalTuple}) by initializing metadata of each car tuple.
 * <p>
 * See {@link #CovarianceInitiator(SDFAttributeList)} for details.
 * 
 * @author Hauke
 * @author Sven
 * 
 */
public class CovarianceInitiator<M extends IProbability> extends
    AbstractMetadataUpdater<M, MVRelationalTuple<M>>
{
  /**
   * Position of car list in scan tuple or schema.
   * <p>
   * Ugly, but currently needed
   */
  public static final int POS_OF_LIST_IN_SCAN = 1;

  public static ArrayList<int[]> getPathsOfMeasurements(
      SDFAttributeList attrList)
  {
    ArrayList<int[]> tmp = new ArrayList<int[]>();

    return getPathsOfMeasurementValue(attrList, null, tmp);
  }

  private static ArrayList<int[]> getPathsOfMeasurementValue(
      SDFAttributeList attrList, int[] preliminaryPath, ArrayList<int[]> tmp)
  {

    // Erster Durchlauf:
    if (preliminaryPath == null)
    {
      preliminaryPath = new int[0];
    }

    int counter = 0;
    for (SDFAttribute attr : attrList)
    {
      int[] singlePath = new int[preliminaryPath.length + 1];
      for (int i = 0; i < singlePath.length - 1; i++)
      {
        singlePath[i] = preliminaryPath[i];
      }
      singlePath[singlePath.length - 1] = counter;

      if (SDFDatatypes.isMeasurementValue(attr.getDatatype()))
      {
        tmp.add(singlePath);
      }
      if (!SDFDatatypes.isMeasurementValue(attr.getDatatype())
          && attr.getSubattributes() != null)
      {
        tmp = getPathsOfMeasurementValue(attr.getSubattributes(), singlePath,
            tmp);
      }
      counter++;
    }

    return tmp;
  }

  // set by initMetadataItself
  // used by updateMetadata
  private double[][] covMatrix = null;
  private ArrayList<int[]> mvAttrPaths = null;
  private List<String> mvAttrNames = new ArrayList<String>();

  /**
   * Creates a CovarianceInitiator object and initializes the metadata used by
   * {@link #updateMetadata(MVRelationalTuple)}.
   * <p>
   * As basis for initializing the first car list schema in scanSchema is used
   * as there is only one car schema for all cars:
   * <p>
   * scanSchema.carListSchema.firstCarSchema
   * <p>
   * or
   * <p>
   * scanSchema.carListSchema.attributes[0]
   * <p>
   * To find the car list schema in the first place {@link #POS_OF_LIST_IN_SCAN}
   * is used:
   * <p>
   * scanSchema.attributes[POS_OF_LIST_IN_SCAN].attributes[0]
   * <p>
   * The first car schema is then searched for measurement value attributes by
   * invoking
   * {@link SDFDatatypes#isMeasurementValue(de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype)}
   * for each attribute or subattribute. The paths (ArrayList of int[]) of the
   * measurement value attributes found are those used by
   * {@link #updateMetadata(MVRelationalTuple)}. They are set relative to the
   * car schema rather than to the scan schema.
   * <p>
   * Finally, each row of the covariance matrix used by
   * {@link #updateMetadata(MVRelationalTuple)} is set to the covariance list of
   * the corresponding measurement value attribute in the schema (see
   * {@link SDFAttribute#getCovariance()}).
   * 
   * @param scanSchema
   *          the scan schema
   */
  public CovarianceInitiator(SDFAttributeList scanSchema)
  {
    this.initMetadataItself(scanSchema);
  }

  private void initMetadataItself(SDFAttributeList scanSchema)
  {
    // get first car schema out of scan schema by using OrAttributeResolver
    // and POS_OF_LIST_IN_SCAN (ugly)
    SDFAttributeList firstCarSchema = OrAttributeResolver.getSubSchema(
        scanSchema, new int[]
        {
            CovarianceInitiator.POS_OF_LIST_IN_SCAN, 0
        });
    // TODO wrong path

    // get measurement value paths
    this.mvAttrPaths = CovarianceInitiator.getPathsOfMeasurements(firstCarSchema);

    // initialize covariance matrix:
    // number of mv attributes * number of mv attributes
    this.covMatrix = new double[this.mvAttrPaths.size()][this.mvAttrPaths.size()];

    // current row of covariance matrix (corresponds to current measurement
    // value attribute)
    int row = 0;

    // work through all measurement value paths
    for (int[] mvAttrPath : this.mvAttrPaths)
    {
      // and get the corresponding measurement value attribute
      Object mvObject = OrAttributeResolver
          .getSubSchema(firstCarSchema, mvAttrPath);
      if (mvObject instanceof SDFAttribute)
      {
        SDFAttribute mvAttribute = (SDFAttribute) mvObject;
        
        // save attribute name
        this.mvAttrNames.add(mvAttribute.getAttributeName());

        // get covariance list out of attribute
        ArrayList<?> covariances = mvAttribute.getCovariance();

        // initialize current matrix row with covariance list
        for (int i = 0; i < covariances.size(); ++i)
        {
          this.covMatrix[row][i] = (Double) covariances.get(i);
          // TODO class cast exception
        }

        // next row
        row++;
      }
      else
      {
        // not so good..
        // did getPathsOfMeasurements(SDFAttributeList) get wrong paths?
        // TODO throw exception
      }
    }
  }

  /**
   * Initializes metadata (of type {@link IProbability}) of a scan tuple (of
   * type {@link MVRelationalTuple}) by initializing metadata of each car tuple.
   * <p>
   * The metadata used to update scanTuple is being set before. See
   * {@link #CovarianceInitiator(SDFAttributeList)} for details.
   * <p>
   * In order to find the car tuples, {@link #POS_OF_LIST_IN_SCAN} is used.
   * 
   * @param scanTuple
   *          scan tuple of which metadata (M) should be initialized
   * 
   */
  @Override
  public void updateMetadata(MVRelationalTuple<M> scanTuple)
  {
    this.initMetadataOfScanTuple(scanTuple);
  }

  private void initMetadataOfScanTuple(MVRelationalTuple<M> scanTuple)
  {
    // get list tuple out of scan tuple by using POS_OF_LIST_IN_SCAN (ugly)
    MVRelationalTuple<M> listTuple = scanTuple
        .<MVRelationalTuple<M>> getAttribute(CovarianceInitiator.POS_OF_LIST_IN_SCAN);
    // TODO class cast exception

    // work through all car tuples in list tuple
    for (int indexOfCurrCar = 0; indexOfCurrCar < listTuple.getAttributeCount(); ++indexOfCurrCar)
    {
      MVRelationalTuple<M> carTuple = listTuple
          .<MVRelationalTuple<M>> getAttribute(indexOfCurrCar);
      // TODO class cast exception

      // initialize each of them
      this.initMetadataOfCarTuple(carTuple);
    }
  }

  private void initMetadataOfCarTuple(MVRelationalTuple<M> carTuple)
  {
    // use metadata obtained by initMetadataItself
    carTuple.getMetadata().setAttributePaths(this.mvAttrPaths);
    carTuple.getMetadata().setCovariance(this.covMatrix);
    carTuple.getMetadata().setMVAttributeNames(this.mvAttrNames);
  }
}
