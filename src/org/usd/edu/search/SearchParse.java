package org.usd.edu.search;

import java.util.ArrayList;
import java.util.Scanner;

/*
 * Author: Mark Helenurm
 * The University of South Dakota
 * Phenoscape Project
 * Date: 13 July 2012
 * Mark.Helenurm@usd.edu
 */

public class SearchParse {
    
    
    
    class SearchTree {
        private ArrayList<EvaluatableExpression> comparisons;
        
        public void reset() {
            for(EvaluatableExpression ex : comparisons)
            {
                ex.reset();
            }
        }
    }
    
    
    
    
    
    
    
    
}