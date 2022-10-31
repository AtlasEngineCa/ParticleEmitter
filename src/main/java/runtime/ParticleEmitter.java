package runtime;

import emitters.EmitterLifetime;
import emitters.EmitterRate;
import emitters.EmitterShape;
import emitters.init.EmitterInitialization;
import emitters.init.EmitterLocalSpace;
import particle.ParticleAppearanceTinting;
import particle.ParticleInitialSpeed;

import java.util.HashSet;
import java.util.Set;

public class ParticleEmitter implements ParticleInterface {
    private Set<Particle> particles = new HashSet<>();

    private int emitter_age;
    private final double emitter_random_1;
    private final double emitter_random_2;
    private final double emitter_random_3;
    private final double emitter_random_4;

    private final EmitterLocalSpace local_space;
    private final EmitterInitialization initialization;

    private final EmitterLifetime lifetime;
    private final EmitterRate rate;
    private final EmitterShape shape;

    private final ParticleAppearanceTinting particleColour;
    private final ParticleInitialSpeed particleSpeed;

    public int particle_count() {
        return particles.size();
    }
    public double emitter_age() {
        return emitter_age;
    }
    public double emitter_random_1() {
        return emitter_random_1;
    }
    public double emitter_random_2() {
        return emitter_random_2;
    }
    public double emitter_random_3() {
        return emitter_random_3;
    }
    public double emitter_random_4() {
        return emitter_random_4;
    }
    public double particle_age() {
        return 0;
    }
    public double particle_random_1() {
        return 0;
    }
    public double particle_random_2() {
        return 0;
    }
    public double particle_random_3() {
        return 0;
    }
    public double particle_random_4() {
        return 0;
    }

    public ParticleEmitter(EmitterInitialization initialization, EmitterLocalSpace local_space,
                           EmitterLifetime lifetime, EmitterRate rate, EmitterShape shape,
                           ParticleInitialSpeed particleSpeed, ParticleAppearanceTinting particleColour) {
        this.emitter_age = 0;

        this.emitter_random_1 = Math.random();
        this.emitter_random_2 = Math.random();
        this.emitter_random_3 = Math.random();
        this.emitter_random_4 = Math.random();

        this.initialization = initialization;
        this.local_space = local_space;

        this.lifetime = lifetime;
        this.rate = rate;
        this.shape = shape;
        this.particleSpeed = particleSpeed;
        this.particleColour = particleColour;
    }

    public void tick() {
        emitter_age++;
        boolean canCreateParticle = rate.canEmit(this);
    }

    @Override
    public String toString() {
        return "ParticleEmitter{" +
                "emitter_age=" + emitter_age +
                ", emitter_random1=" + emitter_random_1 +
                ", emitter_random2=" + emitter_random_2 +
                ", emitter_random3=" + emitter_random_3 +
                ", emitter_random4=" + emitter_random_4 +
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
