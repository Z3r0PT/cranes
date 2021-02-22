import java.util.*;

public class Board  implements Ilayout, Cloneable {
    private static final int dim = 3;
    private int board[][];
    private int goal[][] = new int[dim][dim];
    private String s;
    private int g;
    private HashMap<Integer, Point> map_goal = new HashMap<Integer, Point>();
    private String objective;

    public Board() {
        board = new int[dim][dim];
    }

    /**
     * Construtor de Board, este construtor é usado apenas para o estado inicial onde não existe custo
     * @param str  Estado inicial
     * @param obj   Estado final
     */
    public Board(String str, String obj){
        s = str;
        g = 0;
        if (str.length() != dim * dim) throw new
                IllegalStateException("Invalid arg in Board constructor");
        board = new int[dim][dim];
        int si = 0;
        objective = obj;
        setGoal(obj);
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                board[i][j] = Character.getNumericValue(str.charAt(si++));
            }
        }

    }

    /**
     *  Construtor de Board
     * @param str   Estado inicial
     * @param G Custo do estado
     * @param obj   Estado final
     * @throws IllegalStateException
     */
    public Board(String str, int G, String obj) throws IllegalStateException {
        s = str;
        g = G;
        if (str.length() != dim * dim) throw new
                IllegalStateException("Invalid arg in Board constructor");
        board = new int[dim][dim];
        int si = 0;
        objective = obj;
        setGoal(obj);
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                board[i][j] = Character.getNumericValue(str.charAt(si++));
            }
        }
    }

    /**
     * Metodo que retorna o estado em forma de matrix 3x3
     * @return  Matriz 3x3 do estado atual
     */
    public String toString() {
        String str = s;
        return str.substring(0, 3)+"\n"+
                str.substring(3, 6)+"\n"+
                str.substring(6, 9)+"\n";
    }

    /*@Override
    public List<Ilayout> children(){
        List<Ilayout> childs = new ArrayList<>();
        for (int i = 0; i < (s.length()-1); i++){
            for (int j = (i+1); j < s.length(); j++){
                String word = s;
                int v = setValue(word.charAt(i), word.charAt(j));
                StringBuilder sb = new StringBuilder(word);
                sb.setCharAt(i, word.charAt(j));
                sb.setCharAt(j, word.charAt(i));
                childs.add(new Board(new String(sb), v, objective));
            }
        }
        return childs;
    }*/

    /**
     * Metodo que gera todos os nos sucessores deste estado
     * @return  Lista de todos os nos sucessores deste estado
     */
    @Override
    public List<Ilayout> children(){
        List<Ilayout> childs = new ArrayList<>();
        int size = s.length();
        int position = 0;
        int swap_pos = position + 1;
        try {
            while (position < size) {
                char arr[] = s.toCharArray();
                char value = arr[position];
                char swap_value = arr[swap_pos];
                arr[position] = swap_value;
                arr[swap_pos] = value;
                int cost = setValue(value, swap_value);
                if (cost != 20) {
                    childs.add(new Board(new String(arr), cost, objective));
                }
                swap_pos++;
                if (swap_pos == size && position != (size - 1)) {
                    position++;
                    swap_pos = position + 1;
                }
            }
        }catch (Exception e){
            return childs;
        }
        return childs;
    }

    /**
     * Metodo que verifica se um determinado estado e a solucao
     * @param l Estado a verificar
     * @return  true se for a solucao, false se nao for
     */
    @Override
    public boolean isGoal(Ilayout l) {
        return this.equals(l);
    }

    /**
     * Metodo que compara dois objetos
     * @param that  Objeto a comparar
     * @return  true se forem iguais, false se não forem iguais
     */
    @Override
    public boolean equals(Object that) {
        if (that instanceof Board) {
            Board b = ((Board) that);
            return s.equals(b.s);
        } else
            return false;
    }

    /**
     * Metodo que calcula o hashcode
     * @return  Hashcode do estado
     */
    @Override
    public int hashCode() {
        return 17 * Arrays.deepHashCode(this.board);
    }

    /**
     * Metodo que verifica se um caracter e um numero par
     * @param c Caracter a verificar
     * @return  true se for par, false se não for par
     */
    public boolean isEven(char c){
        switch (c){
            case '0':
            case '2':
            case '4':
            case '6':
            case '8':
                return true;
        }
        return false;


    }

    /**
     * Metodo para definir o estado objetivo
     * @param str   Estado objetivo
     */
    public void setGoal(String str) {
        int position = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                this.map_goal.put(Character.getNumericValue(str.charAt(position)), new Point(i,j));
                this.goal[i][j] = Character.getNumericValue(str.charAt(position++));
            }
        }

    }

    /**
     * Metodo que devolve o custo de uma troca
     * @param c1  Primeiro caracter a ser trocado
     * @param c2  Segundo caracter a ser trocado
     * @return  Custo da troca
     */
    public int setValue(char c1, char c2){
        if(isEven(c1) && isEven(c2)){
            return 20;
        }
        if((isEven(c1) && !isEven(c2)) || (!isEven(c1) && isEven(c2))){
            return 5;
        }
        if(!isEven(c1) && !isEven(c2)){
            return 1;
        }
        return 0;
    }

    /**
     * Metodo que devolve o custo do estado atual
     * @return  Custo do estado atual
     */
    @Override
    public double getG() {
        return g;
    }

    /**
     * Metodo que o HashMap dos pontos feitos com o objetivo
     * @return
     */
    public HashMap<Integer, Point> getMap() {
        return this.map_goal;
    }

    /**
     * Metodo que calcula a distancia de Manhattan
     * @return Valor da distancia de Manhattan para todos os pontos
     */
    public int manhattan() {
        HashMap<Integer, Point> map_goal = this.getMap();
        int result = 0;
        int s = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                s = this.board[i][j];
                if (s != 0) {
                    Point piece = map_goal.get(s);
                    result += (Math.abs(j - piece.getY()) + Math.abs(i - piece.getX()));
                }
            }
        }
        return result;
    }

    /**
     * Metodo que verifica quantos blocos estao fora de ordem quando comparados com o objetivo (Metodo nao utilizado, substituido por distancia de Hamming)
     * @return  Total de blocos fora de ordem
     */
    public int BlockDifferent() {
        int result = 0;
        int s = 0;
        int g = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                s = this.board[i][j];
                g = this.goal[i][j];
                if (s != g) {
                    result++;
                }
            }
        }
        return result;
    }

    /**
     * Metodo que calcula a ditancia de Hamming
     * @return  Total de blocos fora de ordem
     */
    public int Hamming(){
        int i = 0;
        int result = 0;
        while(i < s.length()){
            if(s.charAt(i) != objective.charAt(i)){
                result++;
            }
            i++;
        }
        return result;
    }

    /**
     * Metodo de devolve a heuristica com o mairo valor
     * @return  Maior valor calculado das heuristicas
     */
    public int getMaxHeu()
    {
        int x = this.manhattan();
        int k = this.BlockDifferent();
        int z = this.Hamming();
        if (x >= z)
            return  x;
        else
            return z;
    }

    /**
     * Metodo que devolve o valor calculado das heuristicas para o estado atual
     * @return  Valor calculado das heuristicas
     */
    @Override
    public int getH() {
        return this.getMaxHeu();
    }}


