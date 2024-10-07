package requirement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Hospital {
    List<Doctor> doctors;

    public Hospital(String doctorsFile) {
        doctors = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(doctorsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                int numTimeslots = Integer.parseInt(data[2]);
                Doctor doctor = new Doctor(id, name, numTimeslots);
                doctors.add(doctor);
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public String makeAppointment(int doctorId, int timeSlotIndex, String patientName) {
        for (Doctor doctor : doctors) {
            if (doctor.id != doctorId) {
                continue;
            }
            if (timeSlotIndex < 0 || timeSlotIndex > doctor.timeSlots.length) {
                return ("Timeslot index is out of boundary. (FAILURE)");
            }
            if (doctor.timeSlots[timeSlotIndex]) {
                doctor.timeSlots[timeSlotIndex] = false;
                doctor.patients[timeSlotIndex] = patientName;
                return ("Appointment made successfully. (SUCCESS)");
            } else {
                return ("Doctor is already busy at this timeslot. (FAILURE)");
            }
        }
        return ("Doctor ID not found in hospital. (FAILURE)");
    }

    public String cancelAppointment(int doctorId, int timeSlotIndex, String patientName) {
        for (Doctor doctor : doctors) {
            if (doctor.id != doctorId) {
                continue;
            }
            if (timeSlotIndex < 0 || timeSlotIndex > doctor.timeSlots.length) {
                return ("Timeslot index is out of boundary. (FAILURE)");
            }
            if (doctor.timeSlots[timeSlotIndex]) {
                return ("Doctor doenn't have an appointment at this timeSlot. (FAILURE)");
            } else if (doctor.patients[timeSlotIndex].equals(patientName)) {
                doctor.timeSlots[timeSlotIndex] = true;
                doctor.patients[timeSlotIndex] = "";
                return ("Appointment cancelled successfully. (SUCCESS)");
            } else {
                return ("Doctor has an appointment to a different patient at this timeslot. (FAILURE)");
            }
        }
        return ("Doctor ID not found in hospital.");
    }

    public void print() {
        for (Doctor doctor : doctors) {
            System.out.println("Doctor ID: " + doctor.id);
            System.out.println("Schedule:");
            for (int i = 0; i < doctor.timeSlots.length; i++) {
                String status = doctor.timeSlots[i] ? "Available" : "Occupied";
                String patient = doctor.timeSlots[i] ? "" : doctor.patients[i];
                System.out.println(
                        "Timeslot " + i + ": " + status + (status.equals("Occupied") ? " - Patient: " + patient : ""));
            }
            System.out.println();
        }
    }
}
