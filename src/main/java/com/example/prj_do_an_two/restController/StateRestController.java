package com.example.prj_do_an_two.restController;

import com.example.prj_do_an_two.dto.StateDto;
import com.example.prj_do_an_two.entity.Country;
import com.example.prj_do_an_two.entity.State;
import com.example.prj_do_an_two.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StateRestController {
    @Autowired private StateRepository stateRepository;

    @GetMapping("/settings/list_states_by_country/{id}")
    public List<StateDto> listByCountry(@PathVariable("id") Integer countryId) {
        List<State> states = stateRepository.findAllByCountryOrderByNameAsc(new Country(countryId));
        List<StateDto> stateDtos = new ArrayList<>();

        for(State state : states) {
            stateDtos.add(new StateDto(state.getId(), state.getName()));
        }

        return stateDtos;

    }

}
