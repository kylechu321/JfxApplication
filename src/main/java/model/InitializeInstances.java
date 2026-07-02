package model;

import fileHandler.FileHandler;

import java.util.ArrayList;

public class InitializeInstances {
    private FileHandler  fileHandler = new FileHandler();

    public InitData initialize() throws Exception{
        InitData initData = new InitData();
        ArrayList<AvailableProject> availableProjects = fileHandler.readAvailableProject();
        initData.setAvailableProjects(availableProjects);

        return initData;
    }
}
