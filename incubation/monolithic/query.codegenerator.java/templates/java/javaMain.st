javaMain(importList,osgiBindCode,bodyCode,startCode)::=<<
package main;

$importList: {importClass| 
import $importClass$;
}$
 
class Main {
	public static void main(String[] args) throws IOException {

		$osgiBindCode$
	
		$bodyCode$
	
		$startCode$
	}
}

>>