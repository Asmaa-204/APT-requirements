package requirement;

public class Doctor {
    int id;
    String name;
    boolean[] timeSlots;
    String[] patients;

    Doctor(int id, String name, int numTimeSlots) {
        this.id = id;
        this.name = name;
        this.timeSlots = new boolean[numTimeSlots];
        this.patients = new String[numTimeSlots];
        for (int i = 0; i < numTimeSlots; i++) {
            this.timeSlots[i] = true;
            this.patients[i] = "";
        }
    }
}
