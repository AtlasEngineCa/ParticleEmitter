package net.worldseed.runtime;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.worldseed.emitters.EmitterLifetime;
import net.worldseed.emitters.EmitterRate;
import net.worldseed.emitters.EmitterShape;
import net.worldseed.emitters.init.EmitterInitialization;
import net.worldseed.emitters.init.EmitterLocalSpace;
import net.worldseed.emitters.lifetime.EmitterLifetimeExpression;
import net.worldseed.emitters.lifetime.EmitterLifetimeLooping;
import net.worldseed.emitters.lifetime.EmitterLifetimeOnce;
import net.worldseed.emitters.rate.EmitterRateInstant;
import net.worldseed.emitters.rate.EmitterRateSteady;
import net.worldseed.emitters.shape.*;
import net.worldseed.particle.ParticleAppearanceTinting;
import net.worldseed.particle.ParticleInitialSpeed;
import net.worldseed.particle.ParticleLifetimeExpression;

import java.lang.reflect.InvocationTargetException;

public class ParticleParser {
    public static ParticleEmitter parse(int updatesPerSecond, JsonObject description) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
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
