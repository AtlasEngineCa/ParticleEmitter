package net.worldseed.particleemitter.emitters.lifetime;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.worldseed.particleemitter.emitters.EmitterLifetime;
import net.worldseed.particleemitter.runtime.ParticleEmitterScript;
import net.worldseed.particleemitter.runtime.ParticleInterface;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public final class EmitterLifetimeExpression implements EmitterLifetime {
    private final ParticleEmitterScript activeExpression;
    private final ParticleEmitterScript expirationExpression;
    private boolean expired = false;

    public EmitterLifetimeExpression(ParticleEmitterScript activeExpression, ParticleEmitterScript expirationExpression) {
        this.activeExpression = activeExpression;
        this.expirationExpression = expirationExpression;
    }

    public static EmitterLifetime parse(JsonObject asJsonObject) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (asJsonObject == null)
            return new EmitterLifetimeExpression(ParticleEmitterScript.fromDouble(1), ParticleEmitterScript.fromDouble(0));
        JsonElement active_expression = asJsonObject.get("activation_expression");
        JsonElement expiration_expression = asJsonObject.get("expiration_expression");
        String activeExpression = active_expression == null ? "1" : active_expression.getAsString();
        String expirationExpression = expiration_expression == null ? "0" : expiration_expression.getAsString();

        return new EmitterLifetimeExpression(ParticleEmitterScript.fromString(activeExpression), ParticleEmitterScript.fromString(expirationExpression));
    }

    @Override
    public LifetimeState getState(ParticleInterface i) {
        if (expired) return LifetimeState.DEAD;
        if (expirationExpression.evaluate(i) != 0) {
            expired = true;
            return LifetimeState.DEAD;
        }

        if (activeExpression.evaluate(i) != 0) return LifetimeState.ALIVE;
        return LifetimeState.INACTIVE;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (EmitterLifetimeExpression) obj;
        return Objects.equals(this.activeExpression, that.activeExpression) &&
                Objects.equals(this.expirationExpression, that.expirationExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activeExpression, expirationExpression);
    }

    @Override
    public String toString() {
        return "EmitterLifetimeExpression[" +
                "activeExpression=" + activeExpression + ", " +
                "expirationExpression=" + expirationExpression + ']';
    }

}