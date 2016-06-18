package org.romeo.compliments.web.domain;

import org.junit.Test;
import org.romeo.compliments.persistence.domain.*;
import org.romeo.compliments.persistence.domain.Compliment;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * User: tylerromeo
 * Date: 6/18/16
 * Time: 7:39 PM
 */
public class UserTest {

    @Test
    public void testFromDbUser() throws Exception {
        long id = 1235l;
        String name = "testName";
        String email = "testEmail";
        String imageUrl = "imageUrl";
        List<Compliment> complimentsSent = new ArrayList<Compliment>();
        List<Compliment> complimentsReceived = new ArrayList<Compliment>();

        Compliment c1 = new Compliment();
        Compliment c2 = new Compliment();
        Compliment c3 = new Compliment();
        Compliment c4 = new Compliment();
        complimentsSent.add(c1);
        complimentsReceived.add(c2);
        complimentsReceived.add(c3);
        complimentsReceived.add(c4);

        org.romeo.compliments.persistence.domain.User dbUser = new org.romeo.compliments.persistence.domain.User();
        dbUser.setId(id);
        dbUser.setName(name);
        dbUser.setEmail(email);
        dbUser.setImageUrl(imageUrl);
        dbUser.setComplimentsSent(complimentsSent);
        dbUser.setComplimentsReceived(complimentsReceived);

        User webUser = User.fromDbUser(dbUser);
        assertEquals(id, webUser.getId());
        assertEquals(name, webUser.getName());
        assertEquals(email, webUser.getEmail());
        assertEquals(imageUrl, webUser.getImageUrl());
        assertEquals(complimentsSent.size(), webUser.getComplimentsSent());
        assertEquals(complimentsReceived.size(), webUser.getComplimentsReceived());

    }
}