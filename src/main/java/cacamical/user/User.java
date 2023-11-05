package cacamical.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class User {

    @Id
    @Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_creation")
    private Date dateCreation;
    
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToMany
    @JoinTable(
        name = "user_amitie",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "ami_id")
    )
    @JsonIgnore
    private Set<User> amis = new HashSet<>(); // Represents the friends of the user

    // Constructors, getters, and setters

    public User() {
        // Default constructor
    }

    public User(String nom, String prenom, Date dateCreation, String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
    }
    
    @PrePersist
    protected void onCreate() {
        dateCreation = new Date();
    }
    // Getters and setters for the fields

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<User> getAmis() {
        return amis;
    }

    public void setAmis(Set<User> amis) {
        this.amis = amis;
    }

    public void addAmi(User friend) {
        this.amis.add(friend);
        friend.getAmis().add(this);
    }
    
    public void removeAmi(User friend) {
        this.amis.remove(friend);
        friend.getAmis().remove(this);
    }


}