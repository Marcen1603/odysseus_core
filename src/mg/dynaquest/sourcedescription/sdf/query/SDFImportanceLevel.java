package mg.dynaquest.sourcedescription.sdf.query;

import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFQueryVoc;

public class SDFImportanceLevel {
	public static float VERY_IMPORTANT = 1f;

	public static float IMPORTANT = 0.75f;

	public static float NEUTRAL = 0.5f;

	public static float NICE_TO_HAVE = 0.25f;

	public static float DISPENSABLE = 0f;

	public static float getLevel(String URI) {
		if (URI.equals(SDFQueryVoc.VeryImportant)) {
			return VERY_IMPORTANT;
		}
		if (URI.equals(SDFQueryVoc.Important)) {
			return IMPORTANT;
		}
		if (URI.equals(SDFQueryVoc.Neutral)) {
			return NEUTRAL;
		}
		if (URI.equals(SDFQueryVoc.NiceToHave)) {
			return NICE_TO_HAVE;
		}
		if (URI.equals(SDFQueryVoc.Dispensable)) {
			return DISPENSABLE;
		}
		return -1;
	}
}