/**
 * 
 */
package mg.dynaquest.monitor.responseprediction.learner.weka;

import mg.dynaquest.monitor.responseprediction.learner.Attribute;
import weka.core.FastVector;

/**
 * Class for non numerical attributes used in learners based
 * on weka algorithms.
 * @author Jonas Jacobi
 */
public  abstract class WekaAttribute <T> extends Attribute<T, String> {
	weka.core.Attribute attribute;
	
	public WekaAttribute(T... classValues) {
		super(classValues);
	}
	
	@Override
	protected void setClassValues(T... values) {
		super.setClassValues(values);
		FastVector attributeValues = new FastVector();
		for(T curValue : values){
			attributeValues.addElement(curValue.toString());
		}
		this.attribute = new weka.core.Attribute("class", attributeValues);
	}
	
	/**
	 * Get a weka representation of the attribute
	 * @return the representation of this attribute in weka
	 */
	public weka.core.Attribute getAttribute() {
		return this.attribute;
	}
}