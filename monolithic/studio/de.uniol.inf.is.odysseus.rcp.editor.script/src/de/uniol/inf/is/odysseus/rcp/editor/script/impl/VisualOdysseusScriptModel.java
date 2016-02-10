package de.uniol.inf.is.odysseus.rcp.editor.script.impl;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptTextBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.VisualOdysseusScriptException;
import de.uniol.inf.is.odysseus.rcp.editor.script.blocks.DefaultOdysseusScriptTextBlock;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptParser;

public class VisualOdysseusScriptModel {

	private List<IVisualOdysseusScriptTextBlock> textBlocks;
	
	public VisualOdysseusScriptModel() {
		// nothing to do
	}
	
	public void parse( List<String> odysseusScriptTextLines ) throws VisualOdysseusScriptException {
		Preconditions.checkNotNull(odysseusScriptTextLines, "odysseusScriptTextLines must not be null!");
		
		String currentKeyword = null;
		StringBuilder currentText = new StringBuilder();
		
		textBlocks = Lists.newArrayList();
		
		for( String odysseusScriptTextLine : odysseusScriptTextLines ) {
			String line = odysseusScriptTextLine;
			
			int parameterKeyPos = line.indexOf(OdysseusScriptParser.PARAMETER_KEY);
			if( parameterKeyPos != -1) {
				
				// finish current text block
				if( currentKeyword != null ) {
					DefaultOdysseusScriptTextBlock block = new DefaultOdysseusScriptTextBlock();
					block.init(currentKeyword, currentText.toString(), Lists.newArrayList(textBlocks));
					
					textBlocks.add(block);
					currentKeyword = null;
					currentText = new StringBuilder();
				}
				
				int pos = line.indexOf(" ", parameterKeyPos + 1);
				if( pos == -1 ) {
					currentKeyword = line.trim();
				} else {
					currentKeyword = line.substring(parameterKeyPos, pos).trim();
					String restOfLine = line.substring(pos).trim();
					currentText.append(restOfLine);
				}
			} else {
				if( currentText.length() > 0 ) {
					currentText.append("\n");
				}
				currentText.append(line);
			}
		}
		
		// finish last text block
		if( currentKeyword != null ) {
			DefaultOdysseusScriptTextBlock block = new DefaultOdysseusScriptTextBlock();
			block.init(currentKeyword, currentText.toString(), Lists.newArrayList(textBlocks));
			
			textBlocks.add(block);
			currentKeyword = null;
			currentText = null;
		}
	}
	
	public List<IVisualOdysseusScriptTextBlock> getTextBlocks() {
		return textBlocks;
	}

	public String generateOdysseusScript() throws VisualOdysseusScriptException {
		StringBuilder resultingScript = new StringBuilder();
		for( IVisualOdysseusScriptTextBlock textBlock : textBlocks ) {
			String script = textBlock.generateOdysseusScript();
			
			resultingScript.append(script).append("\n");
		}
		
		return resultingScript.toString();
	}
}
