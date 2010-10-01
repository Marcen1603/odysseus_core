package de.uniol.inf.is.odysseus.objecttracking.predicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * This predicate calculates the mahalanobis distance between two vectors.
 * 
 * @author André Bolles
 *
 * @param <T> The metadata of tuples must provide a covariance matrix
 */
public class MahalanobisPredicate<T extends IProbability> extends AbstractPredicate<MVRelationalTuple<T>> implements IRelationalPredicate{


	private static final long serialVersionUID = 9146263764814383957L;

	private double threshold;
	
	private String operator;
	
	private int[] leftMVpos;
	private int[] rightMVpos;
	
	/* I think, this will not be used. */
	private Map<SDFAttribute, SDFAttribute> replacementMap = new HashMap<SDFAttribute, SDFAttribute>();
	
	/**
	 * 
	 * @param threshold y-value in equation (x-u)^T S (x-u) < | <= | = | => | > y
	 */
	public MahalanobisPredicate(double threshold, String operator){
		this.threshold = threshold;
		this.operator = operator;
	}
	
	public MahalanobisPredicate(MahalanobisPredicate<T> mahalanobisPredicate) {
		this.threshold = mahalanobisPredicate.threshold;
		this.operator = mahalanobisPredicate.operator;
	}

	@Override
	public List<SDFAttribute> getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(SDFAttributeList leftSchema, SDFAttributeList rightSchema) {
		// setting the left measurement value positions
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i<leftSchema.size(); i++){
			SDFAttribute attr = leftSchema.get(i);
			if(SDFDatatypes.isMeasurementValue(attr.getDatatype())){
				list.add(new Integer(i));	
			}
		}
		
		this.leftMVpos = new int[list.size()];
		for(int i = 0; i<this.leftMVpos.length; i++){
			this.leftMVpos[i] = list.get(i);
		}
		
		// setting the right measurement values positions
		list.clear();
		for(int i = 0; i<rightSchema.size(); i++){
			SDFAttribute attr = rightSchema.get(i);
			if(SDFDatatypes.isMeasurementValue(attr.getDatatype())){
				list.add(new Integer(i));	
			}
		}
		
		this.rightMVpos = new int[list.size()];
		for(int i = 0; i<this.rightMVpos.length; i++){
			this.rightMVpos[i] = list.get(i);
		}
	}

	@Override
	public boolean evaluate(MVRelationalTuple<T> input) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * This method returns true, if the comparison between mahalanobis distance between a point left and
	 * a multivariate normally distributed vector right and a threshold is true.
	 * (left - right)^T rightCov (left - right)
	 */
	@Override
	public boolean evaluate(MVRelationalTuple<T> left,
			MVRelationalTuple<T> right) {
		left.setMeasurementValuePositions(this.leftMVpos.clone());
		right.setMeasurementValuePositions(this.rightMVpos.clone());
		
		// Darf man auf MV-Werte beschränken oder muss auch nicht MV-Werte berücksichtigen?
		// Wenn man das muss, was ist dann mit Strings etc.?
		// Spielt im Object-Tracking eh keine Rolle, da man dort nur MV-Werte und keine nicht
		// MV-Werte hat.
		double[] leftMVVector = left.getMeasurementValues();
		RealMatrix leftV = new RealMatrixImpl(leftMVVector);
		
		double[] rightMVVector = right.getMeasurementValues();
		RealMatrix rightV = new RealMatrixImpl(rightMVVector);
		
		double[][] rightCov = right.getMetadata().getCovariance();
		RealMatrix rightCovMatrix = new RealMatrixImpl(rightCov);
		RealMatrix rightCovInvMatrix = rightCovMatrix.inverse();
		
		RealMatrix distanceMatrix = leftV.subtract(rightV).transpose().multiply(rightCovInvMatrix).multiply(leftV.subtract(rightV));
		double distance = distanceMatrix.getEntry(0, 0);
		
		if(this.operator.equals("<")){
			return distance < this.threshold;
		}else if(this.operator.equals("<=")){
			return distance <= this.threshold;
		}else if(this.operator.equals("=")){
			return distance == this.threshold;
		}else if(this.operator.equals(">=")){
			return distance >= this.threshold;
		}else{
			return distance > this.threshold;
		}
		
		
	}

	@Override
	public void replaceAttribute(SDFAttribute curAttr, SDFAttribute newAttr) {
		this.replacementMap.put(curAttr, newAttr);
		
	}
	
	private SDFAttribute getReplacement(SDFAttribute a) {
		SDFAttribute ret = a;
		SDFAttribute tmp = null;
		while ((tmp=replacementMap.get(ret))!=null){
			ret = tmp;
		}
		return ret;
	}

	@Override
	public MahalanobisPredicate<T> clone(){
		return new MahalanobisPredicate<T>(this);
	}
}
