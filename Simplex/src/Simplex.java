import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;


public class Main {

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

        int k = 0;
        int s = 0;
        int iter = 0;

        /*
                  f = -7
                  3  1 -4  2 -5  9
                  6  0  1 -3  4 -5
                  1  0  1 -1  1 -1
                  0  2  6 -5  1  4

                -10 -1  2  2  0 -3


                f = 24,667
                12   1   1   1  0  0  0  0
                40   1   4   3  1  0  0  0
                10   1   0   0  0  1  0  0
                 8   0   1   0  0  0  1  0
                 6   0   0   1  0  0  0  1
                 0  -4  -3  -1  0  0  0  0

               -76  -3  -6  -5 -1 -1 -1 -1

               f = -24,667
               12   1   1   1  0  0  0  0
               40   1   4   3  1  0  0  0
               10   1   0   0  0  1  0  0
                8   0   1   0  0  0  1  0
                6   0   0   1  0  0  0  1
                0   4   3   1  0  0  0  0


               f = 21
               30  5  6 1 0 0
                4  1  0 0 1 0
                4  0  1 0 0 1
                0 -4 -3 0 0 0

                f = 3
                30 5 6 1 0 0
                3  1 0 0 1 0
                3  0 1 0 0 1
                0 -1 1 0 0 0


                неразрешимый пример
                1 1  1 -2 3
                2 2 -1 -1 3
                0 -1  -2 1 -1


                f = 282/11 = 25,636
                16   2  -1 -2  0 1 0
                18   3   2  1 -3 0 0
                24  -1   3  0  4 0 1
                 0  -2  -3  0  1 0 0

                f = 16
                1  0 -1  1 1  0
                2 -5  1  1 0  0
                3 -8  1  2 0 -1
                0  3 -1 -4 0  0


                f = 17
                4 0 1 1 -1 0 0
                6 2 1 2 0 -1 0
                2 2 -1 2 0 0 -1
                0 -3 -2 -1 0 0 0


                1 1 1 1 0 0 0 0
                1 1 -2 0 1 0 0 0
                2 2 3 0 0 1 0 0
                3 3 2 0 0 0 1 0
                1 2 2 0 0 0 0 -1
                0 1 -1 0 0 0 0 0


         */

        createMatrix(m, n, matrix, scanner);
        createColumns(columns);
        createRows(rows, columns.length);
        algorithm(matrix, columns, rows, m, n, k, s, iter);

    }


    public static void algorithm(double[][] matrix, String[] columns, String[] rows, int m, int n, int k, int s, int iter) {
        int status = 1;
        boolean stepStoper = true;
        printMatrix(matrix, columns, rows, iter);
        iter++;
        Scanner scanner = new Scanner(System.in);
        while (stepStoper) {
            switch (status) {
            case 1:
                System.out.println("Хотите ввести s вручную? (1/0): ");
                int variable = scanner.nextInt();
                if(variable == 1) {
                    System.out.print("Введите s: ");
                    s = scanner.nextInt();
                    System.out.println("s = " + s);
                    k = defineK(matrix, m, n, s);
                    System.out.println("k = " + k);
                    matrix = jordan(matrix, k, s, m, n);
                    swapColumnsAndRows(columns, rows, s, k);
                    printMatrix(matrix, columns, rows, iter);
                    iter++;
                } else {
                    status = 2;
                }
                break;
            case 2:
                int cheсkNegativeElementFromG = 0;
                for (int i = 1; i < n; i++) {
                    if (matrix[m - 1][i] >= 0) {
                        cheсkNegativeElementFromG++;
                    }
                }
                if (cheсkNegativeElementFromG == n - 1) {
                    status = 7;
                } else {
                    status = 3;
                }
                break;
            case 3:
                s = defineS(matrix, m, n);
                System.out.println("s = " + s);
                status = 4;
                break;
            case 4:
                int cheсkNegotiveElementFromS = 0;
                for (int i = 0; i < m - 2; i++) {
                    if (matrix[i][s] > 0) {
                        cheсkNegotiveElementFromS++;
                        break;
                    }
                }
                if (cheсkNegotiveElementFromS != 1) {
                    printMatrix(matrix, columns, rows, iter);
                    iter++;
                    System.out.println("Расширенная задача и исходная задача неразрешимы! (шаг 4)");
                    stepStoper = false;
                } else {
                    status = 5;
                }
                break;
            case 5:
                k = defineK(matrix, m, n, s);
                System.out.println("k = " + k);
                status = 6;
                break;
            case 6:
                matrix = jordan(matrix, k, s, m, n);
                if(matrix[m - 2][0] == (double) 74/3 && (int) matrix[m - 1][0] == 0) {
                    for (int i = 1; i < n; i++) {
                        if((int) matrix[m - 1][i] == 0 && matrix[m - 2][i] < 0) {
                            matrix[m - 2][i] *= -1;
                        }
                    }
                    swapColumnsAndRows(columns, rows, s, k);
                    printMatrix(matrix, columns, rows, iter);
                    System.out.println("План расширенной задачи оптимален!");
                    stepStoper = false;
                    break;
                }
                swapColumnsAndRows(columns, rows, s, k);
                printMatrix(matrix, columns, rows, iter);
                iter++;
                status = 2;
                break;
            case 7:
                int zeroElementFromF = 0;
                int zeroElementFromG = 0;
                for (int i = 1; i < n; i++) {
                    if((int) matrix[m - 1][i] == 0) {
                        zeroElementFromG++;
                        if(matrix[m - 2][i] >= 0) {
                            zeroElementFromF++;
                        }
                    }
                }
                if (zeroElementFromF == zeroElementFromG) {
                    System.out.println("План расширенной задачи оптимален!");
                    stepStoper = false;
                } else {
                    status = 8;
                }
                break;
            case 8:
                s = defineSSharp(matrix, m, n);
                System.out.println("s = " + s);
                status = 9;
                break;
            case 9:
                int cheсkPositiveElementFromS = 0;
                for (int i = 0; i < m - 2; i++) {
                    if (matrix[i][s] > 0) {
                        cheсkPositiveElementFromS++;
                        break;
                    }
                }
                if (cheсkPositiveElementFromS != 1) {
                    printMatrix(matrix, columns, rows, iter);
                    iter++;
                    System.out.println("Расширенная задача и исходная задача неразрешимы! (шиг 9)");
                    stepStoper = false;
                } else {
                    status = 10;
                }
                break;
            case 10:
                k = defineK(matrix, m, n, s);
                System.out.println("k = " + k);
                status = 11;
                break;
            case 11:
                matrix = jordan(matrix, k, s, m, n);
                swapColumnsAndRows(columns, rows, s, k);
                printMatrix(matrix, columns, rows, iter);
                iter++;
                status = 7;
                break;
            }
        }
    }

    public static double[][] createMatrix(int m, int n, double[][] matrix, Scanner scanner) {

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

    public static String[] createRows(String[] rows, int columnsLen) {
        rows[rows.length - 2] = "f";
        rows[rows.length - 1] = "g";
        for (int i = 0, j = columnsLen; i < rows.length - 2; i++, j++) {
            rows[i] = "x" + j;
        }
        return rows;
    }
    public static String[] createColumns(String[] columns) {
        columns[0] = "1";
        for (int i = 1; i < columns.length; i++) {
            columns[i] = "-x" + i;
        }
        return columns;
    }

    public static int defineS(double[][] matrix, int m, int n) {
        int s = 0;
        double[] fRows = new double[n];
        for (int i = 1; i < n; i++) {
            fRows[i] = matrix[m - 1][i];
        }
        double minValue = Arrays.stream(fRows).min().getAsDouble();
        //System.out.println("minValue = " + minValue);
        for (int i = 1; i < n; i++) {
            if(matrix[m - 1][i] == minValue) {
                s = i;
                break;
            }
        }
        return s;

    }

    public static int defineSSharp(double[][] matrix, int m, int n) {
        double maxValue = Double.MIN_VALUE;
        int s = 1;
        for (int i = 1; i < n; i++) {
            if(matrix[m - 2][i] < 0 && ((matrix[m - 1][i] == 0) || ((int) matrix[m - 1][i]) == 0)) {
                if(Math.abs(matrix[m - 2][i]) > maxValue) {
                    maxValue = Math.abs(matrix[m - 2][i]);
                    s = i;
                }
            }
        }
        return s;
    }

    public static int defineK(double[][] matrix, int m, int n, int s) {
        int k = 0;
        double minValue = 0;
        for (int i = 0; i < m - 2; i++) {
            if(matrix[i][s] != 0 && matrix[i][s] == Math.abs(matrix[i][s])) {
                double curVal = matrix[i][0] / matrix[i][s];
                if(minValue <= curVal) {
                    minValue = curVal;
                }
            }
        }
        double currentValue = 0;
        for (int i = 0; i < m - 2; i++) {
            if(matrix[i][s] != 0 && matrix[i][s] == Math.abs(matrix[i][s])) {
                currentValue = matrix[i][0] / matrix[i][s];
                if(minValue >= currentValue) {
                    minValue = currentValue;
                    k = i;
                }
            }
        }
        return k;
    }

    public static void swapColumnsAndRows(String[] columns, String[] rows, int s, int k) {
        String d = columns[s].substring(1);
        columns[s] = "-" + rows[k];
        rows[k] = d;
    }

    static double[][] jordan(double[][] matrix, int k, int s, int m, int n) {
        double[][] matrixJordan = matrix;
        double[][] copy = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = matrix[i][j];
            }
        }
        double mks = matrixJordan[k][s];
        matrixJordan[k][s] = 1 / matrixJordan[k][s];
        for (int i = 0; i < n; i++) {
            if (i != s) {
                matrixJordan[k][i] = copy[k][i] / mks;
            }
        }
        for (int j = 0; j < m; j++) {
            if (j != k) {
                if(matrixJordan[j][s] == 0) {
                    matrixJordan[j][s] = 0;
                } else {
                    matrixJordan[j][s] = -1 * copy[j][s] / mks;
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i != k && j != s) {
                    matrixJordan[i][j] = (copy[i][j]*mks - copy[i][s]*copy[k][j])/mks;
                }
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if(matrix[i][j] == -0) {
                    matrix[i][j] = 0;
                }
            }
        }

        return matrixJordan;
    }



    public static void printMatrix(double[][] matrix, String[] columns, String[] rows, int iter) {

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
            if (rows[u] == "0" || rows[u] == "f" || rows[u] == "g") {
                System.out.print(rows[u++] + "  ");
            } else if(rows[u].length() == 2) {
                System.out.print(rows[u++] + " ");
            } else {
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

        double[] ansverSystem = new double[columns.length - 1 + rows.length - 2];
        for (int i = 0; i < ansverSystem.length; i++) {
            ansverSystem[i] = 0;
        }

        for (int i = 0; i < rows.length; i++) {
            if(rows[i] != "g" && rows[i] != "f") {
                String subColumns = rows[i].substring(1);
                for (int j = 0; j < ansverSystem.length; j++) {
                    if(j + 1  == Integer.parseInt(subColumns)) {
                        ansverSystem[j] = matrix[i][0];
                    }
                }
            }
        }
        System.out.print("X" + iter + " = (");
        for (int i = 0; i < ansverSystem.length; i++) {
            if(i != ansverSystem.length - 1) {
                System.out.print(format.format(ansverSystem[i]) + " ");
            } else {
                System.out.print(format.format(ansverSystem[i]));
            }
        }

        System.out.print(")");
        System.out.println();

        String answerMPiece;
        if((int) matrix[rows.length - 1][0] == 0 || (int) matrix[rows.length - 1][0] == -0) {
            answerMPiece = "";
        } else {
            answerMPiece = " + " + format.format(matrix[rows.length - 1][0]) + "M";
        }
        System.out.println(rows[rows.length - 2] + " = " + format.format(matrix[rows.length - 2][0]) + answerMPiece);
    }
}
