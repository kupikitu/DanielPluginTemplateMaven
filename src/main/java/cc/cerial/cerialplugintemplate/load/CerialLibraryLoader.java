package cc.cerial.cerialplugintemplate.load;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public class CerialLibraryLoader implements PluginLoader {
    private static final Map<String, String> repos = new HashMap<>();
    private static final List<String> deps = new ArrayList<>();

    static {
        repos.put("maven-central", "https://repo.maven.apache.org/maven2/");
        deps.add("io.github.classgraph:classgraph:4.8.179");
        deps.add("org.spongepowered:configurate-yaml:4.2.0");
    }

    @Override
    public void classloader(@NotNull PluginClasspathBuilder pluginClasspathBuilder) {
        ComponentLogger logger = pluginClasspathBuilder.getContext().getLogger();
        MiniMessage mm = MiniMessage.miniMessage();
        logger.info("Loading libraries...");
        MavenLibraryResolver resolver = new MavenLibraryResolver();
        for (Map.Entry<String, String> repo: repos.entrySet()) {
            resolver.addRepository(new RemoteRepository.Builder(
                    repo.getKey(),
                    "default",
                    repo.getValue()
            ).build());
            logger.info(mm.deserialize("Registered repository <b><id></b> with URL <b><url></b>...",
                    Placeholder.unparsed("id", repo.getKey()),
                    Placeholder.unparsed("url", repo.getValue())));
        }

        for (String dep: deps) {
            resolver.addDependency(new Dependency(new DefaultArtifact(dep), null));
            logger.info(mm.deserialize("Added dependency <b><dep></b>...",
                    Placeholder.unparsed("dep", dep)));
        }

        pluginClasspathBuilder.addLibrary(resolver);
    }
}
