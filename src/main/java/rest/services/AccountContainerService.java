package rest.services;

import DTO.DeleteAccountRequestDto;
import exceptions.AccountAlreadyExistsException;
import exceptions.AccountDoesNotExistException;
import exceptions.NotUserAccountException;
import models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rest.repositories.IAccountJpaRepository;
import rest.services.interfaces.IAccountContainerService;

import java.util.Optional;

@Service
public class AccountContainerService implements IAccountContainerService {
    private final IAccountJpaRepository accountJpaRepository;

    @Autowired
    public AccountContainerService(IAccountJpaRepository accountJpaRepository) {
        this.accountJpaRepository = accountJpaRepository;
    }

    @Override
    public Account getAccountByUserId(String id) {
        return accountJpaRepository.getAccountByUserId(id);
    }

    @Override
    public Optional<Account> getAccountById(int id) throws AccountDoesNotExistException {
        Optional<Account> account = accountJpaRepository.findById(id);
        if (account.isPresent()) {
            return account;
        } else {
            throw new AccountDoesNotExistException();
        }
    }

    @Override
    public void addAccount(Account account) throws AccountAlreadyExistsException {
       Account acc = accountJpaRepository.getAccountByUserId(account.getUserId());

        if (acc != null) {
            throw new AccountAlreadyExistsException();
        } else {
            accountJpaRepository.save(account);
        }
    }

    @Override
    public void deleteAccount(DeleteAccountRequestDto deleteAccountRequestDto) throws NotUserAccountException {
        Optional<Account> account = accountJpaRepository.findById(deleteAccountRequestDto.getAccountId());
        if (account.isPresent() && account.get().getUserId().equals(deleteAccountRequestDto.getUserId())) {
            accountJpaRepository.deleteById(deleteAccountRequestDto.getAccountId());
        } else {
            throw new NotUserAccountException();
        }
    }
}
