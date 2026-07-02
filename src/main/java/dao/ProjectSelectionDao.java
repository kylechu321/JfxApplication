package dao;

import model.User;
import model.UserParticipationHistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ProjectSelectionDao {
    void setup() throws SQLException;
    List<UserParticipationHistory> loadParticipationHistory(User user) throws SQLException;
    void saveParticipationHistory(List<UserParticipationHistory> historyList, User user) throws SQLException;
    List<UserParticipationHistory> loadAllParticipationHistory() throws SQLException;


}

