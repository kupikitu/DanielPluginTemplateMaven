package cc.cerial.cerialplugintemplate.interfaces;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;

@SuppressWarnings("UnstableApiUsage")
public interface PluginCommand {
    LiteralCommandNode<CommandSourceStack> getCommand();
}
