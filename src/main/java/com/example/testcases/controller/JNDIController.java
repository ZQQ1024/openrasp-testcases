package com.example.testcases.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;

@RestController
@RequestMapping("/api/vulns/jndi")
public class JNDIController {
    @GetMapping("/lookup")
    public String lookup(@RequestParam("hostname") String hostname) {
        try {
            Hashtable<String, String> env = new Hashtable<>();
            // GenericURLContext是作为各种具体 URL Context 如 LDAP, DNS 等的基类
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
            env.put(Context.PROVIDER_URL, "dns:");

            Context ctx = new InitialContext(env);
            String dnsLookup = "dns://" + hostname;
            Object result = ctx.lookup(dnsLookup);
            return "DNS lookup result: " + result.toString();
        } catch (Exception e) {
            return "Error during DNS lookup: " + e.getMessage();
        }
    }
}
