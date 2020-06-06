package ru.cherniak.menuvotingsystem.util.exception;

public class ModificationRestrictionException extends RuntimeException {
    public static final String EXCEPTION_MODIFICATION_RESTRICTION = "exception.user.modificationRestriction";

    public ModificationRestrictionException() {
        super(EXCEPTION_MODIFICATION_RESTRICTION);
    }
}