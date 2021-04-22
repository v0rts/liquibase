package liquibase.command.core;

import liquibase.command.*;
import liquibase.integration.commandline.Main;

import java.util.ArrayList;
import java.util.List;

public class StatusCommandStep extends AbstractCliWrapperCommandStep {
    public static final CommandArgumentDefinition<String> URL_ARG;
    public static final CommandArgumentDefinition<String> USERNAME_ARG;
    public static final CommandArgumentDefinition<String> PASSWORD_ARG;
    public static final CommandArgumentDefinition<String> CHANGELOG_FILE_ARG;
    public static final CommandArgumentDefinition<String> CONTEXTS_ARG;
    public static final CommandArgumentDefinition<String> LABELS_ARG;
    public static final CommandArgumentDefinition<String> VERBOSE_ARG;

    static {
        CommandStepBuilder builder = new CommandStepBuilder(StatusCommandStep.class);
        URL_ARG = builder.argument("url", String.class).required()
            .description("The JDBC database connection URL").build();
        USERNAME_ARG = builder.argument("username", String.class)
            .description("Username to use to connect to the database").build();
        PASSWORD_ARG = builder.argument("password", String.class)
            .description("Password to use to connect to the database").build();
        CHANGELOG_FILE_ARG = builder.argument("changeLogFile", String.class)
            .description("The root changelog").build();
        CONTEXTS_ARG = builder.argument("contexts", String.class)
            .description("Changeset contexts to match").build();
        LABELS_ARG = builder.argument("labels", String.class)
            .description("Changeset labels to match").build();
        VERBOSE_ARG = builder.argument("verbose", String.class).required()
            .description("Verbose flag").build();
    }

    @Override
    public String[] getName() {
        return new String[] {"status"};
    }

    @Override
    public void run(CommandResultsBuilder resultsBuilder) throws Exception {
        CommandScope commandScope = resultsBuilder.getCommandScope();
        List<String> rhsArgs = new ArrayList<>();
        rhsArgs.add("verbose");
        String[] args = createArgs(commandScope, rhsArgs);
        for (int i=0; i < args.length; i++) {
            if (args[i].toLowerCase().startsWith("--verbose")) {
                args[i] = "--verbose";
                break;
            }
        }
        int statusCode = Main.run(args);
        addStatusMessage(resultsBuilder, statusCode);
        resultsBuilder.addResult("statusCode", statusCode);
    }

    @Override
    public void adjustCommandDefinition(CommandDefinition commandDefinition) {
        commandDefinition.setShortDescription("Generate a list of pending changesets");
        commandDefinition.setLongDescription("Generate a list of pending changesets");
    }
}
