package com.shekhar.ecom_proj.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class OrderNumberGenerator {

    public String generate() {

        String date = LocalDate.now()
                .format(DateTimeFormatter.BASIC_ISO_DATE);

        String random = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();

        return "ORD-" + date + "-" + random;
    }
}
