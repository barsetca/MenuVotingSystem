package ru.cherniak.menuvotingsystem;

import org.junit.jupiter.api.extension.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

public class TimingExtension implements BeforeTestExecutionCallback{

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext){
        log.info("test name - " + extensionContext.getDisplayName() +
                ", testClassName - " + extensionContext.getRequiredTestClass().getSimpleName());
    }
}
