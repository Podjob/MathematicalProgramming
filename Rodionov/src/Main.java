import java.util.Scanner;
import java.text.DecimalFormat;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите размерность матрицы");
        System.out.print("Введите параметр m: ");
        int m = scanner.nextInt();
        System.out.print("Введите параметр n: ");
        int n = scanner.nextInt();
/*
  обычный пример
        1 1 2  3
        1 1 2  1
        2 1 0 -1
  пример с нулевой строкой
        4  1  2  1 0
        6  1  1  0 1
        10 1 -1 -2 3
  обычный пример
        5  2 3 4
        1  2 1 2
        10 1 3 5
  пример несовместной матрицы
        1  1  2 -4
       -1  2  1 -5
        1 -1 -1  3


        3 3 3 3 3 3 3
        3 3 3 3 3 3 3
        3 3 3 3 3 3 3
        3 3 3 3 3 3 3
        3 3 3 3 3 3 3
        3 3 3 3 3 3 3

 */

        double[][] matrix = new double[m][n + 1];
        double[][] copyMatrix = new double[m][n + 1];
        System.out.println("Введите элементы матрицы:");
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n + 1; j++) {
                matrix[i][j] = scanner.nextDouble();
                copyMatrix[i][j] = matrix[i][j];
            }
        }


        String[] columns = new String[n + 1];
        String[] rows = new String[m];

        for (int i = 0; i < rows.length; i++) {
            rows[i] = "0";
        }

        columns[0] = "1";
        for (int i = 1; i < columns.length; i++) {
            columns[i] = "-x" + i;
        }

        int q = 0;
        firstPrintMatix(matrix, columns, rows, q);

        for (int i = 0; i < m; i++) {
            
            // проверка на несовместность
            boolean incompatibility = false;
            int nullCheck = 0;
            int nullInMtrix = 0;

            for (String nullColumns: columns) {
                if (nullColumns != "0" && nullColumns != "1"){
                    nullCheck++;
                }
            }

            for (int j = 0; j < m; j++) {
                for (int h = 1; h < n + 1; h++) {
                    if(matrix[j][h] == 0 && columns[h] != "0" && columns[h] != "1" && matrix[j][0] != 0){
                        nullInMtrix++;
                        if(nullInMtrix == nullCheck){
                            incompatibility = true;
                            break;
                        }
                    }
                }
            }

            if(incompatibility){
                System.out.println("Система не совместна!");
                break;
            }
            // конец проверки на несовместность

            // проверка матрицы на нуевую строчку
            boolean nullStringCheck = false;
            nullCheck = 0;
            nullInMtrix = 0;

            for (String nullColumns: columns) {
                if (nullColumns != "0" && nullColumns != "1"){
                    nullCheck++;
                }
            }

            for (int j = 0; j < m; j++) {
                for (int h = 1; h < n + 1; h++) {
                    if(matrix[j][h] == 0 && columns[h] != "0" && columns[h] != "1"){
                        nullInMtrix++;
                        if(nullInMtrix == nullCheck){
                            nullStringCheck = true;
                            break;
                        }
                    }
                }
            }

            if(nullStringCheck){
                printResponseNullRows(matrix, columns, rows);
                break;
            }
            // конец проверки матрицы на нулевую строку

            System.out.print("Введите параметр k: ");
            int k = scanner.nextInt();
            System.out.print("Введите параметр s: ");
            int s = scanner.nextInt();

            // вывод матрицы после вычислений
            if(matrix[k-1][s] == 0) {
                System.out.println("На ноль делить нельзя");
                i--;
            }else{
                printMatrix(jordan(matrix, k, s, m, n, copyMatrix),columns, rows, s, k, i, m, n);
            }
        }
    }


    public static void firstPrintMatix(double[][] matrix, String[] columns, String[] rows, int i){

        int maxLength = 0;

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
                int length = String.valueOf(num).length();
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
            System.out.print(outputValueMatrix.toString() + " ");
        }
        System.out.println();
        int u = 0;
        for (double[] row : matrix) {
            if (rows[u] == "0"){
                System.out.print(rows[u++] + "  ");
            }else{
                System.out.print(rows[u++] + " ");
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
                System.out.print(outputValueMatrix.toString() + " ");
            }
            System.out.println();
        }

        System.out.println();

    }
    public static void printResponseNullRows(double[][] matrix, String[] columns, String[] rows){


        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);


        System.out.println("Ответ:");
        for (int i = 0; i < rows.length; i++) {
            if(rows[i] != "0"){
                System.out.print(rows[i] + " = " + format.format(matrix[i][0]));
                for (int j = 0; j < columns.length; j++) {
                    if(columns[j] != "0" && columns[j] != "1"){
                        System.out.print(" + (" + format.format(-1 * matrix[i][j]) + "*a" + columns[j].substring(2, 3) + ")");
                    }
                }
            }
            System.out.println();
        }


    }
    public static void printMatrix(double[][] matrix, String[] columns, String[] rows, int s, int k, int i, int m, int n) {
        int maxLength = 0;

        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);

        String d = columns[s].substring(1, 3);
        columns[s] = rows[k-1];
        rows[k-1] = d;

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
            System.out.print(outputValueMatrix.toString() + " ");
        }
        System.out.println();
        int u = 0;
        for (double[] row : matrix) {
            if (rows[u] == "0"){
                System.out.print(rows[u++] + "  ");
            }else{
                System.out.print(rows[u++] + " ");
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
                System.out.print(outputValueMatrix.toString() + " ");
            }
            System.out.println();
        }

        System.out.println();

        if(i+1 == m){
            System.out.println("Ответ:");
            for (int j = 0; j < rows.length; j++) {
                System.out.println(rows[j] + " = " + format.format(matrix[j][0]));
            }
        }
    }

    static double[][] jordan(double[][] matrix, int k, int s, int m, int n, double[][]copyMatrix){
        double[][] matrixJordan = matrix;

        double[][] copy = new double[m][n+1];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n + 1; j++) {
                copy[i][j] = matrix[i][j];
            }
        }

        if (matrixJordan[k-1][s] != 0){
            double mks = matrixJordan[k-1][s];
            matrixJordan[k-1][s] = 1 / matrixJordan[k-1][s];
            for (int i = 0; i < n + 1; i++){
                if (i != s){
                    matrixJordan[k-1][i] = copy[k-1][i] / mks;
                }
            }
            for (int j = 0; j < m; j++){
                if (j != k-1){
                    matrixJordan[j][s] = -1 * copy[j][s] / mks;
                }
            }
            for (int i = 0; i < m; i++){
                for (int j = 0; j < n + 1; j++){
                    if (i != k-1 && j != s){
                        matrixJordan[i][j] = (copy[i][j]*mks - copy[i][s]*copy[k-1][j])/mks;
                    }
                }
            }

            return matrixJordan;
        }else{
            return matrix;
        }
    }

}
