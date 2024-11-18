package rest.controllers;

import DTO.DeleteAccountRequestDto;
import DTO.UpdateAccountRequestDto;
import exceptions.AccountAlreadyExistsException;
import exceptions.AccountDoesNotExistException;
import exceptions.NotUserAccountException;
import models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rest.services.AccountContainerService;
import rest.services.AccountService;

import java.util.Optional;

@RestController
public class AccountController {
    private final AccountContainerService accountContainerService;
    private final AccountService accountService;

    @Autowired
    public AccountController(AccountContainerService accountContainerService, AccountService accountService) {
        this.accountContainerService = accountContainerService;
        this.accountService = accountService;
    }

    @GetMapping(value = "/account/user/{id}")
    public Account getAccountByUserId(@PathVariable("id") String id) {
        return accountContainerService.getAccountByUserId(id);
    }

    @GetMapping(value = "/account/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable("id") int id) {
        try {
            return new ResponseEntity<>(accountContainerService.getAccountById(id), HttpStatus.OK);
        } catch (AccountDoesNotExistException e) {
            return new ResponseEntity<>("This account does not exist!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/account", headers = "Accept=application/json")
    public ResponseEntity<?> createAccount(@RequestBody Account account, UriComponentsBuilder ucBuilder) {
        HttpHeaders headers = new HttpHeaders();
        try {
            accountContainerService.addAccount(account);
            headers.setLocation(ucBuilder.path("/post/{id}").buildAndExpand(account.getId()).toUri());
        } catch (AccountAlreadyExistsException e) {
            return new ResponseEntity<>("Account already exists!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(account, headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/account", headers = "Accept=application/json")
    public ResponseEntity<?> updateAccount(@RequestBody UpdateAccountRequestDto updateAccountRequestDto) {
        try {
            accountService.updateAccount(updateAccountRequestDto);
        } catch (AccountDoesNotExistException e) {
            return new ResponseEntity<>("This account does not exist!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping(value = "/account")
    public ResponseEntity<?> deleteAccount(@RequestBody DeleteAccountRequestDto deleteAccountRequestDto) {
        try {
            accountContainerService.deleteAccount(deleteAccountRequestDto);
        } catch (NotUserAccountException e) {
            return new ResponseEntity<>("Can't delete this account!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
