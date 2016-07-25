package org.romeo.compliments.domain;

import javax.persistence.*;

/**
 * User: tylerromeo
 * Date: 7/25/16
 * Time: 1:41 PM
 */
@Entity
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String reaction;

    public Reaction() {

    }

    public Reaction(Long id, String reaction) {
        this.id = id;
        this.reaction = reaction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reaction reaction1 = (Reaction) o;

        if (id != null ? !id.equals(reaction1.id) : reaction1.id != null) return false;
        return reaction != null ? reaction.equals(reaction1.reaction) : reaction1.reaction == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (reaction != null ? reaction.hashCode() : 0);
        return result;
    }
}
