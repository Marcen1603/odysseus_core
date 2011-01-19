package de.uniol.inf.is.odysseus.datamining.classification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TreePrinter {

	private void printNode(HoeffdingNode<?> node, int depth) {
		if (depth > 0) {
			for (int i = 0; i < depth; i++) {
				System.out.print(" |");
			}

		}
		System.out.print("-- ");
		if(depth > 0 ){
			System.out.print(node.getTestValue());
		}
		System.out.print(" class: " + node.getMajorityClass());
		System.out.print(" " + node.getRelativeFrequancy());		
		if(!node.isLeaf()){
		System.out.print(" split: " + node.getTestAttribute());
		}
		System.out.println();
	}

	public void printTree(HoeffdingNode<?> root) {
		System.out.println(printTree(root, 0) +" nodes");
	}

	private int printTree(HoeffdingNode<?> node, int depth) {
		printNode(node, depth);
		int printedNodes = 1;
		if (!node.isLeaf()) {
			List<HoeffdingNode<?>> children = new ArrayList<HoeffdingNode<?>>(node.getChildren());
			Collections.sort(children);
			for (HoeffdingNode<?> child : children) {
				printedNodes += printTree(child, depth + 1);
			}
		}
		return printedNodes;
	}

}
