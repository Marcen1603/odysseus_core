package de.offis.salsa.obsrec;

import java.util.logging.Logger;

import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;

import de.offis.salsa.obsrec.annotations.ObjectRule;
import de.offis.salsa.obsrec.annotations.ScanSegmentation;
import de.offis.salsa.obsrec.datasegm.IScanSegmentation;
import de.offis.salsa.obsrec.objrules.IObjectRule;

class AnnotationListener implements ClassAnnotationDiscoveryListener {
	private Logger log = Logger.getLogger("Objektwelt.AnnotationListener");
	
	
	private Objectworld objektwelt;
	
	public AnnotationListener(Objectworld objektwelt) {
		this.objektwelt = objektwelt;
	}

	@Override
	public String[] supportedAnnotations() {
		return new String[]{
				ObjectRule.class.getName(),
				ScanSegmentation.class.getName()
				};
	}
	
	@Override
	public void discovered(String clazz, String annotation) {
		try {
			if(annotation.equals(ObjectRule.class.getName())){
				Object objRule = Class.forName(clazz).newInstance();
				objektwelt.registerObjectRule((IObjectRule) objRule);
//				log.info(msg);
			} else if (annotation.equals(ScanSegmentation.class.getName())) {
				Object scanSeg = Class.forName(clazz).newInstance();
//				Annotation anno =  scanSeg.getClass().getAnnotation(Class.forName(annotation));
//				String name = ((ObjectRule) anno).name();
				objektwelt.registerScanSegmentation((IScanSegmentation) scanSeg);
			}
			
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
