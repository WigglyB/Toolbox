package toolbox.Expression;

import java.util.Map;

public class Variable implements Expression {
    String name;
    public Variable(String Name){
        name=Name;
    }
    public double evaluate(Map<String, Double> variables) {
        if(variables.containsKey(name)){
            return variables.get(name);
        }else{
            throw new ArithmeticException("variable not found "+name);
        }
    }
    public String Trace(){
        return name;
    }
}
