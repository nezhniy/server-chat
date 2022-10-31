package ru.itsjava.services;

import java.io.IOException;

public interface ServerService extends Observable{
    void start() throws IOException;
}
