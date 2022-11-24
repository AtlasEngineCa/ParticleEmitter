package net.worldseed.particleemitter.runtime;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.worldseed.particleemitter.emitters.EmitterLifetime;
import net.worldseed.particleemitter.emitters.EmitterRate;
import net.worldseed.particleemitter.emitters.EmitterShape;
import net.worldseed.particleemitter.emitters.init.EmitterInitialization;
import net.worldseed.particleemitter.emitters.init.EmitterLocalSpace;
import net.worldseed.particleemitter.emitters.lifetime.EmitterLifetimeExpression;
import net.worldseed.particleemitter.emitters.lifetime.EmitterLifetimeLooping;
import net.worldseed.particleemitter.emitters.lifetime.EmitterLifetimeOnce;
import net.worldseed.particleemitter.emitters.rate.EmitterRateInstant;
import net.worldseed.particleemitter.emitters.rate.EmitterRateSteady;
import net.worldseed.particleemitter.emitters.shape.*;
import net.worldseed.particleemitter.particle.ParticleAppearanceTinting;
import net.worldseed.particleemitter.particle.ParticleInitialSpeed;
import net.worldseed.particleemitter.particle.ParticleLifetimeExpression;

import java.lang.reflect.InvocationTargetException;

public class ParticleParser {
    /**
     * Parses a particle emitter from a JSON object.
     * @param updatesPerSecond The number of times per second you will call the tick function.
     * @param description The JSON object describing the particle emitter.
     * @return The particle emitter.
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static ParticleEmitter parse(net.minestom.server.particle.Particle type, int updatesPerSecond, JsonObject description) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String version = description.get("format_version").getAsString();
        JsonObject particleEffect = description.get("particle_effect").getAsJsonObject();
        JsonObject components = particleEffect.get("components").getAsJsonObject();

        JsonElement emitterLocalSpace = components.get("minecraft:emitter_local_space");
        JsonElement emitterInitialization = components.get("minecraft:emitter_initialization");

        JsonElement emitterRateInstant = components.get("minecraft:emitter_rate_instant");
        JsonElement emitterRateSteady = components.get("minecraft:emitter_rate_steady");

        EmitterRate rate;
        if (emitterRateInstant != null) {
            rate = EmitterRateInstant.parse(emitterRateInstant.getAsJsonObject());
        } else if (emitterRateSteady != null) {
            rate = EmitterRateSteady.parse(emitterRateSteady.getAsJsonObject());
        } else {
            rate = new EmitterRateInstant(ParticleEmitterScript.fromDouble(100));
        }

        JsonElement emitterLifetimeLooping = components.get("minecraft:emitter_lifetime_looping");
        JsonElement emitterLifetimeOnce = components.get("minecraft:emitter_lifetime_once");
        JsonElement emitterLifetimeExpression = components.get("minecraft:emitter_lifetime_expression");

        EmitterLifetime lifetime;
        if (emitterLifetimeLooping != null) {
            lifetime = EmitterLifetimeLooping.parse(emitterLifetimeLooping.getAsJsonObject());
        } else if (emitterLifetimeOnce != null) {
            lifetime = EmitterLifetimeOnce.parse(emitterLifetimeOnce.getAsJsonObject());
        } else if (emitterLifetimeExpression != null) {
            lifetime = EmitterLifetimeExpression.parse(emitterLifetimeExpression.getAsJsonObject());
        } else {
            lifetime = new EmitterLifetimeOnce(ParticleEmitterScript.fromDouble(100));
        }

        JsonElement emitterShapePoint = components.get("minecraft:emitter_shape_point");
        JsonElement emitterShapeSphere = components.get("minecraft:emitter_shape_sphere");
        JsonElement emitterShapeBox = components.get("minecraft:emitter_shape_box");
        JsonElement emitterShapeCustom = components.get("minecraft:emitter_shape_custom");
        JsonElement emitterShapeEntityAABB = components.get("minecraft:emitter_shape_entity_aabb");
        JsonElement emitterShapeDisc = components.get("minecraft:emitter_shape_disc");

        EmitterShape shape;
        if (emitterShapePoint != null) {
            shape = EmitterShapePoint.parse(emitterShapePoint.getAsJsonObject());
        } else if (emitterShapeSphere != null) {
            shape = EmitterShapeSphere.parse(emitterShapeSphere.getAsJsonObject());
        } else if (emitterShapeBox != null) {
            shape = EmitterShapeBox.parse(emitterShapeBox.getAsJsonObject());
        } else if (emitterShapeCustom != null) {
            shape = EmitterShapePoint.parse(emitterShapeCustom.getAsJsonObject());
        } else if (emitterShapeEntityAABB != null) {
            shape = EmitterShapeEntityAABB.parse(emitterShapeEntityAABB.getAsJsonObject());
        } else if (emitterShapeDisc != null) {
            shape = EmitterShapeDisc.parse(emitterShapeDisc.getAsJsonObject());
        } else {
            shape = new EmitterShapePoint();
        }

        JsonElement particleInitialSpeed = components.get("minecraft:particle_initial_speed");
        JsonObject particleAppearanceTinting = components.get("minecraft:particle_appearance_tinting").getAsJsonObject();
        JsonObject particleLifetimeExpression = components.get("minecraft:particle_lifetime_expression").getAsJsonObject();

        return new ParticleEmitter(
                type,
                updatesPerSecond,
                EmitterInitialization.parse(emitterInitialization),
                EmitterLocalSpace.parse(emitterLocalSpace),
                lifetime, rate, shape,
                ParticleInitialSpeed.parse(particleInitialSpeed),
                ParticleAppearanceTinting.parse(particleAppearanceTinting),
                ParticleLifetimeExpression.parse(particleLifetimeExpression)
            );
    }
}
