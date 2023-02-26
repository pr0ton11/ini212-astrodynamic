package ch.hftm.astrodynamic.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import ch.hftm.astrodynamic.model.Mission;
import ch.hftm.astrodynamic.model.conditions.Approach;
import ch.hftm.astrodynamic.model.conditions.Condition;
import ch.hftm.astrodynamic.model.conditions.SetupHeavyLander;
import ch.hftm.astrodynamic.model.conditions.SetupISS;
import ch.hftm.astrodynamic.physics.Planetoid;
import ch.hftm.astrodynamic.physics.Spaceship;
import ch.hftm.astrodynamic.scalar.LengthScalar;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

 // Singleton class containing all the Missions
public final class MissionRepository implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private static MissionRepository instance;  // Single instance of the mission repository

    private MissionRepository() {}  // Constructor

    private transient ObservableList<Mission> missions = FXCollections.observableArrayList();  // List of missions
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
            Mission clonedMission = new Mission();
            clonedMission.setName(getInstance().activeMission.getName());
            clonedMission.setDescription(getInstance().activeMission.getDescription());
            getInstance().activeMission.getPlanetoids().forEach((Planetoid p) -> clonedMission.addPlanetoid(p));
            getInstance().activeMission.getSpaceships().forEach((Spaceship s) -> clonedMission.addSpaceship(s));
            getInstance().activeMission.getConditions().forEach((Condition c) -> clonedMission.addCondition(c));
            // Can not clone any further because fields are not accessible
            // Or in case of the elapsed simulation time, this does not make sense for a mission that we would certainly change
            return clonedMission;
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
    }

    // Custom serializer for this class
    // Used to serialize a JavaFX ObservableList
    private void writeObject(ObjectOutputStream outputStream) throws IOException {
        // Use default serialization for the serializable objects
        outputStream.defaultWriteObject();
        // Add the observable list as array to the outputStream
        outputStream.writeObject(missions.toArray());
    }
    
    // Custom deserializer for this class
    // Used to deserialize a JavaFX ObservableList
    private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
        // Use default deserialization for the deserializable objects
        inputStream.defaultReadObject();
        // Extract the raw missions from inputStream
        Object[] rawMissions = (Object[])inputStream.readObject();
        // Reinitialize mission
        missions = FXCollections.observableArrayList();
        // Add the missions
        for (Object mission : rawMissions) {
            // Cast object to mission
            missions.add((Mission) mission);
        }
        
    }

}
