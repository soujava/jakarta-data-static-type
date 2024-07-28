package os.expert.demo.helidon;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.nosql.AttributeConverter;
import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.semistructured.Element;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class EngineConverter implements AttributeConverter<Engine,Object> {

    private static final Logger LOGGER = Logger.getLogger(EngineConverter.class.getName());

    private static final Jsonb JSONB = JsonbBuilder.create();

    @Override
    public Object convertToDatabaseColumn(Engine attribute) {
        LOGGER.info("Converting to database column: " + attribute);
        if (attribute == null) {
            return null;
        }
        return JSONB.fromJson(JSONB.toJson(attribute), Map.class);
    }

    @Override
    public Engine convertToEntityAttribute(Object dbData) {

        if(dbData == null) {
            return null;
        }
        if(dbData instanceof Element element) {
            var elements = element.get(new TypeReference<List<Element>>() {
            }).stream().collect(Collectors.toMap(Element::name, Element::get));
            return JSONB.fromJson(JSONB.toJson(elements), Engine.class);
        }
        if(dbData instanceof Map map) {
            return JSONB.fromJson(JSONB.toJson(map), Engine.class);
        }
        throw new IllegalArgumentException("The dbData is not a valid type: " + dbData.getClass());
    }
}