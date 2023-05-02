import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.minestom.server.particle.Particle;
import org.junit.jupiter.api.Test;
import net.worldseed.particleemitter.runtime.ParticleParser;
import net.worldseed.particleemitter.runtime.ParticleEmitter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

public class ParticleParserTest {
    static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    static final String root = "./src/test/resources/particles/";

    @Test
    public void parseMagic() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        File file = new File(root + "magic.particle.json");
        FileInputStream fis = new FileInputStream(file);
        JsonReader reader = new JsonReader(new InputStreamReader(fis, "UTF-8"));
        JsonObject map = GSON.fromJson(reader, JsonObject.class);
        ParticleEmitter emitter = ParticleParser.parse(Particle.DUST, 1000, map);
    }

    @Test
    public void parseLoading() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        File file = new File(root + "loading.particle.json");
        FileInputStream fis = new FileInputStream(file);
        JsonReader reader = new JsonReader(new InputStreamReader(fis, "UTF-8"));
        JsonObject map = GSON.fromJson(reader, JsonObject.class);
        ParticleEmitter emitter = ParticleParser.parse(Particle.DUST, 1000, map);
    }

    @Test
    public void parseError() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        File file = new File(root + "error.particle.json");
        FileInputStream fis = new FileInputStream(file);
        JsonReader reader = new JsonReader(new InputStreamReader(fis, "UTF-8"));
        JsonObject map = GSON.fromJson(reader, JsonObject.class);
        ParticleEmitter emitter = ParticleParser.parse(Particle.DUST, 1000, map);
    }
}