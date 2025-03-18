package parser;
import scanner.Scanner;
import scanner.ScannerTester;
import scanner.ScanErrorException;
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
 * Parser parses a string of tokens from a scanner to evaluate the expression
 * following a certain grammar. It works on the simplified PASCAL commands 
 * WRITELN, BEGIN, and END, which can perform mathematical computations 
 * containing that addition, subtraction, multiplication, and division 
 * operations. The commands may be grouped in blocks and will be evaluated
 * accordingly. This parser also allows for the saving of values into 
 * variables.
 *
 * @author Kaitlyn Wang
 * @version 9/25/23
 */
public class Parser
{
    private Scanner scanner;
    private String currentToken;
    private Map<String, Integer> variables;

    /**
     * Constructor for Parser takes in a Scanner as an instance variable
     * which will feed it a stream of tokens, it also instantiates
     * an empty map for the instance variable variables.
     * 
     * @param s is the Scanner from which the Parser will read tokens
     */
    public Parser(Scanner s) throws ScanErrorException
    {
        scanner = s;
        currentToken = scanner.nextToken();
        variables = new HashMap();
    }

    /**
     * The eat method checks that the currentToken is equal to the expected 
     * value and if it matches, it advances to the next token given
     * by the Scanner. otherwise, it throws an exception as an unexpected
     * value.
     * 
     * @param expected is the expected value
     */
    private void eat(String expected) throws ScanErrorException
    {
        if (expected.equals(currentToken))
        {
            String previous = currentToken;
            currentToken = scanner.nextToken();
        } 
        else 
        {
            throw new IllegalArgumentException(expected + " was expected," + 
                                               "but found " + currentToken);
        }
    }

    /**
     * This method converts the current token to an integer value 
     * 
     * @precondition current token is a string that represents 
     * a valid int value
     * @return int is the integer value of the current token (a string)
     */
    private int parseNumber() throws ScanErrorException
    {
        int num = Integer.parseInt(currentToken);
        eat(currentToken);
        return num;
    }
    
    /**
     * This method acts generally to parse a statement in PASCAL. 
     * According to the grammar, it is able to handle WRITELN commands
     * by printing out the value of the expression inside the command,
     * BEGIN commands by parsing the block between BEGIN and its corresponding
     * END command, and creates new variables when any other 
     * unrecognized input is given.
     * 
     */
    public void parseStatement() throws ScanErrorException
    {
        if (currentToken.equals("WRITELN"))
        {
            eat("WRITELN");
            eat("(");
            int val = parseExpression();
            eat(")");
            eat(";");
            System.out.println(val);
        } 
        else if (currentToken.equals("BEGIN"))
        {
            eat("BEGIN");
            while (!currentToken.equals("END"))
            {
                parseBlock();
            } 
            eat("END");
            eat(";");
        } 
        else
        {
            String varname = currentToken;
            eat(varname);
            System.out.println("new var " + varname);
            eat(":=");
            variables.put(varname, parseExpression());
            eat(";");
        }
    }

    /**
     * This method parses a block of code, made up of nothing or some
     * amount of commands inside a BEGIN and END statement. When the
     * END token is reached, the block has
     * ended and the parsing stops.
     * 
     * @precondition the parser has recognized a BEGIN token
     * 
     */
    private void parseBlock() throws ScanErrorException
    {
        while (!currentToken.equals("END"))
        {
            parseStatement();
        }
    }

    /**
     * This is the general method for parsing a factor, which is 
     * an expression that contains the following possible values:
     * a number itself, a number with a negative sign preceding it,
     * or some expression enclosed in parentheses.
     * 
     * @precondition the current token takes one of the three forms described
     *               above
     * @return int is the integer value of the factor
     */
    private int parseFactor() throws ScanErrorException
    {
        int factor = -1;
        if(currentToken.equals("("))
        {
            eat("(");
            if (variables.containsKey(currentToken))
            {
                factor = variables.get(currentToken);
            }
            else
            {
                factor = parseExpression();
            }
            eat(")");
        } 
        else if (currentToken.equals("-")) 
        {
            eat("-");
            if (variables.containsKey(currentToken))
            {
                factor = -1 * variables.get(currentToken);
            }
            else
            {
                factor = -1 * parseFactor();
            }
        } 
        else
        {
            if (variables.containsKey(currentToken))
            {
                factor = variables.get(currentToken);
                eat(currentToken);
            } 
            else
            {
                factor = parseNumber();
            }   
        }
        return factor;
    }

    /**
     * This method parses a term, which is any expression that begins with
     * a factor and contains any number of factors with * or / in
     * between the factors. By processing terms,
     * this method allows for multiplication and division between factors. 
     * 
     * @precondition the next stream of tokens is a valid term containing
     *               only factors and * or / operations in between them
     * @return int is the integer value of the term
     */
    private int parseTerm() throws ScanErrorException
    {
        int term = -1;
        term = parseFactor();
        while (currentToken.equals("*") | currentToken.equals("/"))
        {
            if (currentToken.equals("*"))
            {
                eat(currentToken);
                term *= parseFactor();
            } 
            else if (currentToken.equals("/"))
            {
                eat(currentToken);
                term /= parseFactor();
            }
        }
        return term;
    }

    /**
     * This method parses an "expression," which is any stream of tokens that
     * begins with a term and contains any combination of terms with possible
     * + or - operations in between them. By processing "expressions,"
     * this method allows for addition and subtraction between terms, where
     * addition and multiplication is always done after multiplication and
     * division to maintain the proper order of operations.
     * 
     * @precondition the next stream of tokens is a valid expression 
     *               containing only terms and + or - operations 
     *               in between them
     * @return int is the integer value of the term
     */
    private int parseExpression() throws ScanErrorException
    {
        int expression = -1;
        expression = parseTerm();
        while (currentToken.equals("+") | currentToken.equals("-"))
        {
            if (currentToken.equals("+"))
            {
                eat(currentToken);
                expression += parseTerm();
            } 
            else if (currentToken.equals("-"))
            {
                eat(currentToken);
                expression -= parseTerm();
            }
        }
        return expression;
    }

}
