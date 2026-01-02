package com.example.transaction_service.controllers;

import com.example.transaction_service.entities.Transaction;
import com.example.transaction_service.entities.TransactionType;
import com.example.transaction_service.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
public class TransactionControllerGraphQL {

    private final TransactionRepository transactionRepository;

    // =========================
    //        QUERIES
    // =========================

    @QueryMapping
    public List<Transaction> allTransactions() {
        return transactionRepository.findAll();
    }

    @QueryMapping
    public Transaction transactionById(@Argument Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Transaction %s not found", id)
                ));
    }

    @QueryMapping
    public Map<String, Object> totalSolde() {
        long count = transactionRepository.count();
        double sum = transactionRepository.sumSoldes();
        double average = count > 0 ? sum / count : 0;

        return Map.of(
                "count", count,
                "sum", sum,
                "average", average
        );
    }

    // =========================
    //        MUTATION
    // =========================

    @MutationMapping
    public Transaction saveTransaction(
            @Argument("transaction") Map<String, Object> input) throws Exception {

        double solde = Double.parseDouble(input.get("solde").toString());
        String dateStr = input.get("dateCreation").toString();
        TransactionType type = TransactionType.valueOf(input.get("type").toString());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateCreation = sdf.parse(dateStr);

        Transaction t = new Transaction(null, solde, dateCreation, type);

        return transactionRepository.save(t);
    }
}
