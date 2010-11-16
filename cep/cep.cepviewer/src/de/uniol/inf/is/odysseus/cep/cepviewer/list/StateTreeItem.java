package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.uniol.inf.is.odysseus.cep.cepviewer.testdata.StateMachine;
//import de.uniol.inf.is.odysseus.cep.cepviewer.testdata.StateMachineInstance;

import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;


public class StateTreeItem extends Item {

	private StateMachine stateMachine;
	private TreeItem item;
	private StateMachineInstance stateMachineInstance;

	public StateTreeItem(Tree tree, int style, StateMachine stateMachine) {
		super(tree, SWT.NONE);
		this.item = new TreeItem(tree, style);
		this.stateMachine = stateMachine;
	}

	public StateTreeItem(Tree tree, int style) {
		super(tree, SWT.NONE);
		this.item = new TreeItem(tree, style);
	}
	
	public StateTreeItem(Tree tree, int style, StateMachineInstance stateMachineInstance) {
		super(tree, SWT.NONE);
		this.item = new TreeItem(tree, style);
		this.stateMachineInstance = stateMachineInstance;
	}

	public StateTreeItem(TreeItem treeItem, int style, StateMachineInstance stateMachineInstance) {
		super(treeItem, SWT.NONE);
		this.item = new TreeItem(treeItem, style);
		this.stateMachineInstance = stateMachineInstance;
	}

	public int getItemCount() {
		return this.item.getItemCount();
	}

	@Override
	public void setImage(Image image) {
		this.item.setImage(image);
	}

	@Override
	public void setText(String string) {
		if (this.item.getItemCount() > 1) {
			this.item.setText(string + " (" + this.item.getItemCount() + ")");
		} else {
			this.item.setText(string);
		}
	}
	
	public StateMachineInstance getStateMachineInstance() {
		return this.stateMachineInstance;
	}

	public StateMachine getStateMachine() {
		return this.stateMachine;
	}

}
