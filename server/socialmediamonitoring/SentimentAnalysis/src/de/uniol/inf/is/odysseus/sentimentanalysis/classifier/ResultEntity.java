package de.uniol.inf.is.odysseus.sentimentanalysis.classifier;

/**
 * ResultEntity is used in the KNearestNeighbor classifier
 * @author Marc Preuschaft
 *
 */
public class ResultEntity implements Comparable<ResultEntity> {

	private Double score;
	private int label;

	public ResultEntity(double score, Integer label) {
		this.score = score;
		this.label = label;
	}

	public Double getScore() {
		return score;
	}

	public int getLabel() {
		return label;
	}

	@Override
	public int compareTo(ResultEntity other) {
		return score.compareTo(other.score);
	}

}
