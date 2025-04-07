package cc.cerial.cerialplugintemplate;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;

import java.util.function.Predicate;

/**
 * A class which houses basic utility functions.
 */
public class PluginUtils {
    private final String prefix;

    /**
     * Constructs a new PluginUtils class.
     * @param prefix The prefix for sending messages.
     */
    public PluginUtils(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefixRaw() {
        return this.prefix;
    }

    /**
     * @return A TagResolver for {@code <prefix>} tag.
     */
    public TagResolver.Single getPrefixPlaceholder() {
        return Placeholder.parsed("prefix", getPrefixRaw());
    }

    /**
     * Gives a predicate for a permission check, which checks if the executor has the required permission
     * (if there is one) or if the sender has one.
     * @param permission The permission you want to check.
     * @return The permission check predicate.
     */
    @SuppressWarnings("UnstableApiUsage")
    public Predicate<CommandSourceStack> getPermCheck(String permission) {
        return (s ->
                (s.getExecutor() != null && s.getExecutor().hasPermission(permission)) ||
                  s.getSender().hasPermission(permission));
    }

    @SuppressWarnings("UnstableApiUsage")
    public CommandSender getExecutorOrSender(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack stack = ctx.getSource();
        return stack.getExecutor() != null ? stack.getExecutor() : stack.getSender();
    }
}
