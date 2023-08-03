package uk.ac.ebi.ddi.api.readers.arrayexpress.arrayexpresscli;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ddi.api.readers.arrayexpress.arrayexpresscli.utils.ArrayExpressUtils;
import uk.ac.ebi.ddi.api.readers.arrayexpress.arrayexpresscli.utils.Constants;
import uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.ExperimentReader;
import uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.experiments.*;
import uk.ac.ebi.ddi.api.readers.arrayexpress.protocolsreader.ProtocolReader;
import uk.ac.ebi.ddi.api.readers.arrayexpress.protocolsreader.model.protocols.Protocol;
import uk.ac.ebi.ddi.api.readers.arrayexpress.protocolsreader.model.protocols.Protocols;
import uk.ac.ebi.ddi.ddidomaindb.dataset.DSField;
import uk.ac.ebi.ddi.xml.validator.parser.marshaller.OmicsDataMarshaller;
import uk.ac.ebi.ddi.xml.validator.parser.model.Database;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entries;
import uk.ac.ebi.ddi.xml.validator.parser.model.Entry;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * This program takes a ArrayExpress Experiment File and Protocols and generate for all the
 * OmicsDI file.
 *
 * @author Yasset Perez-Riverol
 */

public class GenerateArrayExpressFile {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateArrayExpressFile.class);

    private static final String NOT_AVAILABLE = "Not available";

    private static final String DATABASE_NAME = "ArrayExpress";

    private static final String ARRAYEXPRESS_DESCRIPTION = "ArrayExpress Archive of " +
            "Functional Genomics Data stores data from high-throughput functional genomics experiments, " +
            "and provides these data for reuse to the research community.";

    /**
     * This program take an output folder as a parameter an create different EBE eyes files for
     * all the project in MetabolomicsWorkbench. It loop all the project in MetabolomeWorkbench and
     * print them to the give output
     *
     * @param args
     */
    public static void main(String[] args) {

        // Definite command line
        CommandLineParser parser = new PosixParser();
        Options options = new Options();

        //Help page
        String helpOpt = "help";
        options.addOption("h", helpOpt, false, "print help message");

        String experimentFileStr = "experimentFile";
        options.addOption(experimentFileStr, true, "Input File with all the ArrayExpress experiments.");

        String protocolFileStr = "protocolFile";
        options.addOption(protocolFileStr, true, "Input File with all the ArrayExpress protocols.");

        String omicsDIFileStr = "omicsDIFile";
        options.addOption(omicsDIFileStr, true, "Output File for omicsDI");

        try {
            CommandLine line = parser.parse(options, args);
            if (line.hasOption(helpOpt) || !line.hasOption(protocolFileStr) || !line.hasOption(experimentFileStr)
                    || !line.hasOption(omicsDIFileStr)) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("validatorCLI", options);
            } else {
                File omicsDIFile = new File(line.getOptionValue(omicsDIFileStr));
                Experiments experiments = new ExperimentReader(
                        new File(line.getOptionValue(experimentFileStr))).getExperiments();
                Protocols protocols = new ProtocolReader(new File(line.getOptionValue(protocolFileStr))).getProtocols();
                generate(experiments, protocols, omicsDIFile);
            }

        } catch (Exception ex) {
            LOGGER.error("Exception occurred, ", ex);
        }


    }

    private static String generateProtocol(Map<Integer, Protocol> sampleProtocol) {
        StringBuilder sampleProtocolStr = new StringBuilder();
        for (Protocol sampleProtcolValue : sampleProtocol.values()) {
            sampleProtocolStr
                    .append(" ")
                    .append(Constants.Protocols.getByType(sampleProtcolValue.getType()).getName())
                    .append(" - ").append(ArrayExpressUtils.refineProtocol(sampleProtcolValue.getText()))
                    .append("\n");
        }
        return sampleProtocolStr.toString().trim();
    }

    private static Entry generate(Experiment ex, Map<String, Protocol> protocolMap) {
        Entry entry = new Entry();

        entry.setId(ex.getAccession());
        entry.setName(ex.getName());
        entry.setDescription(ex.getDescription());

        //Add Dates
        if (ex.getReleasedate() != null && !ex.getReleasedate().toString().isEmpty()) {
            entry.addDate(DSField.Date.PUBLICATION.getName(), ex.getReleasedate().toString());
        }
        if (ex.getLastupdatedate() != null && !ex.getLastupdatedate().isEmpty()) {
            entry.addDate(DSField.Date.PUBLICATION_UPDATED.getName(), ex.getLastupdatedate());
        }
        if (ex.getSubmissiondate() != null && !ex.getSubmissiondate().isEmpty()) {
            entry.addDate(DSField.Additional.SUBMISSION_DATE.getName(), ex.getSubmissiondate());
        }

        //Add Protocol information as additional fields

        Map<Integer, Protocol> sampleProtocol = new TreeMap<>();
        Map<Integer, Protocol> dataProtocol = new TreeMap<>();

        for (uk.ac.ebi.ddi.api.readers.arrayexpress.experimentsreader.model.experiments.Protocol protcl :ex.getProtocol()) {
            Protocol protocol = protocolMap.get(protcl.getAccession());
            if (protocol != null) {
                if (protocol.getSoftware() != null && !protocol.getSoftware().isEmpty()) {
                    entry.addAdditionalField(DSField.Additional.SOFTWARE_INFO.getName(), protocol.getSoftware());
                }
                if (protocol.getHardware() != null && !protocol.getHardware().isEmpty()) {
                    entry.addAdditionalField(DSField.Additional.INSTRUMENT.getName(), protocol.getHardware());
                }
                Constants.Protocols protocolConstant = Constants.Protocols.getByType(protocol.getType());
                if (protocolConstant != null && protocol.getText() != null && !protocol.getText().isEmpty()) {
                    if (protocolConstant.getField() == DSField.Additional.SAMPLE) {
                        sampleProtocol.put(protocolConstant.getLevel(), protocol);
                    } else if (protocolConstant.getField() == DSField.Additional.DATA) {
                        dataProtocol.put(protocolConstant.getLevel(), protocol);
                    }
                }

            }
        }

        if (!sampleProtocol.isEmpty()) {
            entry.addAdditionalField(DSField.Additional.SAMPLE.getName(), generateProtocol(sampleProtocol));
        }

        if (!dataProtocol.isEmpty()) {
            entry.addAdditionalField(DSField.Additional.DATA.getName(), generateProtocol(dataProtocol));
        }

        if (ex.getExperimentalfactor() != null) {
            for (Experimentalfactor factor : ex.getExperimentalfactor()) {
                if (factor != null && factor.getName() != null
                        && ArrayExpressUtils.cotainsValue(Constants.CELL_TYPE, factor.getName())) {
                    String[] values = ArrayExpressUtils.refineValues(factor.getValue());
                    for (String value : values) {
                        entry.addAdditionalField(DSField.Additional.CELL_TYPE_FIELD.getName(), value);
                    }
                }
                if (factor != null && factor.getName() != null
                        && ArrayExpressUtils.cotainsValue(Constants.TISSUE, factor.getName())) {
                    String[] values = ArrayExpressUtils.refineValues(factor.getValue());
                    for (String value : values) {
                        entry.addAdditionalField(
                                DSField.Additional.TISSUE_FIELD.getName(), ArrayExpressUtils.toTitleCase(value));
                    }
                }
            }
        }

        if (ex.getSampleattribute() != null) {
            for (Sampleattribute sample : ex.getSampleattribute()) {
                if (sample != null && sample.getValue() != null
                        && ArrayExpressUtils.cotainsValue(Constants.DISEASE, sample.getCategory())) {
                    String[] values = ArrayExpressUtils.refineValues(sample.getValue());
                    for (String value : values) {
                        entry.addAdditionalField(
                                DSField.Additional.DISEASE_FIELD.getName(), ArrayExpressUtils.toTitleCase(value));
                    }
                }
            }
        }

        entry.addAdditionalField(DSField.Additional.LINK.getName(), Constants.ARRAYEXPRESS_URL + entry.getId());

        for (String type : ex.getExperimenttype()) {
            if (type != null && !type.isEmpty()) {
                entry.addAdditionalField(
                        DSField.Additional.OMICS.getName(), Constants.ArrayExpressType.getByType(type).getOmicsType());
            }
            entry.addAdditionalField(DSField.Additional.SUBMITTER_KEYWORDS.getName(), type);
        }

        if (ex.getProvider() != null && !ex.getProvider().isEmpty()) {
            for (Provider provider : ex.getProvider()) {
                if (provider != null && provider.getContact() != null && !provider.getContact().isEmpty()) {
                    entry.addAdditionalField(DSField.Additional.SUBMITTER.getName(), provider.getContact());
                    if (provider.getEmail() != null && !provider.getEmail().isEmpty()) {
                        entry.addAdditionalField(DSField.Additional.SUBMITTER_EMAIL.getName(), provider.getEmail());
                    }
                }
            }
        }
        if (ex.getBibliography() != null && !ex.getBibliography().isEmpty()) {
            for (Bibliography biblio : ex.getBibliography()) {
                if (biblio != null && biblio.getAccession() != null && !biblio.getAccession().isEmpty()) {
                    entry.addCrossReferenceValue(DSField.CrossRef.PUBMED.getName(), biblio.getAccession());
                }
            }
        }
        for (Sampleattribute sampleattribute : ex.getSampleattribute()) {
            if (sampleattribute != null && sampleattribute.getCategory() != null) {
                if (sampleattribute.getCategory().equalsIgnoreCase(Constants.ORGANISM_TAG)) {
                    entry.addAdditionalField(
                            DSField.Additional.SPECIE_FIELD.getName(),
                            ArrayExpressUtils.toTitleCase(sampleattribute.getValue()));
                }
            }
        }

        HashSet<String> repositories = new HashSet<>();
        repositories.add("ArrayExpress");

        //TODO : We need to remove this code after biostudies integration
        /*ex.getSecondaryaccession().forEach(accession -> {
            if (accession != null && !accession.isEmpty()) {
                String str = accession.substring(0, 3);
                switch (str) {
                    case "ERP":
                    case "SRP":
                        repositories.add("ENA");
                        LOGGER.debug("{} secondary accession prefix: {} added ENA repository", ex.getAccession(), str);
                        break;
                    case "GSE":
                    case "GDS":
                        repositories.add("GEO");
                        LOGGER.debug("{} secondary accession prefix: {} added GEO repository", ex.getAccession(), str);
                        break;
                    default:
                        LOGGER.debug("{} unknown secondary accession prefix: {}", ex.getAccession(), str);
                        break; //noop
                }
                entry.addAdditionalField(DSField.Additional.SECONDARY_ACCESSION.getName(), accession);
            }
        });*/

        for (String repository : repositories) {
            entry.addAdditionalField(DSField.Additional.REPOSITORY.getName(), repository);
        }
        return entry;
    }

    /**
     * Performs the EB-eye generation of a defined public project, submission summary, and output directory.
     *
     * @throws Exception
     */
    public static void generate(Experiments experiments, Protocols protocols, Writer writer) throws Exception {
        OmicsDataMarshaller mm = new OmicsDataMarshaller();

        Database database = new Database();
        database.setDescription(ARRAYEXPRESS_DESCRIPTION);
        database.setName(DATABASE_NAME);
        database.setReleaseDate(experiments.getRetrieved().toString());
        Entries entries = new Entries();

        try {
            Map<String, Protocol> protocolMap = createProtocolMap(protocols);
            if (experiments.getExperiment() != null && writer != null) {
                experiments.getExperiment().forEach(ex -> {
                    if (ex.getUser() != null && ex.getUser().contains(BigInteger.valueOf(1))) {
                        entries.addEntry(generate(ex, protocolMap));
                    } else {
                        LOGGER.error("Project " + ex.getAccession() + " is still private, not generating EB-eye XML.");
                    }

                });
                database.setEntryCount(entries.getEntry().size());

                database.setEntries(entries);
                mm.marshall(database, writer);
            }
        } catch (Exception e) {
            LOGGER.error("Exception occurred, ", e);
        }
    }

    public static void generate(Experiments experiments, Protocols protocols, File outFile) throws Exception {
        generate(experiments, protocols, new FileWriter(outFile));
    }

    static Map<String, Protocol> createProtocolMap(Protocols protocols) {
        if (protocols != null) {
            return protocols.getProtocol().stream()
                    .collect(Collectors.toMap(Protocol::getAccession, Function.identity()));
        }
        return null;
    }

}
