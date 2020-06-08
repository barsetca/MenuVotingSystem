package ru.cherniak.menuvotingsystem;

import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLogging implements BeforeTestExecutionCallback {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        log.info("test name - " + extensionContext.getDisplayName() +
                ", testClassName - " + extensionContext.getRequiredTestClass().getSimpleName());
    }
}
