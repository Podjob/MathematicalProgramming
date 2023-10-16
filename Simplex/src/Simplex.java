import java.text.DecimalFormat;
import java.util.Scanner;

public class Simplex {

    public static void main(String[] args) {


        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите размерность матрицы");
        System.out.print("Введите параметр m (не включая строки g) : ");
        int m = scanner.nextInt();
        System.out.print("Введите параметр n (не включая столбец 1): ");
        int n = scanner.nextInt();

        m += 1;
        n += 1;

        double[][] matrix = new double[m][n];
        String[] rows = new String[m];
        String[] columns = new String[n];

        /*
                  3  1 -4  2 -5  9
                  6  0  1 -3  4 -5
                  1  0  1 -1  1 -1
                  0  2  6 -5  1  4

                -10 -1  2  2  0 -3
         */

        createMatrix(m, n, matrix, scanner);
        createRows(rows);
        createColumns(columns);

        printMatrix(matrix, columns, rows);

        System.out.println();

        int s = defineS(matrix, m, n);
        int k = defineK(matrix, m, n, s);
        swapColumnsAndRows(columns, rows, s, k);

        System.out.println();

        printMatrix(matrix, columns, rows);

        matrix = jordan(matrix, k, s, m, n);

        printMatrix(matrix, columns, rows);


    }


    public static void algorithm(double[][] matrix, String[] columns, String[] rows, int m, int n, int k, int s){
        boolean status = true;
        while (status) {

            int cheсkNegativeElementFromG = 0;
            int zeroElementFromG = 0;
            // если какой-то g < 0
            for (int i = 1; i < n; i++) {
                if(matrix[m - 1][i] * -1 == Math.abs(matrix[m - 1][i])){
                    cheсkNegativeElementFromG++;
                }
                if(matrix[m - 1][i] == 0){
                    zeroElementFromG++;
                }
            }
            // начало шага 2
            if(cheсkNegativeElementFromG == n - 1){
                int checkZeroF = 0;
                int checkingFIsLessZero = 0;
                for (int i = 1; i < n; i++) {
                    if(matrix[m - 2][i] >= 0){
                        checkZeroF++;
                    }else if(matrix[m - 2][i] < 0){
                        checkingFIsLessZero++;
                    }
                }
                // шаг 7
                if(zeroElementFromG == n - 1 && checkZeroF == n - 1){
                    printMatrix(matrix, columns, rows);
                    System.out.println("План расширенной задачи оптимален!");
                    status = false;
                    continue;
                }else if(checkingFIsLessZero == n - 1){ // шаг 8
                    int p = 0;
                    double maxValue = 0;
                    double currentValue = 0;
                    for (int i = 1; i < n; i++) {
                        if(maxValue <= matrix[m - 2][i]){
                            maxValue = currentValue;
                            p = i;
                        }
                    }
                    s = p;
                    // шаг 9
                    int cheсkNegativeElementFromS = 0;
                    for (int i = 0; i < m - 2; i++) {
                        if(matrix[i][s] < 0){
                            cheсkNegativeElementFromS++;
                        }
                    }
                    if(cheсkNegativeElementFromS == n - 1){
                        printMatrix(matrix, columns, rows);
                        System.out.println("Расширенная задача и исходная задача неразрешимы!");
                        status = false;
                        break;
                    }else{ // шаг 10 и 11
                        k = defineK(matrix, m, n, s);
                        matrix = jordan(matrix, k, s, m, n);
                        printMatrix(matrix, columns, rows);
                        continue;
                    }
                }
            }else{
                s = defineS(matrix, m, n);
            }

            int cheсkNegativeElementFromS = 0;
            for (int i = 0; i < m - 2; i++) {
                if(matrix[s][i] * -1 != Math.abs(matrix[s][i])){
                    cheсkNegativeElementFromS++;
                }
            }
            if(cheсkNegativeElementFromS == m -2){
                printMatrix(matrix, columns, rows);
                System.out.println("Расширенная задача и исходная задача неразрешимы!");
                status = false;
                continue;
            }
        }
    }

    public static double[][] createMatrix(int m, int n, double[][] matrix, Scanner scanner){

        System.out.println("Введите элементы матрицы:");
        for (int i = 0; i < m - 1; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = scanner.nextDouble();
            }
        }

        for (int j = 0; j < n; j++) {
            double sumColumns = 0;
            for (int i = 0; i < m - 2; i++) {
                sumColumns += matrix[i][j];
            }
            matrix[m - 1][j] = -sumColumns;
        }
        return matrix;
    }

    public static String[] createRows(String[] rows){
        rows[rows.length - 2] = "f";
        rows[rows.length - 1] = "g";
        for (int i = 0, j = rows.length + 1; i < rows.length - 2; i++, j++) {
            rows[i] = "x" + j;
        }
        return rows;
    }
    public static String[] createColumns(String[] columns){
        columns[0] = "1";
        for (int i = 1; i < columns.length; i++) {
            columns[i] = "-x" + i;
        }
        return columns;
    }

    public static int defineS(double[][] matrix, int m, int n){
        double maxValue = 0;
        int s = 0;
        for (int i = 1; i < n; i++) {
            if(matrix[m - 1][i] * -1 == Math.abs(matrix[m - 1][i]) && Math.abs(matrix[m - 1][i]) >= maxValue){
                maxValue = Math.abs(matrix[m - 1][i]);
                s = i;
            }
        }
        return s;
    }

    public static int defineK(double[][] matrix, int m, int n, int s){
        int k = 0;
        double minValue = 0;
        double currentValue = 0;
        for (int i = 0; i < m - 2; i++) {
            if(matrix[i][s] != 0){
                currentValue = matrix[i][0] / matrix[i][s];
                if(minValue >= currentValue){
                    minValue = currentValue;
                    k = i;
                }
            }
        }
        return k;
    }


    public static void swapColumnsAndRows(String[] columns, String[] rows, int s, int k){
        String d = columns[s].substring(1);
        columns[s] = "-" + rows[k];
        rows[k] = d;
    }

    static double[][] jordan(double[][] matrix, int k, int s, int m, int n){
        double[][] matrixJordan = matrix;
        double[][] copy = new double[m][n];
        for (int i = 0; i < m - 1; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = matrix[i][j];
            }
        }
        double mks = matrixJordan[k][s];
        matrixJordan[k][s] = 1 / matrixJordan[k][s];
        for (int i = 0; i < n; i++){
            if (i != s){
                matrixJordan[k][i] = copy[k][i] / mks;
            }
        }
        for (int j = 0; j < m - 1; j++){
            if (j != k){
                matrixJordan[j][s] = -1 * copy[j][s] / mks;
            }
        }
        for (int i = 0; i < m - 1; i++){
            for (int j = 0; j < n; j++){
                if (i != k && j != s){
                    matrixJordan[i][j] = (copy[i][j]*mks - copy[i][s]*copy[k][j])/mks;
                }
            }
        }
        for (int j = 0; j < n; j++) {
            double sumColumns = 0;
            for (int i = 0; i < m - 2; i++) {
                sumColumns += matrix[i][j];
            }
            matrix[m - 1][j] = -sumColumns;
        }

        return matrixJordan;
    }

    public static void printMatrix(double[][] matrix, String[] columns, String[] rows) {

        int maxLength = 3;

        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);

        for (String numColumns: columns) {
            int length = numColumns.length();
            if (length > maxLength) {
                maxLength = length;
            }
        }

        for (double[] row : matrix) {
            for (double num : row) {
                int length = String.valueOf(format.format(num)).length();
                if (length > maxLength) {
                    maxLength = length;
                }
            }
        }

        System.out.print("   ");

        for (String numColumns: columns) {
            String numString = String.valueOf(numColumns);
            int numLength = numString.length();
            int padding = maxLength - numLength;
            StringBuilder outputValueMatrix = new StringBuilder();

            for (int j = 0; j < padding; j++) {
                outputValueMatrix.append(" ");
            }

            outputValueMatrix.append(numString);
            System.out.print(outputValueMatrix + " ");
        }
        System.out.println();
        int u = 0;
        for (double[] row : matrix) {
            if (rows[u] == "0" || rows[u] == "f" || rows[u] == "g"){
                System.out.print(rows[u++] + "  ");
            }else if(rows[u].length() == 2){
                System.out.print(rows[u++] + " ");
            }else {
                System.out.print(rows[u++]);
            }
            for (double num : row) {
                String numString = String.valueOf(format.format(num));
                int numLength = numString.length();
                int padding = maxLength - numLength;
                StringBuilder outputValueMatrix = new StringBuilder();

                for (int j = 0; j < padding; j++) {
                    outputValueMatrix.append(" ");
                }

                outputValueMatrix.append(numString);
                System.out.print(outputValueMatrix + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println(rows[rows.length - 2] + " = " + matrix[rows.length - 2][0]);
    }
}
