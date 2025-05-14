package clases;

public class clases {

    public static class Map {
        private int row;
        private int column;
        private String name;
        private char[][] terrenos;
        private int[][] objetos;

        public Map(int row, int column, String name) {
            this.row = row;
            this.column = column;
            this.name = name;
            this.terrenos = new char[row][column]; // inicializar la matriz
        }

        public int getRow() { return row; }
        public int getColumn() { return column; }
        public String getName() { return name; }

        public char getTerreno(int fila, int columna) {
            return terrenos[fila][columna];
        }

        public void setTerreno(int fila, int columna, char valor) {
            terrenos[fila][columna] = valor;
        }
        public int getObjeto(int fila, int columna){
            return objetos[fila][columna];
        }
        public void setObjeto(int fila, int columna, int valor) {
            objetos[fila][columna] = valor;
        }
    }

}
