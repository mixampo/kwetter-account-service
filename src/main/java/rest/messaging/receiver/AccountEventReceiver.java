package rest.messaging.receiver;

import exceptions.AccountAlreadyExistsException;
import models.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rest.messaging.handlers.account.AccountMessageHandler;
import rest.services.AccountContainerService;
import rest.services.AccountService;

@Component
public class AccountEventReceiver {
    private Logger log = LoggerFactory.getLogger(AccountEventReceiver.class);
    private AccountMessageHandler accountMessageHandler;
    private AccountContainerService accountContainerService;
    private AccountService accountService;

    @Autowired
    public AccountEventReceiver(AmqpTemplate amqpTemplate, AccountContainerService accountContainerService, AccountService accountService) {
        this.accountMessageHandler = new AccountMessageHandler();
        this.accountContainerService = accountContainerService;
        this.accountService = accountService;
    }

    @RabbitListener(queues = "${kwetter.rabbitmq.queue}")
    public void receiveNewAccount(Message createAccountMessage) {
        try {
            Account newAccount = accountMessageHandler.deserializeAccount(createAccountMessage.getBody());
            accountContainerService.addAccount(newAccount);
        } catch (AccountAlreadyExistsException e) {
            e.printStackTrace();
        }
    }
}
