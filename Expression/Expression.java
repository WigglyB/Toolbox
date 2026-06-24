package toolbox.Expression;

import java.io.Serializable;
import java.util.Map;

public interface Expression extends Serializable{
    public abstract double evaluate(Map<String,Double> variables);
    public abstract String Trace();
    
    public static Expression PI=new Expression() {
        @Override
        public double evaluate(Map<String, Double> variables) {
            return Math.PI;
        }
        public String Trace(){
            return "π";
        }
        
    };
    public static Expression E=new Expression() {
        @Override
        public double evaluate(Map<String, Double> variables) {
            return Math.E;
        }
        public String Trace(){
            return "e";
        }
        
    };
} 
