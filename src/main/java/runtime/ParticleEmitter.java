package runtime;

import emitters.EmitterLifetime;
import emitters.EmitterRate;
import emitters.EmitterShape;
import emitters.init.EmitterInitialization;
import emitters.init.EmitterLocalSpace;
import net.hollowcube.mql.foreign.Query;
import net.hollowcube.mql.jit.MqlCompiler;
import particle.ParticleAppearanceTinting;
import particle.ParticleInitialSpeed;

public class ParticleEmitter {
    private int emitter_age;

    private final double emitter_random1;
    private final double emitter_random2;
    private final double emitter_random3;
    private final double emitter_random4;

    private final EmitterLocalSpace local_space;
    private final EmitterInitialization initialization;

    private final EmitterLifetime lifetime;
    private final EmitterRate rate;
    private final EmitterShape shape;

    private final ParticleAppearanceTinting particleColour;
    private final ParticleInitialSpeed particleSpeed;

    @Query
    public double emitter_age() {
        return emitter_age;
    }

    @Query
    public double emitter_random1() {
        return emitter_random1;
    }

    @Query
    public double emitter_random2() {
        return emitter_random2;
    }

    @Query
    public double emitter_random3() {
        return emitter_random3;
    }

    @Query
    public double emitter_random4() {
        return emitter_random4;
    }

    public ParticleEmitter(EmitterInitialization initialization, EmitterLocalSpace local_space,
                           EmitterLifetime lifetime, EmitterRate rate, EmitterShape shape,
                           ParticleInitialSpeed particleSpeed, ParticleAppearanceTinting particleColour) {
        this.emitter_age = 0;

        this.emitter_random1 = Math.random();
        this.emitter_random2 = Math.random();
        this.emitter_random3 = Math.random();
        this.emitter_random4 = Math.random();

        this.initialization = initialization;
        this.local_space = local_space;

        this.lifetime = lifetime;
        this.rate = rate;
        this.shape = shape;
        this.particleSpeed = particleSpeed;
        this.particleColour = particleColour;

        MqlCompiler<ParticleEmitterScript> compiler = new MqlCompiler<>(ParticleEmitterScript.class);
        Class<ParticleEmitterScript> scriptClass = compiler.compile("1 + variable.emitter_age");
        try {
            System.out.println(scriptClass.newInstance().evaluate(this));
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void tick() {
        emitter_age++;
    }

    @Override
    public String toString() {
        return "ParticleEmitter{" +
                "emitter_age=" + emitter_age +
                ", emitter_random1=" + emitter_random1 +
                ", emitter_random2=" + emitter_random2 +
                ", emitter_random3=" + emitter_random3 +
                ", emitter_random4=" + emitter_random4 +
                ", local_space=" + local_space +
                ", initialization=" + initialization +
                ", lifetime=" + lifetime +
                ", rate=" + rate +
                ", shape=" + shape +
                ", particleColour=" + particleColour +
                ", particleSpeed=" + particleSpeed +
                '}';
    }
}
