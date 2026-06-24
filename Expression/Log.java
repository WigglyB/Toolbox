package toolbox.Expression;

import java.util.Map;

public class Log implements Expression {
    Expression left,right;
    public Log(Expression top,Expression base){
        this.left=top;
        this.right=base;
    }
    public Log(Number left,Expression right){
        this(new Constant(left), right);
    }
    public Log(Expression left,Number right){
        this(left, new Constant(right));
    }
    public Log(Number left,Number right){
        this(new Constant(left),new Constant(right));
    }
    @Override
    public double evaluate(Map<String, Double> variables) {
        return (Math.log(left.evaluate(variables))/(Math.log(right.evaluate(variables))));
    }
    public String Trace(){
        return "log("+left.Trace()+","+right.Trace()+")";
    }
}