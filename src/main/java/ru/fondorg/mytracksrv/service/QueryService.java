package ru.fondorg.mytracksrv.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.Objects;

/**
 * Utility service for common querying tasks
 */
@Service
public class QueryService {

    /**
     * Gets Pageable object from specified query parameters map
     *
     * @param queryParams Query parameters map
     * @return Pageable object
     */
    Pageable getPageable(MultiValueMap<String, String> queryParams) {
        int page = Integer.parseInt(Objects.requireNonNullElse(queryParams.getFirst("page"), "1"));
        int size = Integer.parseInt(Objects.requireNonNullElse(queryParams.getFirst("size"), "5"));
        return PageRequest.of(page - 1, size);
    }

}
