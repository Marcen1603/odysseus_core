package de.uniol.inf.is.odysseus.wrapper.mail.contentreader;

import javax.mail.Message;

/**
 * Interface defining methods to read and transform the content from mail messages
 * @author Thomas Vogelgesang
 *
 */
public interface IContentReader<T> {

	public T ReadContent(Message message);
	
	
}
