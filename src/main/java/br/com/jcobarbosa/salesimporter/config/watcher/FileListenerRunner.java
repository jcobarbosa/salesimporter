package br.com.jcobarbosa.salesimporter.config.watcher;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FileListenerRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(FileListenerFactory.class);

    @Autowired
    private FileListenerFactory fileListenerFactory;

    @Override
    public void run(String... args) throws Exception {
        FileAlterationMonitor fileAlterationMonitor = fileListenerFactory.getMonitor();

        try {
            fileAlterationMonitor.start();
        } catch (Exception e) {
            logger.error("Couldn't start monitor. " + e.getMessage());
            e.printStackTrace();
        }
    }
}