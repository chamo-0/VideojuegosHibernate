package org.example;

import jakarta.persistence.*;

@Entity
@Table(name="achievements")
public class achievements {

    @Id
    @Column(name="id")
    private int id;

   @ManyToOne
   @JoinColumn(name="game_id")
    private games gameId;

    @Column(name="name")
    private String nombre;

    @Column(name="description")
    private String descripcion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public games getGameId() {
        return gameId;
    }

    public void setGameId(games gameId) {
        this.gameId = gameId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "achievements{" +
                "id=" + id +
                ", gameId=" + gameId +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
