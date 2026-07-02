package model;

import java.sql.SQLException;

import dao.*;

public class Model {
    private UserDao userDao;
    private ProjectSelectionDao projectSelectionDao;
    private ProjectDao projectDao;
    private User currentUser;

    public Model() {
        userDao = new UserDaoImpl();
        projectSelectionDao = new ProjectSelectionDaoImpl();
        projectDao = new ProjectDaoImpl();
    }


    public void setup() throws SQLException {
        userDao.setup();
        projectSelectionDao.setup();
        projectDao.setup();

    }
    public UserDao getUserDao() {
        return userDao;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
    }

    public ProjectSelectionDao getProjectSelectionDao() {
        return projectSelectionDao;
    }

    public void setProjectSelectionDao(ProjectSelectionDao projectSelectionDao) {
        this.projectSelectionDao = projectSelectionDao;
    }

    public ProjectDao getProjectDao() {
        return projectDao;
    }

    public void setProjectDao(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }
}
