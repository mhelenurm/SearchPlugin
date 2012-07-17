package org.usd.edu.search;

import java.util.ArrayList;

public class SearchRules {
    ArrayList<SearchRule> rules;
    
    public SearchRules() {
        rules = new ArrayList<SearchRule>();
    }
    
    public SearchRules(ArrayList<SearchRule> rule) {
        rules = rule;
    }
    
    public void addRule(SearchRule rule) {
        rules.add(rule);
    }
    
    public boolean verifyRules(ArrayList<Integer> values) {
        for(int i = 0; i < values.size(); i++) {
            for(SearchRule r : rules) {
                if(r.ruleApplies((int)values.get(i))) {
                    if(!r.verifyRule((i==0)?-1:(int)values.get(i-1), (int)values.get(i), (i==values.size()-1)?-1:(int)values.get(i+1)))
                        return false;
                }
            }
        }
        return true;
    }
}