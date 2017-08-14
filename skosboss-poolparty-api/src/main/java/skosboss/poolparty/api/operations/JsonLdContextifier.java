package skosboss.poolparty.api.operations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.jsonldjava.core.JsonLdConsts;
import java.io.IOException;
import java.util.Objects;

public final class JsonLdContextifier {

	private JsonLdContextifier() {}

	public static JsonNode contextify(JsonNode json, JsonNode context) throws IOException {
		Objects.requireNonNull(json, "json should not be null");
		Objects.requireNonNull(context, "context should not be null");

		if (json.isObject()) {
			return contextifyObject(json, context);
		} else {
			return contextifyList(json, context);
		}
	}

	private static JsonNode contextifyObject(JsonNode json, JsonNode context) {
		return ((ObjectNode) json).set(JsonLdConsts.CONTEXT, context);
	}

	private static JsonNode contextifyList(JsonNode json, JsonNode context) {
		JsonNodeFactory factory = JsonNodeFactory.instance;
		ObjectNode doc = factory.objectNode();

		if (json.size() == 0) {
			return doc;
		}

		doc.set(JsonLdConsts.CONTEXT, context);
		doc.set("member", json);

		return doc;
	}
}
