package ch.hftm.astrodynamic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import ch.hftm.astrodynamic.model.Mission;
import ch.hftm.astrodynamic.utils.Log;
import ch.hftm.astrodynamic.utils.MissionRepository;
import ch.hftm.astrodynamic.utils.Serializer;


/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Tests the Data Access Object
public class DAOTest {
 
    private Logger log = Log.build();  // Logger for unit tests

    @Test
    public void ReferenceTest() {
        
        MissionRepository.addTestMissions();
        log.fine("Added mission from Test Missions");
        Mission mission2 = MissionRepository.getMissionByName("New Dawn");
        log.fine("Loaded mission from test missions");
        MissionRepository.setActiveMission(mission2);
        log.fine("Set active mission");
        Assert.assertEquals(MissionRepository.getActiveMission(), mission2);
        Assert.assertEquals(MissionRepository.getActiveMission(), MissionRepository.getMissionByName("New Dawn"));
        Assert.assertEquals(MissionRepository.getMissionByName("New Dawn"), mission2);
        log.fine("Checked mission pointers");

        // Do changes
        MissionRepository.getActiveMission().setName("Old Dawn");
        log.fine("Updated active mission");
        Assert.assertEquals(MissionRepository.getActiveMission(), mission2);
        Assert.assertEquals(MissionRepository.getActiveMission(), MissionRepository.getMissionByName("Old Dawn"));
        Assert.assertEquals(MissionRepository.getMissionByName("Old Dawn"), mission2);
        log.fine("Checked updated mission pointers");
    }

    @Test
    public void SerializationTest() {
        
    }

    @Test
    public void MissionSerializationTest() {
        // Initialization
        MissionRepository.addTestMissions();
        log.fine("Added mission from Test Missions");
        Mission mission2 = MissionRepository.getMissionByName("New Dawn");
        log.fine("Loaded mission from test missions");
        MissionRepository.setActiveMission(mission2);
        // Preparation
        String serializedMission = Serializer.serializeMission(MissionRepository.getActiveMission());
        log.fine(String.format("Serialized (active) mission New Dawn to JSON: \n %s", serializedMission));

    }

    @Test
    public void MissionCloneTest() {
        // Initialization
        MissionRepository.addTestMissions();
        log.fine("Added mission from Test Missions");
        Mission mission2 = MissionRepository.getMissionByName("New Dawn");
        log.fine("Loaded mission from test missions");
        MissionRepository.setActiveMission(mission2);
        // Preparation
        String serializedMission = Serializer.serializeMission(MissionRepository.getActiveMission());
        Mission clonedMission2 = Serializer.deserializeMission(serializedMission);
        // Tests
        assertEquals(MissionRepository.getActiveMission().getName(), clonedMission2.getName());
        assertEquals(MissionRepository.getActiveMission().getDescription(), clonedMission2.getDescription());
        for (String planetoidName :MissionRepository.getActiveMission().getPlanetoidNames()) {
            assertTrue(clonedMission2.getPlanetoidNames().contains(planetoidName));
        }
        log.info(serializedMission);
    }

}
