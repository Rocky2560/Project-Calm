package com.example.Project.Calm.Controller;


import com.example.Project.Calm.DTO.*;
import com.example.Project.Calm.Service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
        import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAll(Authentication auth) {
        return ResponseEntity.ok(clientService.getAllClients(auth.getName()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ClientDTO>> search(
            @RequestParam String q, Authentication auth) {
        return ResponseEntity.ok(clientService.searchClients(auth.getName(), q));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getOne(@PathVariable String id, Authentication auth) {
        return ResponseEntity.ok(clientService.getClient(id, auth.getName()));
    }

    @PostMapping
    public ResponseEntity<ClientDTO> create(
            @Valid @RequestBody ClientDTO dto, Authentication auth) {
        return ResponseEntity.ok(clientService.createClient(dto, auth.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> update(
            @PathVariable String id, @Valid @RequestBody ClientDTO dto, Authentication auth) {
        return ResponseEntity.ok(clientService.updateClient(id, dto, auth.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, Authentication auth) {
        clientService.deleteClient(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}



//API Endpoints Summary:
//
//Method	Endpoint	Description
//POST	/api/auth/register	User registration
//POST	/api/auth/login	User login
//GET	/api/clients	Get all clients
//GET	/api/clients/search?q=	Search clients
//POST	/api/clients	Create client
//PUT	/api/clients/{id}	Update client
//DELETE	/api/clients/{id}	Delete client
//GET	/api/payments	Get all payments
//POST	/api/payments	Create payment
//PUT	/api/payments/{id}	Update payment
//DELETE	/api/payments/{id}	Delete payment
//Copy these files to your Spring Boot project and run with mvn spring-boot:run.