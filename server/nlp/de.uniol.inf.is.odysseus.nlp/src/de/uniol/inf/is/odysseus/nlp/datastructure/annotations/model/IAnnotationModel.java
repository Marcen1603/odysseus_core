package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model;

import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.Annotated;
import de.uniol.inf.is.odysseus.nlp.datastructure.annotations.IAnnotation;

/**
 * Interface for Algorithms for IAnnotation based Annotating
 */
public interface IAnnotationModel<A extends IAnnotation> {
	/**
	 * Specifies the Annotation, used in this model
	 */
	//public Class<? extends IAnnotation> type();

	/**
	 * Runs the annotation process and adds annotations to {@link Annotated}
	 * annotated.
	 * 
	 * @param annotated
	 *            The object to whom the annotations are applied.
	 */
	public void annotate(Annotated annotated);

	@Override
	public boolean equals(Object obj);

	/**
	 * @return Identifier of Annotation class
	 */
	public String identifier();

}
