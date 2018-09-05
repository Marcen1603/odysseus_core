package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.Part;

public abstract class MimeTypeHandlerRegistry<T> implements IMimeTypeHandlerRegistry<T> {

	private HashMap<String, IMimeTypeHandler<T>> handlers;

	public MimeTypeHandlerRegistry() {
		handlers = new HashMap<String, IMimeTypeHandler<T>>();
		Init();
	}

	/**
	 * creates mime type handlers and registers them in the registry
	 */
	protected abstract void Init();

	public void RegisterHandler(IMimeTypeHandler<T> handler) {
		if (handler == null) {
			throw new IllegalArgumentException("mime type handler is null");
		}
		if (handlers.containsKey(handler.getMimeType())) {
			throw new IllegalArgumentException(
					"handler for mime type '" + handler.getMimeType() + "' already registered");
		}
		handlers.put(handler.getMimeType(), handler);
		handler.setRegistry(this);
	}

	public boolean UnregisterHandler(String mimeType) {
		if (handlers.containsKey(mimeType)) {
			handlers.remove(mimeType);
			return true;
		} else {
			return false;
		}
	}

	public Set<String> getRegisteredHandlers() {
		return handlers.keySet();
	}

	public IMimeTypeHandler<T> getHandler(String mimeType) throws MimeTypeException {
		if (this.handlers.containsKey(mimeType)) {
			return this.handlers.get(mimeType);
		} else {
			throw new MimeTypeException("no handler registered for type '" + mimeType + "'");
		}
	}

	public T HandlePart(Part p) throws MessagingException, MimeTypeException, IOException {
		Set<String> registeredHandlers = this.getRegisteredHandlers();

		for (String mimeType : registeredHandlers) {
			if (p.isMimeType(mimeType)) {
				IMimeTypeHandler<T> handler = this.getHandler(mimeType);
				return handler.getContent(p);
			}
		}

		throw new MimeTypeException("No matching mime type handler available: " + p.getContentType());
	}
}
