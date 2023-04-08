package com.mockproject.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

public class ListUtils {

    public static <T> void checkList(Optional<List<T>> list){
        if(list.get().size() == 0)
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }
}
