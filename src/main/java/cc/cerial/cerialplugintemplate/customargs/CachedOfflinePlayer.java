package cc.daniel.danielplugintemplate.customargs;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import cc.daniel.Danielplugintemplate.danielPluginTemplate;
import cc.daniel.danielplugintemplate.PluginUtils;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class CachedOfflinePlayer implements CustomArgumentType<OfflinePlayer, String> {

    private final boolean completePlayers;

    public CachedOfflinePlayer(boolean completePlayers) {
        this.completePlayers = completePlayers;
    }

    @Override
    public OfflinePlayer parse(StringReader reader) throws CommandSyntaxException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.string();
    }

    @Override
    public <S> OfflinePlayer parse(StringReader reader, S source) throws CommandSyntaxException {
        PluginUtils utils = danielPluginTemplate.getUtils();
        String name = getNativeType().parse(reader);
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(name);
        if (player == null)
            throw utils.createSyntaxException("<prefix> <red><u><player></u> has never joined the server.</red>",
                                                    Placeholder.unparsed("player", name));
        
        return player;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (!completePlayers) return builder.buildFuture();

        Arrays.stream(Bukkit.getServer().getOfflinePlayers())
            .filter(p -> p.getName().toLowerCase().startsWith(builder.getRemainingLowerCase()))
            .forEach(p -> builder.suggest(p.getName()));
        return builder.buildFuture();
    }
}
