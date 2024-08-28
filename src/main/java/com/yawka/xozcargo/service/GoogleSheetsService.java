package com.yawka.xozcargo.service;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GoogleSheetsService {

    String saveDataFromSheet(String spreadsheetId, String race) throws IOException, GeneralSecurityException;
}
