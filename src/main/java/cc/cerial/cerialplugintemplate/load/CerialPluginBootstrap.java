package cc.cerial.cerialplugintemplate.load;

import cc.cerial.cerialplugintemplate.interfaces.PluginCommand;
import cc.cerial.cerialplugintemplate.interfaces.RootCommand;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

/**
 * The bootstrap. By default, it is used for registering commands. You may use it for (almost)
 * anything you need to do before any plugins or worlds load.
 */
@SuppressWarnings("UnstableApiUsage")
public class CerialPluginBootstrap implements PluginBootstrap {
    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            // TODO: Add subcommands using registerSubcommands() method, or registerCommands() to register
            // individual commands. 
        });
    }

    /**
     * Registers subcommands under the package with a root command.
     * @param ctx The bootstrap context.
     * @param event The event from the registerEventHandler lambda.
     * @param root The root command you want to use.
     * @param pkg The package of where the commands will register from.
     * @see #registerCommands(BootstrapContext, ReloadableRegistrarEvent, String)
     */
    private void registerSubcommands(BootstrapContext ctx,
                                     ReloadableRegistrarEvent<@NotNull Commands> event,
                                     LiteralArgumentBuilder<CommandSourceStack> root,
                                     String pkg) {
        ctx.getLogger().info("Registering subcommands from package {}...", pkg);
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .addClassLoader(this.getClass().getClassLoader())
                .acceptPackages(pkg)
                .scan()) {
            for (ClassInfo info: scanResult.getClassesImplementing("cc.cerial.cerialplugintemplate.interfaces.PluginCommand")) {
                try {
                    PluginCommand command = (PluginCommand) info.loadClass().getConstructor().newInstance();
                    if (command.getClass().getAnnotation(RootCommand.class) != null) continue;
                    root.then(command.getCommand());
                    ctx.getLogger().info("Registered command /{} {} from class {}.", root.getLiteral(),
                            command.getCommand().getLiteral(), info.getSimpleName());
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        event.registrar().register(root.build());
    }

    /**
     * Registers individual commands under the package.
     * @param ctx The bootstrap context.
     * @param event The event from the registerEventHandler lambda.
     * @param pkg The package of where the commands will register from.
     * @see #registerSubcommands(BootstrapContext, ReloadableRegistrarEvent, LiteralArgumentBuilder, String)
     */
    private void registerCommands(BootstrapContext ctx,
                                  ReloadableRegistrarEvent<@NotNull Commands> event,
                                  String pkg) {
        ctx.getLogger().info("Registering commands from package {}...", pkg);
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .addClassLoader(this.getClass().getClassLoader())
                .acceptPackages(pkg)
                .scan()) {
            for (ClassInfo info: scanResult.getClassesImplementing("cc.cerial.cerialplugintemplate.interfaces.PluginCommand")) {
                try {
                    PluginCommand command = (PluginCommand) info.loadClass().getConstructor().newInstance();
                    event.registrar().register(command.getCommand());
                    ctx.getLogger().info("Registered command /{} from class {}.", command.getCommand().getLiteral(),
                            info.getSimpleName());
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
