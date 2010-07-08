/* Generated By:JavaCC: Do not edit this line. RelationalPredicateParserTokenManager.java */
package de.uniol.inf.is.odysseus.parser.pql.relational.predicateParser;
import de.uniol.inf.is.odysseus.base.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.base.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.base.predicate.OrPredicate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

/** Token Manager. */
@SuppressWarnings("all")
public class RelationalPredicateParserTokenManager implements RelationalPredicateParserConstants
{

  /** Debug output. */
  public  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0xe0L) != 0L)
         {
            jjmatchedKind = 11;
            return 18;
         }
         if ((active0 & 0x100000L) != 0L)
            return 19;
         return -1;
      case 1:
         if ((active0 & 0x60L) != 0L)
         {
            jjmatchedKind = 11;
            jjmatchedPos = 1;
            return 18;
         }
         if ((active0 & 0x80L) != 0L)
            return 18;
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 40:
         return jjStopAtPos(0, 16);
      case 41:
         return jjStopAtPos(0, 17);
      case 42:
         return jjStopAtPos(0, 18);
      case 43:
         return jjStopAtPos(0, 19);
      case 44:
         return jjStopAtPos(0, 23);
      case 45:
         return jjStartNfaWithStates_0(0, 20, 19);
      case 47:
         return jjStopAtPos(0, 21);
      case 65:
         return jjMoveStringLiteralDfa1_0(0x20L);
      case 78:
         return jjMoveStringLiteralDfa1_0(0x40L);
      case 79:
         return jjMoveStringLiteralDfa1_0(0x80L);
      case 94:
         return jjStopAtPos(0, 22);
      default :
         return jjMoveNfa_0(0, 0);
   }
}
private int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 78:
         return jjMoveStringLiteralDfa2_0(active0, 0x20L);
      case 79:
         return jjMoveStringLiteralDfa2_0(active0, 0x40L);
      case 82:
         if ((active0 & 0x80L) != 0L)
            return jjStartNfaWithStates_0(1, 7, 18);
         break;
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0);
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 68:
         if ((active0 & 0x20L) != 0L)
            return jjStartNfaWithStates_0(2, 5, 18);
         break;
      case 84:
         if ((active0 & 0x40L) != 0L)
            return jjStartNfaWithStates_0(2, 6, 18);
         break;
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 18;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 18:
               case 7:
                  if ((0x7ff440000000000L & l) == 0L)
                     break;
                  if (kind > 11)
                     kind = 11;
                  jjCheckNAdd(7);
                  break;
               case 0:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 9)
                        kind = 9;
                     jjCheckNAddStates(0, 2);
                  }
                  else if ((0x7000000000000000L & l) != 0L)
                  {
                     if (kind > 15)
                        kind = 15;
                  }
                  else if (curChar == 34)
                     jjCheckNAddTwoStates(9, 10);
                  else if (curChar == 46)
                     jjCheckNAdd(5);
                  else if (curChar == 45)
                     jjCheckNAddTwoStates(1, 4);
                  if (curChar == 60)
                     jjCheckNAddTwoStates(15, 12);
                  else if (curChar == 62)
                     jjCheckNAdd(12);
                  break;
               case 19:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(1, 2);
                  else if (curChar == 46)
                     jjCheckNAdd(5);
                  break;
               case 1:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddTwoStates(1, 2);
                  break;
               case 2:
                  if (curChar == 46)
                     jjCheckNAdd(3);
                  break;
               case 3:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 8)
                     kind = 8;
                  jjCheckNAdd(3);
                  break;
               case 4:
                  if (curChar == 46)
                     jjCheckNAdd(5);
                  break;
               case 5:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 8)
                     kind = 8;
                  jjCheckNAdd(5);
                  break;
               case 8:
                  if (curChar == 34)
                     jjCheckNAddTwoStates(9, 10);
                  break;
               case 9:
                  if ((0xfffffffbffffffffL & l) != 0L)
                     jjCheckNAddTwoStates(9, 10);
                  break;
               case 10:
                  if (curChar == 34 && kind > 14)
                     kind = 14;
                  break;
               case 11:
                  if ((0x7000000000000000L & l) != 0L && kind > 15)
                     kind = 15;
                  break;
               case 12:
                  if (curChar == 61 && kind > 15)
                     kind = 15;
                  break;
               case 13:
                  if (curChar == 62)
                     jjCheckNAdd(12);
                  break;
               case 14:
                  if (curChar == 60)
                     jjCheckNAddTwoStates(15, 12);
                  break;
               case 15:
                  if (curChar == 62 && kind > 15)
                     kind = 15;
                  break;
               case 16:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 9)
                     kind = 9;
                  jjCheckNAddStates(0, 2);
                  break;
               case 17:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 9)
                     kind = 9;
                  jjCheckNAdd(17);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 18:
                  if ((0x7fffffe87fffffeL & l) != 0L)
                  {
                     if (kind > 11)
                        kind = 11;
                     jjCheckNAdd(7);
                  }
                  if ((0x7fffffe07fffffeL & l) != 0L)
                  {
                     if (kind > 11)
                        kind = 11;
                     jjCheckNAddTwoStates(6, 7);
                  }
                  break;
               case 0:
               case 6:
                  if ((0x7fffffe07fffffeL & l) == 0L)
                     break;
                  if (kind > 11)
                     kind = 11;
                  jjCheckNAddTwoStates(6, 7);
                  break;
               case 7:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 11)
                     kind = 11;
                  jjCheckNAdd(7);
                  break;
               case 9:
                  jjAddStates(3, 4);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 9:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjAddStates(3, 4);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 18 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   1, 2, 17, 9, 10, 
};

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, "\101\116\104", "\116\117\124", "\117\122", null, 
null, null, null, null, null, null, null, "\50", "\51", "\52", "\53", "\55", "\57", 
"\136", "\54", };

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
};
static final long[] jjtoToken = {
   0xffcbe1L, 
};
static final long[] jjtoSkip = {
   0x1eL, 
};
protected SimpleCharStream input_stream;
private final int[] jjrounds = new int[18];
private final int[] jjstateSet = new int[36];
protected char curChar;
/** Constructor. */
public RelationalPredicateParserTokenManager(SimpleCharStream stream){
   if (SimpleCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}

/** Constructor. */
public RelationalPredicateParserTokenManager(SimpleCharStream stream, int lexState){
   this(stream);
   SwitchTo(lexState);
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 18; i-- > 0;)
      jjrounds[i] = 0x80000000;
}

/** Reinitialise parser. */
public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}

/** Switch to specified lex state. */
public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

/** Get the next Token. */
public Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(java.io.IOException e)
   {
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   try { input_stream.backup(0);
      while (curChar <= 32 && (0x100002600L & (1L << curChar)) != 0L)
         curChar = input_stream.BeginToken();
   }
   catch (java.io.IOException e1) { continue EOFLoop; }
   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

private void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}

}
