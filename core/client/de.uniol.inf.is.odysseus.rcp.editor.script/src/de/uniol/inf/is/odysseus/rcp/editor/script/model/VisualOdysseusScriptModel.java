package de.uniol.inf.is.odysseus.rcp.editor.script.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.editor.script.IOdysseusScriptTransformRule;
import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.OdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.VisualOdysseusScriptException;
import de.uniol.inf.is.odysseus.rcp.editor.script.blocks.DefaultOdysseusScriptBlock;
import de.uniol.inf.is.odysseus.rcp.editor.script.rules.TransformRuleRegistry;

public class VisualOdysseusScriptModel {

	private final List<IVisualOdysseusScriptBlock> visualTextBlocks = Lists.newArrayList();

	public VisualOdysseusScriptModel() {
		// nothing to do
	}
	
	public void parse( InputStream inputStream ) throws VisualOdysseusScriptException {
		try {
			parse(readLines(inputStream));
		} catch (IOException e) {
			throw new VisualOdysseusScriptException("Could not read odysseus script from input stream", e);
		}
	}
	
	public void parse( String completeScript ) throws VisualOdysseusScriptException {
		parse(toLines(completeScript));
	}

	private static List<String> readLines(InputStream inputStream) throws IOException {
		List<String> lines = Lists.newArrayList();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			String line = reader.readLine();
			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}
		}
		return lines;
	}
	
	public void parse(List<String> odysseusScriptTextLines) throws VisualOdysseusScriptException {
		Preconditions.checkNotNull(odysseusScriptTextLines, "odysseusScriptTextLines must not be null!");

		List<OdysseusScriptBlock> scriptBlocks = determineOdysseusScriptBlocks(odysseusScriptTextLines);
		List<OdysseusScriptBlock> scriptBlocksCopy = Lists.newArrayList(scriptBlocks);
		Collection<IOdysseusScriptTransformRule> rules = TransformRuleRegistry.getInstance().getRules();

		Map<OdysseusScriptBlock, IVisualOdysseusScriptBlock> transformResultMap = Maps.newHashMap();
		Map<OdysseusScriptBlock, IVisualOdysseusScriptBlock> otherTransformMap = Maps.newHashMap();
		while (!scriptBlocks.isEmpty()) {

			// 1. determine rules which can be executed
			// respecting to use rules which transforms script blocks beginning
			// at the script
			ImmutableList<OdysseusScriptBlock> immutableBlocks = ImmutableList.copyOf(scriptBlocks);

			int minIndex = -1;
			int priority = -1;
			IOdysseusScriptTransformRule selectedRule = null;
			List<OdysseusScriptBlock> selectedBlocks = null;

			for (IOdysseusScriptTransformRule rule : rules) {
				List<OdysseusScriptBlock> blocksPossibleToTransform = rule.determineTransformableBlocks(immutableBlocks);
				if (blocksPossibleToTransform == null || blocksPossibleToTransform.isEmpty()) {
					// rule not executable
					continue;
				}

				int blockIndex = determineLowestIndex(blocksPossibleToTransform, scriptBlocksCopy);
				if (blockIndex == -1) {
					throw new VisualOdysseusScriptException("Failure to transform odysseus script with rule " + rule.getName());
				}

				if (minIndex == -1 || blockIndex < minIndex || (blockIndex == minIndex && rule.getPriority() > priority)) {
					minIndex = blockIndex;
					priority = rule.getPriority();
					selectedRule = rule;
					selectedBlocks = blocksPossibleToTransform;
				}
			}

			// 2. execute rule and save result in temporary map
			if (selectedRule != null && selectedBlocks != null) {
				List<OdysseusScriptBlock> scriptBlocksBefore = Lists.newArrayList(scriptBlocks);
				IVisualOdysseusScriptBlock visualTextBlock = selectedRule.transform(scriptBlocks, selectedBlocks);
				if (visualTextBlock != null) {
					if (scriptBlocksBefore.size() == scriptBlocks.size()) {
						// rule did not change anything! error
						throw new VisualOdysseusScriptException("Odysseus script transformation rule '" + selectedRule.getName() + "' didn't change script blocks!");
					}

					for (OdysseusScriptBlock selectedBlock : selectedBlocks) {
						transformResultMap.put(selectedBlock, visualTextBlock);
					}
					for (OdysseusScriptBlock scriptBlockBefore : scriptBlocksBefore) {
						if (!scriptBlocks.contains(scriptBlockBefore)) {
							// rule removed other blocks! mark them!
							otherTransformMap.put(scriptBlockBefore, visualTextBlock);
						}
					}

				} else {
					throw new VisualOdysseusScriptException("Odysseus script transformation rule '" + selectedRule.getName() + "' returned null!");
				}
			} else {
				// no rule executable anymore
				break;
			}
		}

		if (!scriptBlocks.isEmpty()) {
			// using fallback to show the remaining script blocks however
			for (OdysseusScriptBlock scriptBlock : scriptBlocks) {
				DefaultOdysseusScriptBlock defBlock = new DefaultOdysseusScriptBlock(scriptBlock.getKeyword(), scriptBlock.getText());
				transformResultMap.put(scriptBlock, defBlock);
			}
			scriptBlocks.clear();
		}

		// At this point, we have a map (transformResultMap), which describes,
		// which
		// script block is transformed to which visual odysseus script text
		// block.
		// No we can build the resulting list of visual odysseus script text
		// blocks
		// according to the order in scriptBlocksCopy
		visualTextBlocks.clear();

		DefaultOdysseusScriptBlock currentDefBlock = null;
		while (!scriptBlocksCopy.isEmpty()) {
			OdysseusScriptBlock topBlock = scriptBlocksCopy.remove(0);
			IVisualOdysseusScriptBlock visualBlock = transformResultMap.get(topBlock);

			// merge default blocks together if possible
			if (visualBlock instanceof DefaultOdysseusScriptBlock) {
				if (currentDefBlock == null) {
					currentDefBlock = (DefaultOdysseusScriptBlock) visualBlock;
				} else {
					currentDefBlock.integrate(topBlock.getKeyword(), topBlock.getText());
				}
				continue;
			}

			if (currentDefBlock != null) {
				visualTextBlocks.add(currentDefBlock);
				currentDefBlock = null;
			}

			if (visualBlock != null) {
				visualTextBlocks.add(visualBlock);

				List<OdysseusScriptBlock> blocksToRemoveFromMap = Lists.newArrayList();
				for (Entry<OdysseusScriptBlock, IVisualOdysseusScriptBlock> transformMapEntry : transformResultMap.entrySet()) {
					if (transformMapEntry.getValue() == visualBlock) {
						blocksToRemoveFromMap.add(transformMapEntry.getKey());
					}
				}

				for (OdysseusScriptBlock blockToRemove : blocksToRemoveFromMap) {
					scriptBlocksCopy.remove(blockToRemove);
					transformResultMap.remove(blockToRemove);
				}
			} else {
				if (!otherTransformMap.containsKey(topBlock)) {
					throw new VisualOdysseusScriptException("OdysseusScript block '" + topBlock + "' is left over during transformation");
				}
			}
		}

		if (currentDefBlock != null) {
			visualTextBlocks.add(currentDefBlock);
			currentDefBlock = null;
		}

	}

	public List<IVisualOdysseusScriptBlock> getVisualTextBlocks() {
		return visualTextBlocks;
	}

	private static <T> int determineLowestIndex(List<T> toFind, List<T> listToTraverse) {
		int minIndex = -1;

		for (T block : toFind) {
			int index = listToTraverse.indexOf(block);
			if (minIndex == -1 || index < minIndex) {
				minIndex = index;
			}
		}

		return minIndex;
	}

	public String generateOdysseusScript() throws VisualOdysseusScriptException {
		StringBuilder resultingScript = new StringBuilder();
		for (IVisualOdysseusScriptBlock textBlock : visualTextBlocks) {
			String script = textBlock.generateOdysseusScript();

			if (script == null) {
				throw new VisualOdysseusScriptException("Visual text block " + textBlock + " returned null string!");
			}

			resultingScript.append(script).append("\n");
		}

		return resultingScript.toString();
	}

	private static List<OdysseusScriptBlock> determineOdysseusScriptBlocks(List<String> odysseusScriptTextLines) {
		String currentKeyword = null;
		StringBuilder currentText = new StringBuilder();

		List<OdysseusScriptBlock> scriptBlocks = Lists.newArrayList();
		for (String odysseusScriptTextLine : odysseusScriptTextLines) {
			String line = odysseusScriptTextLine;

			int parameterKeyPos = line.indexOf("#");
			int commentPos = line.indexOf("///");
			if (parameterKeyPos != -1 && (commentPos == -1 || (commentPos > parameterKeyPos))) {

				// finish current text block
				if (currentKeyword != null) {
					scriptBlocks.add(new OdysseusScriptBlock(currentKeyword, currentText.toString()));

					currentKeyword = null;
					currentText = new StringBuilder();
				}

				int pos = line.indexOf(" ", parameterKeyPos + 1);
				if (pos == -1) {
					currentKeyword = line.trim();
				} else {
					currentKeyword = line.substring(parameterKeyPos, pos).trim();
					String restOfLine = line.substring(pos).trim();
					currentText.append(restOfLine);
				}
			} else {
				if (currentText.length() > 0) {
					currentText.append("\n");
				}
				currentText.append(line);
			}
		}

		// finish last text block
		if (currentKeyword != null) {
			scriptBlocks.add(new OdysseusScriptBlock(currentKeyword, currentText.toString()));
		}

		return scriptBlocks;
	}

	public void dispose() {
		for (IVisualOdysseusScriptBlock block : visualTextBlocks) {
			block.dispose();
		}
	}

	public void dispose(IVisualOdysseusScriptBlock visualBlock) {
		Preconditions.checkNotNull(visualBlock, "visualBlock must not be null!");

		if (visualTextBlocks.contains(visualBlock)) {
			visualBlock.dispose();
			visualTextBlocks.remove(visualBlock);
		}
	}

	public boolean moveUp(IVisualOdysseusScriptBlock visualBlock) throws VisualOdysseusScriptException {
		Preconditions.checkNotNull(visualBlock, "visualBlock must not be null!");

		int index = visualTextBlocks.indexOf(visualBlock);
		if (index == -1 || index == 0) {
			return false;
		}

		IVisualOdysseusScriptBlock temp = visualTextBlocks.get(index - 1);
		visualTextBlocks.set(index - 1, visualBlock);
		visualTextBlocks.set(index, temp);

		tryRecreateVisualBlocks();
		
		return true;
	}

	
	public boolean moveDown(IVisualOdysseusScriptBlock visualBlock) throws VisualOdysseusScriptException {
		Preconditions.checkNotNull(visualBlock, "visualBlock must not be null!");

		int index = visualTextBlocks.indexOf(visualBlock);
		if( index == -1 || index == visualTextBlocks.size() - 1 ) {
			return false;
		}
		
		IVisualOdysseusScriptBlock temp = visualTextBlocks.get(index + 1);
		visualTextBlocks.set(index + 1, visualBlock);
		visualTextBlocks.set(index, temp);
		
		tryRecreateVisualBlocks();
		
		return true;
	}

	private void tryRecreateVisualBlocks() throws VisualOdysseusScriptException {
		String tempScript = generateOdysseusScript();
		dispose();
		parse(tempScript);
	}

	private static List<String> toLines(String tempScript) {
		List<String> lines = Lists.newArrayList();

		try (Scanner scanner = new Scanner(tempScript)) {
			while (scanner.hasNextLine()) {
				lines.add(scanner.nextLine());
			}
		}

		return lines;
	}

	public void addBlock(IVisualOdysseusScriptBlock newBlock) {
		Preconditions.checkNotNull(newBlock, "newBlock must not be null!");

		visualTextBlocks.add(newBlock);
	}


}
