package uk.ac.ebi.ddi.api.readers.expressionatlas;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.api.readers.expressionatlas.utils.FastOmicsDIReader;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.xml.validator.parser.OmicsXMLFile;
import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * This program takes a ArrayExpress Experiment File and Protocols and generate for all the
 * OmicsDI file.
 *
 * @author Yasset Perez-Riverol
 */

public class GenerateExpressionAtlasFile {

    private static final Logger logger = LoggerFactory.getLogger(GenerateExpressionAtlasFile.class);

    /**
     * This program take an output folder as a parameter an create different EBE eyes files for
     * all the project in MetabolomicsWorkbench. It loop all the project in MetabolomeWorkbench and
     * print them to the give output
     *
     * @param args
     */
    public static void main(String[] args){

        // Definite command line
        CommandLineParser parser = new PosixParser();
        Options options = new Options();

        //Help page
        String helpOpt = "help";
        options.addOption("h", helpOpt, false, "print help message");

        String experimentFileStr = "experimentFile";
        options.addOption(experimentFileStr, true, "Input File with all the ArrayExpress experiments.");

        String geneFileStr  = "geneFile";
        options.addOption(geneFileStr, true, "Input File with all the ArrayExpress protocols.");

        String omicsDIFileStr = "omicsDIFile";
        options.addOption(omicsDIFileStr, true, "Output File for omicsDI");

        // Parse command line
        CommandLine line = null;
        try {
            line = parser.parse(options, args);
            if (line.hasOption(helpOpt) || !line.hasOption(geneFileStr) ||
                    !line.hasOption(experimentFileStr)
                    || !line.hasOption(omicsDIFileStr)) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("validatorCLI", options);
            }else{
                File omicsDIFile = new File (line.getOptionValue(omicsDIFileStr));
                OmicsXMLFile experiments = new OmicsXMLFile(new File (line.getOptionValue(experimentFileStr)));
                System.out.println(experiments.getAllEntries().size());
                List<Entry> genes = FastOmicsDIReader.getInstance(new File(line.getOptionValue(geneFileStr))).read();
                System.out.println(genes.size());
                generate(experiments, genes, omicsDIFile);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception ex){
            ex.getStackTrace();
        }


    }

    /**
     * Performs the EB-eye generation of a defined public project, submission summary, and output directory.
     * @throws Exception
     */
    public static void generate(OmicsXMLFile experimentFile, List<Entry> genes, File outputFile) throws Exception {

        OmicsDataMarshaller mm = new OmicsDataMarshaller();

        Database database = new Database();
        database.setDescription(experimentFile.getDescription());
        database.setName(experimentFile.getName());
        database.setReleaseDate(experimentFile.getReleaseDate());
        List<Entry> entries = experimentFile.getAllEntries();

        entries.parallelStream().forEach( entry ->{
            Set<String> geneSet = genes.parallelStream().filter(geneEntry -> (geneEntry.getCrossReferences() != null && geneEntry.getCrossReferences().containsValue(entry.getId())))
                    .collect(Collectors.mapping(Entry::getId, Collectors.toSet()));

            entry.addCrossReferenceValue(DSField.CrossRef.ENSEMBL_EXPRESSION_ATLAS.getName(), geneSet);
        });

        database.setEntryCount(entries.size());
        database.setEntries(entries);
        mm.marshall(database, new FileWriter(outputFile));

    }
}
