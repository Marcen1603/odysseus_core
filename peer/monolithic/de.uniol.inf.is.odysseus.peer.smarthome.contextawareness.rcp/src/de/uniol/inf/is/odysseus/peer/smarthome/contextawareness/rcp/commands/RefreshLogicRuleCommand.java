package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.views.LogicRuleShowViewPart;

public class RefreshLogicRuleCommand extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Optional<LogicRuleShowViewPart> optView = LogicRuleShowViewPart
				.getInstance();
		if (optView.isPresent()) {
			LogicRuleShowViewPart view = optView.get();
			view.refresh();
		}
		return null;
	}
}
