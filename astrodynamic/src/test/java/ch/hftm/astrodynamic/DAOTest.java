package ch.hftm.astrodynamic;

import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import ch.hftm.astrodynamic.model.Mission;
import ch.hftm.astrodynamic.utils.Log;
import ch.hftm.astrodynamic.utils.MissionRepository;


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


}
