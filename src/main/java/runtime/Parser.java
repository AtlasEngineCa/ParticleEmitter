package runtime;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import emitters.EmitterLifetime;
import emitters.EmitterRate;
import emitters.EmitterShape;
import emitters.init.EmitterInitialization;
import emitters.init.EmitterLocalSpace;
import emitters.lifetime.EmitterLifetimeExpression;
import emitters.lifetime.EmitterLifetimeLooping;
import emitters.lifetime.EmitterLifetimeOnce;
import emitters.rate.EmitterRateInstant;
import emitters.rate.EmitterRateSteady;
import emitters.shape.*;
import particle.ParticleAppearanceTinting;
import particle.ParticleInitialSpeed;

public class Parser {
    public static ParticleEmitter parse(JsonObject description) {
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
            rate = new EmitterRateInstant("100");
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
            lifetime = new EmitterLifetimeOnce("100");
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
            shape = new EmitterShapePoint("0", "0", "0", "0", "0", "0");
        }

        JsonElement particleInitialSpeed = components.get("minecraft:particle_initial_speed");
        JsonObject particleAppearanceTinting = components.get("minecraft:particle_appearance_tinting").getAsJsonObject();

        ParticleEmitter emitter = new ParticleEmitter(
                EmitterInitialization.parse(emitterInitialization),
                EmitterLocalSpace.parse(emitterLocalSpace),
                lifetime, rate, shape,
                ParticleInitialSpeed.parse(particleInitialSpeed),
                ParticleAppearanceTinting.parse(particleAppearanceTinting)
            );

        return null;
    }
}
