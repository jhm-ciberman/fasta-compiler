package com.ciberman.fastacompiler;

import com.ciberman.fastacompiler.lexer.Automata;
import com.ciberman.fastacompiler.lexer.FunctionalAutomata;
import com.ciberman.fastacompiler.lexer.MatrixAutomata;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FastaCliParser {

    public static class FastaCommandConfig {
        public FileInputSource inputSource;
        public String outputPath;
        public Automata automataImpl;
    }

    private final CommandLineParser cliParser;
    private final Options options;

    public FastaCliParser() {
        this.options = new Options();

        Option lexerOption = new Option("l", "lexer", true, "Select the lexer implementation. Values: \"functional\", \"matrix\". (Default is \"matrix\")");
        lexerOption.setRequired(false);
        this.options.addOption(lexerOption);

        Option demoOption = new Option("d", "demo", true, "Loads a built-in demo file. Values: \"99bottles\", \"basic\", \"factorial\", \"if\", \"loop\", \"syntax\", \"vars\".");
        demoOption.setRequired(false);
        this.options.addOption(demoOption);

        Option outOption = new Option("o", "out", true, "Specify the output assambly file");
        outOption.setRequired(false);
        this.options.addOption(outOption);

        this.cliParser = new DefaultParser();
    }

    public FastaCommandConfig parse(String[] args) throws FileNotFoundException {
        CommandLine cmd;
        try {
            cmd = this.cliParser.parse(this.options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            this.printHelpAndExit();
            return null;
        }

        FastaCommandConfig config = new FastaCommandConfig();
        config.automataImpl = this.getAutomataImpl(cmd.getOptionValue("lexer"));

        if (cmd.hasOption("demo")) {
            String fileName = cmd.getOptionValue("demo") + ".fasta";
            InputStream stream = Main.class.getClassLoader().getResourceAsStream(fileName);
            config.inputSource = new FileInputSource(stream, fileName);
            config.outputPath = cmd.getOptionValue("demo") + ".asm";
        } else {
            String[] inputArgs = cmd.getArgs();
            if (inputArgs.length > 0) {
                config.inputSource = new FileInputSource(new FileInputStream(inputArgs[0]), inputArgs[0]);
                config.outputPath = inputArgs[0] + ".asm";
            } else {
                this.printHelpAndExit();
                return null;
            }
        }

        if (cmd.hasOption("out")) {
            config.outputPath = cmd.getOptionValue("out");
        }

        return config;
    }

    private void printHelpAndExit() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("fasta-compiler [options] inputfile", this.options);
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
