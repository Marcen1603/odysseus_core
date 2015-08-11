package de.uniol.inf.is.odysseus.query.transformation.java.analysis;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import de.uniol.inf.is.odysseus.query.transformation.compiler.TransformationParameter;
import de.uniol.inf.is.odysseus.query.transformation.java.filewriter.FileHelper;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.OdysseusIndex;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.ReadJavaImports;

public class JavaImportAnalyse {
	
	private Set<String> importList = new HashSet<String>();
	
	private Set<String> clazzToSearchList = new HashSet<String>();
	
	
	@SuppressWarnings("unchecked")
	public Set<String> analyseCodeForImport(TransformationParameter parameter, String code){
		
		FileHelper fileHelper = new FileHelper("Temp.java",parameter.getTempDirectory());

		fileHelper.writeToFile(getJavaHead()+code+getJavaBottom());
	
		 FileInputStream in;
		 CompilationUnit cu;
		try {
			in = new FileInputStream(parameter.getTempDirectory()+"\\Temp.java");
			   // parse the file
            cu = JavaParser.parse(in);
            
            in.close();
            
            new TypeVisitor().visit(cu, null);
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		if(!clazzToSearchList.isEmpty()){
			for(String clazz : clazzToSearchList){
				String importString ="";
				
				for(Path file : OdysseusIndex.getInstance().getOdysseusIndex()){
					if(file.getFileName().toString().equals(clazz+".java")){
						importString = ReadJavaImports.getImportForFile(file.toString());
						importList.add(importString);
					}
				
				}
			}
			
		}
		
		return importList;
	}
	
	
	private String getJavaHead(){
		StringBuilder code = new StringBuilder();
		code.append("\n");
		code.append("\n");
		code.append("public class Temp {");		
		code.append("public void test(){");
		code.append("\n");
		code.append("\n");
		code.append("\n");
		
		return code.toString();
	}
	
	private String getJavaBottom(){
	StringBuilder code = new StringBuilder();
		code.append("\n");
		code.append("}");
		code.append("\n");
		code.append("}");
		code.append("\n");
		
		return code.toString();
	}


    @SuppressWarnings("rawtypes")
	private class TypeVisitor extends VoidVisitorAdapter  {

    	//TODO 
    	@Override
    	public void visit(VariableDeclarationExpr n, Object arg)
    	{      
    	    List <VariableDeclarator> myVars = n.getVars();
    	    System.out.println(n.getType().toString());
    	    
    	    clazzToSearchList.add(n.getType().toString());
    	        for (VariableDeclarator test: myVars){
    	        	
    	          if(!test.getInit().getChildrenNodes().isEmpty()){
    	        	  for(Node tempNode : test.getInit().getChildrenNodes()){
    	        		  
    	        		  if(!tempNode.getChildrenNodes().isEmpty()){
    	        			  System.out.println(tempNode.getChildrenNodes().get(0).toString());
    	        			  clazzToSearchList.add(tempNode.getChildrenNodes().get(0).toString());
    	        		  } else{
    	        			  if(!tempNode.toString().equals("null") && !tempNode.toString().matches(".*"+"\\\""+".*"+"\\\""+".*")){
    	        				  System.out.println(tempNode.toString());
        	        			  clazzToSearchList.add(tempNode.toString());
    	        			  }
    	        			 
    	        		  }
    	        	  }  
    	          }
    	        	
    	        
    	        	if(!test.getInit().toString().contains(".")){
    	        		String temp = test.getInit().toString();
    	        		  	        		
    	        		//remove ne and () clean up
    	        		temp =  temp.replace("new","");  
    	        		
    	        		if(temp.matches(".*"+"\\<"+".*"+"\\>"+".*")){
    	        			clazzToSearchList.add(test.getInit().getChildrenNodes().get(0).getChildrenNodes().get(0).toString());
    	        		}
    	        		
    	        		temp = temp.replaceAll("\\("+".*"+"\\)", "");
    	        		
    	        		clazzToSearchList.add(temp);
    	        	}
    	     }
    	}
    }
}
