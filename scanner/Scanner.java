package scanner;
import java.io.*;

/**
 * Scanner is a simple scanner that reads an input string for individual lexemes 
 * and returns the corresponding string of tokens.
 * The Scanner also eliminates comments in the process.
 * 
 * Notebook questions:
 * 
 * If the next character was newline, open parenthesis, or white space, then it's the keyword "IF." 
 * Otherwise, it's a variable name.
 * 
 * The parameter passed by eat is currentChar, which serves as a lookahead to allow
 * the Scanner to consider the current character based off of the next character ahead.
 * For example, this is helpful for operations such as /[digit] or // where the meaning of the / 
 * character changes from divide to a comment line operation based on the next character ahead.
 * 
 * 
 * @author Kaitlyn Wang
 * @version 9.7.23
 *  
 *
 */
public class Scanner
{
    private BufferedReader in;
    private char currentChar;
    private boolean eof;
    /**
     * Scanner constructor for construction of a scanner that 
     * uses an InputStream object for input.  
     * Usage: 
     * FileInputStream inStream = new FileInputStream(new File([file name]);
     * Scanner lex = new Scanner(inStream);
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream) throws ScanErrorException
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
    }
    /**
     * Scanner constructor for constructing a scanner that 
     * scans a given input string.  It sets the end-of-file flag an then reads
     * the first character of the input string into the instance field 
     * currentChar.
     * Usage: Scanner lex = new Scanner(input_string);
     * @param inString the string to scan
     */
    public Scanner(String inString) throws ScanErrorException
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }
    /**
     * The getNextChar method attempts to get the next character from the input
     * stream.  It sets the eof flag true if the end of file is reached on
     * the input stream.  Otherwise, it sets the currentChar to the next integer from the stream
     * converted to a char type.
     * 
     */
    private void getNextChar() throws ScanErrorException
    {
        try 
        {
            int next = in.read();
            if (next == -1 || next == 46) 
            {
                eof = true;
            }
            else
            {
                currentChar = (char) next;
            }
        } catch(Exception e) 
        {
            e.printStackTrace();
            throw new ScanErrorException("Could not get the next character: " + e.getMessage());
        }
    }
    /**
     * This method allows the scanner to take in one character in advance
     * by calling getNextChar() to allow for easy error detection. First 
     * compares the input string to the current character, and if they
     * match then it advances to the next character, otherwise throws
     * a scan error exception which helps with debugging.
     * 
     * @param expected is the current character, should match the expected char
     */
    
    private void eat(char expected) throws ScanErrorException
    {
        if (hasNext())
        {
            if (currentChar == expected)
            {
                getNextChar();
            }
            else
            {
                throw new ScanErrorException("Illegal character: expected " + 
                expected  + " and found " + currentChar);
            }
        }
    }
    /**
     * The method checks if a next character exists, returning false 
     * if the end of file has already been reached. 
     * @return Returns false if input stream has reached the end of file, otherwise true.
     */
    public boolean hasNext()
    {
        return !eof;
    }
    
    /**
     * Helper method that determines whether a given character is a digit or not
     * @param c the character to test
     * @return true if digit, false if anything else
     */
    public static boolean isDigit(char c) 
    {
        return c >= '0' && c <= '9';
    }   
    
    /**
     * Helper method that determines whether a given character is a letter or not, 
     * capital or lowercase
     * @param c the character to test
     * @return true if letter, false if anything else
     */
    public static boolean isLetter(char c) 
    {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }   
    
    /**
     * Helper method that determines whether a given character is a period or not, 
     * @param c the character to test
     * @return true if period, false if anything else
     */
    public static boolean isPeriod(char c) 
    {
        return c == '.';
    }   
    
    /**
     * Helper method that determines whether a given character is a white space or not
     * Next line \n, tab \t, and return \r are all considered white spaces
     * along with the space ' ' character
     * @param c the character to test
     * @return true if white space, false if anything else
     */
    public static boolean isWhiteSpace(char c) 
    {
        return c == ' ' || c == '\n' || c == '\t' || c == '\r';
    }   
    
    /**
     * Helper method that determines whether a given character is an operand or not
     * 
     * @param c the character to test
     * @return true if operand, false if anything else
     */
    public static boolean isOperand(char c) 
    {
        return c == '=' || c == '+' || c == '-' || c == '*' || c == '/' 
               || c == '%' || 
               c == '(' || c == ')' || c==':' || c== '<'|| c== '>' || c==';';
    }   
    
    /**
     * Helper method that determines whether a given character is an operand or not
     * 
     * @param c the character to test
     * @return true if operand, false if anything else
     */
    public static boolean isCommand(char c) 
    {
        return c == ';';
    }   
    
    /**
     * Scans through a single number token. A number is defined as a 
     * string of one or more digits.
     * 
     * If invalid token received, throws a ScanErrorException for invalid lexeme
     * 
     * @return the entire token as a String when finished scanning
     */
    private String scanNumber() throws ScanErrorException
    {
        String num = "";
        while(hasNext() && isDigit(currentChar))
        {
            num += currentChar;
            eat(currentChar);
        }
        if(isWhiteSpace(currentChar) || isOperand(currentChar) || 
            isPeriod(currentChar) || !hasNext())
        {
            return num;
        }
        num += currentChar;        
        throw new ScanErrorException("No lexeme recognized in " + num);
    }
    
    /**
     * Scans through a single identifier token. An identifier is defined as a 
     * string that begins with a letter followed by any combination of letter or digit characters. 
     * 
     * 
     * If invalid token received, throws a ScanErrorException for invalid lexeme
     * 
     * @return the entire token as a String when finished scanning
     */
    private String scanIdentifier() throws ScanErrorException
    {
        String name = "";
        while(hasNext() && isLetter(currentChar) || isDigit(currentChar))
        {
            name += currentChar;
            eat(currentChar);
        }
        if(isWhiteSpace(currentChar) || isOperand(currentChar) ||
            isPeriod(currentChar) || !hasNext())
        {
            return name;
        }
        name += currentChar;        
        throw new ScanErrorException("No lexeme recognized in " + name);
    }
    
    private boolean isStickyOperand(char c)
    {
        return c == '=' || c == '/';
    }
    
    /**
     * Scans through a single operand token. An operand is defined as a 
     * string of one or more operand characters. If a comment operand //
     * is detected, the next line will be ignored.
     * 
     * If invalid token received, throws a ScanErrorException for invalid lexeme
     * 
     * @return the entire token as a String when finished scanning
     */
    private String scanOperand() throws ScanErrorException
    {
        String op = "";
        if (hasNext() && currentChar == '/')
        {
            op += currentChar;
            eat(currentChar);
            if (currentChar == '/')
            {
                while(hasNext() && currentChar != '\n' && currentChar != '\r')
                {
                    eat(currentChar);
                }
                return "";
            }
        } 
        else 
        {
            if (!isStickyOperand(currentChar))
            {
                op += currentChar;
                eat(currentChar);
            }
            while(hasNext() && isStickyOperand(currentChar))
            {
                op += currentChar;
                eat(currentChar);
            }
        }
        if(isWhiteSpace(currentChar) || isLetter(currentChar) || isDigit(currentChar)
            || isPeriod(currentChar) || !hasNext() || isOperand(currentChar))
        {
            return op;
        }
        op += currentChar;        
        throw new ScanErrorException("No lexeme recognized in " + op);
    }
    
    
    /**
     * NextToken advances the scanner by skipping all 
     * leading white spaces until the first character of the 
     * next token is reached. Afterward, it initiates the scanning process for the token 
     * based on the first character:
     * if the first char is a letter, it scans the identifier
     * if the first char is a digit, it scans the number
     * if the first char is an operand, it scans the operand
     * if the character is a period, it initiates the end of file
     * Unidentified characters are skipped in this setting, but can also be 
     * set to throw an exception
     * 
     * @return the entire token after the scan as a String
     * 
     */
    public String nextToken() throws ScanErrorException
    {
        if (hasNext())
        {
            while (hasNext() && isWhiteSpace(currentChar))
            {
                eat(currentChar);
            }
            if (isLetter(currentChar))
            {
                return scanIdentifier();
            }
            else if (isDigit(currentChar))
            {
                return scanNumber();
            }
            else if (isOperand(currentChar))
            {
                return scanOperand();
            }
            else if (isPeriod(currentChar))
            {
                eat(currentChar);
                return "EOF";
            } 
            else
            {
                eat(currentChar); //code to skip unidentified characters
                return "" + currentChar;
                // code to throw error if unidentified characters found
                //throw new ScanErrorException("Unidentified character found."); 
            }
        } 
        else
        {
            return "EOF";
        }
    }  
    
}
