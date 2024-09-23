package com.example.testcases.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.net.InetAddress;
import java.util.Arrays;

@RestController
@RequestMapping("/api/vulns/dns")
public class DNSController {
    @GetMapping("resolve")
    public String[] resolveDNS(@RequestParam("hostname") String hostname) {
        try {
            InetAddress[] addresses = InetAddress.getAllByName(hostname);
            return Arrays.stream(addresses)
                    .map(InetAddress::getHostAddress)
                    .toArray(String[]::new);
        } catch (Exception e) {
            return new String[] {"Error: " + e.getMessage()};
        }
    }
}
