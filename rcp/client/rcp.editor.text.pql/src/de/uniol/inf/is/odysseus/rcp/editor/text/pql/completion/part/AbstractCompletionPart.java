package de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.part;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;

public abstract class AbstractCompletionPart {
	

	public static String getContent(Region region, IDocument doc){
		try{
			return doc.get(region.getOffset(), region.getLength());
		}catch(BadLocationException ex){
			ex.printStackTrace();
		}
		return "";
	}
	
	public static String getContent(int offset, int length, IDocument doc){
		try{
			return doc.get(offset, length);
		}catch(BadLocationException ex){
			ex.printStackTrace();
		}
		return "";
	}
	
	public static String contentBetween(int firstOffset, int secondOffset, IDocument doc){
		return getContent(firstOffset, secondOffset-firstOffset, doc);
	}


}
