package com.example.prj_do_an_two.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StateDto {
    private Integer id;
    private String name;

    public StateDto(Integer id) {
        this.id = id;
    }
}
