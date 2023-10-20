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

@Entity
public class Caca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cacaId")
    private Long cacaId;

    @Column(name = "pointGps")
    private String pointGps;

    @Column(name = "description")
    private String description;

    @Column(name = "note")
    private int note;

    @Column(name = "photo")
    private String photo;

    @Column(name = "titre")
    private String titre;

    @Column(name = "dateAjout")
    private Date dateAjout;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user; // Many Cacas belong to one User

    // Constructors, getters, and setters

    public Caca() {
        // Default constructor
    }

    public Caca(String pointGps, String description, int note, String photo, String titre, Date dateAjout, User user) {
        this.pointGps = pointGps;
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

    public String getPointGps() {
        return pointGps;
    }

    public void setPointGps(String pointGps) {
        this.pointGps = pointGps;
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
}