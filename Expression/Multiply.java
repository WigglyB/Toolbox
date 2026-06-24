package toolbox.Expression;

import java.util.Map;

public class Multiply implements Expression {
    Expression left,right;
    public Multiply(Expression left,Expression right){
        this.left=left;
        this.right=right;
    }
    public Multiply(Number left,Expression right){
        this(new Constant(left), right);
    }
    public Multiply(Expression left,Number right){
        this(left, new Constant(right));
    }
    public Multiply(Number left,Number right){
        this(new Constant(left),new Constant(right));
    }
    @Override
    public double evaluate(Map<String, Double> variables) {
        return left.evaluate(variables)*right.evaluate(variables);
    }
    public String Trace(){
        return "("+left.Trace()+")*("+right.Trace()+")";
    }
}
