import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
public class Main extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!doctype html><html><body>");

        // Retrieve matrix input from the HTML form
        String matrixData = request.getParameter("matrix");

        // Check if the matrix is empty
        if (matrixData.isEmpty()) {
            out.println("<p>Matrix is empty</p>");
            return;
        }

        // Split the matrix data into rows
        String[] rows = matrixData.split("\\r?\\n");

        // Convert the input to a square matrix
        int size = rows.length;
        int[][] matrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            String[] values = rows[i].split("\\s+");
            if (values.length != size) {
                out.println("<p>Invalid matrix format</p>");
                return;
            }
            for (int j = 0; j < size; j++) {
                matrix[i][j] = Integer.parseInt(values[j]);
            }
        }

        boolean isTranspose = request.getParameter("Transpose") != null ;
        boolean isIdentity = request.getParameter("IsIdentity") != null ;

        // Perform operations based on user's choice
        if (isIdentity && isTranspose) {
            out.println("<h2>Original Matrix:</h2>");
            printMatrix(matrix, out);
            out.println("<h2>Transpose:</h2>");
            printMatrix(transpose(matrix), out);
            if (isIdentity(matrix)) {
                out.println("<h2>Identity:</h2>");
                out.println("<p>The matrix is an identity matrix</p>");
            } else {
                out.println("<h2>Identity:</h2>");
                out.println("<p>The matrix is not an identity matrix</p>");
            }
        } else if (isIdentity) {
            out.println("<h2>Original Matrix:</h2>");
            printMatrix(matrix, out);
            if (isIdentity(matrix)) {
                out.println("<h2>Identity:</h2>");
                out.println("<p>The matrix is an identity matrix</p>");
            } else {
                out.println("<h2>Identity:</h2>");
                out.println("<p>The matrix is not an identity matrix</p>");
            }
        } else if (isTranspose) {
            out.println("<h2>Original Matrix:</h2>");
            printMatrix(matrix, out);
            out.println("<h2>Transpose:</h2>");
            printMatrix(transpose(matrix), out);
        } else {
            out.println("<h2>Original Matrix:</h2>");
            printMatrix(matrix, out);
        }
        out.println("</body></html>");
    }

    // Method to check if a matrix is an identity matrix
    private boolean isIdentity(int[][] matrix) {
        int size = matrix.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j && matrix[i][j] != 1) {
                    return false;
                }
                if (i != j && matrix[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // Method to calculate transpose of a matrix
    private int[][] transpose(int[][] matrix) {
        int size = matrix.length;
        int[][] transposedMatrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                transposedMatrix[j][i] = matrix[i][j];
            }
        }
        return transposedMatrix;
    }

    // Method to print a matrix
    private void printMatrix(int[][] matrix, PrintWriter out) {
        out.println("<table border=\"1\">");
        for (int[] row : matrix) {
            out.println("<tr>");
            for (int cell : row) {
                out.println("<td>" + cell + "</td>");
            }
            out.println("</tr>");
        }
        out.println("</table>");
    }
}