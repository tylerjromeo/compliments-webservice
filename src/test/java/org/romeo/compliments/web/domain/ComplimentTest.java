package org.romeo.compliments.web.domain;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * User: tylerromeo
 * Date: 6/18/16
 * Time: 7:39 PM
 */
public class ComplimentTest {

    @Test
    public void testFromDbCompliment() throws Exception {
        Long id = 1235L;
        org.romeo.compliments.persistence.domain.User to = new org.romeo.compliments.persistence.domain.User();
        to.setId(123412341L);
        org.romeo.compliments.persistence.domain.User from = new org.romeo.compliments.persistence.domain.User();
        from.setId(594522345L);
        String content = "beedley deedley";
        Date sendDate = new Date();
        org.romeo.compliments.persistence.domain.Compliment dbCompliment = new org.romeo.compliments.persistence.domain.Compliment();
        dbCompliment.setId(id);
        dbCompliment.setFrom(from);
        dbCompliment.setTo(to);
        dbCompliment.setContents(content);
        dbCompliment.setSendDate(sendDate);

        Compliment converted = Compliment.fromDbCompliment(dbCompliment);
        assertEquals(id, converted.getId());
        assertEquals(to.getId(), converted.getToId());
        assertEquals(from.getId(), converted.getFromId());
        assertEquals(content, converted.getContents());
        assertEquals(sendDate, converted.getSendDate());
    }
}