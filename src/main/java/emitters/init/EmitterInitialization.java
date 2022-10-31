package emitters.init;

import com.google.gson.JsonElement;
import runtime.ParticleEmitter;
import runtime.ParticleEmitterScript;

import java.lang.reflect.InvocationTargetException;

public record EmitterInitialization(ParticleEmitterScript creationExpression, ParticleEmitterScript perUpdateExpression) {
    public static EmitterInitialization parse(JsonElement emitterInitialization) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (emitterInitialization == null) return new EmitterInitialization(null, null);

        JsonElement creation_expression = emitterInitialization.getAsJsonObject().get("creation_expression");
        JsonElement per_update_expression = emitterInitialization.getAsJsonObject().get("per_update_expression");

        String creationExpression = creation_expression == null ? null : creation_expression.getAsString();
        String perUpdateExpression = per_update_expression == null ? null : per_update_expression.getAsString();

        return new EmitterInitialization(ParticleEmitterScript.fromString(creationExpression), ParticleEmitterScript.fromString(perUpdateExpression));
    }

    public void initialize(ParticleEmitter particleEmitter) {
        if (creationExpression != null) creationExpression.evaluate(particleEmitter);
    }

    public void update(ParticleEmitter particleEmitter) {
        if (perUpdateExpression != null) perUpdateExpression.evaluate(particleEmitter);
    }
}
