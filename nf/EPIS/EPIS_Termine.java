import java.lang.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.reflect.*;
import minerva.MinervaParseException;
import minerva.io.MinervaWriter;
import editor.*;


public class EPIS_Termine extends EditorDocument {

  /* *************************
   *                         *
   *      Constructors       *
   *                         *
   ************************* */ 

  public EPIS_Termine() {
    super();
  }

  public EPIS_Termine(Reader in) throws IOException {
    super(in);
  }

  private boolean consume(String pattern) throws MinervaParseException {
    if (!match(pattern))
      throw new MinervaParseException(this,pattern);
    return true;
  }

  static private EPIS_Termine $termin = new EPIS_Termine();
  static private EPIS_Termine $vortragsinfo = new EPIS_Termine();
  static private EPIS_Termine $c = new EPIS_Termine();
  static private EPIS_Termine $b = new EPIS_Termine();
  static private EPIS_Termine $EPIS_Termine = new EPIS_Termine();
  static private EPIS_Termine $a = new EPIS_Termine();
  static private EPIS_Termine $dateinfo = new EPIS_Termine();

  static private Document result = new EditorDocument();
  static private PrintWriter out = new PrintWriter(new MinervaWriter(result));

  private boolean bool$termin0;
  private int startPosition$termin;

  static public String StartTuple = "[[";
  static public String Sep        = "][";
  static public String EndTuple   = "]]\n";

  static public void ReInit() {
    $termin = new EPIS_Termine();
    $vortragsinfo = new EPIS_Termine();
    $c = new EPIS_Termine();
    $b = new EPIS_Termine();
    $EPIS_Termine = new EPIS_Termine();
    $a = new EPIS_Termine();
    $dateinfo = new EPIS_Termine();

    result = new EditorDocument();
    out = new PrintWriter(new MinervaWriter(result));
  }

  static private void Parsetermin(EPIS_Termine Doc) throws MinervaParseException

  {
    Doc.startPosition$termin = Doc.getPosition();
    if ((Doc.bool$termin0=Doc.match("<table[ ]border=\"0\"[ ]cellpadding=\"0\"[ ]cellspacing=\"0\"[ ]width=\"400\">*\n*\n")));
    termin(Doc);
  }

  static private void Parsevortragsinfo(EPIS_Termine Doc) throws MinervaParseException

  { vortragsinfo(Doc); }

  static private void Parsec(EPIS_Termine Doc) throws MinervaParseException

  { c(Doc); }

  static private void Parseb(EPIS_Termine Doc) throws MinervaParseException

  { b(Doc); }

  static private void ParseEPIS_Termine(EPIS_Termine Doc) throws MinervaParseException

  { EPIS_Termine(Doc); }

  static private void Parsea(EPIS_Termine Doc) throws MinervaParseException

  { a(Doc); }

  static private void Parsedateinfo(EPIS_Termine Doc) throws MinervaParseException

  { dateinfo(Doc); }

  static public Document EPIS_Termine(EPIS_Termine Doc) throws MinervaParseException
  {
    int StartPos = Doc.getPosition();
    Doc.consume("*(?<font[ ]class=\"header\">Termine</font><br>*\n)");
    Doc.consume("<font[ ]class=\"header\">Termine</font><br>*\n");
    Doc.consume("[\t ]");
    Doc.consume("*<br>*\n");
    Doc.consume("\t\n");
    Doc.consume("\t\n");
    Doc.startPosition$termin = Doc.getPosition();
    if ((Doc.bool$termin0=Doc.match("<table[ ]border=\"0\"[ ]cellpadding=\"0\"[ ]cellspacing=\"0\"[ ]width=\"400\">*\n*\n"))) {
      do {
        termin(Doc);
        Doc.startPosition$termin = Doc.getPosition();
      } while ((Doc.bool$termin0=Doc.match("<table[ ]border=\"0\"[ ]cellpadding=\"0\"[ ]cellspacing=\"0\"[ ]width=\"400\">*\n*\n")));
    }
    else {
      String[] Choices = {"<table[ ]border=\"0\"[ ]cellpadding=\"0\"[ ]cellspacing=\"0\"[ ]width=\"400\">*\n*\n",};
      throw new MinervaParseException(Doc,Choices);
    }
    Doc.setStartPoint(StartPos);
    Doc.copy();
    $EPIS_Termine.clear();
    $EPIS_Termine.paste();
    return result;
  }

  static private void termin(EPIS_Termine Doc) throws MinervaParseException
  {
    dateinfo(Doc);
    Doc.consume("</table>*\n");
    Doc.consume("<table>");
    vortragsinfo(Doc);
    Doc.consume("</table>*\n*\n");
    Doc.setStartPoint(Doc.startPosition$termin);
    Doc.copy();
    $termin.clear();
    $termin.paste();
  }

  static private void dateinfo(EPIS_Termine Doc) throws MinervaParseException
  {
    int StartPos = Doc.getPosition();
    Doc.consume("*<FONT*>");
    a(Doc);
    Doc.consume("</font>");
{
                    System.out.println($a);                    
                }
    Doc.consume("*<IMG*/tr>*\n");
    Doc.setStartPoint(StartPos);
    Doc.copy();
    $dateinfo.clear();
    $dateinfo.paste();
  }

  static private void a(EPIS_Termine Doc) throws MinervaParseException
  {
    int StartPos = Doc.getPosition();
    Doc.consume("*(?</font>)");
    Doc.setStartPoint(StartPos);
    Doc.copy();
    $a.clear();
    $a.paste();
  }

  static private void vortragsinfo(EPIS_Termine Doc) throws MinervaParseException
  {
    int StartPos = Doc.getPosition();
    Doc.consume("*(?\t<b>)");
    if (Doc.match("\t<b>")) {
      do {
        b(Doc);
        Doc.consume("[:]</b><br>*\n");
        Doc.consume("*[\"]");
        c(Doc);
        Doc.consume("[\"]*<br><br>*\n*\n*\n");
      } while (Doc.match("\t<b>"));
    }
    else {
      String[] Choices = {"\t<b>",};
      throw new MinervaParseException(Doc,Choices);
    }
    Doc.consume("*</tr>*\n");
{
                    System.out.println($b);                    
                    System.out.println($c);                    
                }
    Doc.setStartPoint(StartPos);
    Doc.copy();
    $vortragsinfo.clear();
    $vortragsinfo.paste();
  }

  static private void b(EPIS_Termine Doc) throws MinervaParseException
  {
    int StartPos = Doc.getPosition();
    Doc.consume("*(?[:]</b><br>*\n)");
    Doc.setStartPoint(StartPos);
    Doc.copy();
    $b.clear();
    $b.paste();
  }

  static private void c(EPIS_Termine Doc) throws MinervaParseException
  {
    int StartPos = Doc.getPosition();
    Doc.consume("*(?[\"]*<br><br>*\n*\n*\n)");
    Doc.setStartPoint(StartPos);
    Doc.copy();
    $c.clear();
    $c.paste();
  }

  static public void main(String[] argv) throws Exception {
    if (argv.length==0 || argv[0].startsWith("-")) {
      System.out.println("Usage: java EPIS_Termine NT [SOURCE]...");
      System.out.println("Wrap SOURCE(s) of textual documents, or standard input");
      System.out.println("where NT is a (root) production to call");
      System.out.println("      SOURCE is the source of document to be wrapped");
      System.out.println("Each SOURCE  may be specified as a local file or by means of an URL");
      System.out.println();
      System.out.println("Wrapper generated by Minerva Wrapper Generator of Araneus Project.");
      System.out.println("(more info about Araneus Project at http://www.dia.uniroma3.it/Araneus)");
      return;
    }
    Document result;
    Class thisClass = Class.forName("EPIS_Termine");
    Class[] pars = new Class[1];
    pars[0] = thisClass;
    Method method = thisClass.getMethod(argv[0],pars);
    EPIS_Termine[] args = new EPIS_Termine[1];
    EPIS_Termine doc = new EPIS_Termine();
    args[0] = doc;
    for(int i=1; i<argv.length; i++) {
      try {
        try {
          doc.open(new InputStreamReader(new URL(argv[i]).openStream()));
        }
        catch (MalformedURLException mue) {
          doc.open(new FileReader(argv[i]));
        }
      }
      catch (Exception e) {
        System.out.println("Exception opening "+argv[i]+". "+e.getMessage());
        System.out.println("Skipping this source");
        continue;
      }
      try {
        ReInit(); 
        System.out.println(argv[i]+":");
        result = (Document)method.invoke(null,args);
        System.out.println(result);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (argv.length==1) {
      Reader reader = new InputStreamReader(System.in);
      doc.open(reader);
      try {
        result = (Document)method.invoke(null,args);
        System.out.println(result);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}    /*** end of class EPIS_Termine ***/
