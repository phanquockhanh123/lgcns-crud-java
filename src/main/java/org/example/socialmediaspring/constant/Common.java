package org.example.socialmediaspring.constant;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Objects;

public class Common {
    public static final int PAGE_SIZE = 20;
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int DEFAULT_PAGE = 0;
    private Common() {
    }

    public static PageRequest getPageRequest(Integer pageIndex, Integer pageSize, Sort sort) {
        int index = pageIndex != null && pageIndex > Common.DEFAULT_PAGE
                ? pageIndex : Common.DEFAULT_PAGE;
        int size = pageSize != null && pageSize > 0
                ? pageSize : Common.DEFAULT_PAGE_SIZE;
        if (Objects.nonNull(sort)) {
            return PageRequest.of(index, size, sort);
        }
        return PageRequest.of(index, size);
    }
}
