package cc.cerial.cerialplugintemplate.commands.debugutils;

import cc.cerial.cerialplugintemplate.CerialPluginTemplate;
import cc.cerial.cerialplugintemplate.PluginUtils;
import cc.cerial.cerialplugintemplate.interfaces.PluginCommand;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;

@SuppressWarnings("UnstableApiUsage")
public class DebugUtilsReloadCommand implements PluginCommand {
    @Override
    public LiteralCommandNode<CommandSourceStack> getCommand() {
        return Commands.literal("reload").executes(this::execute).build();
    }

    private int execute(CommandContext<CommandSourceStack> ctx) {
        Bukkit.reloadData();
        CerialPluginTemplate.get().onReload();
        PluginUtils utils = CerialPluginTemplate.getUtils();
        utils.getExecutorOrSender(ctx).sendRichMessage("<prefix> <green>Successfully reloaded the plugin.</green>",
                utils.getPrefixPlaceholder());
        return 1;
    }
}
