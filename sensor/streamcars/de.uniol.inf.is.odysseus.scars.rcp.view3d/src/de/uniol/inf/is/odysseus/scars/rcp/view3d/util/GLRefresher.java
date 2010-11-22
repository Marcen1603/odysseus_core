package de.uniol.inf.is.odysseus.scars.rcp.view3d.util;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.widgets.Display;

/**
 * Verwaltet und Steuert das permanente Rendern von <code>GLScene</code>s. Dabei
 * muss das Rendern innerhalb des SWT-Threads erfolgen. Es sollte wie folgt
 * verwendet werden:
 * <p>
 * <code>
 * Display.getCurrent().asyncExec(new GLRefresher(glScene));
 * </code>
 * <p>
 * Dies muss nur ein einziges Mal passieren. Nach einem Aufruf von
 * <code>run</code> sorgt <code>GLRefresher</code> dafür, dass es
 * schnellstmöglich wieder aufgerufen wird.
 * 
 * @author Timo Michelsen
 * 
 */
public class GLRefresher implements Runnable {

	private GLScene scene;

	/**
	 * Konstruktor. Erstellt eine neue <code>GLRefresher</code>-Instanz. Die
	 * neue Instanz steuert die übergebene <code>GLScene</code>.
	 * 
	 * @param scene
	 *            Die zu steuerende <code>GLScene</code>. Sie darf nicht null
	 *            sein.
	 */
	public GLRefresher(GLScene scene) {
		setScene(scene);
	}

	@Override
	public void run() {
		if (!getScene().isDisposed()) {
			getScene().render();
			Display.getCurrent().asyncExec(this);
		}
	}

	/**
	 * Liefert die <code>GLScene</code>, welche von diesem <code>GLRefresher</code>
	 * verwaltet wird, zurück.
	 * 
	 * @return Die verwaltete <code>GLScene</code>
	 */
	public final GLScene getScene() {
		return scene;
	}

	// setzt die GLScene. Prüft auf null-Argument.
	private void setScene(GLScene scene) {
		Assert.isNotNull(scene);
		this.scene = scene;
	}

}
