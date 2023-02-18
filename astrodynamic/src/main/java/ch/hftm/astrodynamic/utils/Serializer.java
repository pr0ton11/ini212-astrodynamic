package ch.hftm.astrodynamic.utils;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
        
            // om.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        }
    }

    // Loads the MissionRepository Object from a path
    public static void fromFile(String path) {

    }

    // Writes the MissionRepository Object to a path
    public static void toFile(String path) {

    }

    // Writes the MissionRepository Object to a string
    public static String getString() {
        // Configure the object mapper, if not initialized
        configureObjectMapper();
        try {
            return om.writeValueAsString(MissionRepository.getInstance());
        } catch (JsonProcessingException ex) {
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
            log.severe(ex.toString());
            return "";
        }
    }

}
