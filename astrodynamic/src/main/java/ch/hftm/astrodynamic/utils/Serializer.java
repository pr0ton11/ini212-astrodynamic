package ch.hftm.astrodynamic.utils;

import java.nio.file.Paths;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import ch.hftm.astrodynamic.model.Mission;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Abstraction for the JSON serialization
public class Serializer {
    
    private static Logger log = Log.build();
    private static ObjectMapper om = null;  // Maps the object to a serializer

    private static void configureObjectMapper() {
        if (om == null) {
            om = new ObjectMapper();
            // Custom serializer modules
            SimpleModule ScalarDeserializer = new SimpleModule("ScalarDeserializer", new Version(1, 0, 0, null, null, null));
            ScalarDeserializer.addDeserializer(Scalar.class, new ScalarDeserializer());
            om.registerModule(ScalarDeserializer);
            // Enable detection of fields of any visibility, allows privte fields to be serialized
            om.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        
            // This should not be required for our use case
            // om.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        }
    }

    // Loads the MissionRepository Object from a path
    public static void fromFile(String path) {
        try {
            // Read file with embedded file parser
            MissionRepository mr = om.readValue(path, MissionRepository.class);
            // Update current mission repository from path
            MissionRepository.overrideInstance(mr);
        } catch (JsonProcessingException ex) {
            log.severe(String.format("Could not load MissionRepository from file %s:", path));
            log.severe(ex.toString());
        }
    }

    // Writes the MissionRepository Object to a path
    public static void toFile(String path) {
        try {
            // Write file with embedded file parser
            om.writeValue(Paths.get(path).toFile(), MissionRepository.getInstance());
        } catch (Exception ex) {
            log.severe(String.format("Could not write MissionRepository to file %s:", path));
            log.severe(ex.toString());
        }
    }

    // Writes the MissionRepository Object to a string
    public static String getString() {
        // Configure the object mapper, if not initialized
        configureObjectMapper();
        try {
            return om.writeValueAsString(MissionRepository.getInstance());
        } catch (JsonProcessingException ex) {
            log.severe("Could not serialize MissionRepository to string");
            log.severe(ex.toString());
            return "";
        }
    }

    // Loads the current MissionRepository from a string
    public static MissionRepository fromString(String element) {
        // Configure the object mapper, if not initialized
        configureObjectMapper();
        try {
            return om.readValue(element, MissionRepository.class);
        } catch (JsonProcessingException ex) {
            log.severe(String.format("Could not load MissionRepository from string %s:", element));
            log.severe(ex.toString());
            return null;
        }
    }

    // Deserializes mission from a json string
    public static Mission deserializeMission(String element) {
        // Configure the object mapper, if not initialized
        configureObjectMapper();
        try {
            return om.readValue(element, Mission.class);
        } catch (JsonProcessingException ex) {
            log.severe(String.format("Could not load Mission from string %s:", element));
            log.severe(ex.toString());
            return null;
        }
    }

    // Serializes a mission as a json string
    public static String serializeMission(Mission mission) {
        // Configure the object mapper, if not initialized
        configureObjectMapper();
        try {
            return om.writeValueAsString(mission);
        } catch (JsonProcessingException ex) {
            log.severe("Could not serialize Mission to string");
            log.severe(ex.toString());
            return "";
        }
    }

}
