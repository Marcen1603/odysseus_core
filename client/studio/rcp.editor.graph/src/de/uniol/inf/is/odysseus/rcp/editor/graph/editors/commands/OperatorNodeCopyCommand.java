package de.uniol.inf.is.odysseus.rcp.editor.graph.editors.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.Clipboard;

import de.uniol.inf.is.odysseus.rcp.editor.graph.editors.model.OperatorNode;

public class OperatorNodeCopyCommand extends Command{
		
		private List<OperatorNode> nodes = new ArrayList<>();	
		private Object oldContents;
		
		@Override
		public void execute() {
			this.oldContents = Clipboard.getDefault().getContents();
			Clipboard.getDefault().setContents(nodes);		
		}
		@Override
		public void undo() {	
			Clipboard.getDefault().setContents(oldContents);
		}	

		public List<OperatorNode> getNodes() {
			return nodes;
		}

		public void setNodes(List<OperatorNode> nodes) {
			this.nodes = nodes;
		}	

	}
