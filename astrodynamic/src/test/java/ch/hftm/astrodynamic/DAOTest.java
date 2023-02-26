package ch.hftm.astrodynamic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
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
        Serializer.toFile("test.bin"); // Saves state to test.bin
        Serializer.fromFile("test.bin"); // Loads from test.bin
        File testFile = new File("test.bin");
        testFile.delete();
        log.fine("Serialized and deserialized MissionRepository from file");
        // Tests
        assertEquals(MissionRepository.getActiveMission().getName(), mission2.getName());
        assertEquals(MissionRepository.getActiveMission().getDescription(), mission2.getDescription());
        for (String planetoidName :MissionRepository.getActiveMission().getPlanetoidNames()) {
            assertTrue(mission2.getPlanetoidNames().contains(planetoidName));
        }
        log.fine("Validated MissionRepository loaded from file");
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
        Mission clonedMission = MissionRepository.cloneMission();
        // Tests
        assertEquals(MissionRepository.getActiveMission().getName(), clonedMission.getName());
        assertEquals(MissionRepository.getActiveMission().getDescription(), clonedMission.getDescription());
        for (String planetoidName :MissionRepository.getActiveMission().getPlanetoidNames()) {
            assertTrue(clonedMission.getPlanetoidNames().contains(planetoidName));
        }
        log.fine("Checked cloned mission for inconsistencies");
        // Update values in original mission
        // This shiuld not update active mission
        MissionRepository.getActiveMission().setName("Changed Name");
        assertNotSame(MissionRepository.getActiveMission().getName(), clonedMission.getName());
    }

}
