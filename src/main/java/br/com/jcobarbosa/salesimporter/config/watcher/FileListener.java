package br.com.jcobarbosa.salesimporter.config.watcher;

import br.com.jcobarbosa.salesimporter.config.SalesFileProperties;
import br.com.jcobarbosa.salesimporter.service.SalesImportingService;
import br.com.jcobarbosa.salesimporter.service.SalesImportingServiceImpl;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileListener extends FileAlterationListenerAdaptor {

    private static final Logger logger = LoggerFactory.getLogger(FileListener.class);

    @Autowired
    private SalesFileProperties salesFileProperties;

    @Autowired
    private SalesImportingService listenerService;

    public FileListener(SalesImportingServiceImpl listenerService) {
        this.listenerService = listenerService;
    }

    @Override
    public void onFileCreate(File file) {
        String fileName;
        logger.info("Working on [" + file.getAbsolutePath() + "]...");

        try {
            fileName = file.getName();

            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            BufferedReader reader  = new BufferedReader(inputStreamReader);

            listenerService.process(fileName, reader);
        } catch (Exception e) {
            logger.error("Couldn't read crated file [" + file.getName() + "]. " + e.getMessage());
        }
    }

    @Override
    public void onFileChange(File file) {
    }

    @Override
    public void onFileDelete(File file) {
    }

    @Override
    public void onDirectoryCreate(File directory) {
    }

    @Override
    public void onDirectoryChange(File directory) {
    }

    @Override
    public void onDirectoryDelete(File directory) {
    }

    @Override
    public void onStart(FileAlterationObserver observer) {
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
    }
}