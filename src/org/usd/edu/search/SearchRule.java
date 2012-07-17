package org.usd.edu.search;

public class SearchRule {
    enum Rule {
        ALWAYS_PRECEDES,
        ALWAYS_POSTCEDES,
        NEVER_PRECEDES,
        NEVER_POSTCEDES
    }
    
    private int op1, op2;
    private Rule rule;
    
    public SearchRule(int o1, Rule r, int o2) {
        op1 = o1;
        op2 = o2;
        rule = r;
    }
    
    public boolean ruleApplies(int b) {
        return op1==b;
    }
    
    public boolean verifyRule(int a, int b, int c) { //precondition: ruleApplies(b)
        //a precedes, b is the current item, c postcedes (a and c can be negative if there isn't a postceding value
        switch(rule) {
            case ALWAYS_PRECEDES:
                if(c == -1)
                    return false;
                return c==op2;
            case ALWAYS_POSTCEDES:
                if(a == -1)
                    return false;
                return a==op2;
            case NEVER_PRECEDES:
                return !(c==op2);
            case NEVER_POSTCEDES:
                return !(a==op2);
        }
        return false;
    }
}
