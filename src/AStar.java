import java.text.DecimalFormat;
import java.util.*;

class AStar {
    public static int generated = 0;
    public static int expanded = 0;
    DecimalFormat df = new DecimalFormat("#,###");


    /**
     * Classe State
     */
    public class State {
        private Ilayout state;
        private State father;
        private double g; // cost from the current to the initial node
        private int h;

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
            h = l.getH();
            if (father != null)
                g = father.g + l.getG();
            else
                g = 0.0;
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
        public double getG() {
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
     * Metodo que executa o algoritmo AStar
     * @param s Layout inicial
     * @param goal  Layout objetivo
     * @return  Iterador do caminho de menor custo calculado pelo algoritmo
     */
    final public Iterator<State> solve(Ilayout s, Ilayout goal) {
        State actual;
        State v = null;
        Hashtable<Ilayout, State> fechados_table = new Hashtable<Ilayout, State>();
        Queue<State> abertos = new PriorityQueue<State>(10, (s1, s2) -> (int) Math.signum((s1.g + s1.h) - (s2.g + s2.h)));
        abertos.add(new State(s, null));
        List<State> sucs;
        while (!abertos.isEmpty()) {
            actual = abertos.poll();
            if (goal.isGoal(actual.state)) {
                abertos.clear();
                fechados_table.clear();
                //System.out.println("generated nodes = " + df.format(generated).replaceAll(",", " "));
                //System.out.println("expanded nodes = " + df.format(expanded).replaceAll(",", " "));
                v = actual;
            } else {
                sucs = sucessores(actual);
                fechados_table.put(actual.state, actual);
                boolean contains;
                for (State e : sucs) {
                    contains = false;
                    if (fechados_table.containsKey(e.state)) {
                        contains = true;
                        if (fechados_table.get(e.state).getG() > actual.getG()) {
                            fechados_table.get(e.state).father = actual;
                        }
                    }

                    if (!contains)
                        abertos.add(e);
                }
            }
        }
        return new IT2(v);
    }
}

/**
 * Classe iterador de AStar
 */
class IT2 implements Iterator<AStar.State> {
    private AStar.State last;
    private Stack<AStar.State> stack;

    /**
     * Construtor de classe IT2
     * @param actual  Estado de AStar
     */
    public IT2(AStar.State actual) {
        last = actual;
        stack = new Stack<AStar.State>();
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
    public AStar.State next() {
        return stack.pop();
    }

    /**
     * Metodo que remove elementos da stack(Nao utilizado)
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}

