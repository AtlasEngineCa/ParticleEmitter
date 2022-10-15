package runtime;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Parser {
    public static ParticleEmitter parse(JsonObject description) {
        String version = description.get("format_version").getAsString();
        JsonObject particleEffect = description.get("particle_effect").getAsJsonObject();
        JsonObject components = particleEffect.get("components").getAsJsonObject();

        JsonElement emitterLocalSpace = components.get("minecraft:emitter_local_space");
        JsonElement emitterInitialization = components.get("minecraft:emitter_initialization");

        JsonElement emitterRateInstant = components.get("minecraft:emitter_rate_instant");
        JsonElement emitterRateSteady = components.get("minecraft:emitter_rate_steady");

        JsonElement emitterLifetimeLooping = components.get("minecraft:emitter_lifetime_looping");
        JsonElement emitterLifetimeOnce = components.get("minecraft:emitter_lifetime_once");
        JsonElement emitterLifetimeExpression = components.get("minecraft:emitter_lifetime_expression");

        JsonElement emitterShapePoint = components.get("minecraft:emitter_shape_point");
        JsonElement emitterShapeSphere = components.get("minecraft:emitter_shape_sphere");
        JsonElement emitterShapeBox = components.get("minecraft:emitter_shape_box");
        JsonElement emitterShapeCustom = components.get("minecraft:emitter_shape_custom");
        JsonElement emitterShapeEntityAABB = components.get("minecraft:emitter_shape_entity_aabb");
        JsonElement emitterShapeDisc = components.get("minecraft:emitter_shape_disc");

        JsonElement particleInitialSpeed = components.get("minecraft:particle_initial_speed");
        JsonElement particleAppearanceTinting = components.get("minecraft:particle_appearance_tinting");

        return null;
    }
}
