package toolbox.Expression;

import java.util.Map;

public class Root implements Expression {
    Expression left,right;
    public Root(Expression bottom,Expression top){
        this.left=bottom;
        this.right=top;
    }
    public Root(Number left,Expression right){
        this(new Constant(left), right);
    }
    public Root(Expression left,Number right){
        this(left, new Constant(right));
    }
    public Root(Number left,Number right){
        this(new Constant(left),new Constant(right));
    }
    @Override
    public double evaluate(Map<String, Double> variables) {
        return Math.pow(left.evaluate(variables),1/right.evaluate(variables));
    }
    public String Trace(){
        return "root("+left.Trace()+","+right.Trace()+")";
    }
}
