package ru.prusakova.linkshortener.exception;

public class NotFoundException extends LinkShortenerException {

    public NotFoundException(String msg) {
        super(msg);
    }

    public NotFoundException(String msg, Exception e) {
        super(msg, e);
    }
}
