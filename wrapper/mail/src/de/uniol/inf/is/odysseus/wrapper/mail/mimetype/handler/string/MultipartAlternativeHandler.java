package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string;

import java.util.List;
import java.util.stream.Collectors;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic.AbstractMultipartAlternativeHandler;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic.IContentCombiner;

public class MultipartAlternativeHandler extends AbstractMultipartAlternativeHandler<String> {

	public MultipartAlternativeHandler(String preferredMimeType) {
		this.setCombiner(new IContentCombiner<String>() {
			
			@Override
			public String CombineSubContents(List<Pair<String, String>> subcontents) {
				String selectedContent = "";

				if (!subcontents.isEmpty()) {
					List<Pair<String, String>> filtered = subcontents.stream()
							.filter(element -> element.getE1() == preferredMimeType).collect(Collectors.toList());

					selectedContent = !filtered.isEmpty() ? filtered.get(0).getE2() : subcontents.get(0).getE2();
				}

				return selectedContent;
			}
		});
	}

	

}
