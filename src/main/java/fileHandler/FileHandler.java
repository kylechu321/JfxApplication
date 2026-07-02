package fileHandler;

import model.AvailableProject;
import model.InitData;
import model.UserParticipationHistory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    public ArrayList<AvailableProject> readAvailableProject() throws Exception {
        String filePath = "src/main/resources/data/projects.csv";
        ArrayList<AvailableProject> availableProjects = new ArrayList<>();

        try (InputStream in = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            if (br.readLine() != null) {
                List<String> lines = br.lines().toList();
                int count = 1;
                for (String line : lines) {
                    String[] values = line.split(",");
                    String projectName = values[0];
                    String projectLocation = values[1];
                    String projectWeekday = values[2];
                    int hourlyRate = Integer.parseInt(values[3]);
                    int registeredSlot = Integer.parseInt(values[4]);
                    int totalSlot = Integer.parseInt(values[5]);

                    AvailableProject availableProjectInto = new AvailableProject(count ,projectName, projectLocation, projectWeekday, hourlyRate, registeredSlot, totalSlot);
                    availableProjects.add(availableProjectInto);
                    count++;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return availableProjects;
    }

    public boolean exportParticipationHistory(InitData initData) throws Exception{
        String filename = "history_" + initData.getUser().getUsername() + ".txt";
        String filePath = "src/main/resources/export";
        List<UserParticipationHistory> histories = initData.getUserParticipationHistoryList();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + "/" + filename))) {
            if (histories != null && !histories.isEmpty()) {
                for (UserParticipationHistory history : histories) {
                    writer.write("Registration ID: " + history.getRegistrationId());
                    writer.newLine();
                    writer.write("Date: " + history.getFormatDate());
                    writer.newLine();
                    writer.write("Project: " + history.getProjectName());
                    writer.newLine();
                    writer.write("Location: " + history.getProjectLocation());
                    writer.newLine();
                    writer.write("Weekday: " + history.getProjectWeekday());
                    writer.newLine();
                    writer.write("Slots: " + history.getNumberOfSlots());
                    writer.newLine();
                    writer.write("Hours per Slot: " + history.getHoursPerSlot());
                    writer.newLine();
                    writer.write("Total Contribution: $" + history.getTotalContribution());
                    writer.newLine();
                    writer.write("--------------------------------------------------");
                    writer.newLine();
                }
            } else {
                writer.write("No participation history found.");
                writer.newLine();
            }
            writer.flush();
            return true;
        } catch (Exception e) {
        System.out.println("Error exporting history: " + e.getMessage());
        return false;
    }

    }
}

