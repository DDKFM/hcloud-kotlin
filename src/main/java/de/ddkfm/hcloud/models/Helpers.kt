package de.ddkfm.hcloud.models

import org.apache.commons.cli.CommandLineParser
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Options

/**
 * Created by maxsc on 20.02.2018.
 */
class Cli {
    private var options = Options()
    private lateinit var args : Array<String>;
    constructor(args : Array<String>) {
        this.args = args
        options.addOption("t", "token", true, "token")
        options.addOption("h", "help", false, "show help")
        options.addOption("s", "server", true, "Server-API")
        options.addOption("c", "create", true, "Create")
    }
    fun parse() {
        var parser = DefaultParser()
        var cmd = parser.parse(this.options, this.args)

        if(cmd.hasOption("s")) {

        }

    }
    private fun help() {
        var formatter = HelpFormatter();
        formatter.printHelp("Kotlin-API", options)
        System.exit(0);
    }
}