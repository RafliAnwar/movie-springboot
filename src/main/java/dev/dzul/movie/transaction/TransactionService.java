package dev.dzul.movie.transaction;

import dev.dzul.movie.subscription.Subscription;
import dev.dzul.movie.subscription.SubscriptionRepository;
import dev.dzul.movie.subscription.SubscriptionService;
import dev.dzul.movie.user.User;
import dev.dzul.movie.user.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class TransactionService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    public ResponseDTO createTransaction(TransactionDTO transactionDTO) {
        // Fetch User
        Optional<User> userOptional = userRepository.findById(transactionDTO.getId_user());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + transactionDTO.getId_user());
        }
        User user = userOptional.get();

        // Fetch Subscription
        Optional<Subscription> subscriptionOptional = subscriptionRepository.findById(transactionDTO.getId_subscription());
        if (subscriptionOptional.isEmpty()) {
            throw new IllegalArgumentException("Subscription not found with ID: " + transactionDTO.getId_subscription());
        }
        Subscription subscription = subscriptionOptional.get();

        // Check User Balance
        if (user.getBalance() < subscription.getPrice()) {
            throw new IllegalArgumentException("Insufficient balance for the transaction.");
        }

        // Create Transaction
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setSubscription(subscription);
        transaction.setTransaction_date(LocalDateTime.now());
        transaction.setEnd_of_subscription(LocalDateTime.now().plusDays(subscription.getDuration()));
        transaction.setTransaction_code(generateTransactionCode());
        transaction.setStatus("PAID");

        //Transaction create
        Transaction savedTransaction = transactionRepository.save(transaction);
        user.setBalance(user.getBalance() - subscription.getPrice());
        userRepository.save(user);

        // Map to ResponseDTO
        ResponseDTO responseDTO = new ResponseDTO();
        BeanUtils.copyProperties(savedTransaction, responseDTO);
        responseDTO.setUsername(user.getUsername());
        responseDTO.setSubscription(subscription);

        return responseDTO;
    }

    private String generateTransactionCode() {
        int length = 20;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder transactionCode = new StringBuilder();
        for (int i = 0; i < length; i++) {
            transactionCode.append(characters.charAt(random.nextInt(characters.length())));
        }
        return transactionCode.toString();
    }
}
