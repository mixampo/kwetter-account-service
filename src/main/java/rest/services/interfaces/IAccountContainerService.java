package rest.services.interfaces;

import DTO.DeleteAccountRequestDto;
import exceptions.AccountAlreadyExistsException;
import exceptions.AccountDoesNotExistException;
import exceptions.NotUserAccountException;
import models.Account;

import java.util.Optional;

public interface IAccountContainerService {
    Account getAccountByUserId(String id);
    Optional<Account> getAccountById(int id) throws AccountDoesNotExistException;
    void addAccount(Account account) throws AccountAlreadyExistsException;
    void deleteAccount(DeleteAccountRequestDto deleteAccountRequestDto) throws NotUserAccountException;
}
