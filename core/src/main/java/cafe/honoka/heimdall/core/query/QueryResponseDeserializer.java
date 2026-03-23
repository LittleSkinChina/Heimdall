package cafe.honoka.heimdall.core.query;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class QueryResponseDeserializer implements JsonDeserializer<BaseQueryResponse> {
    @Override
    public BaseQueryResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.getAsJsonObject().has("xuid")) {
            return context.deserialize(json, XuidQueryResponse.class);
        } else if (json.getAsJsonObject().has("uuid")) {
            return context.deserialize(json, UuidQueryResponse.class);
        } else {
            throw new JsonParseException("Unknown query response type");
        }
    }
}
