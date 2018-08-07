/* BibTeXParserTokenManager.java */
/* Generated By:JavaCC: Do not edit this line. BibTeXParserTokenManager.java */
package org.jbibtex;
import java.io.FileReader;
import java.io.Reader;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jbibtex.policies.BibTeXEntryKeyConflictResolutionPolicy;
import org.jbibtex.policies.BibTeXEntryKeyConflictResolutionPolicies;

/** Token Manager. */
@SuppressWarnings("unused")public class BibTeXParserTokenManager implements BibTeXParserConstants {
        void backup(int n){
                input_stream.backup(n);
        }

  /** Debug output. */
  public  java.io.PrintStream debugStream = System.out;
  /** Set debug output. */
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_1(int pos, long active0){
   switch (pos)
   {
      default :
         return -1;
   }
}
private final int jjStartNfa_1(int pos, long active0){
   return jjMoveNfa_1(jjStopStringLiteralDfa_1(pos, active0), pos + 1);
}
private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private int jjMoveStringLiteralDfa0_1(){
   switch(curChar)
   {
      case 34:
         return jjStopAtPos(0, 17);
      case 35:
         return jjStopAtPos(0, 14);
      case 37:
         return jjStopAtPos(0, 2);
      case 40:
         return jjStartNfaWithStates_1(0, 16, 29);
      case 41:
         return jjStopAtPos(0, 19);
      case 44:
         return jjStopAtPos(0, 12);
      case 61:
         return jjStopAtPos(0, 13);
      case 64:
         return jjStopAtPos(0, 11);
      case 123:
         return jjStopAtPos(0, 15);
      case 125:
         return jjStopAtPos(0, 18);
      default :
         return jjMoveNfa_1(0, 0);
   }
}
private int jjStartNfaWithStates_1(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_1(state, pos + 1);
}
static final long[] jjbitVec0 = {
   0xfffffffeL, 0x0L, 0x0L, 0x0L
};
static final long[] jjbitVec2 = {
   0x0L, 0x0L, 0xa000000100000000L, 0xff7fffffff7fffffL
};
static final long[] jjbitVec3 = {
   0x0L, 0x0L, 0x100000000000L, 0x0L
};
static final long[] jjbitVec4 = {
   0x400000000L, 0x0L, 0x0L, 0x0L
};
private int jjMoveNfa_1(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 30;
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
               case 0:
                  if ((0x3ff210000000000L & l) == 0L)
                     break;
                  if (kind > 24)
                     kind = 24;
                  { jjCheckNAdd(29); }
                  break;
               case 29:
                  if ((0x87ffef8300000000L & l) == 0L)
                     break;
                  if (kind > 24)
                     kind = 24;
                  { jjCheckNAdd(29); }
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
               case 0:
                  if ((0x7fffffe8ffffffeL & l) != 0L)
                  {
                     if (kind > 24)
                        kind = 24;
                     { jjCheckNAdd(29); }
                  }
                  if ((0x8000000080000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 23;
                  else if ((0x1000000010000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 15;
                  else if ((0x20000000200L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 8;
                  else if ((0x800000008L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 1:
                  if ((0x800000008000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 2;
                  break;
               case 2:
                  if ((0x200000002000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 3;
                  break;
               case 3:
                  if ((0x200000002000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 4;
                  break;
               case 4:
                  if ((0x2000000020L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 5;
                  break;
               case 5:
                  if ((0x400000004000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 6;
                  break;
               case 6:
                  if ((0x10000000100000L & l) != 0L && kind > 20)
                     kind = 20;
                  break;
               case 7:
                  if ((0x20000000200L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 8;
                  break;
               case 8:
                  if ((0x400000004000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 9;
                  break;
               case 9:
                  if ((0x800000008L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 10;
                  break;
               case 10:
                  if ((0x100000001000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 11;
                  break;
               case 11:
                  if ((0x20000000200000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 12;
                  break;
               case 12:
                  if ((0x1000000010L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 13;
                  break;
               case 13:
                  if ((0x2000000020L & l) != 0L && kind > 21)
                     kind = 21;
                  break;
               case 14:
                  if ((0x1000000010000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 15;
                  break;
               case 15:
                  if ((0x4000000040000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 16;
                  break;
               case 16:
                  if ((0x2000000020L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 17;
                  break;
               case 17:
                  if ((0x200000002L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 18;
                  break;
               case 18:
                  if ((0x200000002000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 19;
                  break;
               case 19:
                  if ((0x400000004L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 20;
                  break;
               case 20:
                  if ((0x100000001000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 21;
                  break;
               case 21:
                  if ((0x2000000020L & l) != 0L && kind > 22)
                     kind = 22;
                  break;
               case 22:
                  if ((0x8000000080000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 23;
                  break;
               case 23:
                  if ((0x10000000100000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 24;
                  break;
               case 24:
                  if ((0x4000000040000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 25;
                  break;
               case 25:
                  if ((0x20000000200L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 26;
                  break;
               case 26:
                  if ((0x400000004000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 27;
                  break;
               case 27:
                  if ((0x8000000080L & l) != 0L && kind > 23)
                     kind = 23;
                  break;
               case 28:
                  if ((0x7fffffe8ffffffeL & l) == 0L)
                     break;
                  if (kind > 24)
                     kind = 24;
                  { jjCheckNAdd(29); }
                  break;
               case 29:
                  if ((0x7fffffeaffffffeL & l) == 0L)
                     break;
                  if (kind > 24)
                     kind = 24;
                  { jjCheckNAdd(29); }
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
               case 29:
                  if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 24)
                     kind = 24;
                  { jjCheckNAdd(29); }
                  break;
               default : if (i1 == 0 || l1 == 0 || i2 == 0 ||  l2 == 0) break; else break;
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
      if ((i = jjnewStateCnt) == (startsAt = 30 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
private int jjMoveStringLiteralDfa0_0()
{
   return jjMoveNfa_0(0, 0);
}
static final long[] jjbitVec5 = {
   0xfffffffffffffffeL, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static final long[] jjbitVec6 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 1;
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
               case 0:
                  if ((0xffffffdfffffffffL & l) == 0L)
                     break;
                  kind = 1;
                  jjstateSet[jjnewStateCnt++] = 0;
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
               case 0:
                  if ((0xfffffffffffffffeL & l) == 0L)
                     break;
                  kind = 1;
                  jjstateSet[jjnewStateCnt++] = 0;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 1)
                     kind = 1;
                  jjstateSet[jjnewStateCnt++] = 0;
                  break;
               default : if (i1 == 0 || l1 == 0 || i2 == 0 ||  l2 == 0) break; else break;
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
      if ((i = jjnewStateCnt) == (startsAt = 1 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
private int jjMoveStringLiteralDfa0_3()
{
   return 1;
}
private int jjMoveStringLiteralDfa0_2()
{
   return jjMoveNfa_2(4, 0);
}
private int jjMoveNfa_2(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 4;
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
               case 4:
                  if ((0xffffffffffffdbffL & l) != 0L)
                  {
                     if (kind > 3)
                        kind = 3;
                     { jjCheckNAddStates(0, 2); }
                  }
                  else if ((0x2400L & l) != 0L)
                  {
                     if (kind > 3)
                        kind = 3;
                  }
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 2;
                  break;
               case 0:
                  if ((0xffffffffffffdbffL & l) == 0L)
                     break;
                  kind = 3;
                  { jjCheckNAddStates(0, 2); }
                  break;
               case 1:
                  if ((0x2400L & l) != 0L && kind > 3)
                     kind = 3;
                  break;
               case 2:
                  if (curChar == 10 && kind > 3)
                     kind = 3;
                  break;
               case 3:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 2;
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
               case 4:
               case 0:
                  kind = 3;
                  { jjCheckNAddStates(0, 2); }
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 4:
               case 0:
                  if (!jjCanMove_1(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 3)
                     kind = 3;
                  { jjCheckNAddStates(0, 2); }
                  break;
               default : if (i1 == 0 || l1 == 0 || i2 == 0 ||  l2 == 0) break; else break;
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
      if ((i = jjnewStateCnt) == (startsAt = 4 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   0, 1, 3, 
};
private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
      case 0:
         return ((jjbitVec2[i2] & l2) != 0L);
      case 32:
         return ((jjbitVec3[i2] & l2) != 0L);
      case 33:
         return ((jjbitVec4[i2] & l2) != 0L);
      default :
         if ((jjbitVec0[i1] & l1) != 0L)
            return true;
         return false;
   }
}
private static final boolean jjCanMove_1(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
      case 0:
         return ((jjbitVec6[i2] & l2) != 0L);
      default :
         if ((jjbitVec5[i1] & l1) != 0L)
            return true;
         return false;
   }
}

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, null, null, null, null, null, "\100", "\54", 
"\75", "\43", "\173", "\50", "\42", "\175", "\51", null, null, null, null, null, null, 
null, null, null, null, null, };
protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   if (jjmatchedPos < 0)
   {
      if (image == null)
         curTokenImage = "";
      else
         curTokenImage = image.toString();
      beginLine = endLine = input_stream.getEndLine();
      beginColumn = endColumn = input_stream.getEndColumn();
   }
   else
   {
      String im = jjstrLiteralImages[jjmatchedKind];
      curTokenImage = (im == null) ? input_stream.GetImage() : im;
      beginLine = input_stream.getBeginLine();
      beginColumn = input_stream.getBeginColumn();
      endLine = input_stream.getEndLine();
      endColumn = input_stream.getEndColumn();
   }
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

int curLexState = 1;
int defaultLexState = 1;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

/** Get the next Token. */
public Token getNextToken() 
{
  Token specialToken = null;
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(Exception e)
   {
      jjmatchedKind = 0;
      jjmatchedPos = -1;
      matchedToken = jjFillToken();
      matchedToken.specialToken = specialToken;
      return matchedToken;
   }
   image = jjimage;
   image.setLength(0);
   jjimageLen = 0;

   for (;;)
   {
     switch(curLexState)
     {
       case 0:
         jjmatchedKind = 1;
         jjmatchedPos = -1;
         curPos = 0;
         curPos = jjMoveStringLiteralDfa0_0();
         break;
       case 1:
         try { input_stream.backup(0);
            while (curChar <= 32 && (0x100003600L & (1L << curChar)) != 0L)
               curChar = input_stream.BeginToken();
         }
         catch (java.io.IOException e1) { continue EOFLoop; }
         jjmatchedKind = 0x7fffffff;
         jjmatchedPos = 0;
         curPos = jjMoveStringLiteralDfa0_1();
         break;
       case 2:
         jjmatchedKind = 3;
         jjmatchedPos = -1;
         curPos = 0;
         curPos = jjMoveStringLiteralDfa0_2();
         if (jjmatchedPos < 0 || (jjmatchedPos == 0 && jjmatchedKind > 4))
         {
            jjmatchedKind = 4;
            jjmatchedPos = 0;
         }
         break;
       case 3:
         jjmatchedKind = 0x7fffffff;
         jjmatchedPos = 0;
         curPos = jjMoveStringLiteralDfa0_3();
         if (jjmatchedPos == 0 && jjmatchedKind > 5)
         {
            jjmatchedKind = 5;
         }
         break;
     }
     if (jjmatchedKind != 0x7fffffff)
     {
        if (jjmatchedPos + 1 < curPos)
           input_stream.backup(curPos - jjmatchedPos - 1);
        if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
        {
           matchedToken = jjFillToken();
           matchedToken.specialToken = specialToken;
       if (jjnewLexState[jjmatchedKind] != -1)
         curLexState = jjnewLexState[jjmatchedKind];
           return matchedToken;
        }
        else if ((jjtoSkip[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
        {
           if ((jjtoSpecial[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
           {
              matchedToken = jjFillToken();
              if (specialToken == null)
                 specialToken = matchedToken;
              else
              {
                 matchedToken.specialToken = specialToken;
                 specialToken = (specialToken.next = matchedToken);
              }
              SkipLexicalActions(matchedToken);
           }
           else
              SkipLexicalActions(null);
         if (jjnewLexState[jjmatchedKind] != -1)
           curLexState = jjnewLexState[jjmatchedKind];
           continue EOFLoop;
        }
        jjimageLen += jjmatchedPos + 1;
      if (jjnewLexState[jjmatchedKind] != -1)
        curLexState = jjnewLexState[jjmatchedKind];
        curPos = 0;
        jjmatchedKind = 0x7fffffff;
        try {
           curChar = input_stream.readChar();
           continue;
        }
        catch (java.io.IOException e1) { }
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
     throw new TokenMgrException(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrException.LEXICAL_ERROR);
   }
  }
}

void SkipLexicalActions(Token matchedToken)
{
   switch(jjmatchedKind)
   {
      default :
         break;
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

    /** Constructor. */
    public BibTeXParserTokenManager(SimpleCharStream stream){

      if (SimpleCharStream.staticFlag)
            throw new RuntimeException("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");

    input_stream = stream;
  }

  /** Constructor. */
  public BibTeXParserTokenManager (SimpleCharStream stream, int lexState){
    ReInit(stream);
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
    for (i = 30; i-- > 0;)
      jjrounds[i] = 0x80000000;
  }

  /** Reinitialise parser. */
  public void ReInit( SimpleCharStream stream, int lexState)
  {
  
    ReInit( stream);
    SwitchTo(lexState);
  }

  /** Switch to specified lex state. */
  public void SwitchTo(int lexState)
  {
    if (lexState >= 4 || lexState < 0)
      throw new TokenMgrException("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrException.INVALID_LEXICAL_STATE);
    else
      curLexState = lexState;
  }

/** Lexer state names. */
public static final String[] lexStateNames = {
   "IN_HEADER",
   "DEFAULT",
   "IN_INLINE_COMMENT",
   "IN_LITERAL",
};

/** Lex State array. */
public static final int[] jjnewLexState = {
   -1, 1, 2, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
   -1, -1, -1, -1, -1, -1, 
};
static final long[] jjtoToken = {
   0x1fff821L, 
};
static final long[] jjtoSkip = {
   0x7caL, 
};
static final long[] jjtoSpecial = {
   0x8L, 
};
static final long[] jjtoMore = {
   0x14L, 
};
    protected SimpleCharStream  input_stream;

    private final int[] jjrounds = new int[30];
    private final int[] jjstateSet = new int[2 * 30];

    private final StringBuilder jjimage = new StringBuilder();
    private StringBuilder image = jjimage;
    private int jjimageLen;
    private int lengthOfMatch;
    
    protected int curChar;
}
