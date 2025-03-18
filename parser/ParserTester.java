package parser;
import java.io.*;
import java.io.FileInputStream;
import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import java.nio.file.*; 


/**
 * The ParserTester tests the parser's ability to break down tokens
 * from a text file by running all the commands until the end of file is 
 * reached.
 * 
 * @author Kaitlyn Wang
 * @version 10/6/23
 */
public class ParserTester 
{
    /**
     * The main method tests whether a parser can process a given sample text 
     * properly. If the file path is invalid, a FileNotFoundException will
     * be thrown.
     *
     * @param args arguments from the command line
     */
    public static void main(String [ ] args) throws Exception
    {
        try
        {
            BufferedReader in;
            String content = new String(Files.readAllBytes(
                                 Paths.get("/Users/24kaitlynw/Documents/testing")));
            scanner.Scanner lex = new scanner.Scanner(content);
            Parser p = new Parser(lex);
            while (lex.hasNext())
            {
                p.parseStatement();
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            throw new FileNotFoundException("Testing file not found");
        }
    }

}
