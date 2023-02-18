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

public class ScalarDeserializer extends StdDeserializer<Scalar> {

    private static Logger log = Log.build();

    public ScalarDeserializer() {
        this(null);
    }

    public ScalarDeserializer(Class<?> vc) {
        super(vc);
    }

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
            log.severe("Serialization of Scalar failed, invalid unit detected. Dafuq?");
            return null;
        }
    }
    
}
