package mg.dynaquest.sourcedescription.sdf.function;

import java.util.HashMap;

import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFFunctions;

public class SDFFunctionFactory {
	private volatile static HashMap<String,SDFFunction> functionCache = new HashMap<String, SDFFunction>();

	protected SDFFunctionFactory() {
	}

	public synchronized static SDFFunction getFunction(String functionURI,
			String functionTypeURI) {
		
		SDFFunction function = (SDFFunction) functionCache.get(functionURI);
		if (function == null) {

			while (true) {
				if (functionTypeURI.equals(SDFFunctions.OneToOne)) {
					function = new SDFOneToOneFunction(functionURI);
					break;
				}
				if (functionTypeURI.equals(SDFFunctions.OneToMany)) {
					function = new SDFOneToManyFunction(functionURI);
					break;
				}
				if (functionTypeURI.equals(SDFFunctions.ManyToOne)) {
					function = new SDFManyToOneFunction(functionURI);
					break;
				}
				if (functionTypeURI.equals(SDFFunctions.Identity)) {
					function = new SDFIdentityFunction(functionURI);
					break;
				}
				if (functionTypeURI.equals(SDFFunctions.MathCalc)) {
					function = new SDFMathCalcFunction(functionURI);
					break;
				}
				if (functionTypeURI.equals(SDFFunctions.Translate)) {
					function = new SDFTranslateFunction(functionURI);
					break;
				}
				if (functionTypeURI.equals(SDFFunctions.ToString)) {
					function = new SDFToStringFunction(functionURI);
					break;
				}
				if (functionTypeURI.equals(SDFFunctions.ToNumber)) {
					function = new SDFToNumberFunction(functionURI);
					break;
				}
				if (functionTypeURI.equals(SDFFunctions.TSplittReg)) {
					function = new SDFTSplittRegFunction(functionURI);
					break;
				}
				if (functionTypeURI.equals(SDFFunctions.TSplitt)) {
					function = new SDFTSplittFunction(functionURI);
					break;
				}
				if (functionTypeURI.equals(SDFFunctions.MSplitt)) {
					function = new SDFMSplittFunction(functionURI);
					break;
				}
				if (functionTypeURI.equals(SDFFunctions.Merge)) {
					function = new SDFMergeFunction(functionURI);
					break;
				}
				System.out.println("Fehlerhafte Function: " + functionTypeURI);
				break;
			}
			functionCache.put(functionURI, function);
		}
		return function;

	}
}