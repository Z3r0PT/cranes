import java.util.*;

public class BestFirst {
    /**
     * Classe State
     */
    static class State {
        private Ilayout layout;
        private State father;
        private double g;
        private int h;
        private int f;
        /**
         * Construtor da classe State
         * @param l Layout do estado atual
         * @param n State pai do layout atual
         */
        public State(Ilayout l, State n) {
            layout = l;
            father = n;
            h = l.getH();
            f = ((int) l.getG()) + l.getH();
            if (father!=null)
                g = father.g + l.getG();
            else g = 0.0;
        }
        /**
         * Metodo que devolve o State atual com Matriz 3x3
         * @return  Matriz 3x3 do estado atual
         */
        public String toString() { return layout.toString(); }
        /**
         * Metodo que devolve o custo do estado atual
         * @return  Custo do estado atual
         */
        public double getG() {return g;}
        /**
         * Metodo que retorna o pai do State atual
         * @return  Pai do State atual
         */
        public State getFather(){
            return this.father;
        }
    }



    protected Queue<State> abertos; //Valores a testar
    private List<State> fechados; //Valores testados
    private State actual; //Estado atual
    private Ilayout objective; //Layout objetivo

    /**
     * Metodo que gera todos os sucessores de um estado
     * @param n Estado
     * @return  Lista de sucessores do estado
     */
    final private List<State> sucessores(State n) {
        List<State> sucs = new ArrayList<>();
        List<Ilayout> children = n.layout.children();
        for(Ilayout e: children) {
            if (n.father == null || !e.equals(n.father.layout)){
                State nn = new State(e, n);
                sucs.add(nn);
            }
        }
        return sucs;
    }

    /**
     * Metodo que executa o algoritmo Best First Search
     * @param s Layout inicial
     * @param goal  Layout objetivo
     * @return  Iterador do caminho de menor custo calculado pelo algoritmo
     */
    final public Iterator<State> solve(Ilayout s, Ilayout goal) {
        objective = goal;
        Queue<State> abertos = new PriorityQueue<>(10,
                (s1, s2) -> (int) Math.signum((s1.g + s1.h) - (s2.g + s2.h)));
        //List<State> fechados = new ArrayList<>();
        Hashtable<Ilayout, BestFirst.State> fechados = new Hashtable<>();
        abertos.add(new State(s, null));
        List<State> sucs;
        Iterator<State> i = abertos.iterator();
        State solution = null;
        while (!abertos.isEmpty()){
            State v = abertos.poll();
            if(v.layout.isGoal(objective)){
                solution = v;
                break;
            }else{
                sucs = sucessores(v);
                for(State state : sucs){
                    if(!fechados.containsKey(v.layout)){
                        abertos.add(state);
                    }
                }
            }
            fechados.put(v.layout, v);
        }
        return new IT(solution);
    }
    }

/**
 * Classe iterador de Best First
 */
    class IT implements Iterator<BestFirst.State> {
    private BestFirst.State last;
    private Stack<BestFirst.State> stack;
    /**
     * Construtor de classe IT
     * @param actual  Estado de Best First
     */
    public IT(BestFirst.State actual) {
        last = actual;
        stack = new Stack<BestFirst.State>();
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
    public BestFirst.State next() {
        return stack.pop();
    }
    /**
     * Metodo que remove elementos da stack(Nao utilizado)
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}

