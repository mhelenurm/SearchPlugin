package org.usd.edu.search;

/*
 * Author: Mark Helenurm
 * The University of South Dakota
 * Phenoscape Project
 * Date: 13 July 2012
 * Mark.Helenurm@usd.edu
 */

import java.util.*;

public class SearchTree {
    private SearchItem _root;
    private ArrayList<EvaluatableExpression> _allExp;
    
    public SearchTree(SearchItem root) {
        _root = root;
        _allExp = new ArrayList<EvaluatableExpression>();
        addExp(_root);
    }
    
    public boolean evaluate() {
        return _root.evaluate();
    }
    
    private void addExp(SearchItem item) {
        if(item instanceof SearchOperator) {
            SearchItem[] nodes = ((SearchOperator)item).getNodes();
            addExp(nodes[0]);
            addExp(nodes[1]);
        } else if(item instanceof EvaluatableExpression) {
            _allExp.add((EvaluatableExpression)item);
        }
    }
    
    public void testAnnotation(String type, String val) {
        for(EvaluatableExpression ex : _allExp) {
            ex.hitCheck(type, val);
        }
    }
    
    public void clear() {
        for(EvaluatableExpression ex : _allExp) {
            ex.reset();
        }
    }
}