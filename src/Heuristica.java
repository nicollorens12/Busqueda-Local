import aima.search.framework.HeuristicFunction;

public class Heuristica implements HeuristicFunction {
    public double getHeuristicValue(Object n){
        return ((Estado) n).Heuristica();
    }
}
