package co.edu.unbosque.entrecolbackend.util;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.Year;

public class YearAdapter implements JsonSerializer<Year>, JsonDeserializer<Year> {

    @Override
    public JsonElement serialize(Year year, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(year.getValue());
    }

    @Override
    public Year deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return Year.of(json.getAsInt());
    }
}

