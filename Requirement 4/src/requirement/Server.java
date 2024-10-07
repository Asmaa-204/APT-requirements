package requirement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int MAKE_PORT = 6666;
    public static final int CANCEL_PORT = 6667;
    private static Hospital hospital = new Hospital(
            "/home/asmaa/Desktop/APT requirements/Requirement 4/src/requirement/doctorFile");

    public static void main(String[] args) throws IOException {

        System.out.println("The server started .. ");

        new Thread() {
            public void run() {
                ServerSocket ss = null;
                try {
                    ss = new ServerSocket(MAKE_PORT);
                    while (true) {
                        new CancelMakeHandler(ss.accept(), true).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        ss.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        new Thread() {
            public void run() {
                ServerSocket ss = null;
                try {
                    ss = new ServerSocket(CANCEL_PORT);
                    while (true) {
                        new CancelMakeHandler(ss.accept(), false).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        ss.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }

    private static class CancelMakeHandler extends Thread {
        Socket socket;
        boolean isMake;

        public CancelMakeHandler(Socket s, boolean isMake) {
            this.socket = s;
            this.isMake = isMake;
        }

        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);

                String patientName = br.readLine();

                String message;
                while ((message = br.readLine()) != null && !message.equals("3")) {
                    String[] parts = message.split(",");
                    int doctorId = Integer.parseInt(parts[0]);
                    System.err.println(doctorId);
                    int timeslotIndex = Integer.parseInt(parts[1]);

                    sleep(2000);
                    String response;
                    synchronized (hospital) {
                        if (isMake) {
                            response = hospital.makeAppointment(doctorId, timeslotIndex, patientName);
                        } else {
                            response = hospital.cancelAppointment(doctorId, timeslotIndex, patientName);
                        }
                        out.println(response);
                        hospital.print();
                    }
                }

                out.close();
                br.close();
            } catch (IOException e) {
                System.out.println("error");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Couldn't close a socket, what's going on?");
                }
                System.out.println("Connection closed");
            }
        }
    }

}
