package com.alerom.rng.archive.rng_archive_server.exceptions;

public class ArtifactNotFoundException extends RuntimeException {
    public ArtifactNotFoundException(String message) {
        super(message);
    }
}