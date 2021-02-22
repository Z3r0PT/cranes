public class Point {
    private int x;
    private int y;

    /**
     * Construtor da classe Point
     * @param x Coordenada x
     * @param y Coordenada y
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Metodo que devolve a coordenada x de um ponto
     * @return  Coordenada x de um ponto
     */
    public int getX() {
        return x;
    }

    /**
     * Metodo que define o valor da coordenada x
     * @param x Valor da coordenada x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Metodo que devolve a coordenada y de um ponto
     * @return Coordenada y de um ponto
     */
    public int getY() {
        return y;
    }

    /**
     * Metodo que define o valor da coordenada y
     * @param y Valor da coordenada y
     */
    public void setY(int y) {
        this.y = y;
    }
}