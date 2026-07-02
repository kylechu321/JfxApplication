package model;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class InitData {
    private ArrayList<AvailableProject> availableProjects = new ArrayList<>();
    private User user = new User();
    private Model model;
    private List<CartItem> cartItems = new ArrayList<>();
    private List<UserParticipationHistory> userParticipationHistoryList = new ArrayList<>();
    private Set<String> savedRegistrationIds = new HashSet<>();
    private List<UserParticipationHistory> loadedHistory = new ArrayList<>();
    private List<UserParticipationHistory> newHistory = new ArrayList<>();
    private List<AvailableProject> relatedProjects = new ArrayList<>();
    private Map<String, List<AvailableProject>> groupedProjects = new HashMap<>();

    public void setAvailableProjects(ArrayList<AvailableProject> availableProjects) {
        this.availableProjects = availableProjects;
    }

    public ArrayList<AvailableProject> getAvailableProjects() {
        return availableProjects;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public List<CartItem> getCartItems() {
        return (cartItems != null) ? cartItems : new ArrayList<>();
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = (cartItems != null) ? cartItems : new ArrayList<>();
    }

    public List<UserParticipationHistory> getUserParticipationHistoryList() {
        return userParticipationHistoryList;
    }

    public void setUserParticipationHistoryList(List<UserParticipationHistory> userParticipationHistoryList) {
        this.userParticipationHistoryList = userParticipationHistoryList;
    }

    public void addUserParticipationHistory(UserParticipationHistory history) {
        userParticipationHistoryList.add(history);
    }

    public boolean isAlreadySaved(String registrationId) {
        return savedRegistrationIds.contains(registrationId);
    }

    public void markAsSaved(String registrationId) {
        savedRegistrationIds.add(registrationId);
    }

    public void setLoadedHistory(List<UserParticipationHistory> history) {
        this.loadedHistory = history;
    }

    public void addNewHistory(UserParticipationHistory record) {
        this.newHistory.add(record);
    }

    public List<UserParticipationHistory> getNewHistory() {
        return newHistory;
    }

    public List<AvailableProject> getRelatedProjects() {
        return relatedProjects;
    }

    public void setRelatedProjects(List<AvailableProject> relatedProjects) {
        this.relatedProjects = relatedProjects;
    }

    public Map<String, List<AvailableProject>> getGroupedProjects() {
        return groupedProjects;
    }

    public void setGroupedProjects(Map<String, List<AvailableProject>> groupedProjects) {
        this.groupedProjects = groupedProjects;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Model getModel() {
        return model;
    }

    //method to group projects by name
    public void groupProjectsByName() {
        if (availableProjects != null && !availableProjects.isEmpty()) {
            groupedProjects = availableProjects.stream()
                    .collect(Collectors.groupingBy(AvailableProject::getProjectName));
        } else {
            groupedProjects = new HashMap<>();
        }
    }

    // to get one representative per group
    public List<AvailableProject> getGroupedProjectRepresentatives() {
        return groupedProjects.values().stream()
                .map(list -> list.get(0))
                .collect(Collectors.toList());
    }

    //method to return only enabled projects
    public List<AvailableProject> getEnabledProjects() {
        if (availableProjects == null) return new ArrayList<>();
        return availableProjects.stream()
                .filter(AvailableProject::isEnabled)
                .collect(Collectors.toList());
    }

    public void toggleProjectStatus(int projectId) {
        for (AvailableProject project : availableProjects) {
            if (project.getProjectId() == projectId) {
                boolean newStatus = !project.isEnabled();
                project.setEnabled(newStatus);

                //Persist change to database
                try {
                    model.getProjectDao().updateProjectStatus(String.valueOf(projectId), newStatus);
                } catch (SQLException e) {
                    System.out.println("Failed to update project status in DB: " + e.getMessage());
                }

                break;
            }
        }

        groupProjectsByName();
    }

    public void applySlotDeductionFromCart() {
        if (cartItems == null || cartItems.isEmpty()) return;

        for (CartItem item : cartItems) {
            AvailableProject project = item.getAvailableProject();
            int slotsUsed = item.getNumberOfSlots();
            int updatedSlots = project.getRegisteredSlot() - slotsUsed;
            project.setRegisteredSlot(updatedSlots);
        }
    }


}
