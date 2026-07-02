package model;

public class AvailableProject {
    private int projectId;
    private String projectName;
    private String projectLocation;
    private String projectWeekday;
    private int hourlyRate;
    private int registeredSlot;
    private int totalSlot;
    private boolean enabled;

    public AvailableProject() {}

    public AvailableProject(final int projectId,
                            final String projectName,
                            final String projectLocation,
                            final String projectWeekday,
                            final int hourlyRate,
                            final int registeredSlot,
                            final int totalSlot) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectLocation = projectLocation;
        this.projectWeekday = projectWeekday;
        this.hourlyRate = hourlyRate;
        this.registeredSlot = registeredSlot;
        this.totalSlot = totalSlot;
    }

    public AvailableProject(final int projectId,
                            final String projectName,
                            final String projectLocation,
                            final String projectWeekday,
                            final int hourlyRate,
                            final int registeredSlot,
                            final int totalSlot,
                            final boolean enabled) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectLocation = projectLocation;
        this.projectWeekday = projectWeekday;
        this.hourlyRate = hourlyRate;
        this.registeredSlot = registeredSlot;
        this.totalSlot = totalSlot;
        this.enabled = enabled;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectLocation() {
        return projectLocation;
    }

    public void setProjectLocation(String projectLocation) {
        this.projectLocation = projectLocation;
    }

    public String getProjectWeekday() {
        return projectWeekday;
    }

    public void setProjectWeekday(String projectWeekday) {
        this.projectWeekday = projectWeekday;
    }

    public int getRegisteredSlot() {
        return registeredSlot;
    }

    public void setRegisteredSlot(int registeredSlot) {
        this.registeredSlot = registeredSlot;
    }

    public int getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(int hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public int getTotalSlot() {
        return totalSlot;
    }

    public void setTotalSlot(int totalSlot) {
        this.totalSlot = totalSlot;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return projectName + " - " + projectLocation + " (" + projectWeekday + ")";
    }

}
