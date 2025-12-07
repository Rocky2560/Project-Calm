package com.example.Project.Calm.Service;

// src/main/java/com/example/calm/service/ClientService.java

import com.example.Project.Calm.Controller.ClientController;
import com.example.Project.Calm.DTO.ClientDto;
import com.example.Project.Calm.DTO.NewClientDto;
import com.example.Project.Calm.model.Client;
import com.example.Project.Calm.model.ClientServiceType;
import com.example.Project.Calm.Repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ReminderService reminderService;

    public List<ClientDto> getAllActiveClients() {
        return clientRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<Client> findOverdueClients() { return clientRepository.findOverdueClients(); }

    public List<Client> findMissingInfoClients() { return clientRepository.findMissingInfoClients(); }

    public ClientDto getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        return convertToDto(client);
    }

    public ClientDto createClient(NewClientDto dto) {
        Client client = new Client();
        client.setName(dto.getName());
        client.setEmail(dto.getEmail());
        client.setPhone(dto.getPhone());
        client.setAddress(dto.getAddress());
        client.setUtr(dto.getUtr());
        client.setCompanyRegNo(dto.getCompanyRegNo());
        client.setVatNo(dto.getVatNo());
        client.setPayrollNo(dto.getPayrollNo());
        client.setServices(dto.getServices());
        client.setBenefits(dto.getBenefits());
        client.setBenefitNotes(dto.getBenefitNotes());
        client.setInternalNotes(dto.getInternalNotes());
        client.setPendingPayments(0);
        client.setOverduePayments(0);
        client.setPaidThisYear(0);
        client = clientRepository.save(client);

        if (isMissingCriticalInfo(client)) {
            reminderService.scheduleMissingInfoReminder(client.getId());
        }
        reminderService.scheduleWelcomeEmail(client.getId());
        return convertToDto(client);
    }

    private boolean isMissingCriticalInfo(Client client) {
        if (client.getUtr() == null || client.getUtr().trim().isEmpty()) return true;
        if (client.getServices().contains(ClientServiceType.VAT) &&
                (client.getVatNo() == null || client.getVatNo().trim().isEmpty())) return true;
        return false;
    }

    public void updateClient(Long id, NewClientDto dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        // Update fields
        clientRepository.save(client);
    }

    private ClientDto convertToDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setEmail(client.getEmail());
        dto.setPhone(client.getPhone());
        dto.setAddress(client.getAddress());
        dto.setUtr(client.getUtr());
        dto.setCompanyRegNo(client.getCompanyRegNo());
        dto.setVatNo(client.getVatNo());
        dto.setPayrollNo(client.getPayrollNo());
        dto.setServices(client.getServices());
        dto.setBenefits(client.getBenefits());
        dto.setBenefitNotes(client.getBenefitNotes());
        dto.setInternalNotes(client.getInternalNotes());
        dto.setCreatedAt(client.getCreatedAt());
        dto.setPendingPayments(client.getPendingPayments());
        dto.setOverduePayments(client.getOverduePayments());
        dto.setPaidThisYear(client.getPaidThisYear());
        return dto;
    }


//    List<String> getUpcomingReminders(int limit); // or List<ReminderDto>
//    List<ClientController.Note> getRecentNotes(int limit);         // Note is com.example.Project.Calm.Controller.ClientController.Note
    // → Better: define Note in shared model package later
}
