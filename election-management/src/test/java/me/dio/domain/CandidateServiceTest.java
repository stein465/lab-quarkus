package me.dio.domain;


import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;


import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class CandidateServiceTest {

    @Inject
    CandidateService service;

    @InjectMock
    CandidateRepository repository;

    @Test
    void save(){
        Candidate candidate = Instancio.create(Candidate.class);

        service.save(candidate);

        verify(repository).save(List.of(candidate));
        verifyNoMoreInteractions(repository);


    }

    @Test
    void findAll(){
        List<Candidate> cadidatesMock = Instancio.stream(Candidate.class).limit(10).toList();

        when(repository.findAll()).thenReturn(cadidatesMock);

        List<Candidate> result = service.findAll();

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);

        assertEquals(cadidatesMock, result);

    }

    @Test
    void findByID(){
        Candidate cadidatesMock = Instancio.create(Candidate.class);

        when(repository.findById(cadidatesMock.id())).thenReturn(Optional.of(cadidatesMock));

        Candidate result = service.findById(cadidatesMock.id());

        verify(repository).findById(cadidatesMock.id());
        verifyNoMoreInteractions(repository);

        assertEquals(cadidatesMock, result);

    }

    @Test
    void findByID_whenCandidateIsFoud_returnCadidate(){
        Candidate cadidatesMock = Instancio.create(Candidate.class);

        when(repository.findById(cadidatesMock.id())).thenReturn(Optional.of(cadidatesMock));

        Candidate result = service.findById(cadidatesMock.id());

        verify(repository).findById(cadidatesMock.id());
        verifyNoMoreInteractions(repository);

        assertEquals(cadidatesMock, result);

    }

    @Test
    void findByID_whenCandidateIsFoud_ThrowsException(){
        Candidate cadidatesMock = Instancio.create(Candidate.class);

        when(repository.findById(cadidatesMock.id())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> service.findById(cadidatesMock.id()));


        verify(repository).findById(cadidatesMock.id());
        verifyNoMoreInteractions(repository);
    }

}