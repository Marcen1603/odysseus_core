package de.uniol.inf.is.odysseus.assoziation;

public class DefaultAssociationEvaluationFunction implements IAssociationEvaluationFunction{

	/**
	 * Diese Funktion sorgt dafür, dass nur noch eindeutige Zuordnungen vorhanden sind.
	 */
	@Override
	public double[][] singleMatchingEvaluation(double[][] matchingMatrix) {
		double[][] singleMatchingMatrix = null;
		int index = -1;
		if(matchingMatrix.length > 0) {
			singleMatchingMatrix = new double[matchingMatrix.length][matchingMatrix[0].length];
		}
		
		for (int i = 0; i < matchingMatrix.length; i++) {
			index = getMaxRowIndex(matchingMatrix[i]);
			if(index != -1) {
				singleMatchingMatrix[i][index] = matchingMatrix[i][index];
			}
			index = -1;
		}
		
		double maxValue = -1;
		int maxValueIndex = -1;
		for (int i = 0; i < singleMatchingMatrix[0].length; i++) {
			for (int j = 0; j < singleMatchingMatrix.length; j++) {
				if(singleMatchingMatrix[j][i] != 0.0d && maxValue < singleMatchingMatrix[j][i]) {
					//Wenn der Wert größer als der vorher errechnete Wert ist wird dieser als neuer Wert übernommen
					maxValue = matchingMatrix[j][i];
					maxValueIndex = j;
					singleMatchingMatrix[j][i] = 0;
				} if(maxValue >= singleMatchingMatrix[j][i]) {
					// Falls der Wert kleiner oder gleich dem vorher errechneten Wert ist wird dieser ignoriert 
					singleMatchingMatrix[j][i] = 0;
				}
			}
			if(maxValue != -1) {
				singleMatchingMatrix[maxValueIndex][i] = maxValue;
			}
			maxValue = -1;
			maxValueIndex = -1;
		}
		
		return singleMatchingMatrix;
	}
	
	/**
	 * 
	 * @param row aktuelle Zeiler der Matrix
	 * @return Index des maximalen Wertes der Zeile
	 */
	private int getMaxRowIndex(double[] row) {
		int index = -1;
		double maxValue = 0.0d;
		for (int i = 0; i < row.length; i++) {
			if(maxValue < row[i]) {
				maxValue = row[i];
				index = i;
			}
		}
		return index;
	}
}
