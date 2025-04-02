package cc.cerial.cerialplugintemplate.commands.debugutils;

import cc.cerial.cerialplugintemplate.CerialPluginTemplate;
import cc.cerial.cerialplugintemplate.commands.PluginCommand;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

@SuppressWarnings("UnstableApiUsage")
public class DebugUtilsRootCommand implements PluginCommand {
    @Override
    public LiteralCommandNode<CommandSourceStack> getCommand() {
        return Commands.literal("debugutils")
                .requires(CerialPluginTemplate.getUtils().getPermCheck("*"))
                .build();
    }
}
