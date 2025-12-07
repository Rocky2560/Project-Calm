// src/main/java/com/example/calm/repository/ClientRepository.java
package com.example.Project.Calm.Repository;

import com.example.Project.Calm.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByEmailContainingIgnoreCaseOrNameContainingIgnoreCase(String email, String name);

    @Query("SELECT c FROM Client c WHERE c.overduePayments > 0")
    List<Client> findOverdueClients();

    @Query("""
    SELECT c FROM Client c
    LEFT JOIN c.services s
    WHERE c.utr IS NULL OR c.utr = ''
       OR (s = com.example.Project.Calm.model.ClientServiceType.VAT 
           AND (c.vatNo IS NULL OR c.vatNo = ''))
""") List<Client> findMissingInfoClients();



    @Query("SELECT c FROM Client c WHERE c.createdAt >= :since")
    List<Client> findRecentClients(@Param("since") java.time.LocalDate since);
}
