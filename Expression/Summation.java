package toolbox.Expression;

import java.util.HashMap;
import java.util.Map;

public class Summation implements Expression {
    Expression Analyzed;
    String varName;
    Expression start,end;
    public Summation(Expression analyzed,String varName,Expression start, Expression end){
        this.Analyzed=analyzed;
        this.varName=varName;
        this.start=start;
        this.end=end;
    }
    @Override
    public double evaluate(Map<String, Double> variables) {
        double total=0;
        Map<String,Double> localVars=new HashMap<>(variables);
        double startpoint=start.evaluate(variables);
        double endpoint=end.evaluate(variables);
        for(double i=startpoint;i<endpoint;i++){
            localVars.put(varName, (double)i);
            total+=Analyzed.evaluate(localVars);
        }
        return total;
    }
    public String Trace(){
        return "sum("+Analyzed.Trace()+")with ("+start.Trace()+")<"+varName+"<("+end.Trace()+")";
    }
}
