/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.offis.scampi.stack;

import de.offis.scampi.stack.encryption.IEncryption;
import de.offis.scampi.stack.control.ControlModules;

/**
 *
 * @author Claas
 */
public class ProtocolObject {
    
    private IInterpreter interpreter = null;
    private IEncryption encryption = null;
    private ITranslator translator = null;
    private ITypeChecker typeChecker = null;
    private IProcessor processor = null;
    private IBuilder builder = null;
    private ControlModules controlModules = new ControlModules();
    private Object content = null;

    public void setContent(Object content) {
        this.content = content;
    }

    public Object getContent() {
        return content;
    }

    public IEncryption getEncryption() {
        return encryption;
    }

    public IInterpreter getInterpreter() {
        return interpreter;
    }

    public IProcessor getProcessor() {
        return processor;
    }

    public ITranslator getTranslator() {
        return translator;
    }

    public IBuilder getBuilder() {
        return builder;
    }

    public ITypeChecker getTypeChecker() {
        return typeChecker;
    }

    public ControlModules getControlModules() {
        return controlModules;
    }

    public void setControlModules(ControlModules controlModules) {
        this.controlModules = controlModules;
    }

    public ProtocolObject(IProcessor processor, IInterpreter interpreter, ITranslator translator, IBuilder builder, ITypeChecker typeChecker) throws Exception
    {
        if(processor == null || interpreter == null || translator == null || typeChecker == null)
            throw new Exception("Illegeal Arguments when Building SCAIObject");

        this.processor = processor;
        this.interpreter = interpreter;
        this.translator = translator;
        this.builder = builder;
        this.typeChecker = typeChecker;
    }

    public ProtocolObject(IProcessor processor, IInterpreter interpreter, ITranslator translator, IBuilder builder, ITypeChecker typeChecker, IEncryption encryption) throws Exception
    {
        this(processor, interpreter, translator, builder, typeChecker);
        this.encryption = encryption;
    }

}
