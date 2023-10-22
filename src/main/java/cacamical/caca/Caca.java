package cacamical.caca;

import java.util.Date;

import cacamical.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.PrePersist;


@Entity
public class Caca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cacaId")
    private Long cacaId;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "description")
    private String description;

    @Column(name = "note")
    private int note;

    @Column(name = "photo")
    private String photo;

    @Column(name = "titre")
    private String titre;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateAjout")
    private Date dateAjout;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user; // Many Cacas belong to one User

    // Constructors, getters, and setters

    public Caca() {
        // Default constructor
    }

    public Caca(Double latitude, Double longitude, String description, int note, String photo, String titre, Date dateAjout, User user) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.note = note;
        this.photo = photo;
        this.titre = titre;
        this.user = user;
        this.dateAjout = dateAjout;
    }

    // Getters and setters for the fields

    public Long getCacaId() {
        return cacaId;
    }

    public void setCacaId(Long cacaId) {
        this.cacaId = cacaId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Date getDateAjout() {
        return dateAjout;
    }
    
    public void setDateAjout(Date dateAjout) {
        this.dateAjout = dateAjout;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @PrePersist
    public void beforeSave() {
        dateAjout = new Date();
    }
}