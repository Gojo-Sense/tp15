package com.example.transaction_service.repositories;

import com.example.transaction_service.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT SUM(c.solde) FROM Transaction c")
    Double sumSoldes();
}
