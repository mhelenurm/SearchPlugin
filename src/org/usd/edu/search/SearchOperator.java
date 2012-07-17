package org.usd.edu.search;

/*
 * Author: Mark Helenurm
 * The University of South Dakota
 * Phenoscape Project
 * Date: 13 July 2012
 * Mark.Helenurm@usd.edu
 */

public class SearchOperator extends SearchItem {
    
    
    
    private static final Operator OPS[] = new Operator[] {
        Operator.AND,
        Operator.OR,
        Operator.XOR
    };
    
    private static final String OPNAMES[][] = new String[][] {
        {"&", "&&", "and"},
        {"|", "||", "or"},
        {"^", "xor", "exclusiveor"}
    };

    private Operator _operator;
    private SearchItem _nodes[];
    
    public SearchOperator(String operator, SearchItem nodes[]) {
        _operator = Operator.AND;
        _nodes = nodes;
        for(int i = 0; i < OPNAMES.length; i++)
        {
            for(int j = 0; j < OPNAMES[i].length; j++)
            {
                if(operator.equals(OPNAMES[i][j]))
                {
                    _operator = OPS[i];
                }
            }
        }
    }
    public SearchOperator(Operator operator, SearchItem nodes[]) {
        _operator = operator;
        _nodes = nodes;
    }
    
    public SearchOperator(String operator) {
        _operator = Operator.AND;
        for(int i = 0; i < OPNAMES.length; i++)
        {
            for(int j = 0; j < OPNAMES[i].length; j++)
            {
                if(operator.equals(OPNAMES[i][j]))
                {
                    _operator = OPS[i];
                }
            }
        }
        _nodes = new SearchItem[2];

    }
    
    public void setNodes(SearchItem[] nodes) {
        _nodes = nodes;
    }
    
    public SearchItem[] getNodes() {
        return _nodes;
    }
    
    public Operator getOp() {
        return _operator;
    }
    
    //public void add
    
    public boolean evaluate() {
        switch(_operator) {
            case AND:
                return _nodes[0].evaluate() && _nodes[1].evaluate();
            case OR:
                return _nodes[0].evaluate() || _nodes[1].evaluate();
            case XOR:
                return !(_nodes[0].evaluate() && _nodes[1].evaluate() || !_nodes[0].evaluate() && !_nodes[1].evaluate());
        }
        return false;
    }
}