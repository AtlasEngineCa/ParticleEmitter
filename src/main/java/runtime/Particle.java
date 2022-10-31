package runtime;

import net.hollowcube.mql.foreign.Query;

public class Particle {
    private final ParticleEmitter emitter;
    int particle_age;

    final double particle_random_1;
    final double particle_random_2;
    final double particle_random_3;
    final double particle_random_4;

    @Query
    public double particle_random_1() {
        return particle_random_1;
    }

    @Query
    public double particle_random_2() {
        return particle_random_2;
    }

    @Query
    public double particle_random_3() {
        return particle_random_3;
    }

    @Query
    public double particle_random_4() {
        return particle_random_4;
    }

    @Query
    public double emitter_age() {
        return emitter.emitter_age();
    }

    @Query
    public double emitter_random1() {
        return emitter.emitter_random1();
    }

    @Query
    public double emitter_random2() {
        return emitter.emitter_random2();
    }

    @Query
    public double emitter_random3() {
        return emitter.emitter_random3();
    }

    @Query
    public double emitter_random4() {
        return emitter.emitter_random4();
    }

    public Particle(ParticleEmitter emitter) {
        this.particle_age = 0;

        this.particle_random_1 = Math.random();
        this.particle_random_2 = Math.random();
        this.particle_random_3 = Math.random();
        this.particle_random_4 = Math.random();

        this.emitter = emitter;
    }

    public void tick() {
        particle_age++;
    }
}
