package org.romeo.compliments.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.romeo.compliments.MainApplication;
import org.romeo.compliments.persistence.UserRepository;
import org.romeo.compliments.web.domain.PaginatedList;
import org.romeo.compliments.web.domain.User;
import org.romeo.compliments.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

;

/**
 * User: tylerromeo
 * Date: 6/18/16
 * Time: 2:52 PM
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainApplication.class)
@TestPropertySource(locations = "classpath:test.properties")
@WebIntegrationTest
public class UserControllerTest {

    @Autowired
    UserController userController;

    @Autowired
    UserRepository userRepository;

    List<org.romeo.compliments.persistence.domain.User> testUsers;

    @Before
    public void setup() {
        userRepository.deleteAll();

        testUsers = new ArrayList<>();
        testUsers.add(new org.romeo.compliments.persistence.domain.User("a", "b", "c"));
        testUsers.add(new org.romeo.compliments.persistence.domain.User("d", "e", "f"));
        testUsers.add(new org.romeo.compliments.persistence.domain.User("g", "h", "i"));
        testUsers.add(new org.romeo.compliments.persistence.domain.User("j", "k", "l"));
        testUsers.add(new org.romeo.compliments.persistence.domain.User("m", "n", "o"));
        testUsers.add(new org.romeo.compliments.persistence.domain.User("p", "q", "r"));
        testUsers.add(new org.romeo.compliments.persistence.domain.User("s", "t", "u"));
        testUsers.add(new org.romeo.compliments.persistence.domain.User("w", "x", "y"));
        userRepository.save(testUsers);
    }

    @Test
    public void testGetAll() throws Exception {
        String email = null;
        int pageNum = 0;
        int size = 5;
        PaginatedList<User> results = userController.getAll(email, pageNum, size);

        assertEquals(testUsers.size(), results.getTotalResults());
        assertEquals(pageNum, results.getPage());
        assertEquals(size, results.getCount());
        assertTrue(!results.getNext().isEmpty());
        assertTrue(String.format("next url: %s should contain page=%d", results.getNext(), pageNum + 1), results.getNext().contains("page=" + (pageNum + 1)));
        assertTrue(String.format("next url: %s should contain size=%d", results.getNext(), size), results.getNext().contains("size=" + size));
        for (User u : results.getResults()) {
            assertNotNull(u);
            assertNotNull(u.getImageUrl());
            assertNotNull(u.getName());
            assertNotNull(u.getEmail());
        }
    }

    @Test
    public void testGetAll_SecondPage() throws Exception {
        String email = null;
        int pageNum = 1;
        int size = 5;
        PaginatedList<User> results = userController.getAll(email, pageNum, size);

        assertEquals(testUsers.size(), results.getTotalResults());
        assertEquals(pageNum, results.getPage());
        assertEquals(testUsers.size() - (pageNum * size), results.getCount());
        for (User u : results.getResults()) {
            assertNotNull(u);
            assertNotNull(u.getImageUrl());
            assertNotNull(u.getName());
            assertNotNull(u.getEmail());
        }
    }

    @Test
    public void testGetAll_LastPage() throws Exception {
        String email = null;
        int pageNum = 0;
        int size = 30;
        PaginatedList<User> results = userController.getAll(email, pageNum, size);

        assertTrue(results.getNext().isEmpty());
    }

    @Test
    public void testGetAll_EmptyPage() throws Exception {
        String email = null;
        int pageNum = 100;
        int size = 5;
        PaginatedList<User> results = userController.getAll(email, pageNum, size);

        assertEquals(testUsers.size(), results.getTotalResults());
        assertEquals(pageNum, results.getPage());
        assertEquals(0, results.getCount());
        assertTrue(results.getResults().isEmpty());
    }

    @Test
    public void testGetById() throws Exception {
        Iterable<org.romeo.compliments.persistence.domain.User> allUsers = userRepository.findAll();
        org.romeo.compliments.persistence.domain.User userToGet = allUsers.iterator().next();
        User result = userController.getById(userToGet.getId());
        assertEquals(result, User.fromDbUser(userToGet));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetById_IdNotFound() throws Exception {
        long id = 12341234;
        userController.getById(id);
    }
}