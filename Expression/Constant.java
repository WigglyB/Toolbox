package toolbox.Expression;

import java.util.Map;

public class Constant implements Expression {
    Number value;
    public Constant(Number v){
        value=v;
    }
    public double evaluate(Map<String, Double> variables) {
        return value.doubleValue();
    }
    public String Trace(){
        return ""+value;
    }
}
