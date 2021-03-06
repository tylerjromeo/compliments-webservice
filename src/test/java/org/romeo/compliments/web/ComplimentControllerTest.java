package org.romeo.compliments.web;

import org.hibernate.ObjectNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.romeo.compliments.MainApplication;
import org.romeo.compliments.domain.User;
import org.romeo.compliments.persistence.ComplimentRepository;
import org.romeo.compliments.persistence.UserRepository;
import org.romeo.compliments.domain.Compliment;
import org.romeo.compliments.web.domain.ComplimentRequest;
import org.romeo.compliments.web.domain.PaginatedList;
import org.romeo.compliments.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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

    User testUser1;
    User testUser2;
    User testUser3;

    List<org.romeo.compliments.domain.Compliment> complimentList;
    org.romeo.compliments.domain.Compliment testCompliment1;
    org.romeo.compliments.domain.Compliment testCompliment2;
    org.romeo.compliments.domain.Compliment testCompliment3;
    org.romeo.compliments.domain.Compliment testCompliment4;

    @Before
    public void setup() {
        testUser1 = new User("a", "b", "c");
        testUser2 = new User("d", "e", "f");
        testUser3 = new User("g", "h", "i");
        userRepository.save(testUser1);
        userRepository.save(testUser2);
        userRepository.save(testUser3);

        testCompliment1 = new org.romeo.compliments.domain.Compliment(testUser1, testUser2, "1 I think you are great. Love, 2");
        testCompliment2 = new org.romeo.compliments.domain.Compliment(testUser1, testUser2, "1 I think you are really great. Love, 2");
        testCompliment3 = new org.romeo.compliments.domain.Compliment(testUser2, testUser3, "2 I think you are great. Love, 3");
        testCompliment4 = new org.romeo.compliments.domain.Compliment(testUser1, testUser3, "1 I think you are great. Love, 3");
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
    public void testGetAll_to() throws Exception {
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
            assertEquals(to, Long.valueOf(c.getToUserId()));
        }
    }

    @Test
    public void testGetAll_from() throws Exception {
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
            assertEquals(from, Long.valueOf(c.getFromUserId()));
        }
    }

    @Test
    public void testGetAll_noIds() throws Exception {
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
    public void testGetAll_toAndFrom() throws Exception {
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
            assertEquals(to, Long.valueOf(c.getToUserId()));
            assertEquals(from, Long.valueOf(c.getFromUserId()));
        }
    }

    @Test
    public void testAdd() throws Exception {
        String content = "new compliment content";
        ComplimentRequest compliment = new ComplimentRequest(testUser1.getEmail(), content, testUser2.getId());

        Compliment response = complimentController.add(compliment);

        org.romeo.compliments.domain.Compliment dbCompliment = complimentRepository.findById(response.getId());

        assertNotNull(response);
        assertEquals(testUser1.getId(), response.getToUserId());
        assertEquals(compliment.getContents(), response.getContents());
        assertEquals(testUser2.getId(), response.getFromUserId());

        assertNotNull("Added compliment not found in db", dbCompliment);
        assertEquals(response.getId(), dbCompliment.getId());
        assertEquals(response.getToUserId(), dbCompliment.getTo().getId());
        assertEquals(response.getContents(), dbCompliment.getContents());
        assertEquals(response.getFromUserId(), dbCompliment.getFrom().getId());
        assertNotNull(dbCompliment.getSendDate());
    }

    @Test
    public void testAddByEmail_noEmailMatch() throws Exception {
        String newEmail = "test@example.com";
        assertNull("new email address should not exist in the db", userRepository.findByEmail(newEmail));
        String content = "new compliment content";
        ComplimentRequest compliment = new ComplimentRequest(newEmail, content, testUser2.getId());

        Compliment response = complimentController.add(compliment);

        org.romeo.compliments.domain.Compliment dbCompliment = complimentRepository.findById(response.getId());

        User newUser = userRepository.findByEmail(newEmail);

        assertNotNull("user with new email address should have been added to db", newUser);
        assertEquals(newEmail, newUser.getEmail());

        assertNotNull(response);
        assertEquals(newUser.getId(), response.getToUserId());
        assertEquals(compliment.getContents(), response.getContents());
        assertEquals(testUser2.getId(), response.getFromUserId());

        assertNotNull("Added compliment not found in db", dbCompliment);
        assertEquals(response.getId(), dbCompliment.getId());
        assertEquals(response.getToUserId(), dbCompliment.getTo().getId());
        assertEquals(response.getContents(), dbCompliment.getContents());
        assertEquals(response.getFromUserId(), dbCompliment.getFrom().getId());
        assertNotNull(dbCompliment.getSendDate());

    }

    //TODO: complement self test

    @Test
    @Transactional
    public void addReaction() throws Exception {
        Compliment compliment = complimentController.getAll(null, null, 0, 10).getResults().get(0);
        assertNotNull(compliment);

        assertTrue("compliment should not have any reactions", compliment.getReactions() == null || compliment.getReactions().isEmpty());

        Compliment.Reaction reaction = new Compliment.Reaction(null, "joy");

        Compliment.Reaction result = complimentController.addReaction(compliment.getId(), reaction);

        assertEquals(reaction.getReaction(), result.getReaction());

        List<Compliment.Reaction> dbReactions = complimentRepository.findById(compliment.getId()).getReactions();
        assertEquals("There should only be one reaction to the compliment", 1, dbReactions.size());
        //There's only one reaction, so getting 0 should give us the same one
        Compliment.Reaction dbReaction = dbReactions.get(0);

        assertEquals(result.getId(), dbReaction.getId());
        assertEquals(result.getReaction(), dbReaction.getReaction());

    }

    @Test
    @Transactional
    public void addReaction_multiple() throws Exception {
        Compliment compliment = complimentController.getAll(null, null, 0, 10).getResults().get(0);
        assertNotNull(compliment);

        assertTrue("compliment should not have any reactions", compliment.getReactions() == null || compliment.getReactions().isEmpty());

        Compliment.Reaction reaction = new Compliment.Reaction(null, "joy");
        Compliment.Reaction reaction2 = new Compliment.Reaction(null, "crying");

        Compliment.Reaction result = complimentController.addReaction(compliment.getId(), reaction);
        Compliment.Reaction result2 = complimentController.addReaction(compliment.getId(), reaction2);

        assertEquals(reaction.getReaction(), result.getReaction());
        assertEquals(reaction2.getReaction(), result2.getReaction());

        List<Compliment.Reaction> dbReactions = complimentRepository.findById(compliment.getId()).getReactions();
        assertEquals("There should only be two reactions to the compliment", 2, dbReactions.size());

    }

    @Test
    @Transactional
    public void addReaction_duplicate() throws Exception {
        Compliment compliment = complimentController.getAll(null, null, 0, 10).getResults().get(0);
        assertNotNull(compliment);

        assertTrue("compliment should not have any reactions", compliment.getReactions() == null || compliment.getReactions().isEmpty());

        Compliment.Reaction reaction = new Compliment.Reaction(null, "joy");
        Compliment.Reaction reaction2 = new Compliment.Reaction(null, "joy");

        Compliment.Reaction result = complimentController.addReaction(compliment.getId(), reaction);
        Compliment.Reaction result2 = complimentController.addReaction(compliment.getId(), reaction2);

        assertEquals(reaction.getReaction(), result.getReaction());
        assertEquals(reaction2.getReaction(), result2.getReaction());
        assertEquals("The duplicate call should return the same id as the first one", result.getId(), result2.getId());

        List<Compliment.Reaction> dbReactions = complimentRepository.findById(compliment.getId()).getReactions();
        assertEquals("There should only be one reactions to the compliment", 1, dbReactions.size());

    }

    //@Test
    //TODO: implement once user making request is known
    public void addReaction_otherUserReceived() throws Exception {

    }

    @Test(expected = ResourceNotFoundException.class)
    @Transactional
    public void addReaction_complimentDoesNotExist() throws Exception {
        long badId = 9123857029835L;
        assertNull("compliment should not exist in test data", complimentRepository.findOne(badId));
        //Should throw ResourceNotFoundException
        complimentController.addReaction(badId, new Compliment.Reaction(null, "test"));
    }

    @Test
    @Transactional
    public void deleteReaction() throws Exception {
        Compliment compliment = complimentController.getAll(null, null, 0, 10).getResults().get(0);
        assertNotNull(compliment);

        assertTrue("compliment should not have any reactions", compliment.getReactions() == null || compliment.getReactions().isEmpty());

        Compliment.Reaction reaction = new Compliment.Reaction(null, "joy");

        Compliment.Reaction result = complimentController.addReaction(compliment.getId(), reaction);

        assertEquals(reaction.getReaction(), result.getReaction());
        assertEquals(1, complimentRepository.findById(compliment.getId()).getReactions().size());

        complimentController.deleteReaction(compliment.getId(), result.getId());

        assertEquals(0, complimentRepository.findById(compliment.getId()).getReactions().size());
    }

    @Test(expected = ResourceNotFoundException.class)
    @Transactional
    public void deleteReaction_reactionDoesNotExist() throws Exception {
        Compliment compliment = complimentController.getAll(null, null, 0, 10).getResults().get(0);
        assertNotNull(compliment);

        assertTrue("compliment should not have any reactions", compliment.getReactions() == null || compliment.getReactions().isEmpty());

        complimentController.deleteReaction(compliment.getId(), 1L);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteReaction_complimentDoesNotExist() throws Exception {
        long badId = 9123857029835L;
        assertNull("compliment should not exist in test data", complimentRepository.findOne(badId));
        //Should throw ResourceNotFoundException
        complimentController.deleteReaction(badId, 0L);
    }

    //@Test
    //TODO: implement once user making request is known
    public void deleteReaction_otherUserReceived() throws Exception {

    }
}