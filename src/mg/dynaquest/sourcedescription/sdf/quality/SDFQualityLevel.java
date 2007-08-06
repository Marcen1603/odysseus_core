package mg.dynaquest.sourcedescription.sdf.quality;

/**
 * Nur ein enum um von den eigentlichen Werte abstahieren zu können (in java 1.5
 * soll es sowas ja dann auch direkt geben. Ist nicht so richtig schön, die
 * Zahlen sowohl in der SDF-Beschreibung (sdf_quality) als auch hier zu haben
 * ...
 */

public class SDFQualityLevel {

	public static float VERY_GOOD = 1f;

	public static float GOOD = 0.75f;

	public static float MEDIUM = 0.5f;

	public static float BAD = 0.25f;

	public static float VERY_BAD = 0f;
}