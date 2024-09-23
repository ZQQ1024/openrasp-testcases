package com.example.testcases.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vulns/library")
public class LibraryController {
    @GetMapping("load")
    public ResponseEntity<String>  loadLibrary(@RequestParam("name") String libraryName) {
        try {
            System.load(libraryName);

            return ResponseEntity.ok("Library '" + libraryName + "' loaded successfully.");
        } catch (UnsatisfiedLinkError e) {
            return ResponseEntity.ok("Library '" + libraryName + "' loaded successfully.");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to load library '" + libraryName + "': " + e.getMessage());
        }
    }
}
