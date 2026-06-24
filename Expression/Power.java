package toolbox.Expression;

import java.util.Map;

public class Power implements Expression {
    Expression left,right;
    public Power(Expression bottom,Expression top){
        this.left=bottom;
        this.right=top;
    }
    public Power(Number left,Expression right){
        this(new Constant(left), right);
    }
    public Power(Expression left,Number right){
        this(left, new Constant(right));
    }
    public Power(Number left,Number right){
        this(new Constant(left),new Constant(right));
    }
    @Override
    public double evaluate(Map<String, Double> variables) {
        return Math.pow(left.evaluate(variables),right.evaluate(variables));
    }
    public String Trace(){
        return "pow("+left.Trace()+","+right.Trace()+")";
    }
}
