package de.uniol.inf.is.odysseus.iql.basic.ui.coloring;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor;
import org.eclipse.xtext.ui.editor.utils.TextStyle;

public class AbstractIQLHighlightingConfiguration extends DefaultHighlightingConfiguration {
	
	public static final String JAVA_ID = "java";
	public static final String JAVA_KEYWORDS_ID = "javakeywords";

	public void configure(IHighlightingConfigurationAcceptor acceptor) {
		super.configure(acceptor);
		acceptor.acceptDefaultHighlighting(JAVA_KEYWORDS_ID, "Java Keywords", javaKeywordsTextStyle());
		acceptor.acceptDefaultHighlighting(JAVA_ID, "Java", javaTextStyle());
	}
	
	public TextStyle javaTextStyle() {
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(0, 153, 153));
		return textStyle;
	}
	
	public TextStyle javaKeywordsTextStyle() {
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(0, 102, 102));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}
}
