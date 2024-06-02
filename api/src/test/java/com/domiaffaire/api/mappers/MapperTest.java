package com.domiaffaire.api.mappers;

import com.domiaffaire.api.dto.PackDTO;
import com.domiaffaire.api.entities.Pack;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class MapperTest {

    private Mapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new Mapper();
    }

    @Test
    public void shouldMapPackToPackDTO(){
        Pack pack = new Pack();
        pack.setId("1");
        pack.setDesignation("Pro +");
        pack.setPrice(70);
        pack.setDescription("Description");

        PackDTO packDTO = mapper.fromPackToPackDTO(pack);

        Assertions.assertEquals(pack.getDesignation(),packDTO.getDesignation());
        Assertions.assertEquals(pack.getId(),packDTO.getId());
        Assertions.assertEquals(pack.getDescription(),packDTO.getDescription());
        Assertions.assertEquals(pack.getPrice(),packDTO.getPrice());
    }











//    @BeforeAll
//    static void beforeAll() {
//        System.out.println("before all method");
//    }
//
//    @BeforeEach
//    void setUp() {
//        System.out.println("Inside before each method");
//
//    }
//
//    @AfterEach
//    void tearDown() {
//        System.out.println("Inside after each method");
//    }
//
//    @Test
//    public void testMethod1(){
//        System.out.println("My first test");
//    }
//
//    @Test
//    public void testMethod2(){
//        System.out.println("My second test");
//    }
}