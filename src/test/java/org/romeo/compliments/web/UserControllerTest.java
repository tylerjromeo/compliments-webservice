package org.romeo.compliments.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.romeo.compliments.MainApplication;
import org.romeo.compliments.persistence.UserRepository;
import org.romeo.compliments.web.domain.User;
import org.romeo.compliments.web.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

;

/**
 * User: tylerromeo
 * Date: 6/18/16
 * Time: 2:52 PM
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainApplication.class)
@WebIntegrationTest
public class UserControllerTest {

    @Autowired
    @InjectMocks
    UserController userController;

    @Mock
    UserRepository userRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAll() throws Exception {

    }

    @Test
    public void testGetById() throws Exception {
        long id = 12341234;
        org.romeo.compliments.persistence.domain.User testUser = mock(org.romeo.compliments.persistence.domain.User.class);
        when(userRepository.findOne(id)).thenReturn(testUser);
        User result = userController.getById(id);
        assertEquals(result, User.fromDbUser(testUser));
        verify(userRepository).findOne(id);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetById_IdNotFound() throws Exception {
        long id = 12341234;
        when(userRepository.findOne(id)).thenReturn(null);
        userController.getById(id);
        verify(userRepository).findAll();
        verify(userRepository).findOne(id);
    }
}