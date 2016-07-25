package org.romeo.compliments.web;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.romeo.compliments.MainApplication;
import org.romeo.compliments.persistence.ComplimentRepository;
import org.romeo.compliments.persistence.UserRepository;
import org.romeo.compliments.web.domain.ComplimentRequest;
import org.romeo.compliments.domain.User;
import org.romeo.compliments.web.domain.PaginatedList;
import org.romeo.compliments.web.domain.UserRequest;
import org.romeo.compliments.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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

    @Autowired
    ComplimentRepository complimentRepository;

    @Autowired
    ComplimentController complimentController;

    List<org.romeo.compliments.domain.User> testUsers;

    @Before
    public void setup() {
        testUsers = new ArrayList<>();
        testUsers.add(new org.romeo.compliments.domain.User("a", "b", "c"));
        testUsers.add(new org.romeo.compliments.domain.User("d", "e", "f"));
        testUsers.add(new org.romeo.compliments.domain.User("g", "h", "i"));
        testUsers.add(new org.romeo.compliments.domain.User("j", "k", "l"));
        testUsers.add(new org.romeo.compliments.domain.User("m", "n", "o"));
        testUsers.add(new org.romeo.compliments.domain.User("p", "q", "r"));
        testUsers.add(new org.romeo.compliments.domain.User("s", "t", "u"));
        testUsers.add(new org.romeo.compliments.domain.User("w", "x", "y"));
        userRepository.save(testUsers);
    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
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
        Iterable<org.romeo.compliments.domain.User> allUsers = userRepository.findAll();
        User userToGet = allUsers.iterator().next();
        User result = userController.getById(userToGet.getId());
        //https://forum.hibernate.org/viewtopic.php?p=2465916
        //Can't compare these directly because of hibernate limitations. Serves me right for unit testing hibernate code
        assertEquals(userToGet.getEmail(), result.getEmail());
        assertEquals(userToGet.getName(), result.getName());
        assertEquals(userToGet.getImageUrl(), result.getImageUrl());
        assertEquals(userToGet.getComplimentsReceivedCount(), result.getComplimentsReceivedCount());
        assertEquals(userToGet.getComplimentsSentCount(), result.getComplimentsSentCount());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetById_IdNotFound() throws Exception {
        long id = 12341234;
        userController.getById(id);
    }

    @Test
    public void testAddUser_doesNotExist() throws Exception {
        UserRequest newUser = new UserRequest("email", "name", "image");
        assertNull("There should not be a user with this email in the db already", userRepository.findByEmail(newUser.getEmail()));

        User response = userController.add(newUser);
        assertNotNull(response);
        assertEquals(newUser.getEmail(), response.getEmail());
        assertEquals(newUser.getName(), response.getName());
        assertEquals(newUser.getImageUrl(), response.getImageUrl());
        assertEquals(0, response.getComplimentsReceivedCount());
        assertEquals(0, response.getComplimentsSentCount());
        assertNotEquals(0, response.getId());

        org.romeo.compliments.domain.User dbUser = userRepository.findOne(response.getId());
        assertNotNull(dbUser);
        assertEquals(response.getEmail(), dbUser.getEmail());
        assertEquals(response.getName(), dbUser.getName());
        assertEquals(response.getImageUrl(), dbUser.getImageUrl());
        assertEquals(response.getComplimentsReceivedCount(), dbUser.getComplimentsReceived().size());
        assertEquals(response.getComplimentsSentCount(), dbUser.getComplimentsSent().size());
    }

    @Test
    public void testAddUser_hasCompliment() throws Exception {
        //get a user to get a compliment from
        long fromUserId = userRepository.findAll().iterator().next().getId();
        String email = "test@example.com";
        //add a compliment from someone so we make sure it sticks around
        complimentController.add(new ComplimentRequest(email, "you are nice", fromUserId));

        UserRequest newUser = new UserRequest(email, "name", "image");

        User response = userController.add(newUser);
        assertNotNull(response);
        assertEquals(newUser.getEmail(), response.getEmail());
        assertEquals(newUser.getName(), response.getName());
        assertEquals(newUser.getImageUrl(), response.getImageUrl());
        assertEquals(1, response.getComplimentsReceivedCount());
        assertEquals(0, response.getComplimentsSentCount());
        assertNotEquals(0, response.getId());

        org.romeo.compliments.domain.User dbUser = userRepository.findOne(response.getId());
        assertNotNull(dbUser);
        assertEquals(response.getEmail(), dbUser.getEmail());
        assertEquals(response.getName(), dbUser.getName());
        assertEquals(response.getImageUrl(), dbUser.getImageUrl());
        assertEquals(response.getComplimentsReceivedCount(), dbUser.getComplimentsReceived().size());
        assertEquals(fromUserId, dbUser.getComplimentsReceived().get(0).getFrom().getId());
        assertEquals(response.getComplimentsSentCount(), dbUser.getComplimentsSent().size());
    }

    @Test
    public void testEditUser() throws Exception {
        org.romeo.compliments.domain.User testUser = userRepository.findAll().iterator().next();
        assertNotNull(testUser);

        UserRequest changedUser = new UserRequest("newEmail", "newName", "newImageUrl");

        User response = userController.edit(testUser.getId(), changedUser);

        assertNotNull(response);
        assertEquals(testUser.getId(), response.getId());
        assertEquals(changedUser.getEmail(), response.getEmail());
        assertEquals(changedUser.getName(), response.getName());
        assertEquals(changedUser.getImageUrl(), response.getImageUrl());
        assertEquals(testUser.getComplimentsReceived().size(), response.getComplimentsReceivedCount());
        assertEquals(testUser.getComplimentsSent().size(), response.getComplimentsSentCount());

        org.romeo.compliments.domain.User dbUser = userRepository.findOne(testUser.getId());

        assertNotNull(dbUser);
        assertEquals(response.getId(), dbUser.getId());
        assertEquals(response.getEmail(), dbUser.getEmail());
        assertEquals(response.getName(), dbUser.getName());
        assertEquals(response.getImageUrl(), dbUser.getImageUrl());
        assertEquals(response.getComplimentsReceivedCount(), dbUser.getComplimentsReceived().size());
        assertEquals(response.getComplimentsSentCount(), dbUser.getComplimentsSent().size());
    }

    @Test
    public void testEditUser_missingFields() throws Exception {
        org.romeo.compliments.domain.User testUser = userRepository.findAll().iterator().next();
        assertNotNull(testUser);

        UserRequest changedUser = new UserRequest(null, null, "newImageUrl");

        User response = userController.edit(testUser.getId(), changedUser);

        assertNotNull(response);
        assertEquals(testUser.getId(), response.getId());
        assertEquals(testUser.getEmail(), response.getEmail());
        assertEquals(testUser.getName(), response.getName());
        assertEquals(changedUser.getImageUrl(), response.getImageUrl());
        assertEquals(testUser.getComplimentsReceived().size(), response.getComplimentsReceivedCount());
        assertEquals(testUser.getComplimentsSent().size(), response.getComplimentsSentCount());

        org.romeo.compliments.domain.User dbUser = userRepository.findOne(testUser.getId());

        assertNotNull(dbUser);
        assertEquals(response.getId(), dbUser.getId());
        assertEquals(response.getEmail(), dbUser.getEmail());
        assertEquals(response.getName(), dbUser.getName());
        assertEquals(response.getImageUrl(), dbUser.getImageUrl());
        assertEquals(response.getComplimentsReceivedCount(), dbUser.getComplimentsReceived().size());
        assertEquals(response.getComplimentsSentCount(), dbUser.getComplimentsSent().size());

    }

    @Test
    public void testEditUser_emptyStringFields() throws Exception {
        org.romeo.compliments.domain.User testUser = userRepository.findAll().iterator().next();
        assertNotNull(testUser);

        UserRequest changedUser = new UserRequest("", "", "");

        User response = userController.edit(testUser.getId(), changedUser);

        assertNotNull(response);
        assertEquals(testUser.getId(), response.getId());
        assertEquals(changedUser.getEmail(), response.getEmail());
        assertEquals(changedUser.getName(), response.getName());
        assertEquals(changedUser.getImageUrl(), response.getImageUrl());
        assertEquals(testUser.getComplimentsReceived().size(), response.getComplimentsReceivedCount());
        assertEquals(testUser.getComplimentsSent().size(), response.getComplimentsSentCount());

        org.romeo.compliments.domain.User dbUser = userRepository.findOne(testUser.getId());

        assertNotNull(dbUser);
        assertEquals(response.getId(), dbUser.getId());
        assertEquals(response.getEmail(), dbUser.getEmail());
        assertEquals(response.getName(), dbUser.getName());
        assertEquals(response.getImageUrl(), dbUser.getImageUrl());
        assertEquals(response.getComplimentsReceivedCount(), dbUser.getComplimentsReceived().size());
        assertEquals(response.getComplimentsSentCount(), dbUser.getComplimentsSent().size());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void testEditUser_doesNotExist() throws Exception {
        long badId = 2345092345l;
        assertFalse("chosen id should not exist in test data", userRepository.exists(badId));

        UserRequest changedUser = new UserRequest(null, null, "newImageUrl");

        userController.edit(badId, changedUser);

    }
}