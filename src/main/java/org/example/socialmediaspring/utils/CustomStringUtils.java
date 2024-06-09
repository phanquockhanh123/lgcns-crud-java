package org.example.socialmediaspring.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.example.socialmediaspring.constant.ErrorCodeConst;
import org.example.socialmediaspring.exception.BizException;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomStringUtils {
    private final MessageSource messageSource;
    private final Locale defaultLocale;
    private static String nullCheckMessage = "%s must be not null";

    public String transformToRestError(String messageCode, Object... arguments) {
        try {
            return messageSource.getMessage(messageCode, arguments, defaultLocale);
        } catch (Exception e) {
            return messageCode;
        }
    }

    public String maskedGetLastSomeDigit(String inputString, int numberOfLastDigit) {
        if (numberOfLastDigit >= inputString.length()) {
            return inputString;
        }
        return inputString.substring(inputString.length() - numberOfLastDigit);
    }

    public static String toWithoutAccents(String str) {
        try {
            if (Strings.isBlank(str)) {
                return Strings.EMPTY;
            }
            String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(nfdNormalizedString).replaceAll("");
        } catch (Exception e) {
            log.error("convert to without accents error", e);
            return Strings.EMPTY;
        }
    }

    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    public static void handleNullCheck(Map<String, Object> params) {
        params.forEach((name, value) -> {
            if (value == null) {
                throw new BizException(ErrorCodeConst.INVALID_INPUT, String.format(nullCheckMessage, name));
            }
        });
    }
}
