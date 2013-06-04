package de.uniol.inf.is.odysseus.p2p_new.distribute.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.planmanagement.ICompiler;

/**
 * Singleton class for the single, static referenced {@link ICompiler}. <br />
 * The referenced {@link ICompiler} can be accessed by calling {@link #get()}.
 * @author Michael Brand
 */
public class CompilerService {

	/**
	 * The {@link Logger} instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(CompilerService.class);
	
	/**
	 * The referenced {@link ICompiler}.
	 * @see #bindCompiler(ICompiler)
	 * @see #unbindCompiler(ICompiler)
	 */
	private static ICompiler compiler;
	
	/**
	 * Binds the referenced {@link ICompiler}. <br />
	 * Called by OSGI-DS.
	 * @see #unbindCompiler(ICompiler)
	 * @param cmp An instance of an {@link ICompiler} implementation.
	 */
	public void bindCompiler(ICompiler cmp) {
		
		compiler = cmp;		
		LOG.debug("Compiler bound {}", cmp);
		
	}
	
	/**
	 * Unbinds an referenced {@link ICompiler}, if <code>cmp</code> is the binded one. <br />
	 * Called by OSGI-DS.
	 * @see #bindCompiler(ICompiler)
	 * @param cmp An instance of an {@link ICompiler} implementation.
	 */
	public void unbindCompiler(ICompiler cmp) {
		
		if(compiler == cmp) {
			
			compiler = null;			
			LOG.debug("Compiler unbound {}", cmp);
			
		}
		
	}
	
	/**
	 * Returns the referenced {@link ICompiler}.
	 */
	public static ICompiler get() {
		
		return compiler;
		
	}
	
	/**
	 * Determines, if a referenced {@link ICompiler} is bound.
	 * @see #bindCompiler(ICompiler)
	 * @return <code>{@link #get()} != null</code>
	 */
	public static boolean isBound() {
		
		return get() != null;
		
	}
	
}