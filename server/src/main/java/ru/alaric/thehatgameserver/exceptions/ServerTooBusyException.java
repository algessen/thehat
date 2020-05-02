package ru.alaric.thehatgameserver.exceptions;

public class ServerTooBusyException extends Exception{
    public ServerTooBusyException(String message) {
        super(message);
    }
}
