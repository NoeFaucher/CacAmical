package cacamical.commentaire;

import jakarta.persistence.*;

import java.util.Date;

import cacamical.caca.Caca;
import cacamical.user.User;

@Entity
public class Commentaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentaireId")
    private Long commentaireId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "cacaId")
    private Caca caca;

    @Column(name = "contenu")
    private String contenu;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dateAjout")
    private Date dateAjout;

    // Constructors, getters, and setters

    public Commentaire() {
        // Default constructor
    }

    public Commentaire(User user, Caca caca, String contenu, Date dateAjout) {
        this.user = user;
        this.caca = caca;
        this.contenu = contenu;
        this.dateAjout = dateAjout;
    }

    // Getters and setters for the fields

    public Long getCommentaireId() {
        return commentaireId;
    }

    public void setCommentaireId(Long commentaireId) {
        this.commentaireId = commentaireId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Caca getCaca() {
        return caca;
    }

    public void setCaca(Caca caca) {
        this.caca = caca;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(Date dateAjout) {
        this.dateAjout = dateAjout;
    }

    @PrePersist
    public void beforeSave() {
        dateAjout = new Date();
    }
}
