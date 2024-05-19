package com.domiaffaire.api.controllers;

import com.domiaffaire.api.dto.FileDTO;
import com.domiaffaire.api.services.DomiAffaireServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
}
