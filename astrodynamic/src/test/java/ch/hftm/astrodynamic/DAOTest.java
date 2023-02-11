package ch.hftm.astrodynamic;

import org.junit.Assert;
import org.junit.Test;

import ch.hftm.astrodynamic.model.Mission;
import ch.hftm.astrodynamic.utils.MissionRepository;

public class DAOTest {
 
    @Test
    public void ReferenceTest() {
        Mission mission1 = new Mission("Driving Miss Daisy", "<h1>Driving Miss Daisy</h1><br>A bunch of scientists want to travel to the ISS.<br>You'll be the driver.<br><img src=\"https://www.nasa.gov/sites/default/files/styles/full_width/public/thumbnails/image/progress_1_29_tianzhou_4_depating_from_tiangong.jpg?itok=sqE2bAY_\" width=300px height=200px>");
        Mission mission2 = new Mission("New Dawn", "<h1>New Dawn</h1><br>We picked a suitable landingspot on Ganymede.<br>Bring a flag.<br><img src=\"https://www.nasa.gov/sites/default/files/thumbnails/image/e1_-_pia24682_-_juno_ganymede_sru_-_darkside.png\" alt=\"Ganymede landing spot\" width=200px height=200px>");
        Mission mission3 = new Mission("In the Well", "<h1>In the Well</h1><br>The jovian ammonia harvesting station lost its engines.<br>Evacuate the personel before it drifts into Jupiter.<br><img src=\"https://www.nasa.gov/sites/default/files/thumbnails/image/hotspot_cover_1280.jpg\" width=400px height=200px>");
    
        MissionRepository.addMission(mission1);
        MissionRepository.addMission(mission2);
        MissionRepository.addMission(mission3);

        MissionRepository.setActiveMission(mission2);
        
        Assert.assertEquals(MissionRepository.getActiveMission(), mission2);
        Assert.assertEquals(MissionRepository.getActiveMission(), MissionRepository.getMissionByName("New Dawn"));
        Assert.assertEquals(MissionRepository.getMissionByName("New Dawn"), mission2);

        // Do changes
        MissionRepository.getActiveMission().setName("Old Dawn");
        Assert.assertEquals(MissionRepository.getActiveMission(), mission2);
        Assert.assertEquals(MissionRepository.getActiveMission(), MissionRepository.getMissionByName("Old Dawn"));
        Assert.assertEquals(MissionRepository.getMissionByName("Old Dawn"), mission2);
    }


}
