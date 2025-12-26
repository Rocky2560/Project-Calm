package com.example.Project.Calm.Service;

// src/main/java/com/example/calm/service/ClientService.java
import com.example.Project.Calm.DTO.ClientDTO;
import com.example.Project.Calm.entity.*;
import com.example.Project.Calm.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepo;
    private final UserRepository userRepo;

    public List<ClientDTO> getAllClients(String userId) {
        return clientRepo.findByUserId(userId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public List<ClientDTO> searchClients(String userId, String search) {
        return clientRepo.searchClients(userId, search).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    public ClientDTO getClient(String id, String userId) {
        Client client = clientRepo.findById(id)
                .filter(c -> c.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Client not found"));
        return toDTO(client);
    }

    public ClientDTO createClient(ClientDTO dto, String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Client client = Client.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .utr(dto.getUtr())
                .companyNumber(dto.getCompanyNumber())
                .status(dto.getStatus() != null ? dto.getStatus() : "active")
                .services(dto.getServices())
                .paymentStatus(dto.getPaymentStatus())
                .loeStatus(dto.getLoeStatus())
                .photoIdVerified(dto.getPhotoIdVerified())
                .auth648Submitted(dto.getAuth648Submitted())
                .amlccVerified(dto.getAmlccVerified())
                .notes(dto.getNotes())
                .user(user)
                .build();
        return toDTO(clientRepo.save(client));
    }

    public ClientDTO updateClient(String id, ClientDTO dto, String userId) {
        Client client = clientRepo.findById(id)
                .filter(c -> c.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Client not found"));

        client.setName(dto.getName());
        client.setEmail(dto.getEmail());
        client.setPhone(dto.getPhone());
        client.setUtr(dto.getUtr());
        client.setCompanyNumber(dto.getCompanyNumber());
        client.setStatus(dto.getStatus());
        client.setServices(dto.getServices());
        client.setPaymentStatus(dto.getPaymentStatus());
        client.setLoeStatus(dto.getLoeStatus());
        client.setPhotoIdVerified(dto.getPhotoIdVerified());
        client.setAuth648Submitted(dto.getAuth648Submitted());
        client.setAmlccVerified(dto.getAmlccVerified());
        client.setNotes(dto.getNotes());
        client.setUpdatedAt(LocalDateTime.now());

        return toDTO(clientRepo.save(client));
    }

    public void deleteClient(String id, String userId) {
        Client client = clientRepo.findById(id)
                .filter(c -> c.getUser().getId().equals(userId))
                .orElseThrow(() -> new RuntimeException("Client not found"));
        clientRepo.delete(client);
    }

    private ClientDTO toDTO(Client c) {
        ClientDTO dto = new ClientDTO();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setEmail(c.getEmail());
        dto.setPhone(c.getPhone());
        dto.setUtr(c.getUtr());
        dto.setCompanyNumber(c.getCompanyNumber());
        dto.setStatus(c.getStatus());
        dto.setServices(c.getServices());
        dto.setPaymentStatus(c.getPaymentStatus());
        dto.setLoeStatus(c.getLoeStatus());
        dto.setPhotoIdVerified(c.getPhotoIdVerified());
        dto.setAuth648Submitted(c.getAuth648Submitted());
        dto.setAmlccVerified(c.getAmlccVerified());
        dto.setNotes(c.getNotes());
        return dto;
    }
}