package br.com.jcobarbosa.salesimporter.config.watcher;

import br.com.jcobarbosa.salesimporter.config.SalesFileProperties;
import br.com.jcobarbosa.salesimporter.service.SalesImportingServiceImpl;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Component
public class FileListenerFactory {

    private static final Logger logger = LoggerFactory.getLogger(FileListenerFactory.class);

    @Autowired
    private SalesFileProperties salesFileProperties;
    private final long interval = TimeUnit.SECONDS.toMillis(1);

    @Autowired
    private SalesImportingServiceImpl listenerService;

    public FileAlterationMonitor getMonitor() {
        IOFileFilter directories = FileFilterUtils.and(FileFilterUtils.directoryFileFilter(), HiddenFileFilter.VISIBLE);
        IOFileFilter files = FileFilterUtils.and(
                FileFilterUtils.fileFileFilter(),
                FileFilterUtils.suffixFileFilter(".dat"));
        IOFileFilter filter = FileFilterUtils.or(directories, files);

        FileAlterationObserver observer = new FileAlterationObserver(new File(salesFileProperties.getInDirectory()), filter);
        observer.addListener(new FileListener(listenerService));

        return new FileAlterationMonitor(interval, observer);
    }
}