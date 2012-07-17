package org.usd.edu.search;

/*
 * Author: Mark Helenurm
 * The University of South Dakota
 * Phenoscape Project
 * Date: 13 July 2012
 * Mark.Helenurm@usd.edu
 */

public class SearchScanner {
    int cursor;
    String readIn;
    
    public SearchScanner(String s) {
        readIn = s.trim();
        cursor = 0;
    }
    
    public String next() {
        if(!hasNext())
            return "";
        String out = "";
        boolean inWord = false;
        boolean inQuotes = false; //any spaces between quotes is ignored. if the quote never ends, it will read to the end of the line.
        for(; cursor < readIn.length(); cursor++)
        {
            char c = readIn.charAt(cursor);
            if(inWord) {
                if(c == ' ') { 
                    if(inQuotes) {
                        out+=c;
                    } else {
                        break;
                    }
                } else {
                    out+=c;
                    if(c == '"')
                    {
                        if(inQuotes)
                            inQuotes = false;
                        else
                            inQuotes = true;
                    }
                }
            } else {
                if(c != ' ') {
                    inWord = true;
                    out += c;
                    if(c == '"')
                        inQuotes = true;
                }
            }
        }
        return out;
    }
    public boolean hasNext() {
        return cursor != readIn.length();
    }
}