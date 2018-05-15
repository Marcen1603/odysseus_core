package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.AbstractMimeTypeHandler;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.MimeTypeException;

public abstract class AbstractMultipartHandler<T> extends AbstractMimeTypeHandler<T> {
	
	private IContentCombiner<T> combiner;
	
	protected IContentCombiner<T> getCombiner() {
		return combiner;
	}

	protected void setCombiner(IContentCombiner<T> combiner) {
		this.combiner = combiner;
	}

	@Override
	public T getContent(Part part) throws IOException, MessagingException, MimeTypeException {
		T content = null;
		if (part instanceof MimeMultipart) {
			MimeMultipart mp = (MimeMultipart) part;
			content = getContent(mp);
			
		} else if (part instanceof MimeMessage) {
			MimeMessage message = (MimeMessage) part;

			content = this.getContent((MimeMultipart) message.getContent());
		}
		return content;
	}

	public T getContent(MimeMultipart mp) throws MessagingException, MimeTypeException, IOException {
		
		List<Pair<String, T>> subcontents = new ArrayList<Pair<String, T>>();
		
		for (int i = 0; i < mp.getCount(); i++) {
			Part p = mp.getBodyPart(i);
			p.getContentType();
			subcontents.add(new Pair<String, T>(p.getContentType(), this.getRegistry().HandlePart(p)));
		}
		
		T content = this.getCombiner().CombineSubContents(subcontents);
		return content;
	}
}
