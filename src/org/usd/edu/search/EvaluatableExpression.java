package org.usd.edu.search;

/*
 * Author: Mark Helenurm
 * The University of South Dakota
 * Phenoscape Project
 * Date: 13 July 2012
 * Mark.Helenurm@usd.edu
 */

public class EvaluatableExpression extends SearchItem {
    
    private boolean _value;
    private String _annoType, _annoVal;
    private CompareOp _compare;
    private static final CompareOp ops[] = {
        CompareOp.CONTAINS,
        CompareOp.NOTCONTAINS,
        CompareOp.EQUALS,
        CompareOp.NOTEQUALS,
        CompareOp.GREATERTHAN,
        CompareOp.GEQUAL,
        CompareOp.LESSTHAN,
        CompareOp.LEQUAL
    };
    
    private static final String COMPARISONS[][] = new String[][]{
    {"~", "contains"},
    {"!~", "doesntcontain"}  ,
    {"=", "==", "equals", "isequalto"},
    {"!=", "doesntequal"},
    {">", "greaterthan"},
    {">=", "greaterthanorequalto", "gequalto", "gequal"},
    {"<", "lessthan"},
    {"<=", "lessthanorequalto", "lequalto", "lequal"}
    };
    
    
    /* preconditions: 
     
     annotype, comparison, and annoval are LOWER CASE, and are TRIMMED
     
     */
    public EvaluatableExpression(String annoval) {
        _compare = CompareOp.CONTAINS;
        _annoType = "";
        _annoVal = annoval;
        if(_annoVal.charAt(0) == '"' && _annoVal.charAt(_annoVal.length()-1) == '"') {
            _annoVal = _annoVal.substring(1, _annoVal.length()-1);
        }
    }
    public EvaluatableExpression(String annotype, String comparison, String annoval) {
        _compare = CompareOp.CONTAINS;
        _annoType = annotype;
        _annoVal = annoval;
        if(_annoVal.charAt(0) == '"' && _annoVal.charAt(_annoVal.length()-1) == '"') {
            _annoVal = _annoVal.substring(1, _annoVal.length()-1);
        }
        
        for(int i = 0; i < COMPARISONS.length; i++)
        {
            for(int j = 0; j < COMPARISONS[i].length; j++)
            {
                if(COMPARISONS[i][j].equals(comparison))
                {
                    _compare = ops[i];
                }
            }
        }
    }
    //type and value are LOWERCASE and TRIMMED
    public void hitCheck(String type, String value) {
        if(type.contains(_annoType)) {
            boolean bothNumbers = true;
            double compnum = 0, valnum = 0;
            try {
                compnum = Double.parseDouble(_annoVal);
                valnum = Double.parseDouble(value);
            } catch(Exception e) {
                bothNumbers = false;
            }
            
            switch(_compare) {
                case CONTAINS:
                    if(!_value)
                        _value = value.contains(_annoVal);
                    break;
                case NOTCONTAINS:
                    if(!_value)
                        _value = value.contains(_annoVal);
                    break;
                case EQUALS:
                    if(!_value)
                    {
                        if(bothNumbers)
                            _value = (compnum == valnum);
                        else
                            _value = value.equals(_annoVal);
                    }
                    break;
                case NOTEQUALS:
                    if(!_value)
                    {
                        if(bothNumbers)
                            _value = (compnum == valnum);
                        else
                            _value = value.equals(_annoVal);
                    }
                    break;
                case GREATERTHAN:
                    if(!_value && bothNumbers)
                        _value = valnum>compnum;
                    break;
                case GEQUAL:
                    if(!_value && bothNumbers)
                        _value = valnum>=compnum;
                    break;
                case LESSTHAN:
                    if(!_value && bothNumbers)
                        _value = valnum<compnum;
                    break;
                case LEQUAL:
                    if(!_value && bothNumbers)
                        _value = valnum<=compnum;
                    break;
                case UNDEFINED:
                    _value = false;
                    break;
            }
        }
    }
    
    public boolean evaluate() {
        boolean retval = _value;
        if(_compare == CompareOp.NOTCONTAINS || _compare == CompareOp.NOTEQUALS)
        {
            retval = !retval;
        }
        return retval;
    }
    
    public void reset() {
        _value = false;
    }
}
