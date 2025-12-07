package com.example.Project.Calm.Controller;

import com.example.Project.Calm.DTO.ClientDto;
import com.example.Project.Calm.DTO.NewClientDto;
import com.example.Project.Calm.Service.ClientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private ClientService clientService;

    // —— Root & Index ——————————————————————————————————————————————————————————————

    @GetMapping("/")
    public String root() {
        return "redirect:/dashboard";
    }

    // —— Dashboard ——————————————————————————————————————————————————————————————————

    @GetMapping("/dashboard")
    public String dashboard(Model model,
                            @RequestParam(required = false) String searchQuery) {
        List<ClientDto> clients = clientService.getAllActiveClients();

        // Filter by name or email (case-insensitive)
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            String lowerQuery = searchQuery.toLowerCase().trim();
            clients = clients.stream()
                    .filter(c -> c.getName().toLowerCase().contains(lowerQuery) ||
                            c.getEmail().toLowerCase().contains(lowerQuery))
                    .collect(Collectors.toList()); // immutable list is safe for read-only view
        }

        model.addAttribute("clients", clients);
        model.addAttribute("overdueClients", clientService.findOverdueClients());
        model.addAttribute("missingInfoClients", clientService.findMissingInfoClients());

        // TODO: Replace hard-coded data with real service calls when ready
//        model.addAttribute("upcomingReminders", clientService.getUpcomingReminders(5));
//        model.addAttribute("recentNotes", clientService.getRecentNotes(5));

        model.addAttribute("searchQuery", searchQuery);
        return "dashboard";
    }

    // —— New Client ———————————————————————————————————————————————————————————————

    @GetMapping("/clients/new")
    public String showNewClientForm(Model model) {
        model.addAttribute("client", new NewClientDto());
        return "new-client";
    }

    @PostMapping("/clients")
    public String createClient(@Valid @ModelAttribute("client") NewClientDto dto,
                               BindingResult result,
                               RedirectAttributes redirectAttrs,
                               Model model) {
        // Validation errors → stay on form
        if (result.hasErrors()) {
            return "new-client";
        }

        try {
            clientService.createClient(dto);
            redirectAttrs.addFlashAttribute("success", "Client created successfully!");
            return "redirect:/dashboard";
        } catch (IllegalArgumentException e) {
            // e.g., invalid data format, business rule violation
            model.addAttribute("error", "Validation failed: " + e.getMessage());
            logger.warn("Client creation validation error: {}", e.getMessage());
        } catch (RuntimeException e) {
            // General failure (e.g., DB constraint, duplicate email, etc.)
            logger.error("Failed to create client", e);
            model.addAttribute("error", "An error occurred while saving the client. Please try again.");
        }

        // Preserve form data on error (optional but user-friendly)
        model.addAttribute("client", dto);
        return "new-client";
    }

    // —— Supporting DTO/Model Classes —————————————————————————————————————————————

    /**
     * Simple DTO for dashboard notes.
     * Consider moving to a dedicated package if reused across controllers/services.
     */
    public static class Note {
        private final String date;
        private final String content;
        private final String author;

        public Note(String date, String content, String author) {
            this.date = date;
            this.content = content;
            this.author = author;
        }

        // Getters (no setters — immutable)
        public String getDate() { return date; }
        public String getContent() { return content; }
        public String getAuthor() { return author; }
    }
}