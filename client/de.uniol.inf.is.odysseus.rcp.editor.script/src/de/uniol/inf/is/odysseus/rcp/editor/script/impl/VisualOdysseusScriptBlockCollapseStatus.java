package de.uniol.inf.is.odysseus.rcp.editor.script.impl;

import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import java.util.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.rcp.editor.script.IVisualOdysseusScriptBlock;

public final class VisualOdysseusScriptBlockCollapseStatus {

	private final List<Boolean> collapseList = Lists.newArrayList();
	private final Map<IVisualOdysseusScriptBlock, Integer> indexMap = Maps.newHashMap();

	public void prepareSize(int size) {

		if (size > collapseList.size()) {
			
			// increase list preserving old values
			while (collapseList.size() < size) {
				collapseList.add(Boolean.FALSE);
			}
		}
	}

	public void setIndex(IVisualOdysseusScriptBlock block, int index) {
		Objects.requireNonNull(block, "block must not be null!");
		// Preconditions.checkArgument(index >= 0, "Index has to be positive or zero");

		indexMap.put(block, index);
	}

	public void setStatus(IVisualOdysseusScriptBlock block, boolean b) {
		Objects.requireNonNull(block, "block must not be null!");

		int index = indexMap.get(block);
		collapseList.set(index, b);
	}

	public boolean getStatus(IVisualOdysseusScriptBlock visualBlock) {
		Objects.requireNonNull(visualBlock, "visualBlock must not be null!");

		return collapseList.get(indexMap.get(visualBlock));
	}

	public void moveUp(IVisualOdysseusScriptBlock visualBlock) {
		Objects.requireNonNull(visualBlock, "visualBlock must not be null!");

		int index = indexMap.get(visualBlock);
		if (index == 0) {
			return;
		}

		Optional<IVisualOdysseusScriptBlock> optBlock = getBlockAtIndex(index - 1);
		if (optBlock.isPresent()) {
			IVisualOdysseusScriptBlock block = optBlock.get();
			
			// swap indices in map
			indexMap.put(visualBlock, index - 1);
			indexMap.put(block, index);

			// swap indices in list
			swapCollapseList(index, index - 1);
			
		} else {
			// should not happen here
			throw new RuntimeException();
		}
	}

	public void moveDown(IVisualOdysseusScriptBlock visualBlock) {
		Objects.requireNonNull(visualBlock, "visualBlock must not be null!");

		int index = indexMap.get(visualBlock);
		if (index == indexMap.size() - 1) {
			return;
		}

		Optional<IVisualOdysseusScriptBlock> optBlock = getBlockAtIndex(index + 1);
		if (optBlock.isPresent()) {
			IVisualOdysseusScriptBlock block = optBlock.get();
			
			// swap indices in map
			indexMap.put(visualBlock, index + 1);
			indexMap.put(block, index);

			// swap indices in list
			swapCollapseList(index, index + 1);
		} else {
			// should not happen here
			throw new RuntimeException();
		}
	}

	public void dispose(IVisualOdysseusScriptBlock visualBlock) {
		Objects.requireNonNull(visualBlock, "visualBlock must not be null!");

		int index = indexMap.remove(visualBlock);
		collapseList.remove(index);

		// update other indices to "move up"
		for (IVisualOdysseusScriptBlock block : indexMap.keySet()) {
			if (indexMap.get(block) > index) {
				indexMap.put(block, indexMap.get(block) - 1);
			}
		}
	}

	private Optional<IVisualOdysseusScriptBlock> getBlockAtIndex(int index) {
		for (IVisualOdysseusScriptBlock block : indexMap.keySet()) {
			if (indexMap.get(block) == index) {
				return Optional.of(block);
			}
		}

		return Optional.absent();
	}

	private void swapCollapseList(int indexA, int indexB) {
		Boolean temp = collapseList.get(indexA);
		collapseList.set(indexA, collapseList.get(indexB));
		collapseList.set(indexB, temp);
	}

	public void clearBlocks() {
		// used when visual blocks are recreated
		indexMap.clear();
	}


}
