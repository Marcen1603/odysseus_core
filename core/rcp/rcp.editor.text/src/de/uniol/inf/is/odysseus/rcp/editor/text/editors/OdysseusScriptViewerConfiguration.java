/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.rcp.editor.text.editors;

import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import de.uniol.inf.is.odysseus.rcp.editor.text.editors.coloring.OdysseusScriptReconcilerStrategy;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.completion.OdysseusScriptCompletionProcessor;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.formatting.DefaultFormattingStrategy;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.formatting.OdysseusScriptContentFormatter;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.partition.IOdysseusScriptPartition;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.partition.OdysseusScriptPartitionRegsitry;

/**
 * @author Dennis Geesen
 * 
 */
public class OdysseusScriptViewerConfiguration extends SourceViewerConfiguration {

	private OdysseusScriptEditor editor;

	public OdysseusScriptViewerConfiguration(OdysseusScriptEditor editor) {
		this.editor = editor;
	}

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		List<String> types = OdysseusScriptPartitionRegsitry.getAllPartionNamesAsList();
		types.add(IDocument.DEFAULT_CONTENT_TYPE);
		return types.toArray(new String[0]);
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		for (IOdysseusScriptPartition osp : OdysseusScriptPartitionRegsitry.getParitions()) {
			DefaultDamagerRepairer dr = new DefaultDamagerRepairer(osp.getReconcilerScanner());
			reconciler.setDamager(dr, osp.getPartitionTokenName());
			reconciler.setRepairer(dr, osp.getPartitionTokenName());
		}

		// and the default one
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(new RuleBasedScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		return reconciler;
	}

	@Override
	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		OdysseusScriptReconcilerStrategy strategy = new OdysseusScriptReconcilerStrategy(this.editor);
		MonoReconciler reconciler = new MonoReconciler(strategy, false);
		reconciler.setProgressMonitor(new NullProgressMonitor());
		reconciler.setDelay(500);
		return reconciler;
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant ca = new ContentAssistant();
		IContentAssistProcessor cp = new OdysseusScriptCompletionProcessor();
		for (IOdysseusScriptPartition osp : OdysseusScriptPartitionRegsitry.getParitions()) {
			ca.setContentAssistProcessor(cp, osp.getPartitionTokenName());
		}
		ca.setContentAssistProcessor(cp, IDocument.DEFAULT_CONTENT_TYPE);
		ca.enableAutoActivation(true);
		ca.setInformationControlCreator(getInformationControlCreator(sourceViewer));
		return ca;
	}

	@Override
	public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
		OdysseusScriptContentFormatter formatter = new OdysseusScriptContentFormatter();
		formatter.enablePartitionAwareFormatting(true);
		for (IOdysseusScriptPartition osp : OdysseusScriptPartitionRegsitry.getParitions()) {			
			IFormattingStrategy fs = osp.getFormattingStrategy();
			formatter.setFormattingStrategy(fs, osp.getPartitionTokenName());
		}
		formatter.setFormattingStrategy(new DefaultFormattingStrategy(), IDocument.DEFAULT_CONTENT_TYPE);		
		return formatter;
	}

}
