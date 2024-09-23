package com.example.testcases.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;

@RestController
@RequestMapping("/api/vulns/xxe")
public class XXEController {
    @PostMapping
    public ResponseEntity<String> parseXml(@RequestBody String xmlData) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            // 注意：这里故意未禁用外部实体，以模拟XXE漏洞
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            ByteArrayInputStream input = new ByteArrayInputStream(xmlData.getBytes());
            org.w3c.dom.Document doc = dBuilder.parse(input);
            doc.getDocumentElement().normalize();

            // 使用Transformer将整个XML文档转换为字符串
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);

            // 返回整个文档的字符串表示
            String xmlString = writer.toString();
            return ResponseEntity.ok(xmlString);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to parse XML: " + e.getMessage());
        }
    }
}
