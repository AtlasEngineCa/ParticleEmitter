package emitters.lifetime;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import emitters.EmitterLifetime;
import runtime.ParticleEmitterScript;

import java.lang.reflect.InvocationTargetException;

public record EmitterLifetimeExpression(ParticleEmitterScript activeExpression, ParticleEmitterScript expirationExpression) implements EmitterLifetime {
    public static EmitterLifetime parse(JsonObject asJsonObject) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (asJsonObject == null) return new EmitterLifetimeExpression(ParticleEmitterScript.fromDouble(1), ParticleEmitterScript.fromDouble(0));
        JsonElement active_expression = asJsonObject.get("activation_expression");
        JsonElement expiration_expression = asJsonObject.get("expiration_expression");
        String activeExpression = active_expression == null ? "1" : active_expression.getAsString();
        String expirationExpression = expiration_expression == null ? "0" : expiration_expression.getAsString();

        return new EmitterLifetimeExpression(ParticleEmitterScript.fromString(activeExpression), ParticleEmitterScript.fromString(expirationExpression));
    }

    @Override
    public LifetimeState getState() {
        return null;
    }
}