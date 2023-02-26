package ch.hftm.astrodynamic.utils;

import java.io.IOException;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import ch.hftm.astrodynamic.scalar.ScalarFactory;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Custom Deserializer for Scalars
public class ScalarDeserializer extends StdDeserializer<Scalar> {

    private static Logger log = Log.build();  // Logger for this Deserializer

    // Constructor
    public ScalarDeserializer() {
        this(null);
    }

    // Constructor
    public ScalarDeserializer(Class<?> vc) {
        super(vc);
    }

    // Custom deserialize function
    // Returns a ScalarFactory created scalar built by the ScalarFactory with the right unit
    @Override
    public Scalar deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);
        try {
            JsonNode unitNode = node.get("unit");
            JsonNode valueNode = node.get("value");
            JsonNode numNode = valueNode.get("num");
            Quad scalarValue = Quad.fromString(numNode.asText());
            Unit scalarUnit = Enum.valueOf(Unit.class, unitNode.asText());
            return ScalarFactory.create(scalarValue, scalarUnit);
        } catch(UnitConversionError ex) {
            log.severe("Serialization of Scalar failed, invalid unit detected");
            return null;
        }
    }
    
}
