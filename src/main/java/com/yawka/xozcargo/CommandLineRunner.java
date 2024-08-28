package com.yawka.xozcargo;


import com.yawka.xozcargo.entity.ItemStatus;
import com.yawka.xozcargo.repository.ItemStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommandLineRunner implements ApplicationRunner {

    @Autowired
    private ItemStatusRepository statusRepository;

    @Override
    public void run(ApplicationArguments args) {
        // Создаем статусы при запуске приложения
        ItemStatus status1 = new ItemStatus();
        status1.setId(1);
        status1.setDescription(List.of("Товар покинул склад в Андижане"));
        ItemStatus status2 = new ItemStatus();
        status2.setId(2);
        status2.setDescription(List.of("Товар покинул склад в Андижане", "Товар в процессе таможенного оформления"));
        ItemStatus status3 = new ItemStatus();
        status3.setId(3);
        status3.setDescription(List.of("Товар покинул склад в Андижане", "Товар в процессе таможенного оформления",
                "Товар поступил на склад в Кара-Суу"));
        ItemStatus status4 = new ItemStatus();
        status4.setId(4);
        status4.setDescription(List.of("Товар покинул склад в Андижане", "Товар в процессе таможенного оформления",
                 "Товар поступил на склад в Кара-Суу", "Товар отправлен в Бишкек"));
        ItemStatus status5 = new ItemStatus();
        status5.setId(5);
        status5.setDescription(List.of("Товар покинул склад в Андижане", "Товар в процессе таможенного оформления",
                "Товар поступил на склад в Кара-Суу", "Товар отправлен в Бишкек", "Товар прибыл в Бишкеке"));
        // Добавьте другие статусы

        statusRepository.save(status1);
        statusRepository.save(status2);
        statusRepository.save(status3);
        statusRepository.save(status4);
        statusRepository.save(status5);
        // Сохраняем остальные статусы
    }
}
