// src/main/java/com/example/calm/repository/ClientRepository.java
package com.example.Project.Calm.Repository;


import com.example.Project.Calm.entity.Client;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ClientRepository extends JpaRepository<Client, String> {
    List<Client> findByUserId(String userId);

    @Query("SELECT c FROM Client c WHERE c.user.id = :userId AND " +
            "(LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "c.phone LIKE CONCAT('%', :search, '%') OR " +
            "c.utr LIKE CONCAT('%', :search, '%'))")
    List<Client> searchClients(@Param("userId") String userId, @Param("search") String search);

    long countByUserIdAndStatus(String userId, String status);
}
