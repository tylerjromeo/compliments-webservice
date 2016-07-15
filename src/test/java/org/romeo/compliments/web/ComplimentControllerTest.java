package org.romeo.compliments.web;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.romeo.compliments.MainApplication;
import org.romeo.compliments.persistence.ComplimentRepository;
import org.romeo.compliments.persistence.UserRepository;
import org.romeo.compliments.web.domain.Compliment;
import org.romeo.compliments.web.domain.ComplimentRequest;
import org.romeo.compliments.web.domain.PaginatedList;
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
public class ComplimentControllerTest {

    @Autowired
    ComplimentController complimentController;

    @Autowired
    ComplimentRepository complimentRepository;

    @Autowired
    UserRepository userRepository;

    org.romeo.compliments.persistence.domain.User testUser1;
    org.romeo.compliments.persistence.domain.User testUser2;
    org.romeo.compliments.persistence.domain.User testUser3;

    List<org.romeo.compliments.persistence.domain.Compliment> complimentList;
    org.romeo.compliments.persistence.domain.Compliment testCompliment1;
    org.romeo.compliments.persistence.domain.Compliment testCompliment2;
    org.romeo.compliments.persistence.domain.Compliment testCompliment3;
    org.romeo.compliments.persistence.domain.Compliment testCompliment4;

    @Before
    public void setup() {
        testUser1 = new org.romeo.compliments.persistence.domain.User("a", "b", "c");
        testUser2 = new org.romeo.compliments.persistence.domain.User("d", "e", "f");
        testUser3 = new org.romeo.compliments.persistence.domain.User("g", "h", "i");
        userRepository.save(testUser1);
        userRepository.save(testUser2);
        userRepository.save(testUser3);

        testCompliment1 = new org.romeo.compliments.persistence.domain.Compliment(testUser1, testUser2, "1 I think you are great. Love, 2");
        testCompliment2 = new org.romeo.compliments.persistence.domain.Compliment(testUser1, testUser2, "1 I think you are really great. Love, 2");
        testCompliment3 = new org.romeo.compliments.persistence.domain.Compliment(testUser2, testUser3, "2 I think you are great. Love, 3");
        testCompliment4 = new org.romeo.compliments.persistence.domain.Compliment(testUser1, testUser3, "1 I think you are great. Love, 3");
        complimentList = new ArrayList<>();
        complimentList.add(testCompliment1);
        complimentList.add(testCompliment2);
        complimentList.add(testCompliment3);
        complimentList.add(testCompliment4);

        complimentRepository.save(complimentList);
    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void testGetAll_to() {
        Long to = testUser2.getId();
        Long from = null;
        int page = 0;
        int size = 1;
        PaginatedList<Compliment> results = complimentController.getAll(to, from, page, size);

        assertEquals(2, results.getTotalResults());
        assertEquals(page, results.getPage());
        assertEquals(1, results.getCount());
        assertTrue(!results.getNext().isEmpty());
        assertTrue(String.format("next url: %s should contain page=%d", results.getNext(), page + 1), results.getNext().contains("page=" + (page + 1)));
        assertTrue(String.format("next url: %s should contain size=%d", results.getNext(), size), results.getNext().contains("size=" + size));
        assertTrue(!results.getResults().isEmpty());
        for (Compliment c : results.getResults()) {
            assertNotNull(c);
            assertNotNull(c.getContents());
            assertNotNull(c.getSendDate());
            assertEquals(to, Long.valueOf(c.getToId()));
        }
    }

    @Test
    public void testGetAll_from() {
        Long to = null;
        Long from = testUser1.getId();
        int page = 0;
        int size = 1;
        PaginatedList<Compliment> results = complimentController.getAll(to, from, page, size);

        assertEquals(3, results.getTotalResults());
        assertEquals(page, results.getPage());
        assertEquals(1, results.getCount());
        assertTrue(!results.getNext().isEmpty());
        assertTrue(String.format("next url: %s should contain page=%d", results.getNext(), page + 1), results.getNext().contains("page=" + (page + 1)));
        assertTrue(String.format("next url: %s should contain size=%d", results.getNext(), size), results.getNext().contains("size=" + size));
        assertTrue(!results.getResults().isEmpty());
        for (Compliment c : results.getResults()) {
            assertNotNull(c);
            assertNotNull(c.getContents());
            assertNotNull(c.getSendDate());
            assertEquals(from, Long.valueOf(c.getFromId()));
        }
    }

    @Test
    public void testGetAll_noIds() {
        Long to = null;
        Long from = null;
        int page = 0;
        int size = 1;
        PaginatedList<Compliment> results = complimentController.getAll(to, from, page, size);

        assertEquals(4, results.getTotalResults());
        assertEquals(page, results.getPage());
        assertEquals(1, results.getCount());
        assertTrue(!results.getNext().isEmpty());
        assertTrue(String.format("next url: %s should contain page=%d", results.getNext(), page + 1), results.getNext().contains("page=" + (page + 1)));
        assertTrue(String.format("next url: %s should contain size=%d", results.getNext(), size), results.getNext().contains("size=" + size));
        assertTrue(!results.getResults().isEmpty());
        for (Compliment c : results.getResults()) {
            assertNotNull(c);
            assertNotNull(c.getContents());
            assertNotNull(c.getSendDate());
        }
    }

    @Test
    public void testGetAll_toAndFrom() {
        Long to = testUser3.getId();
        Long from = testUser1.getId();
        int page = 0;
        int size = 1;
        PaginatedList<Compliment> results = complimentController.getAll(to, from, page, size);

        assertEquals(1, results.getTotalResults());
        assertEquals(page, results.getPage());
        assertEquals(1, results.getCount());
        assertTrue(results.getNext().isEmpty());
        assertTrue(!results.getResults().isEmpty());
        for (Compliment c : results.getResults()) {
            assertNotNull(c);
            assertNotNull(c.getContents());
            assertNotNull(c.getSendDate());
            assertEquals(to, Long.valueOf(c.getToId()));
            assertEquals(from, Long.valueOf(c.getFromId()));
        }
    }

    @Test
    public void testAdd() {
        String content = "new compliment content";
        ComplimentRequest compliment = new ComplimentRequest(testUser1.getEmail(), content, testUser2.getId());

        Compliment response = complimentController.add(compliment);

        org.romeo.compliments.persistence.domain.Compliment dbCompliment = complimentRepository.findById(response.getId());

        assertNotNull(response);
        assertEquals(testUser1.getId(), response.getToId());
        assertEquals(compliment.getContents(), response.getContents());
        assertEquals(testUser2.getId(), response.getFromId());

        assertNotNull("Added compliment not found in db", dbCompliment);
        assertEquals(response.getId(), dbCompliment.getId());
        assertEquals(response.getToId(), dbCompliment.getTo().getId());
        assertEquals(response.getContents(), dbCompliment.getContents());
        assertEquals(response.getFromId(), dbCompliment.getFrom().getId());
        assertNotNull(dbCompliment.getSendDate());
    }

    @Test
    public void testAddByEmail_noEmailMatch() {
        String newEmail = "test@example.com";
        assertNull("new email address should not exist in the db", userRepository.findByEmail(newEmail));
        String content = "new compliment content";
        ComplimentRequest compliment = new ComplimentRequest(newEmail, content, testUser2.getId());

        Compliment response = complimentController.add(compliment);

        org.romeo.compliments.persistence.domain.Compliment dbCompliment = complimentRepository.findById(response.getId());

        org.romeo.compliments.persistence.domain.User newUser = userRepository.findByEmail(newEmail);

        assertNotNull("user with new email address should have been added to db", newUser);
        assertEquals(newEmail, newUser.getEmail());

        assertNotNull(response);
        assertEquals(newUser.getId(), response.getToId());
        assertEquals(compliment.getContents(), response.getContents());
        assertEquals(testUser2.getId(), response.getFromId());

        assertNotNull("Added compliment not found in db", dbCompliment);
        assertEquals(response.getId(), dbCompliment.getId());
        assertEquals(response.getToId(), dbCompliment.getTo().getId());
        assertEquals(response.getContents(), dbCompliment.getContents());
        assertEquals(response.getFromId(), dbCompliment.getFrom().getId());
        assertNotNull(dbCompliment.getSendDate());

    }

}