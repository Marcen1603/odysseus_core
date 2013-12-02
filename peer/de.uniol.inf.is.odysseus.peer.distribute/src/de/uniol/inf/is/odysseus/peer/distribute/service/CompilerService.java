package de.uniol.inf.is.odysseus.peer.distribute.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompiler;

public class CompilerService {

	private static final Logger LOG = LoggerFactory.getLogger(CompilerService.class);
	
	private static ICompiler compiler;
	
	public void bindCompiler(ICompiler cmp) {
		compiler = cmp;		
		LOG.debug("Compiler bound {}", cmp);
	}
	
	public void unbindCompiler(ICompiler cmp) {
		if(compiler == cmp) {
			compiler = null;			
			LOG.debug("Compiler unbound {}", cmp);
		}
	}
	
	public static ICompiler get() {
		return compiler;
	}
	
	public static boolean isBound() {
		return get() != null;
	}
}