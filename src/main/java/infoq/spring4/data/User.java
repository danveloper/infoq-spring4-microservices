package infoq.spring4.data;

import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import java.util.Date;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue
    private long id;
    private String username;
    private Date createDate;
    @OneToMany
    @Cascade(CascadeType.ALL)
    private Set<Phone> phoneNumbers;
    @ElementCollection
    private Set<String> emailAddresses;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setPhoneNumbers(Set<Phone> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public Set<Phone> getPhoneNumbers() {
        return this.phoneNumbers;
    }

    public void setEmailAddresses(Set<String> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public Set<String> getEmailAddresses() {
        return this.emailAddresses;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof User)) {
            return false;
        }
        User otherUser = (User) other;
        return  this.username != null
                && this.id == otherUser.id
                && this.username.equals(otherUser.getUsername());
    }
}
