import java.util.*;

class IDS {
    List<State> seq = new ArrayList<State>();
    private State actual;
    private Ilayout objective; //Layout objetivo
    public static int generated = 0;
    public static int expanded = 0;
    public Hashtable<Ilayout, Integer> visited = new Hashtable<Ilayout, Integer>(); //Nos visitados
    /**
     * Classe State
     */
    public class State {
        private Ilayout state;
        private State father;
        private double g; // cost from the current to the initial node
        /**
         * Metodo que retorna o pai do State atual
         * @return  Pai do State atual
         */
        public State getFather() {
            return this.father;
        }
        /**
         * Construtor da classe State
         * @param l Layout do estado atual
         * @param n State pai do layout atual
         */
        public State(Ilayout l, State n) {
            state = l;
            father = n;
            if (father!=null)
                g = father.g + l.getG();
            else g = 0;
        }
        /**
         * Metodo que devolve o State atual
         * @return  State atual
         */
        public Ilayout getState() {
            return state;
        }
        /**
         * Metodo que devolve o State atual com Matriz 3x3
         * @return  Matriz 3x3 do estado atual
         */
        public String toString() {//feito aqui
            //return "(" + state.toString() + ", " + g + ") ";
            return state.toString();
        }

        /**
         * Metodo que devolve o custo do estado atual
         * @return  Custo do estado atual
         */
        public double getDepth() {
            return g;
        }
    }


    /**
     * Metodo que gera todos os sucessores de um estado
     * @param n Estado
     * @return  Lista de sucessores do estado
     */
    final private List<State> sucessores(State n) {
        //expanded++;
        List<State> sucs = new ArrayList<State>();
        List<Ilayout> children = n.state.children();
        for (Ilayout e : children) {
            if (n.father == null || !e.equals(n.father.state)) {
                State nn = new State(e, n);
                sucs.add(nn);
                //generated++;
            }
        }
        return sucs;
    }
    /**
     * Metodo que executa o algoritmo IDS
     * @param s Layout inicial
     * @param goal  Layout objetivo
     * @return  Iterador do caminho de menor custo calculado pelo algoritmo
     */
    final public Iterator<State> solve(Ilayout s, Ilayout goal) {
        State result = null;
        State start = new State(s,null);
        objective = goal;

        for(int i = 0; i < Integer.MAX_VALUE; i++) {
            //visited = new Hashtable<Ilayout,Integer>();
            result = DLS(start,i);

            if(result != null) {
                return new IT1(result);
            }
        }
        return null;
    }

    /**
     * Metodo DLS que implementa a busca atraves de nivel, chamado de forma recursiva
     * @param s Estado
     * @param p Nivel
     * @return  estado solucao
     */
    final public State DLS(State s, int p) {
        State result = null;
        if(s.state.equals(objective)) {
            return s;
        }

        if(p <= 0)
            return null;

        visited.put(s.state, p);
        for(State state: sucessores(s)) {
            if((visited.containsKey(state.state) && visited.get(state.state) < p -1) || !visited.containsKey(state.state)) {
                result = DLS(state, p-1);
                if(result != null) {
                    return result;
                }
            }
        }

        return null;
    }
}
/**
 * Classe iterador de IDS
 */
class IT1 implements Iterator<IDS.State> {
    private IDS.State last;
    private Stack<IDS.State> stack;
    /**
     * Construtor de classe IT1
     * @param actual  Estado de IDS
     */
    public IT1(IDS.State actual) {
        last = actual;
        stack = new Stack<IDS.State>();
        while (last != null) {
            stack.push(last);
            last = last.getFather();
        }
    }
    /**
     * Metodo que verifica se a stack tem proximo valor
     * @return  true se tiver proximo valor, false caso contrario
     */
    public boolean hasNext() {
        return !stack.empty();
    }
    /**
     * Metodo que devolve o primeiro elemento da stack
     * @return  Primeiro elemento da stack
     */
    public IDS.State next() {
        return stack.pop();
    }
    /**
     * Metodo que remove elementos da stack(Nao utilizado)
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}