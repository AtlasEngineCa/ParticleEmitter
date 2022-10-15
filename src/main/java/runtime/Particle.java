package runtime;

public class Particle {
    int particle_age;

    final double particle_random_1;
    final double particle_random_2;
    final double particle_random_3;
    final double particle_random_4;

    public Particle() {
        this.particle_age = 0;

        this.particle_random_1 = Math.random();
        this.particle_random_2 = Math.random();
        this.particle_random_3 = Math.random();
        this.particle_random_4 = Math.random();
    }

    public void tick() {
        particle_age++;
    }
}
