interface Addable<T> {
    T add(T value);
}

class Matrix implements Addable<Matrix> {
    protected int n;
    protected int m;
    protected int[][] matrix;

    public Matrix(int rows, int columns) {
        n = rows;
        m = columns;
        matrix = new int[n][m];
    }

    public boolean setNumbers(int[] fillingArray) {
        if (fillingArray.length != n * m) {
            return false;
        }

        int index = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = fillingArray[index++];
            }
        }
        return true;
    }

    public Matrix add(Matrix matrixB) {
        if (n != matrixB.n || m != matrixB.m) {
            System.out.println();
            throw new IllegalArgumentException("\nCan't add matrices of different sizes.");
        }
        Matrix result = new Matrix(n, m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                result.matrix[i][j] = matrix[i][j] + matrixB.matrix[i][j];
            }
        }
        return result;
    }

    public void print() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void transpose() {
        int[][] transposed = new int[m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }
        int temp = n;
        n = m;
        m = temp;
        matrix = transposed;
    }
}

class IdentityMatrix extends Matrix {
    public IdentityMatrix(int n) {
        super(n, n);
    }

    @Override
    public boolean setNumbers(int[] fillingArray) {
        if (fillingArray.length != n * n) {
            return false;
        }

        int index = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i == j && fillingArray[index] != 1 || i != j && fillingArray[index] != 0)
                    return false;
                matrix[i][j] = fillingArray[index++];
            }
        }
        return true;
    }

    @Override
    public void transpose() {
        return;
    }
}

public class App {
    public static void main(String[] args) {
        Matrix matrixA = new Matrix(2, 3);
        int[] arr = { 1, 2, 4, 5, 6, 7 };
        matrixA.setNumbers(arr);

        System.out.printf("\n\nMatrix A : \n");
        matrixA.print();

        IdentityMatrix matrixB = new IdentityMatrix(2);
        int[] arr2 = { 1, 0, 0, 1 };
        int[] arr3 = { 1, 1, 0, 0 };
        if (!matrixB.setNumbers(arr3)) {
            System.out.printf("\nCan't set Identity matrix with these data\n");
        }

        if (matrixB.setNumbers(arr2))
        {
            System.out.printf("\nIdentity Matrix B : \n");
            matrixB.print();
        }

        Matrix matrixD = new Matrix(2, 2);
        matrixD.setNumbers(arr3);
        System.out.printf("\nMatrix D\n");
        matrixD.print();

        Matrix result1 = matrixB.add(matrixD);
        System.out.printf("\nMatrix D + MatrixB\n");
        result1.print();

        System.out.printf("\nTraspose of matrixB\n");
        matrixB.transpose();
        matrixB.print();

        matrixA.transpose();
        System.out.printf("\nMatrix A = transpose Matrix A: \n");
        matrixA.print();

        Matrix matrixC = new Matrix(3, 2);
        matrixC.setNumbers(arr);
        System.out.printf("\nMatrix C : \n");
        matrixC.print();

        System.out.printf("\nMatrix A + Matrix C : \n");
        Matrix result = matrixA.add(matrixC);
        result.print();

        result = matrixA.add(matrixB);
    }
}