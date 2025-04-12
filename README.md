# DanielPluginTemplate v2 - Maven

This **haunted** plugin template uses the Maven build system instead of Gradle. Unfortunately, that means no more auto-generating `paper-plugin.yml` or running servers from the IDE — but now that we’ve discovered remote debugging (bless DJ Baraba), we don’t need that no more.

## How to use?

- Click **"Use this template"**
- Click **"Create a new repository"**
- Create your new repository, and boom — you're all set. Qita magic.

## Loading Libraries

Automatic library loading is gone (curse you, Maven), but don't worry — manual declaration still works just fine. Just gotta put in a little **muda** effort.

> **Important:** Libraries you want loaded on boot must use `<scope>provided</scope>`. If not, you’ll just shade them in for no reason like a confused dzabalebaros.

Here’s what to do:

- Open `src/main/java/cc/daniel/danielplugintemplate/load/DanielLibraryLoader.java`
- Find the static block in the code
- To add a repo: add a value in the `repos` HashMap (key = repo ID, value = repo URL)
- To add a dependency: add it to the `deps` list

## Commands

This template uses the [Paper Brigadier Command API](https://docs.papermc.io/paper/dev/command-api/basics/introduction).

Before getting lost in command chaos, make sure to read the docs above. It can get a little cursed if you're not careful.

To create a new command:

- Make a class that implements `PluginCommand`
- Override the `getCommand()` method to define your command

If you want individual commands like `/one`, `/two`, or `/three`, create the classes in the `commands` package.

If you're building a root command (e.g., `/root`), do this:

- Make a package inside `commands` named `root`
- Create a `RootCommand` class for the main command
- Add subcommands like `RootOneCommand`, `RootTwoCommand`, etc.

Once you're done haunting your commands into existence, go to `DanielPluginBootstrap` and register them:

- Use `registerCommands()` for individual ones
- Use `registerSubcommands()` for grouped/root commands

These methods reflectively register all commands in a package — no more manual command registration nightmares.

### Permission Checks

To avoid the *muda* of writing permission checks for every single command:

In `getCommand()`, add a `.requires()` and pass in a predicate:

```java
.requires(DanielPluginTemplate.getUtils().getPermCheck("your.permission.here"))
```

It handles everything — whether it’s being executed through `/execute` or straight from the command sender.

## Remote Debugging & Hot Reloading

Wanna step into the haunted depths of your server? Enable debugging with help from the spirits of PaperMC:

👉 [Remote Debugging Guide](https://docs.papermc.io/paper/dev/debugging#using-a-remote-debugger)

Any JDK works, but the **JetBrains Runtime JDK** is especially powerful — like DJ Baraba himself summoned it for extra debugging juju.

## Configuration

This template uses the [Configurate](https://github.com/SpongePowered/Configurate) library — elegant, flexible, and spooky fast.

To use:

- Define what you need in `config.yml`
- To access it, use `DanielPluginTemplate.getPluginConfig()`

From there:

- Use `#getLoader()` for the YamlConfigurationLoader
- Or use `#getNode()` to get the root CommentedConfigurationNode

Stay organized, stay haunted.
