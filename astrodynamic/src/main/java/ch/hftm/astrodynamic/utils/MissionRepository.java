package ch.hftm.astrodynamic.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import ch.hftm.astrodynamic.model.Mission;
import ch.hftm.astrodynamic.model.conditions.Approach;
import ch.hftm.astrodynamic.model.conditions.SetupHeavyLander;
import ch.hftm.astrodynamic.model.conditions.SetupISS;
import ch.hftm.astrodynamic.scalar.LengthScalar;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

 // Singleton class containing all the Missions
public final class MissionRepository {
    
    private static MissionRepository instance;  // Single instance of the mission repository

    private MissionRepository() {}  // Constructor

    private ObservableList<Mission> missions = FXCollections.observableArrayList();  // List of missions
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
        if (!getInstance().missions.contains(m)) {
            getInstance().missions.add(m);
            return true;
        }

        return false;
    }

    // Gets all missions as list
    public static ArrayList<Mission> getMissions() {
        ArrayList<Mission> missions = new ArrayList<>();
        for (Mission mission : getInstance().missions) {
            missions.add(mission);
        }
        return missions;
    }

    // Replace missions with ArrayList missions
    public static void setMissions(ArrayList<Mission> missions) {
        getInstance().missions.clear();
        getInstance().missions.addAll(missions);
    }

    // Gets all missions as observable list
    @JsonIgnore
    public static ObservableList<Mission> getObservableMissions() {
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

    // Clones the currently active mission to a new Mission
    // Returns the reference to the new mission
    public static Mission cloneMission() throws NoSuchElementException {
        if (getInstance().activeMission != null) {
            return Serializer.deserializeMission(Serializer.serializeMission(getActiveMission()));
        }
        throw new NoSuchElementException();
    }

    // Overrides the currently existing instance with a new mission repository
    // This is used internally to deserialize from a file
    public static void overrideInstance(MissionRepository source) {
        MissionRepository.instance = source;  // Overrides the pointer with a new mission
    }

    // Add static test missions
    public static void addTestMissions() {
        addMission(new Mission("Driving Miss Daisy", "<h1>Driving Miss Daisy</h1><br>A bunch of scientists want to travel to the ISS.<br>You'll be the driver.<br><img src=\"https://www.nasa.gov/sites/default/files/styles/full_width/public/thumbnails/image/progress_1_29_tianzhou_4_depating_from_tiangong.jpg?itok=sqE2bAY_\" width=300px height=200px>"));
        addMission(new Mission("New Dawn", "<h1>New Dawn</h1><br>We picked a suitable landingspot on Ganymede.<br>Bring a flag.<br><img src=\"https://www.nasa.gov/sites/default/files/thumbnails/image/e1_-_pia24682_-_juno_ganymede_sru_-_darkside.png\" alt=\"Ganymede landing spot\" width=200px height=200px>"));
        addMission(new Mission("In the Well", "<h1>In the Well</h1><br>The jovian ammonia harvesting station lost its engines.<br>Evacuate the personel before it drifts into Jupiter.<br><img src=\"https://www.nasa.gov/sites/default/files/thumbnails/image/hotspot_cover_1280.jpg\" width=400px height=200px>"));

        for (Mission m: getMissions()) {
            m.setupStandardSolarSystem();
        }

        Mission tempMission = getMissionByName("Driving Miss Daisy");
        tempMission.addCondition(new SetupHeavyLander(new LengthScalar(300000), tempMission.getAstronomicalObjectByName("Earth")));
        tempMission.addCondition(new SetupISS(new LengthScalar(500000), tempMission.getAstronomicalObjectByName("Earth")));
        tempMission.addCondition(new Approach(new LengthScalar(300), tempMission.getAstronomicalObjectByName("ISS")));

        // Sort the test missions
        sort();

    }

    // Sorts the MissionRepository alphabetically
    public static void sort() {
        // Create a comparator for name based sorting 
        Comparator<Mission> byName = (Mission a, Mission b) -> a.getName().compareTo(b.getName());
        // Sort the Mission Repository based on names
        getInstance().missions.sort(byName);
    }
    
}
