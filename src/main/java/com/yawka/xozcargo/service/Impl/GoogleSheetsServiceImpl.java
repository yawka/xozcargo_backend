package com.yawka.xozcargo.service.Impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.yawka.xozcargo.entity.Item;
import com.yawka.xozcargo.entity.ItemStatus;
import com.yawka.xozcargo.entity.Race;
import com.yawka.xozcargo.entity.Client;
import com.yawka.xozcargo.repository.ItemRepository;
import com.yawka.xozcargo.repository.ItemStatusRepository;
import com.yawka.xozcargo.repository.RaceRepository;
import com.yawka.xozcargo.repository.ClientRepository;
import com.yawka.xozcargo.service.GoogleSheetsService;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;


@Service
public class GoogleSheetsServiceImpl implements GoogleSheetsService {

    private final ClientRepository userRepository;
    private final RaceRepository raceRepository;
    private final ItemRepository itemRepository;

    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES =
            Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
//    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private final ItemStatusRepository itemStatusRepository;
    //private static final String serviceAccountKeyPath = "/home/java/google_key/key.json";
    private static final String serviceAccountKeyPath = "C:\\Users\\yawka\\Desktop\\Spring Lessons of Erkin agai\\key.json";


    public GoogleSheetsServiceImpl(ClientRepository userRepository, RaceRepository raceRepository, ItemRepository itemRepository, ItemStatusRepository itemStatusRepository) {
        this.userRepository = userRepository;
        this.raceRepository = raceRepository;
        this.itemRepository = itemRepository;
        this.itemStatusRepository = itemStatusRepository;
    }




    // Метод для получения учетных данных
    public static GoogleCredential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Путь к файлу ключа учетной записи сервиса на сервере


        // Загрузка ключа учетной записи сервиса из файла
        InputStream in = new FileInputStream(serviceAccountKeyPath);
        if (in == null) {
            throw new FileNotFoundException("Файл ключа учетной записи сервиса не найден");
        }

        // Создание учетных данных
        GoogleCredential credential = GoogleCredential.fromStream(in, HTTP_TRANSPORT, JacksonFactory.getDefaultInstance())
                .createScoped(SCOPES);
        return credential;
    }


    public String saveDataFromSheet(String spreadsheetId, String race) throws IOException, GeneralSecurityException {

        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        //final String spreadsheetId = "1cIBhM9ZVg9WO9ZvoACDOayCSo6ULTNYQC2W6lBWOUzc";
        //                  B-name, C-city, D-X,  E-number, F-item, G-X, H-X, I-X, J - weight
        final String range = "RaceData!B4:J";
        String status = "error";
        Sheets service =
                new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, GoogleSheetsServiceImpl.getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> sheetData = response.getValues();
        if (sheetData == null || sheetData.isEmpty()) {
            System.out.println("No data found.");
            return "No data Found";
        } else {

            System.out.println("We're starting saving");

            //Creating a Race Object and saving via Repo
            Race race1 = new Race();
            race1.setId(race);

            ItemStatus itemStatus = itemStatusRepository.findById(1);

            for (List<Object> row : sheetData) {
                System.out.printf("%s, %s, %s, %s, %s\n", row.get(0), row.get(1), row.get(3), row.get(4), row.get(8));
                String name = (String) row.get(0); // assuming name is in the first column (index 0)
                String city = (String) row.get(1); // city
                String phoneNumber = (String) row.get(3);
                String item = (String) row.get(4);
                String weight = (String) row.get(8);

                // looking if user exists
                Client user = userRepository.findByPhoneNumber(phoneNumber);
                if (user == null) {
                    user = new Client();
                    user.setName(name);
                    user.setPhoneNumber(phoneNumber);
                    userRepository.save(user);
                    status = "User created!";

                } else {
                    user.setName(name); // Update the name if the user exists
                    userRepository.save(user);
                    status = "User updated!";
                }

                // Creating a new Item every time
                Item item1 = new Item();
                item1.setName(item);

                item1.setCity(city);
                // setting user
                item1.setUser(user);

                item1.setStatus(itemStatus);
                item1.setWeight(Integer.valueOf(weight));

                // setting race
                item1.setRace(race1);
                itemRepository.save(item1);
                status = "item saved";

            }
            raceRepository.save(race1);
        }
        //return status;
        return status;
    }
}

