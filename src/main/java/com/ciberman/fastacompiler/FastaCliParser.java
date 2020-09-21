package com.ciberman.fastacompiler;

import com.ciberman.fastacompiler.lexer.Automata;
import com.ciberman.fastacompiler.lexer.FunctionalAutomata;
import com.ciberman.fastacompiler.lexer.MatrixAutomata;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FastaCliParser {

    public static class FastaInputCommand {
        public InputStream stream;
        public String fileName;
        public Automata automataImpl;
    }

    private CommandLineParser cliParser;
    private Options options;

    public FastaCliParser() {
        this.options = new Options();

        Option lexerOption = new Option("l", "lexer", true, "Select the lexer implementation. Values: \"functional\", \"matrix\". (Default is \"matrix\")");
        lexerOption.setRequired(false);
        this.options.addOption(lexerOption);

        Option demoOption = new Option("d", "demo", true, "Loads a built-in demo file. Values: \"basic\", \"factorial\".");
        demoOption.setRequired(false);
        this.options.addOption(demoOption);

        this.cliParser = new DefaultParser();
    }

    public FastaInputCommand parse(String[] args) throws FileNotFoundException {
        CommandLine cmd;
        try {
            cmd = this.cliParser.parse(this.options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            this.printHelpAndExit();
            return null;
        }

        FastaInputCommand input = new FastaInputCommand();
        input.automataImpl = this.getAutomataImpl(cmd.getOptionValue("lexer"));

        if (cmd.hasOption("demo")) {
            input.fileName = cmd.getOptionValue("demo") + ".fasta";
            input.stream = Main.class.getClassLoader().getResourceAsStream(input.fileName);
        } else {
            String[] inputArgs = cmd.getArgs();
            if (inputArgs.length > 0) {
                input.fileName = inputArgs[0];
                input.stream = new FileInputStream(input.fileName);
            } else {
                this.printHelpAndExit();
                return null;
            }
        }

        return input;
    }

    private void printHelpAndExit() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("fastacompiler [options] inputfile", this.options);
        System.exit(1);
    }

    private Automata getAutomataImpl(String automataType) {
        if (automataType != null) {
            switch (automataType) {
                case "functional": return new FunctionalAutomata();
                case "matrix":     return new MatrixAutomata();
                default: throw new IllegalArgumentException("The value provided for the \"lexer\" option is invalid");
            }
        }
        return new MatrixAutomata();
    }

}
