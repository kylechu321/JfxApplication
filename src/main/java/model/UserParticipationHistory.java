package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserParticipationHistory implements Serializable {
    private String registrationId;
    private LocalDateTime confirmationDateTime;
    private int projectId;
    private String projectName;
    private String projectLocation;
    private String projectWeekday;
    private int numberOfSlots;
    private int hoursPerSlot;
    private int hourlyRate;
    private double totalContribution;
    private String username;

    public UserParticipationHistory(final String registrationId,
                                    final LocalDateTime confirmationDateTime,
                                    final String projectName,
                                    final String projectLocation,
                                    final String projectWeekday,
                                    final int numberOfSlots,
                                    final int hoursPerSlot,
                                    final int hourlyRate) {
        this.registrationId = registrationId;
        this.confirmationDateTime = confirmationDateTime;
        this.projectName = projectName;
        this.projectLocation = projectLocation;
        this.projectWeekday = projectWeekday;
        this.numberOfSlots = numberOfSlots;
        this.hoursPerSlot = hoursPerSlot;
        this.hourlyRate = hourlyRate;
        this.totalContribution = hourlyRate * hoursPerSlot * numberOfSlots;
    }

    public UserParticipationHistory(final String registrationId,
                                    final LocalDateTime confirmationDateTime,
                                    final int projectId,
                                    final String projectName,
                                    final String projectLocation,
                                    final String projectWeekday,
                                    final int numberOfSlots,
                                    final int hoursPerSlot,
                                    final int hourlyRate,
                                    final double totalContribution) {
        this.registrationId = registrationId;
        this.confirmationDateTime = confirmationDateTime;
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectLocation = projectLocation;
        this.projectWeekday = projectWeekday;
        this.numberOfSlots = numberOfSlots;
        this.hoursPerSlot = hoursPerSlot;
        this.hourlyRate = hourlyRate;
        this.totalContribution = totalContribution;
    }

    //admin view constructor


    public UserParticipationHistory(final String registrationId,
                                    final LocalDateTime confirmationDateTime,
                                    final String username,
                                    final int projectId,
                                    final String projectName,
                                    final String projectLocation,
                                    final String projectWeekday,
                                    final int numberOfSlots,
                                    final int hoursPerSlot,
                                    final int hourlyRate,
                                    final double totalContribution) {
        this.registrationId = registrationId;
        this.confirmationDateTime = confirmationDateTime;
        this.username = username;
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectLocation = projectLocation;
        this.projectWeekday = projectWeekday;
        this.numberOfSlots = numberOfSlots;
        this.hoursPerSlot = hoursPerSlot;
        this.hourlyRate = hourlyRate;
        this.totalContribution = totalContribution;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public LocalDateTime getConfirmationDateTime() {
        return confirmationDateTime;
    }

    public String getProjectName() {
        return projectName;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getProjectLocation() {
        return projectLocation;
    }

    public String getProjectWeekday() {
        return projectWeekday;
    }

    public int getNumberOfSlots() {
        return numberOfSlots;
    }

    public int getHoursPerSlot() {
        return hoursPerSlot;
    }

    public int getHourlyRate() {
        return hourlyRate;
    }

    public double getTotalContribution() {
        return totalContribution;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFormatDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        return confirmationDateTime.format(formatter);
    }

}
