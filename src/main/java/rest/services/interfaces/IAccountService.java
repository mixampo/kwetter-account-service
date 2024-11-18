package rest.services.interfaces;

import DTO.UpdateAccountRequestDto;
import exceptions.AccountDoesNotExistException;
import models.Account;

public interface IAccountService {
    void updateAccount(UpdateAccountRequestDto updateAccountRequestDto) throws AccountDoesNotExistException;
}
