package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic.AbstractMultipartMixedhandler;
import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic.IContentCombiner;

public class MultipartMixedHandler extends AbstractMultipartMixedhandler<String> {

	public MultipartMixedHandler(String seperator) {

		this.setCombiner(new IContentCombiner<String>() {
			
			@Override
			public String CombineSubContents(List<Pair<String, String>> subcontents) {
				StringBuilder sb = new StringBuilder();

				for (int i = 0; i < subcontents.size(); i++) {
					if (i > 0) {
						sb.append(seperator);
					}
					sb.append(subcontents.get(i).getE2());
				}

				return sb.toString();
			}
		});
	}

}
