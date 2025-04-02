package cc.cerial.cerialplugintemplate.load;

import cc.cerial.cerialplugintemplate.commands.PluginCommand;
import cc.cerial.cerialplugintemplate.commands.debugutils.DebugUtilsRootCommand;
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
 * The bootstrap. By default, it is used for registering commands.
 */
@SuppressWarnings("UnstableApiUsage")
public class CerialPluginBootstrap implements PluginBootstrap {
    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            registerSubcommands(context, commands, new DebugUtilsRootCommand().getCommand().createBuilder(),
                    "cc.cerial.cerialplugintemplate.commands.debugutils");
        });
    }

    private String removeLastPart(String packageString) {
        int dot = packageString.lastIndexOf('.');
        if (dot != -1) return packageString.substring(0, dot); else return packageString;
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
            for (ClassInfo info: scanResult.getClassesImplementing(removeLastPart(pkg)+".PluginCommand")) {
                try {
                    PluginCommand command = (PluginCommand) info.loadClass().getConstructor().newInstance();
                    if (root.build().equals(command.getCommand())) continue;
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
            for (ClassInfo info: scanResult.getClassesImplementing(removeLastPart(pkg)+".PluginCommand")) {
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
