import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.Test;
import runtime.Parser;
import runtime.ParticleEmitter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

public class ParserTest {
    private final ClassLoader classLoader = getClass().getClassLoader();
    static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    @Test
    public void parseMagic() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        File file = new File(classLoader.getResource("magic.particle.json").getFile());
        FileInputStream fis = new FileInputStream(file);
        JsonReader reader = new JsonReader(new InputStreamReader(fis, "UTF-8"));
        JsonObject map = GSON.fromJson(reader, JsonObject.class);
        ParticleEmitter emitter = Parser.parse(map);
    }

    @Test
    public void parseLoading() throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        File file = new File(classLoader.getResource("loading.particle.json").getFile());
        FileInputStream fis = new FileInputStream(file);
        JsonReader reader = new JsonReader(new InputStreamReader(fis, "UTF-8"));
        JsonObject map = GSON.fromJson(reader, JsonObject.class);
        ParticleEmitter emitter = Parser.parse(map);
    }
}