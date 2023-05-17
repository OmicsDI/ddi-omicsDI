package uk.ac.ebi.ddi.similarityCalculator;

import org.apache.commons.cli.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import uk.ac.ebi.ddi.annotation.service.crossreferences.DDIDatasetSimilarityService;
import uk.ac.ebi.ddi.annotation.service.synonyms.DDIExpDataImportService;
import uk.ac.ebi.ddi.annotation.utils.DataType;
import uk.ac.ebi.ddi.service.db.service.similarity.ExpOutputDatasetService;
import uk.ac.ebi.ddi.service.db.service.similarity.TermInDBService;

/**
 * This code is licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0

 *  ==Overview==
 *
 *  This class
 *
 * Created by ypriverol (ypriverol@gmail.com) on 30/09/15.
 */

@Component
public class SimilarityCalculator {

    public static void main(String[] args) {

        //For test, avoid inputing argument
        args = new String[]{"--dataType=ProteomicsData"};


        TermInDBService termInDBService;
        DDIDatasetSimilarityService ddiDatasetSimilarityService = new DDIDatasetSimilarityService();
        ExpOutputDatasetService expOutputDatasetService = new ExpOutputDatasetService();
        DDIExpDataImportService ddiExpDataImportService = new DDIExpDataImportService();
        MongoTemplate mongoTemplate;

        //JavaBeans
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        termInDBService = (TermInDBService) ctx.getBean("termInDBService");
        ddiDatasetSimilarityService = (DDIDatasetSimilarityService) ctx.getBean("ddiDatasetSimilarityService");
        expOutputDatasetService = (ExpOutputDatasetService) ctx.getBean("expOutputDatasetService");
        ddiExpDataImportService = (DDIExpDataImportService) ctx.getBean("ddiExpDataImportService");
        mongoTemplate = (MongoTemplate) ctx.getBean("mongoTemplate");


        // Definite command line
        CommandLineParser parser = new PosixParser();
        Options options = new Options();

        //Help page
        String helpOpt = "help";
        options.addOption("h", helpOpt, false, "print help message");

        String dataTypeOpt = "dataType";
        Option dataTypeOption = OptionBuilder
                .withLongOpt(dataTypeOpt)
                .withArgName("Omics Data Type")
                .hasArgs()
                .withDescription(DataType.PROTEOMICS_DATA.getName() + "/" + DataType.METABOLOMICS_DATA.getName())
                        .create();
        options.addOption(dataTypeOption);


        String dataTypeInput = null;

        // create the parser
        try {
            // parse the command line arguments

            CommandLine line = parser.parse(options, args);
            if (line.hasOption(helpOpt) || line.getOptions().length == 0) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("SimilarityCalculator", options);
            } else {
                if (line.hasOption("dataType")) {
                    System.out.println("the option is:" + line.getOptionValue(dataTypeOpt));
                    dataTypeInput = line.getOptionValue(dataTypeOpt);
                }
            }

        } catch (ParseException exp) {
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
        }


        if (dataTypeInput != null) {
            ddiDatasetSimilarityService.calculateIDFWeight(dataTypeInput);
            ddiDatasetSimilarityService.calculateSimilarity(dataTypeInput);
        }
    }

}
