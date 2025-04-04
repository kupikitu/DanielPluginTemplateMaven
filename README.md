# CerialPluginTemplate v2 - Maven
This plugin template uses the Maven build system instead of Gradle. Unfortunately, this removes the paper-plugin.yml
auto-generation and running servers from the IDE, but now that we discovered remote debugging, it ain't needed no more.
## How to use?
- Click on "Use this template"
- Click "Create a new repository"
- Create a new repository and you're done!
## Loading Libraries
The automatic library loading feature was also removed because of Maven, but don't worry, it is still possible
using manual declaration.<br>
Please note that libraries you want to load on boot must use `<scope>provided</scope>`, otherwise you will just shade them for no reason.
- Go to `src/main/java/cc/cerial/cerialplugintemplate/load/CerialLibraryLoader.java`
- Go to the static block in the code
- To add a repo, put a value in the `repos` HashMap. The key is the repo ID, and the value is the URL.
- To add a dependency, add a value to the `deps` list.
## Commands
This template uses the [Paper Brigadier Command API](https://docs.papermc.io/paper/dev/command-api/basics/introduction).
Before using commands, make sure to read the documentation to understand how this system works, as it can be a bit confusing.<br>
To create a new command, simply create a new class implementing PluginCommand, and override the `getCommand()` method
to set your own command.<br>
If you want to register individual commands (/one, /two, /three) then make the classes in the `commands` package.
Otherwise, make a new package inside commands and name it the root command. If your root command is `/root`, then
you need to make a RootCommand class for handling the root command, and the subcommand classes (RootOneCommand, RootTwoCommand).<br>
Once you're done making your commands, head over to CerialPluginBootstrap, and register your commands inside the lambda
using the `CerialPluginBootstrap#registerSubcommands()` (for subcommands) or `CerialPluginBootstrap#registerCommands` (for individual commands)
methods. These methods will reflectively register all commands in a package in order to save the hassle of manually registering
commands.
### Permission checks
In order to not waste time writing permission checks for each command, there is a utility function for that.<br>
In your `getCommand()` method, add a `.requires()` which allows you to pass in a Predicate. Then, pass in
`CerialPluginTemplate.getUtils().getPermCheck(perm)`, `perm` being the permission you want to check. The function will
automatically handle the check if there is an executor available (if ran through /execute), or if there isn't, it checks
the command sender. 
## Remote Debugging & Hot reloading
To enable debugging on your server, please follow the guide from PaperMC: https://docs.papermc.io/paper/dev/debugging#using-a-remote-debugger<br>
Any JDK will work, but if possible, use JetBrains Runtime JDK, as it has much more debugging capability.
## Configuration
This template uses the [Configurate](https://github.com/SpongePowered/Configurate) library for configuration.<br>
First, make the configuration you want in the config.yml, then to access the configuration, use
`CerialPluginTemplate.getPluginConfig()` and then either access to YamlConfigurationLoader by using `#getLoader()` or access
the CommentedConfigurationNode by using `#getNode()`.