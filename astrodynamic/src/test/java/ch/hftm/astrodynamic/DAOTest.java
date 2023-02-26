package ch.hftm.astrodynamic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
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

    // Checks references
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

    // Tests MissionRepository serialization
    @Test
    public void MissionRepositorySerializationTest() {
        // Initialization
        MissionRepository.addTestMissions();
        log.fine("Added mission from Test Missions");
        Mission mission2 = MissionRepository.getMissionByName("New Dawn");
        log.fine("Loaded mission from test missions");
        MissionRepository.setActiveMission(mission2);
        // Preparation
        Serializer.toFile("MissionRepositorySerializationTest.json");
        Serializer.fromFile("MissionRepositorySerializationTest.json");
        log.fine("Serialized and deserialized MissionRepository from file");
        // Tests
        assertEquals(MissionRepository.getActiveMission().getName(), mission2.getName());
        assertEquals(MissionRepository.getActiveMission().getDescription(), mission2.getDescription());
        for (String planetoidName :MissionRepository.getActiveMission().getPlanetoidNames()) {
            assertTrue(mission2.getPlanetoidNames().contains(planetoidName));
        }
        log.fine("Validated MissionRepository loaded from file");
    }

    // Tests mission serialization
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
        // Tests
        assertEquals(serializedMission, "{\"totalTime\":{\"value\":{\"num\":0},\"unit\":\"TIME\"},\"playerControlledVessel\":null,\"planetoids\":[],\"spaceships\":[],\"conditions\":[],\"name\":\"New Dawn\",\"description\":\"<h1>New Dawn</h1><br>We picked a suitable landingspot on Ganymede.<br>Bring a flag.<br><img src=\\\"https://www.nasa.gov/sites/default/files/thumbnails/image/e1_-_pia24682_-_juno_ganymede_sru_-_darkside.png\\\" alt=\\\"Ganymede landing spot\\\" width=200px height=200px>\"}");
        log.fine("Checked serialized mission (compared to magic string defined in tes)");
        Mission deserializedMission = Serializer.deserializeMission(serializedMission);
        assertEquals(MissionRepository.getActiveMission().getName(), deserializedMission.getName());
        assertEquals(MissionRepository.getActiveMission().getDescription(), deserializedMission.getDescription());
        for (String planetoidName :MissionRepository.getActiveMission().getPlanetoidNames()) {
            assertTrue(deserializedMission.getPlanetoidNames().contains(planetoidName));
        }
        log.fine("Validated deserialized mission with currently active mission");
    }

    // Tests mission cloning
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
        Mission deserializedMission = Serializer.deserializeMission(serializedMission);
        // Tests
        assertEquals(MissionRepository.getActiveMission().getName(), deserializedMission.getName());
        assertEquals(MissionRepository.getActiveMission().getDescription(), deserializedMission.getDescription());
        for (String planetoidName :MissionRepository.getActiveMission().getPlanetoidNames()) {
            assertTrue(deserializedMission.getPlanetoidNames().contains(planetoidName));
        }
        log.fine("Checked cloned mission for inconsistencies");
        // Update values in original mission
        // This shiuld not update active mission
        MissionRepository.getActiveMission().setName("Changed Name");
        assertNotSame(MissionRepository.getActiveMission().getName(), deserializedMission.getName());
    }

}
