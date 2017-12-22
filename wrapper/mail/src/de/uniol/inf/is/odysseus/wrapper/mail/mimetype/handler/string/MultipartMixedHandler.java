package de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.string;

import java.util.List;

import de.uniol.inf.is.odysseus.wrapper.mail.mimetype.handler.generic.AbstractMultipartMixedhandler;

public class MultipartMixedHandler extends AbstractMultipartMixedhandler<String> {

	private String seperator;

	public MultipartMixedHandler(String seperator) {
		this.seperator = seperator;
	}

	@Override
	protected String CombineSubContents(List<String> subcontents) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < subcontents.size(); i++) {
			if (i>0){
				sb.append(seperator);
			}
			sb.append(subcontents.get(i));
		}

		return sb.toString();
	}

}
