package emitters.lifetime;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import emitters.EmitterLifetime;

public record EmitterLifetimeExpression(String activeExpression, String expirationExpression) implements EmitterLifetime {
    public static EmitterLifetime parse(JsonObject asJsonObject) {
        if (asJsonObject == null) return new EmitterLifetimeExpression("", "");
        JsonElement active_expression = asJsonObject.get("activation_expression");
        JsonElement expiration_expression = asJsonObject.get("expiration_expression");
        String activeExpression = active_expression == null ? "" : active_expression.getAsString();
        String expirationExpression = expiration_expression == null ? "" : expiration_expression.getAsString();

        return new EmitterLifetimeExpression(activeExpression, expirationExpression);
    }

    @Override
    public LifetimeState getState() {
        return null;
    }
}