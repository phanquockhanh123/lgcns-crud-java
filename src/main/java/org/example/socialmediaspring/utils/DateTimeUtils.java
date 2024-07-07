package org.example.socialmediaspring.utils;

import org.springframework.cglib.core.Local;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    /**
     * Calculates the duration in hours between the start and end dates.
     *
     * @param startDate the start date as a LocalDateTime object
     * @param endDate the end date as a LocalDateTime object
     * @return the duration in hours
     */
    public static Long getDurationInHours(LocalDateTime startDate, LocalDateTime endDate) {
        Duration duration = Duration.between(startDate, endDate);
        return duration.toHours();
    }

    /**
     * Converts a LocalDateTime object to a timestamp in milliseconds.
     *
     * @param localDateTime the LocalDateTime object to be converted
     * @return the timestamp in milliseconds
     */
    public static Long convertToTimestamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
    public static LocalDateTime convertTimestampToLocalDateTime(Long timestamp) {
        return Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static String localDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        return formattedDateTime;
    }

}
