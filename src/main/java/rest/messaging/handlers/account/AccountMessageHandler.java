package rest.messaging.handlers.account;

import DTO.CreateAccountRequest;
import com.google.gson.Gson;
import models.Account;
import rest.messaging.handlers.account.interfaces.IAccountMessageHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class AccountMessageHandler implements IAccountMessageHandler {
    private Gson gson = new Gson();

    @Override
    public byte[] serializeAccount(Account account) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(account);

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public Account deserializeAccount(byte[] data) {
        String content = new String(data, StandardCharsets.UTF_8);
        CreateAccountRequest createAccountRequest = gson.fromJson(content, CreateAccountRequest.class);

        String[] dateOfBirthparts = createAccountRequest.getDateOfBirth().split("T");
        String formattedDateOfBirth = dateOfBirthparts[0];

        return new Account(createAccountRequest.getUserId(), createAccountRequest.getFirstName(), createAccountRequest.getLastName(),
                LocalDate.parse(formattedDateOfBirth), createAccountRequest.getGender(), createAccountRequest.getCountry(), createAccountRequest.getPhoneNumber());
    }
}
