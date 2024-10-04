package com.example.springboot.MovieApp;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieRestController {

    private final MovieService movieService;
    private final MovieMapper movieMapper;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public List<MovieDTO> get(MovieDTO movieDTO) {
        return movieService.get(movieDTO);
    }

    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public MovieDTO getById(@RequestParam Long id) {
        return movieMapper.map(movieService.getById(id));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public MovieDTO create(@RequestBody MovieDTO movieDTO) {
        return movieService.create(movieDTO);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PATCH)
    public MovieDTO update(@RequestBody MovieDTO movieDTO) {
        return movieService.update(movieDTO);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public String delete(@RequestBody MovieDTO movieDTO) {
        return movieService.delete(movieDTO);
    }

    @RequestMapping(value = "/saveImage", method = RequestMethod.PUT)
    public MovieDTO uploadDataWithImage(@ModelAttribute MovieDTO movieDTO, @RequestParam("image") MultipartFile image) {
        return movieService.uploadDataWithImage(movieDTO, image);
    }
}