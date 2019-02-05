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
package de.uniol.inf.is.odysseus.rcp.editor.text.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.rcp.editor.text.FileExecutorRegistry;
import de.uniol.inf.is.odysseus.rcp.editor.text.IFileExecutor;
import de.uniol.inf.is.odysseus.rcp.exception.ExceptionErrorDialog;
import de.uniol.inf.is.odysseus.rcp.queries.ParserClientUtil;

public class RunQueryCommand extends AbstractHandler implements IHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RunQueryCommand.class);

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        List<IFile> optFileToRun = getSelectedFiles();
        if (!optFileToRun.isEmpty()) {
            runFiles(optFileToRun);

        }
        else {
            IEditorPart editor = getScriptEditor();
            if (editor != null) {
            	IEditorInput editorInput = editor.getEditorInput();
            	if (editorInput instanceof IFileEditorInput) {
            		IFile file = ((IFileEditorInput) editorInput).getFile();
                    runFile(file);
            	}
            }
        }

        return null;
    }

    @Override
    public boolean isEnabled() {
        List<IFile> fileList = getSelectedFiles();

        for( IFile file : fileList ) {
        	if( !FileExecutorRegistry.hasFileExecutor(file.getFileExtension())) {
        		return false;
        	}
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    private static List<IFile> getSelectedFiles() {
        List<IFile> foundFiles = Lists.newArrayList();
        ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();

        if (selection instanceof IStructuredSelection) {
            List<Object> selectedObjects = ((IStructuredSelection) selection).toList();

            for (Object obj : selectedObjects) {
            	if (obj instanceof IFile) {
                    foundFiles.add((IFile) obj);
            	}
            }
        }

        return foundFiles;
    }


    private static IEditorPart getScriptEditor() {
        IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        return part;
    }

    private void runFile(IFile file) {
    	runFiles(Collections.singletonList(file));
    }


    private void runFiles(List<IFile> filesToRun) {
        try {
            for (IFile file : filesToRun) {
                execute(file);
            }
        }
        catch (Exception ex) {
            LOG.error("Exception during running query file", ex);
            ExceptionErrorDialog.open(new Status(IStatus.ERROR, IEditorTextParserConstants.PLUGIN_ID, "'Parsing and Executing Query' has encountered a problem.\n\nScript Execution Error: " + ex.getMessage(), ex), ex);
            // new ExceptionWindow(ex);
        }

    }


    private static void execute(final IFile scriptFile) {
        Job job = new Job("Parsing and Executing Query") {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                try {
                	IFileExecutor executor = FileExecutorRegistry.getFileExecutor(scriptFile.getFileExtension());
                	String text = readLinesFromFile(scriptFile);
                	executor.run(text, ParserClientUtil.createRCPContext(scriptFile));
               } catch (final Throwable ex) {
            	   //ex.printStackTrace();
                    LOG.error("Exception during running query file", ex);
                    ExceptionErrorDialog.open(new Status(IStatus.ERROR, IEditorTextParserConstants.PLUGIN_ID, "'Parsing and Executing Query' has encountered a problem.\n\nScript Execution Error: " + ex.getMessage(), ex), ex);
                    // return new Status(IStatus.ERROR,
                    // IEditorTextParserConstants.PLUGIN_ID,
                    // "Script Execution Error: " + ex.getMessage(), ex);
                }
                return Status.OK_STATUS;
            }
        };
        job.setUser(true);
        job.schedule();
    }

    private static String readLinesFromFile(IFile queryFile) throws CoreException, IOException {
        if (!queryFile.isSynchronized(IResource.DEPTH_ZERO)) {
            queryFile.refreshLocal(IResource.DEPTH_ZERO, null);
        }

        String lines = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(queryFile.getContents()));
        String line = br.readLine();
        while (line != null) {
            lines = lines + line;
            line = br.readLine();
            lines = lines + System.lineSeparator();
        }
        br.close();
        return lines;
    }
}
