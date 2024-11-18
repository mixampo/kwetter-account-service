package rest.messaging.handlers.account.interfaces;

import models.Account;

import java.io.IOException;

public interface IAccountMessageHandler {
    byte[] serializeAccount(Account account) throws IOException;
    Account deserializeAccount(byte[] data);
}
