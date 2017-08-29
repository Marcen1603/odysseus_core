package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model;

/**
 * This interface is implemented in {@link AnnotationModel}-classes if they're joinable.
 * @see IJoinable#join(AnnotationModel)
 */
public interface IJoinable {
	/**
	 * If two {@link AnnotationModel}s are joinable, this method joins the data inside the model to create a new one.
	 * @param model to be joined
	 * @param overwrite if true and model contains data that are already in this, data in this will be overridden by the new ones.
	 * @return new joined model
	 */
	public AnnotationModel<?> join(AnnotationModel<?> model, boolean overwrite);
}
