package ru.prusakova.linkshortener.exception;

public class NotFoundShortLinkException extends LinkShortenerException {

    public NotFoundShortLinkException(String msg) {
        super(msg);
    }
}
