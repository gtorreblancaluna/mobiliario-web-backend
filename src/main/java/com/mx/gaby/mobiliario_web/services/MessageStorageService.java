package com.mx.gaby.mobiliario_web.services;

import com.mx.gaby.mobiliario_web.constants.ApplicationConstant;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
public class MessageStorageService {

    // Guardamos los últimos 100 mensajes para no saturar la RAM
    private final List<String> messageLog = Collections.synchronizedList(new LinkedList<>());
    private static final int MAX_MESSAGES = 100;

    public void addMessage(String msg) {
        if (messageLog.size() >= MAX_MESSAGES) {
            messageLog.remove(0); // Eliminamos el más antiguo
        }
        messageLog
                .add(
                        LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern(
                                        ApplicationConstant.LARGE_FORMAT_DATE_SHOW_IN_FRONT))
                                        + ApplicationConstant.BREAK_LINE_HTML
                                        + msg );
    }

    public List<String> getAllMessages() {
        return new ArrayList<>(messageLog);
    }

    public void clear() {
        messageLog.clear();
    }
}
