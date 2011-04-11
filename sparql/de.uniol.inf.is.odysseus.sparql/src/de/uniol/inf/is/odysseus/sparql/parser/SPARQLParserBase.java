package de.uniol.inf.is.odysseus.sparql.parser;

import java.util.HashMap;
import java.util.Set;

import de.uniol.inf.is.odysseus.sparql.parser.ast.ParseException;
import de.uniol.inf.is.odysseus.sparql.parser.helper.BlankNode;

public class SPARQLParserBase {

	
	private int blankNodeCounter;
	
	private String baseURI;
	private HashMap<String, String> prefixes;
	
	public SPARQLParserBase(){
		this.baseURI = null;
		this.prefixes = new HashMap<String, String>();
		this.blankNodeCounter = 0;
	}

	public void setBaseURI(String base){
		this.baseURI = base;
	}

	public String getBaseURI(){
		return this.baseURI;
	}
	
	public void putPrefix(String prefix, String uri){
		this.prefixes.put(prefix, uri);
	}
	
	public Set<String> getPrefixes(){
		return this.prefixes.keySet();
	}
	
	public String getURIForPrefix(String prefix){
		return this.prefixes.get(prefix);
	}
	
	/**
	 * This method removes start and end 
	 * quotes from a string. So "hello" will be hello
	 * @param str
	 * @return
	 */
	public String stripQuotes(String str){
		int startIndex = str.indexOf("\"");
		int endIndex = str.lastIndexOf("\"");
		return str.substring(startIndex+1, endIndex);
	}
	
	public String unescapeStr(String s) throws ParseException{ 
		return unescape(s, '\\', false, 1, 1) ;
	}
	
	protected String unescapeStr(String s, int line, int column) throws ParseException{
		return unescape(s, '\\', false, line, column) ;
	}
	
	// Worker function
    private String unescape(String s, char escape, boolean pointCodeOnly, int line, int column) throws ParseException
    {
        int i = s.indexOf(escape) ;
        
        if ( i == -1 )
            return s ;
        
        // Dump the initial part straight into the string buffer
        StringBuffer sb = new StringBuffer(s.substring(0,i)) ;
        
        for ( ; i < s.length() ; i++ )
        {
            char ch = s.charAt(i) ;
            // Keep line and column numbers.
            switch (ch)
            {
                case '\n': 
                case '\r':
                    line++ ;
                    column = 1 ;
                    break ;
                default:
                    column++ ;
                    break ;
            }

            if ( ch != escape )
            {
                sb.append(ch) ;
                continue ;
            }
                
            // Escape
            if ( i >= s.length()-1 )
                throwParseException("Illegal escape at end of string", line, column) ;
            char ch2 = s.charAt(i+1) ;
            column = column+1 ;
            i = i + 1 ;
            
            // \\u and \\U
            if ( ch2 == 'u' )
            {
                // i points to the \ so i+6 is next character
                if ( i+4 >= s.length() )
                    throwParseException("\\u escape too short", line, column) ;
                int x = hex(s, i+1, 4, line, column) ;
                sb.append((char)x) ;
                // Jump 1 2 3 4 -- already skipped \ and u
                i = i+4 ;
                column = column+4 ;
                continue ;
            }
            if ( ch2 == 'U' )
            {
                // i points to the \ so i+6 is next character
                if ( i+8 >= s.length() )
                    throwParseException("\\U escape too short", line, column) ;
                int x = hex(s, i+1, 8, line, column) ;
                // Convert to UTF-16 codepoint pair.
                sb.append((char)x) ;
                // Jump 1 2 3 4 5 6 7 8 -- already skipped \ and u
                i = i+8 ;
                column = column+8 ;
                continue ;
            }
            
            // Are we doing just point code escapes?
            // If so, \X-anything else is legal as a literal "\" and "X" 
            
            if ( pointCodeOnly )
            {
                sb.append('\\') ;
                sb.append(ch2) ;
                i = i + 1 ;
                continue ;
            }
            
            // Not just codepoints.  Must be a legal escape.
            char ch3 = 0 ;
            switch (ch2)
            {
                case 'n': ch3 = '\n' ;  break ; 
                case 't': ch3 = '\t' ;  break ;
                case 'r': ch3 = '\r' ;  break ;
                case 'b': ch3 = '\b' ;  break ;
                case 'f': ch3 = '\f' ;  break ;
                case '\'': ch3 = '\'' ; break ;
                case '\"': ch3 = '\"' ; break ;
                case '\\': ch3 = '\\' ; break ;
                default:
                    throwParseException("Unknown escape: \\"+ch2, line, column) ;
            }
            sb.append(ch3) ;
        }
        return sb.toString() ;
    }
	
    // Line and column that started the escape
    private int hex(String s, int i, int len, int line, int column) throws ParseException
    {
        if ( i+len >= s.length() )
        {
            
        }
        int x = 0 ;
        for ( int j = i ; j < i+len ; j++ )
        {
           char ch = s.charAt(j) ;
           column++ ;
           int k = 0  ;
           switch (ch)
           {
               case '0': k = 0 ; break ; 
               case '1': k = 1 ; break ;
               case '2': k = 2 ; break ;
               case '3': k = 3 ; break ;
               case '4': k = 4 ; break ;
               case '5': k = 5 ; break ;
               case '6': k = 6 ; break ;
               case '7': k = 7 ; break ;
               case '8': k = 8 ; break ;
               case '9': k = 9 ; break ;
               case 'A': case 'a': k = 10 ; break ;
               case 'B': case 'b': k = 11 ; break ;
               case 'C': case 'c': k = 12 ; break ;
               case 'D': case 'd': k = 13 ; break ;
               case 'E': case 'e': k = 14 ; break ;
               case 'F': case 'f': k = 15 ; break ;
               default:
                   throwParseException("Illegal hex escape: "+ch, line, column) ;
           }
           x = (x<<4)+k ;
        }
        return x ;
    }
    
    
    protected void throwParseException(String msg, int line, int column) throws ParseException
    {
        throw new ParseException("Line " + line + ", column " + column + ": " + msg) ;
    }
    
    
    public BlankNode createBlankNode(){
    	return new BlankNode("_b" + this.blankNodeCounter++);
    }
//    protected Var createVariable(String s, int line, int column)
//    {
//        s = s.substring(1) ; // Drop the marker
//        
//        // This is done by the parser input stream nowadays.
//        //s = unescapeCodePoint(s, line, column) ;
//        // Check \ u did not put in any illegals. 
//        return Var.alloc(s) ;
//    }
}
