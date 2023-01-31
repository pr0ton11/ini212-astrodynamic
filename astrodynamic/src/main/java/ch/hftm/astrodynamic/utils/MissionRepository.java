package ch.hftm.astrodynamic.utils;

import java.util.List;
import java.util.NoSuchElementException;
import javafx.collections.ObservableList;

import ch.hftm.astrodynamic.model.Mission;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

 // Singleton class containing all the Missions
public final class MissionRepository {
    
    private static MissionRepository instance;  // Single instance of the mission repository

    private MissionRepository() {}  // Constructor

    private ObservableList<Mission> missions;  // List of missions
    private Mission activeMission;  // Current active mission
    private ConfigRepository config;  // Current configuration

    // Retrieving and working with the singleton class, used primarly internally
    public static MissionRepository getInstance() {
        if(instance == null) {
            instance = new MissionRepository();
        }
        return instance;
    }

    // Adds a mission
    public static void addMission(Mission m) {
        getInstance().missions.add(m);
    }

    // Gets all missions as list
    public static List<Mission> getMissions() {
        return getInstance().missions.subList(0, getInstance().missions.size());
    }

    // Gets all missions as observable list
    public static ObservableList<Mission> getObservableMissions() {
        return getInstance().missions;
    }

    // Gets active mission
    public Mission getActiveMission() {
        return activeMission;
    }

    // Sets active mission
    public static void setActiveMission(Mission m) throws NoSuchElementException {
        setActiveMission(m.getName());
    }

    // Sets active mission
    public static void setActiveMission(String name) throws NoSuchElementException {
        getInstance().activeMission = getMissionByName(name);
    }

    // Deletes a mission by name
    public static void deleteMission(String name) throws NoSuchElementException {
        Mission m = getMissionByName(name);
        getInstance().missions.remove(m);
    }

    // Deletes a mission by object
    public static void deleteMission(Mission m) throws NoSuchElementException {
        deleteMission(m.getName());
    }

    // Returns a member of the instance list that has the name (key)
    private static Mission getMissionByName(String name) throws NoSuchElementException {
        for (Mission m : getObservableMissions()) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        throw new NoSuchElementException();
    }
}
