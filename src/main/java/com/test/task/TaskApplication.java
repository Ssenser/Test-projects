package com.test.task;

import com.test.task.dto.FilesCompareResult;
import com.test.task.services.FileComparatorService;
import com.test.task.services.FileCreatorService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
public class TaskApplication {

    private static final Logger LOG = Logger.getLogger(TaskApplication.class.getName());

    private static final String RESULT_FILE_NAME = "Comparing_Result_%s.txt";
    private static final String FILE_NAME_DATE_FORMAT = "yyyy-MM-dd_HH-mm-ss";

    @Autowired
    private FileComparatorService fileComparatorService;

    @Autowired
    private FileCreatorService fileCreatorService;

    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            if (args.length == 2) {
                final FilesCompareResult resultFileName = fileComparatorService.compareFiles(args[0], args[1]);

                String fileName = String.format(RESULT_FILE_NAME, new DateTime().toString(FILE_NAME_DATE_FORMAT));
                fileCreatorService.createFile(
                        System.getProperty("user.dir") + File.separator
                                + File.separator + fileName, resultFileName.toString());
                LOG.log(Level.INFO, "Comparing result was written into file {0}", fileName);
            } else {
                LOG.log(Level.SEVERE, "Pls, provide two arguments - " +
                        "first file name and second file name");
            }
        };
    }
}
