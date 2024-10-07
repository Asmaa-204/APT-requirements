package requirement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	public static final int MAKE_PORT = 6666;
	public static final int CANCEL_PORT = 6667;

	public static void main(String[] args) throws UnknownHostException, IOException {
		Scanner scanner = new Scanner(System.in);

		// Prompt the user to enter their name (patient's name)
		System.out.print("Enter your name: ");
		String patientName = scanner.nextLine();

		// Open connections to the server on make and cancel appointment ports
		Socket makeSocket = new Socket("localhost", MAKE_PORT);
		Socket cancelSocket = new Socket("localhost", CANCEL_PORT);

		// Send the patient's name to both sockets
		PrintWriter makeOut = new PrintWriter(makeSocket.getOutputStream(), true);
		makeOut.println(patientName);

		PrintWriter cancelOut = new PrintWriter(cancelSocket.getOutputStream(),
				true);
		cancelOut.println(patientName);

		BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			System.out.println("Enter your choice: (1) Make Appointment (2) Cancel Appointment (3) exit");

			int choice = Integer.parseInt(consoleReader.readLine());

			if (choice == 1 || choice == 2) {
				// Prompt for doctor ID and timeslot index
				System.out.print("Enter doctor ID: ");
				int doctorId = Integer.parseInt(consoleReader.readLine());
				System.out.print("Enter timeslot index: ");
				int timeslotIndex = Integer.parseInt(consoleReader.readLine());

				// Send the doctor ID and timeslot index to the appropriate socket
				PrintWriter out = (choice == 1) ? makeOut : cancelOut;
				out.println(doctorId + "," + timeslotIndex);

				// Read the response message from the server
				BufferedReader socketReader = (choice == 1)
						? new BufferedReader(new InputStreamReader(makeSocket.getInputStream()))
						: new BufferedReader(new InputStreamReader(cancelSocket.getInputStream()));

				String response = socketReader.readLine();
				System.out.println("Response from server: " + response);
			} else if (choice == 3) {
				break;
			} else {
				System.out.println("Invalid choice. Please try again.");
			}
		}
		scanner.close();
		consoleReader.close();
		makeOut.close();
		cancelOut.close();
		makeSocket.close();
		cancelSocket.close();
	}
}
