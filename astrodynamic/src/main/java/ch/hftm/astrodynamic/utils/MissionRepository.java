package ch.hftm.astrodynamic.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
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

    private ObservableSet<Mission> missions = FXCollections.observableSet();  // List of missions
    private Mission activeMission;  // Current active mission

    // Retrieving and working with the singleton class, used primarly internally
    public static MissionRepository getInstance() {
        if(instance == null) {
            instance = new MissionRepository();
        }
        return instance;
    }

    // Adds a mission
    public static boolean addMission(Mission m) {
        return getInstance().missions.add(m);
    }

    // Gets all missions as list
    public static List<Mission> getMissions() {
        ArrayList<Mission> missions = new ArrayList<>();
        for (Mission mission : getInstance().missions) {
            missions.add(mission);
        }
        return missions;
    }

    // Gets all missions as observable list
    public static ObservableSet<Mission> getObservableMissions() {
        return getInstance().missions;
    }

    // Gets active mission
    public static Mission getActiveMission() {
        return getInstance().activeMission;
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
    public static Mission getMissionByName(String name) throws NoSuchElementException {
        for (Mission m : getObservableMissions()) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        throw new NoSuchElementException();
    }
}
