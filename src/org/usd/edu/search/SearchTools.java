package org.usd.edu.search;

/*
 * Author: Mark Helenurm
 * The University of South Dakota
 * Phenoscape Project
 * Date: 13 July 2012
 * Mark.Helenurm@usd.edu
 */

import java.util.ArrayList;
public class SearchTools {
    private static String lastError = "";
    
    public static final String expressionNames[] = new String[]{"~", "contains", "!~", "doesntcontain", "=", "==", "equals", "isequalto", "!=", "doesntequal", ">", "greaterthan", ">=", "greaterthanorequalto", "gequalto", "gequal", "<", "lessthan", "<=", "lessthanorequalto", "lequalto", "lequal"};
    
    public static final String operatorNames[] = new String[]{"&", "&&", "and", "|", "||", "or", "^", "xor", "exclusiveor"};
    
    public static boolean isOperator(String s) {
        for(int i = 0; i < operatorNames.length; i++) {
            if(operatorNames[i].equals(s))
                return true;
        }
        return false;
    }
    
    public static boolean isExpression(String s) {
        for(int i = 0; i < expressionNames.length; i++) {
            if(expressionNames[i].equals(s))
                return true;
        }
        return false;
    }
    
    public static boolean isParenthesis(String s) {
        return s.length() == 1 && (s.charAt(0) == '(' || s.charAt(0) == ')');
    }
    
    private static String prepareSearchString(String input) { //lowercases it, and then separates parentheses by spaces so they're their own entities
        SearchScanner s = new SearchScanner(input.toLowerCase());
        String output = "";

        while(s.hasNext()) {
            String next = s.next();
            //now we take the parentheses from the beginning and end of the word
            char first = next.charAt(0);
            char last = next.charAt(next.length()-1);
            if(first == '(' || first == ')' || last == '(' || last == ')') {
                String truenext = "";
                int truebegin = 0;
                for(int i = 0; i < next.length(); i++) { //take off the first parentheses and find the true beginning
                    char c = next.charAt(i);
                    if(c == ')' || c == '(')
                    {
                        output += c + " ";
                    } else {
                        truebegin = i;
                        break;
                    }
                }
                next = next.substring(truebegin, next.length());
                String newbuf = "";
                int endindex = 0;
                for(int i = next.length()-1; i >= 0; i--) {
                    char c = next.charAt(i);
                    if(c == ')' || c == '(')
                    {
                        newbuf = " " + c + newbuf;
                    } else {
                        endindex = i;
                        break;
                    }
                }
                next = next.substring(0, endindex+1);
                output += next + newbuf + " ";
            } else {
                output += next + " ";
            }
        }
        return output.toLowerCase().trim();
    }
    
    public static boolean verifySearchString(String input) {
        lastError = "";
        input = prepareSearchString(input);
        SearchScanner s1 = new SearchScanner(input);
        //check parentheses to make sure they're paired correctly
        int pcount = 0;
        while(s1.hasNext()) {
            String next = s1.next();
            if(next.equals("" + '(')) {
                pcount++;
            } else if(next.equals("" + ')')) {
                pcount--;
            }
            if(pcount<0)
            {
                lastError = "Incorrect Parenthesis Syntax";
                return false;
            }
        }
        if(pcount>0)
        {
            lastError = "Incorrect Parenthesis Syntax";
            return false;
        }
        
        //check groups of three terms separated only by parentheses and operators (which must be corrently formatted)
        int opformat = 0; //if in the loop this gets below zero or over two, fail with an error ("Incorrect Logical Operator Syntax")
        s1 = new SearchScanner(input);
        boolean ing3 = false; //are we in a group of three
        int g3ct = 0; //where in the 3-triple we are
        
        ArrayList<Integer> total = new ArrayList<Integer>();
        
        Integer rp = 0; //opening parenthesis
        Integer lp = 1; //closing parenthesis
        Integer g1 = 2; //number 1 of a 3-group
        Integer g2 = 3; //number 2 of a 3-group
        Integer g3 = 4; //number 3 of a 3-group
        Integer lo = 5; //logical operator
        while(s1.hasNext()) {
            String s = s1.next();
            if(isParenthesis(s)) {
                if(s.equals("("))
                    total.add(rp);
                else
                    total.add(lp);
            } else if(isOperator(s)) {
                total.add(lo);
            } else if(isExpression(s)) {
                total.add(g2);
            } else {
                if(total.size()!=0&&total.get(total.size()-1) == g2) {
                    total.add(g3);
                } else {
                    total.add(g1);
                }
            }
        }
        /* RULES:
         
         3 has to be between 2 and 4
         3 has to come after 2
         3 has to come before 4
         
         1 cannot be before 2
         0 cannot be after 4
         
         0 cannot be before 5
         1 cannot be after 5
         
         
         
         */
        if(total.size()<3) {
            if(total.size()==1) {
                return true;
            }
            lastError = "Too Few Operators";
            return false;
        }
        for(int i = 0; i < total.size(); i++) {
            int current = total.get(i);
            if(i==0) {
                if(current != 2 && current != 0) {
                    lastError = "Incorrect Starting Syntax";
                    return false;
                }
                if(current == 2 && total.size()>1 && total.get(1) != 3) {
                    lastError = "Incorrect Triple Syntax";
                    return false;
                }
            } else if(i == total.size()-1) {
                if(current != 4 && current != 1) {
                    lastError = "Incorrect Ending Syntax";
                    return false;
                }
            } else {
                int next = total.get(i+1);
                int prev = total.get(i-1);
                if(current == 3) {
                    if(prev != 2 && next != 4) {
                        lastError = "Incorrect Syntax";
                        return false;
                    }
                }
                if(current == 2) {
                    if(next!=3) {
                        lastError = "Incorrect Syntax";
                        return false;
                    }
                }
                if(current == 4) {
                    if(prev!=3) {
                        lastError = "Incorrect Syntax";
                        return false;
                    }
                }
                if(current == 0) {
                    if(prev == 4) {
                        lastError = "Incorrect Parenthesis Syntax";
                        return false;
                    }
                    if(next  == 5) {
                        lastError = "Incorrect Parenthesis Syntax";
                        return false;
                    }
                }
                if(current == 1) {
                    if(prev == 5) {
                        lastError = "Incorrect Parenthesis Syntax";
                        return false;
                    }
                    if(next  == 2) {
                        lastError = "Incorrect Parenthesis Syntax";
                        return false;
                    }
                }
            }
        }
        
        
        return true;
    }
    
    public static SearchTree buildTree(String input) { //input MUST be verified
        ArrayList<String> allStrings = new ArrayList<String>();
        SearchScanner s = new SearchScanner(prepareSearchString(input));
        
        while(s.hasNext()) {
            String ss = s.next().trim();
            if(ss.length()!=0) {
                allStrings.add(ss);
            }
        }
        
        if(allStrings.size()==1) {
            return new SearchTree(new EvaluatableExpression(allStrings.get(0)));
        }
        
        SearchItem item = getItemInParentheses(allStrings, 0);
        return new SearchTree(item);
    }
    /*
     This takes a code block and reduces it to one SearchItem (which itself may be composed of more than one searchitem)
     It calls itself at a new parenthesis block, with input staying static, and start being the index of the item after the opening parenthesis
     */
    private static SearchItem getItemInParentheses(ArrayList<String> input, int start) {
        ArrayList<SearchItem> expressions = new ArrayList<SearchItem>();
        ArrayList<SearchOperator> directops = new ArrayList<SearchOperator>();
        //we parse these items out
        int level = 0;
        for(int i = start; i < input.size(); i++) {
            //this will break out when level == -1
            String s = input.get(i);
            if(s.equals("(")) {
                level++;
                if(level==1) {
                    //RECURSIVE CALL
                    SearchItem item = getItemInParentheses(input, i+1);
                    expressions.add(item);
                }
            } else if(s.equals(")")) {
                level--;
                if(level<0)
                    break;
            } else if(isOperator(s) && level == 0) {
                
                    directops.add(new SearchOperator(s));
                
            } else if (level == 0){ //it's the beginning of a triple
                expressions.add(new EvaluatableExpression(input.get(i), input.get(i+1), input.get(i+2)));
                i+=2; //make sure it doesn't do any other weird adding
            }
        }
        //now we have full arrays, where expressions.size() == directops.size()+1
        //find and replace operations in the OOO order, until expressions.size() == 1, then we return the only item in the array
        //LOGICAL AND
        for(int i = 0; i < directops.size(); i++) {
            if(directops.get(i).getOp() == Operator.AND) {
                //surrounding expressions are i and i+1
                SearchOperator reduce = new SearchOperator(Operator.AND, new SearchItem[]{expressions.get(i), expressions.get(i+1)});
                directops.remove(i);
                expressions.remove(i);
                expressions.remove(i);
                expressions.add(i, reduce);
            }
        }
        //LOGICAL XOR
        for(int i = 0; i < directops.size(); i++) {
            if(directops.get(i).getOp() == Operator.XOR) {
                //surrounding expressions are i and i+1
                SearchOperator reduce = new SearchOperator(Operator.XOR, new SearchItem[]{expressions.get(i), expressions.get(i+1)});
                directops.remove(i);
                expressions.remove(i);
                expressions.remove(i);
                expressions.add(i, reduce);
            }
        }
        //LOGICAL OR
        for(int i = 0; i < directops.size(); i++) {
            if(directops.get(i).getOp() == Operator.OR) {
                //surrounding expressions are i and i+1
                SearchOperator reduce = new SearchOperator(Operator.OR, new SearchItem[]{expressions.get(i), expressions.get(i+1)});
                directops.remove(i);
                expressions.remove(i);
                expressions.remove(i);
                expressions.add(i, reduce);
            }
        }
        //done! now we just have to return
        if(expressions.isEmpty()) {
            return null;
        } else {
            return expressions.get(0);
        }
    }
    
    public static String getError() {
        return lastError;
    }
}