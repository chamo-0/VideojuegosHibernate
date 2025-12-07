package org.example;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "games")
public class games {
    @Id
    @Column(name="id")
    private int id;

    @Column(name="slug")
    private String slug;

    @Column(name="name")
    private String nombre;

    @Column(name="released_date")
    @Temporal(TemporalType.DATE)
    private Date fechaSalida;

    @Column(name="rating")
    private float calificacion;

    // Relación con Logros (Cascade para borrar logros si borras juego)
    @OneToMany(mappedBy = "gameId", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<achievements> achievements;

    // Relación con Plataformas
    @ManyToMany
    @JoinTable(
            name="gameplatforms", // CORREGIDO: Usamos la tabla del enunciado (tienes 2 en la BD)
            joinColumns = @JoinColumn(name="game_id"),
            inverseJoinColumns = @JoinColumn(name="platform_id")
    )
    private List<platforms> platforms;

    // Relación con Géneros
    @ManyToMany
    @JoinTable(
            name="gamegenres",
            joinColumns = @JoinColumn(name="game_id"),
            // CORREGIDO: En tu BD la columna es 'genre_id' (singular), no 'genres_id'
            inverseJoinColumns = @JoinColumn(name="genre_id")
    )
    private List<genres> genres;

    // --- GETTERS Y SETTERS ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(float calificacion) {
        this.calificacion = calificacion;
    }

    public List<achievements> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<achievements> achievements) {
        this.achievements = achievements;
    }

    public List<platforms> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<platforms> platforms) {
        this.platforms = platforms;
    }

    // ESTOS SON LOS QUE TE FALTABAN Y CAUSABAN EL ERROR ROJO
    public List<genres> getGenres() {
        return genres;
    }

    public void setGenres(List<genres> genres) {
        this.genres = genres;
    }

    @Override
    public String toString() {
        return "games{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}