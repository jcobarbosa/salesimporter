package br.com.jcobarbosa.salesimporter.service;

import java.io.BufferedReader;

public interface SalesImportingService {
    String process(String fileName, BufferedReader reader);
}
