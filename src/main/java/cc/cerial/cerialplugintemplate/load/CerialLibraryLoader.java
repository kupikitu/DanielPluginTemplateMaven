package cc.cerial.cerialplugintemplate.load;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class CerialLibraryLoader implements PluginLoader {
    private static final Map<String, String> repos = new HashMap<>();
    private static final List<String> deps = new ArrayList<>();

    static {
        repos.put("maven-central", "https://repo.maven.apache.org/maven2/");

    }

    @Override
    public void classloader(@NotNull PluginClasspathBuilder pluginClasspathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();
        for (Map.Entry<String, String> repo: repos.entrySet()) {
            resolver.addRepository(new RemoteRepository.Builder(
                    repo.getKey(),
                    "default",
                    repo.getValue()
            ).build());
        }

        for (String dep: deps)
            resolver.addDependency(new Dependency(new DefaultArtifact(dep), null));

        pluginClasspathBuilder.addLibrary(resolver);
    }
}
