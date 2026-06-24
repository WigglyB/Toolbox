package toolbox.Expression;

import java.util.Map;

public class Divide implements Expression {
    Expression left,right;
    public Divide(Expression left,Expression right){
        this.left=left;
        this.right=right;
    }
    public Divide(Number left,Expression right){
        this(new Constant(left), right);
    }
    public Divide(Expression left,Number right){
        this(left, new Constant(right));
    }
    public Divide(Number left,Number right){
        this(new Constant(left),new Constant(right));
    }
    @Override
    public double evaluate(Map<String, Double> variables) {
        double v=right.evaluate(variables);
        if(v!=0){
        return left.evaluate(variables)/v;
        }else{
            return Double.POSITIVE_INFINITY;
        }
    }
    public String Trace(){
        return "("+left.Trace()+")/("+right.Trace()+")";
    }
}
