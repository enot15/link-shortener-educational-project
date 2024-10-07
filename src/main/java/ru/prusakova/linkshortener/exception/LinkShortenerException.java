package ru.prusakova.linkshortener.exception;

public class LinkShortenerException extends RuntimeException {

    public LinkShortenerException(String msg) {
        super(msg);
    }

    public LinkShortenerException(String msg, Exception e) {
        super(msg, e);
    }
}
