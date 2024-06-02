package com.domiaffaire.api.controllers;

import com.domiaffaire.api.dto.FileDTO;
import com.domiaffaire.api.exceptions.FaqNotFoundException;
import com.domiaffaire.api.services.DomiAffaireServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/visitors")
public class VisitorsController {
    private final DomiAffaireServiceImpl service;

    @GetMapping("/company-creation/documents")
    public List<FileDTO> findAllFiles(){
        return service.findAllFilesCompanyCreation();
    }

    @GetMapping("/faqs/{id}")
    public ResponseEntity<?> findFaqById(@PathVariable String id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getFaqById(id));
        } catch (FaqNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\":\"" + e.getMessage() + "\"}");
        }
    }
    @GetMapping("/packs")
    public ResponseEntity<?> findAllPacks(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllPacks());
    }


    @GetMapping("/faqs")
    public ResponseEntity<?> findAllFaqs(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllFaqs());
    }

}
