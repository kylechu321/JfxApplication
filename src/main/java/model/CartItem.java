package model;

public class CartItem {
    private AvailableProject availableProject;
    private int hoursPerSlot;
    private int numberOfSlots;

    public CartItem(final AvailableProject availableProject,
                    final int hoursPerSlot,
                    final int numberOfSlots) {
        this.availableProject = availableProject;
        this.hoursPerSlot = hoursPerSlot;
        this.numberOfSlots = numberOfSlots;
    }

    public AvailableProject getAvailableProject() {
        return availableProject;
    }

    public int getHoursPerSlot() {
        return hoursPerSlot;
    }

    public int getNumberOfSlots() {
        return numberOfSlots;
    }
    @Override
    public String toString() {
        return availableProject.getProjectName() + " on " + availableProject.getProjectWeekday() + " - " + hoursPerSlot + "hr x " + numberOfSlots + " slots";
    }
}
