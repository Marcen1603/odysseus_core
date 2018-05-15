package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;

/**
 * Defining interface for combining subcontents of multipart message content
 * @author Thomas Vogelgesang
 *
 */
public interface IContentCombiner<T> {

	public abstract T CombineSubContents(List<Pair<String, T>> subcontents);
	
}
