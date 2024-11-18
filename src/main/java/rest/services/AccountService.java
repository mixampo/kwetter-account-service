package rest.services;

import DTO.UpdateAccountRequestDto;
import exceptions.AccountDoesNotExistException;
import models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rest.repositories.IAccountJpaRepository;
import rest.services.interfaces.IAccountService;

import java.util.Optional;

@Service
public class AccountService implements IAccountService {
    private final IAccountJpaRepository accountJpaRepository;

    @Autowired
    public AccountService(IAccountJpaRepository accountJpaRepository) {
        this.accountJpaRepository = accountJpaRepository;
    }


    @Override
    public void updateAccount(UpdateAccountRequestDto updateAccountRequestDto) throws AccountDoesNotExistException {
        Optional<Account> acc = accountJpaRepository.findById(updateAccountRequestDto.getAccountId());

        if (acc.isPresent() && acc.get().getUserId().equals(updateAccountRequestDto.getUserId())) {
            accountJpaRepository.save(new Account(updateAccountRequestDto.getAccountId(), updateAccountRequestDto.getUserId(), updateAccountRequestDto.getFirstName(),
                    updateAccountRequestDto.getLastName(), updateAccountRequestDto.getDateOfBirth(), updateAccountRequestDto.getGender(), updateAccountRequestDto.getCountry(), updateAccountRequestDto.getPhoneNumber()));
        } else {
            throw new AccountDoesNotExistException();
        }
    }
}
