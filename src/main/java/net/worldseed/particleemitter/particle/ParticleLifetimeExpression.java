package net.worldseed.particleemitter.particle;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.worldseed.particleemitter.runtime.ParticleEmitterScript;
import net.worldseed.particleemitter.runtime.ParticleInterface;

import java.lang.reflect.InvocationTargetException;

public final class ParticleLifetimeExpression implements ParticleLifetime {
    public static final ParticleLifetime DEFAULT;
    private final ParticleEmitterScript expiration_expression;
    private final ParticleEmitterScript max_lifetime;

    static {
        try {
            DEFAULT = new ParticleLifetimeExpression(ParticleEmitterScript.fromDouble(0), null);
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public ParticleLifetimeExpression(ParticleEmitterScript expiration_expression, ParticleEmitterScript max_lifetime) {
        this.expiration_expression = expiration_expression;
        this.max_lifetime = max_lifetime;
    }

    public static ParticleLifetimeExpression parse(JsonObject asJsonObject) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (asJsonObject == null)
            return new ParticleLifetimeExpression(ParticleEmitterScript.fromDouble(0), null);

        ParticleEmitterScript expiration_expression = null;
        ParticleEmitterScript max_lifetime = null;

        JsonElement expirationExpressionEl = asJsonObject.get("expiration_expression");
        JsonElement maxLifetimeEl = asJsonObject.get("max_lifetime");

        if (expirationExpressionEl == null && maxLifetimeEl == null)
            return new ParticleLifetimeExpression(ParticleEmitterScript.fromDouble(0), null);

        if (expirationExpressionEl != null)
            expiration_expression = ParticleEmitterScript.fromString(expirationExpressionEl.getAsString());
        else max_lifetime = ParticleEmitterScript.fromString(maxLifetimeEl.getAsString());

        return new ParticleLifetimeExpression(expiration_expression, max_lifetime);
    }

    @Override
    public String toString() {
        return "ParticleLifetimeExpression{" +
                "expiration_expression=" + expiration_expression +
                ", max_lifetime=" + max_lifetime +
                '}';
    }

    @Override
    public boolean isAlive(ParticleInterface i) {
        if (expiration_expression != null) {
            return expiration_expression.evaluate(i) != 0;
        } else {
            return i.particle_age() < max_lifetime.evaluate(i);
        }
    }
}