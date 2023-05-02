package net.worldseed.particleemitter.runtime;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.hollowcube.mql.parser.MqlParseError;
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
import net.worldseed.particleemitter.particle.ParticleLifetime;
import net.worldseed.particleemitter.particle.ParticleLifetimeExpression;

import java.lang.reflect.InvocationTargetException;

public class ParticleParser {
    /**
     * Parses a particle emitter from a JSON object.
     * @param updatesPerSecond The number of times per second you will call the tick function.
     * @param description The JSON object describing the particle emitter.
     * @return The particle emitter.
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
            try {
                rate = EmitterRateInstant.parse(emitterRateInstant.getAsJsonObject());
            } catch (MqlParseError mqlParseError) {
                throw new MqlParseError("Failed to parse emitter_rate_instant: " + mqlParseError.getMessage());
            }
        } else if (emitterRateSteady != null) {
            try {
                rate = EmitterRateSteady.parse(emitterRateSteady.getAsJsonObject());
            } catch (MqlParseError mqlParseError) {
                throw new MqlParseError("Failed to parse emitter_rate_steady: " + mqlParseError.getMessage());
            }
        } else {
            rate = new EmitterRateInstant(ParticleEmitterScript.fromDouble(100));
        }

        JsonElement emitterLifetimeLooping = components.get("minecraft:emitter_lifetime_looping");
        JsonElement emitterLifetimeOnce = components.get("minecraft:emitter_lifetime_once");
        JsonElement emitterLifetimeExpression = components.get("minecraft:emitter_lifetime_expression");

        EmitterLifetime lifetime;
        if (emitterLifetimeLooping != null) {
            try {
                lifetime = EmitterLifetimeLooping.parse(emitterLifetimeLooping.getAsJsonObject());
            } catch (MqlParseError mqlParseError) {
                throw new MqlParseError("Failed to parse emitter_lifetime_looping: " + mqlParseError.getMessage());
            }
        } else if (emitterLifetimeOnce != null) {
            try {
                lifetime = EmitterLifetimeOnce.parse(emitterLifetimeOnce.getAsJsonObject());
            } catch (MqlParseError mqlParseError) {
                throw new MqlParseError("Failed to parse emitter_lifetime_once: " + mqlParseError.getMessage());
            }
        } else if (emitterLifetimeExpression != null) {
            try {
                lifetime = EmitterLifetimeExpression.parse(emitterLifetimeExpression.getAsJsonObject());
            } catch (MqlParseError mqlParseError) {
                throw new MqlParseError("Failed to parse emitter_lifetime_expression: " + mqlParseError.getMessage());
            }
        } else {
            try {
                lifetime = new EmitterLifetimeOnce(ParticleEmitterScript.fromDouble(100));
            } catch (MqlParseError mqlParseError) {
                throw new MqlParseError("Failed to parse emitter_lifetime_once: " + mqlParseError.getMessage());
            }
        }

        JsonElement emitterShapePoint = components.get("minecraft:emitter_shape_point");
        JsonElement emitterShapeSphere = components.get("minecraft:emitter_shape_sphere");
        JsonElement emitterShapeBox = components.get("minecraft:emitter_shape_box");
        JsonElement emitterShapeCustom = components.get("minecraft:emitter_shape_custom");
        JsonElement emitterShapeEntityAABB = components.get("minecraft:emitter_shape_entity_aabb");
        JsonElement emitterShapeDisc = components.get("minecraft:emitter_shape_disc");

        EmitterShape shape;
        if (emitterShapePoint != null) {
            try {
                shape = EmitterShapePoint.parse(emitterShapePoint.getAsJsonObject());
            } catch (MqlParseError mqlParseError) {
                throw new MqlParseError("Failed to parse emitter_shape_point: " + mqlParseError.getMessage());
            }
        } else if (emitterShapeSphere != null) {
            try {
                shape = EmitterShapeSphere.parse(emitterShapeSphere.getAsJsonObject());
            } catch (MqlParseError mqlParseError) {
                throw new MqlParseError("Failed to parse emitter_shape_sphere: " + mqlParseError.getMessage());
            }
        } else if (emitterShapeBox != null) {
            try {
                shape = EmitterShapeBox.parse(emitterShapeBox.getAsJsonObject());
            } catch (MqlParseError mqlParseError) {
                throw new MqlParseError("Failed to parse emitter_shape_box: " + mqlParseError.getMessage());
            }
        } else if (emitterShapeCustom != null) {
            try {
                shape = EmitterShapePoint.parse(emitterShapeCustom.getAsJsonObject());
            } catch (MqlParseError mqlParseError) {
                throw new MqlParseError("Failed to parse emitter_shape_custom: " + mqlParseError.getMessage());
            }
        } else if (emitterShapeEntityAABB != null) {
            try {
                shape = EmitterShapeEntityAABB.parse(emitterShapeEntityAABB.getAsJsonObject());
            } catch (MqlParseError mqlParseError) {
                throw new MqlParseError("Failed to parse emitter_shape_entity_aabb: " + mqlParseError.getMessage());
            }
        } else if (emitterShapeDisc != null) {
            try {
                shape = EmitterShapeDisc.parse(emitterShapeDisc.getAsJsonObject());
            } catch (MqlParseError mqlParseError) {
                throw new MqlParseError("Failed to parse emitter_shape_disc: " + mqlParseError.getMessage());
            }
        } else {
            shape = new EmitterShapePoint();
        }

        JsonElement particleInitialSpeed = components.get("minecraft:particle_initial_speed");
        JsonObject particleAppearanceTinting = components.getAsJsonObject("minecraft:particle_appearance_tinting");
        JsonObject particleLifetimeExpression = components.getAsJsonObject("minecraft:particle_lifetime_expression");

        ParticleLifetime parsedParticleLifetimeExpression;
        ParticleAppearanceTinting parsedParticleAppearanceTinting;
        EmitterInitialization parsedEmitterInitialization;
        EmitterLocalSpace parsedEmitterLocalSpace;
        ParticleInitialSpeed parsedParticleInitialSpeed;

        try {
            parsedParticleLifetimeExpression =
                    particleLifetimeExpression != null
                            ? ParticleLifetimeExpression.parse(particleLifetimeExpression)
                            : ParticleLifetimeExpression.DEFAULT;
        } catch (Exception e) {
            throw new MqlParseError("Failed to parse particle_lifetime_expression: " + e.getMessage());
        }

        try {
            parsedParticleAppearanceTinting =
                    particleAppearanceTinting != null
                            ? ParticleAppearanceTinting.parse(particleAppearanceTinting)
                            : ParticleAppearanceTinting.DEFAULT;
        } catch (Exception e) {
            throw new MqlParseError("Failed to parse particle_appearance_tinting: " + e.getMessage());
        }

        try {
            parsedEmitterInitialization = EmitterInitialization.parse(emitterInitialization);
        } catch (Exception e) {
            throw new MqlParseError("Failed to parse emitter_initialization: " + e.getMessage());
        }

        try {
            parsedEmitterLocalSpace = EmitterLocalSpace.parse(emitterLocalSpace);
        } catch (Exception e) {
            throw new MqlParseError("Failed to parse emitter_local_space: " + e.getMessage());
        }

        try {
            parsedParticleInitialSpeed = ParticleInitialSpeed.parse(particleInitialSpeed);
        } catch (Exception e) {
            throw new MqlParseError("Failed to parse particle_initial_speed: " + e.getMessage());
        }

        return new ParticleEmitter(
            type,
            updatesPerSecond,
            parsedEmitterInitialization,
            parsedEmitterLocalSpace,
            lifetime, rate, shape,
            parsedParticleInitialSpeed,
            parsedParticleAppearanceTinting,
            parsedParticleLifetimeExpression
        );
    }

    public static ParticleEmitter parse(int updatesPerSecond, JsonObject description) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return parse(net.minestom.server.particle.Particle.DUST, updatesPerSecond, description);
    }
}
