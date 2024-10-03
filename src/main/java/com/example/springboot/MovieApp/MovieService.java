package com.example.springboot.MovieApp;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper mapper;
    final static String BASE_PATH = System.getProperty("user.dir") + "/src/main/resources/static";
    final static String IMAGE_URL = "/images/";


    @Transactional(readOnly = true,propagation = Propagation.REQUIRES_NEW)
    public List<MovieDTO> get(MovieDTO movieDTO) {
        List<Movie> movies;
        if (movieDTO.getTitle() != null) {
            int pageOffset = movieDTO.getPageOffset() != null ? movieDTO.getPageOffset() : 0;
            int pageSize = movieDTO.getPageSize() != null ? movieDTO.getPageSize() : 10;
            Pageable pageable = PageRequest.of(pageOffset / pageSize, pageSize,
                    Sort.by("title").ascending() // Sort by title ascending
            );
            movies = movieRepository.findAll(pageable).getContent();
        } else if(movieDTO.getPageOffset() != null && movieDTO.getPageSize() != null){
            Pageable pageable = PageRequest.of(
                    movieDTO.getPageOffset() / movieDTO.getPageSize(),
                    movieDTO.getPageSize()
            );
            movies = movieRepository.findAll(pageable).getContent();
        }else{
            movies = movieRepository.findAll();
        }
       return mapper.map(movies);
    }

    @Transactional
    public MovieDTO create(MovieDTO movieDTO) {
        try {
            Movie movie = mapper.map(movieDTO);
            movie = movieRepository.save(movie);
            return mapper.map(movie);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public MovieDTO uploadDataWithImage(MovieDTO movieDTO, MultipartFile image) {
        String oldImagePath = null;
        try {
            Movie movie = mapper.map(movieDTO);

            // store the existing image path
            if(movieDTO.getId() != null){
                Movie oldMovie = getById(movieDTO.getId());
                oldImagePath = oldMovie.getImagePosterPath();
            }

            movie.setImagePosterPath(saveImage(image));
            movie = movieRepository.save(movie);

            // remove the old image
            if(oldImagePath != null){
                removeImage(oldImagePath);
            }

            return mapper.map(movie);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public MovieDTO update(MovieDTO movieDTO) {
        try {
            Movie movie = getById(movieDTO.getId());
            if(movieDTO.getTitle() != null){
                movie.setTitle(movieDTO.getTitle());
            }
            if(movieDTO.getGenre() != null){
                movie.setGenre(movieDTO.getGenre());
            }
            if(movieDTO.getDescription() != null){
                movie.setDescription(movieDTO.getDescription());
            }
            movie = movieRepository.save(movie);
            return mapper.map(movie);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public String delete(MovieDTO movieDTO) {
        try{
            if(movieRepository.existsById(movieDTO.getId())){
                movieRepository.deleteById(movieDTO.getId());
                return "Movie deleted";
            }else{
                throw new RuntimeException("Movie not found");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true,propagation = Propagation.REQUIRES_NEW)
    public Movie getById(Long id){
        return movieRepository.findById(id).orElseThrow(()-> new RuntimeException("Movie not found"));
    }

    private String saveImage(MultipartFile image) {
        try {
            String imageName = IMAGE_URL + UUID.randomUUID() + image.getContentType().replace("image/",".");
            String imagePath = BASE_PATH + imageName;
            image.transferTo(new File(imagePath));
            return imageName;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // remove image
    private void removeImage(String imagePath) {
        try {
            File file = new File(BASE_PATH + imagePath);
            if(file.exists()){
                file.delete();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
