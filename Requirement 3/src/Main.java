import mpi.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    private static int[] readArrayFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            String[] parts = line.split(",");
            int[] array = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                array[i] = Integer.parseInt(parts[i]);
            }
            return array;
        } catch (IOException e) {
            System.out.println("Error reading file");
            return null;
        }
    }

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int[] arraySize = new int[1];

        int[] A1 = null;
        int[] A2 = null;
        int[] localA1;
        int[] localA2;

        if (rank == 0) {
            A1 = readArrayFromFile("/home/asmaa/Desktop/APT requirements/requirement 3/src/A1.txt");
            A2 = readArrayFromFile("/home/asmaa/Desktop/APT requirements/requirement 3/src/A2.txt");

            arraySize[0] = A1.length / size;

            localA1 = new int[arraySize[0]];
            localA2 = new int[arraySize[0]];

            MPI.COMM_WORLD.Bcast(arraySize, 0, 1, MPI.INT, 0);
            MPI.COMM_WORLD.Scatter(A1, 0, A1.length / size, MPI.INT, localA1, 0, A1.length / size, MPI.INT, 0);
            MPI.COMM_WORLD.Scatter(A2, 0, A2.length / size, MPI.INT, localA2, 0, A2.length / size, MPI.INT, 0);
        } else {
            MPI.COMM_WORLD.Bcast(arraySize, 0, 1, MPI.INT, 0);
            localA1 = new int[arraySize[0]];
            localA2 = new int[arraySize[0]];

            int [] dummy = new int[arraySize[0] * size];
            MPI.COMM_WORLD.Scatter(dummy, 0, arraySize[0], MPI.INT, localA1, 0, arraySize[0], MPI.INT, 0);
            MPI.COMM_WORLD.Scatter(dummy, 0, arraySize[0], MPI.INT, localA2, 0, arraySize[0], MPI.INT, 0);
        }

        int[] localSum = new int[1];
        localSum[0] = 0;

        for (int i = 0; i < localA1.length; i++) {
            localSum[0] += localA1[i] + localA2[i];
        }

        int[] finalResult = new int[1];
        MPI.COMM_WORLD.Reduce(localSum, 0, finalResult, 0, 1, MPI.INT, MPI.SUM, 0);

        if (rank == 0) {
            System.out.println(finalResult[0]);
        }

        MPI.Finalize();
    }
}