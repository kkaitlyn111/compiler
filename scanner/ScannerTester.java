package scanner;
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
 * The ScannerTester tests the scanner's ability to break down a text file
 * from a reader into tokens accurately.
 * The individual tokens processed by the scanner are printed out on each line.
 * 
 * @author Kaitlyn Wang
 * @version 9.7.23
 */
public class ScannerTester 
{
    /**
     * The main method tests whether a Scanner can read through a given sample text properly. 
     * The scanner prints out tokens individually on each line
     *
     * @param args arguments from the command line
     */
    public static void main(String [ ] args) throws Exception
    {
        BufferedReader in;
        try
        {
            //FileInputStream inStream = new 
            //FileInputStream(new File("/Users/24kaitlynw/Downloads/scannerTestAdvanced.txt"));
            String content = new String(Files.readAllBytes(
                                 Paths.get("/Users/24kaitlynw/Documents/testing")));
            Scanner lex = new Scanner(content);
            while(lex.hasNext())
            {
                System.out.println(lex.nextToken());
            }
            System.out.println(lex.nextToken());
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            throw new FileNotFoundException("Testing file not found");
        }
    }

}
