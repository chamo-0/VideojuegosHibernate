package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static SessionFactory sessionFactory;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("¡ERROR CRÍTICO! Fallo al conectar con Hibernate.");
            e.printStackTrace();
            return;
        }

        int opcion = 0;
        do {
            mostrarMenu();
            try {
                String input = sc.nextLine();
                if (!input.isEmpty()) {
                    opcion = Integer.parseInt(input);
                } else {
                    opcion = -1;
                }
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1:
                    agregarLogro();
                    break;
                case 2:
                    eliminarLogro();
                    break;
                case 3:
                    agregarGeneroAJuego();
                    break;
                case 4:
                    quitarGeneroDeJuego();
                    break;
                case 5:
                    agregarPlataformaAJuego();
                    break;
                case 6:
                    quitarPlataformaDeJuego();
                    break;
                case 7:
                    borrarPlataformaYJuegos();
                    break;
                case 8:
                    borrarJuegoYLogros();
                    break;
                case 9:
                    hqlLogrosPorNombreJuego();
                    break;
                case 10:
                    hqlJuegosAntesDeAnio();
                    break;
                case 11:
                    hqlJuegosPorPlataforma();
                    break;
                case 12:
                    hqlGenerosDel96();
                    break;

                case 0:
                    System.out.println("Cerrando sesión...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
            if (opcion != 0) {
                System.out.println("\n(Pulsa ENTER para volver al menú)");
                sc.nextLine();
            }

        } while (opcion != 0);

        sessionFactory.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n#############################################");
        System.out.println("      PRÁCTICA HIBERNATE - VIDEOJUEGOS");
        System.out.println("#############################################");
        System.out.println("--- MODIFICACIONES ---");
        System.out.println("1. Añadir un LOGRO a un juego");
        System.out.println("2. Eliminar un LOGRO de un juego");
        System.out.println("3. Añadir un GÉNERO a un juego");
        System.out.println("4. Quitar un GÉNERO de un juego");
        System.out.println("5. Añadir una PLATAFORMA a un juego");
        System.out.println("6. Quitar una PLATAFORMA de un juego");
        System.out.println("7. ¡PELIGRO! Borrar PLATAFORMA (y todos sus juegos)");
        System.out.println("8. Borrar JUEGO (y sus logros)");
        System.out.println("--- CONSULTAS HQL ---");
        System.out.println("9. Ver logros de juegos que empiecen por...");
        System.out.println("10. Ver juegos lanzados antes del año...");
        System.out.println("11. Ver juegos de una plataforma (ej. GameCube)");
        System.out.println("12. Estadísticas de géneros del año 1996");
        System.out.println("0. Salir");
        System.out.print(">>> Selecciona una opción: ");
    }
    private static void agregarLogro() {
        System.out.print("ID del Juego: ");
        int gameId = Integer.parseInt(sc.nextLine());

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        // Usamos 'games.class' en minúscula
        games juego = session.get(games.class, gameId);
        if (juego != null) {
            System.out.print("Nombre del Logro: ");
            String nombre = sc.nextLine();
            System.out.print("Descripción: ");
            String desc = sc.nextLine();

            // Usamos 'achievements' en minúscula
            achievements logro = new achievements();
            logro.setNombre(nombre);
            logro.setDescripcion(desc);
            logro.setGameId(juego); // Relación ManyToOne

            session.save(logro);
            tx.commit();
            System.out.println("Logro añadido a " + juego.getNombre());
        } else {
            System.out.println("Juego no encontrado.");
        }
        session.close();
    }

    private static void eliminarLogro() {
        System.out.print("ID del Logro a borrar: ");
        int achId = Integer.parseInt(sc.nextLine());

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        achievements logro = session.get(achievements.class, achId);
        if (logro != null) {
            session.delete(logro);
            tx.commit();
            System.out.println("Logro eliminado.");
        } else {
            System.out.println("Logro no encontrado.");
        }
        session.close();
    }

    private static void agregarGeneroAJuego() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        System.out.print("ID del Juego: ");
        games juego = session.get(games.class, Integer.parseInt(sc.nextLine()));
        System.out.print("ID del Género a añadir: ");
        genres genero = session.get(genres.class, Integer.parseInt(sc.nextLine()));

        if (juego != null && genero != null) {
            // Asegúrate de tener getGenres() en tu clase 'games'
            juego.getGenres().add(genero);
            session.update(juego);
            tx.commit();
            System.out.println("Género " + genero.getName() + " añadido a " + juego.getNombre());
        } else {
            System.out.println("Juego o Género no encontrados.");
        }
        session.close();
    }

    private static void quitarGeneroDeJuego() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        System.out.print("ID del Juego: ");
        games juego = session.get(games.class, Integer.parseInt(sc.nextLine()));

        System.out.print("ID del Género a quitar: ");
        int idGenero = Integer.parseInt(sc.nextLine());

        if (juego != null) {
            // Eliminar de la lista usando lambda
            boolean eliminado = juego.getGenres().removeIf(g -> g.getId() == idGenero);

            if (eliminado) {
                session.update(juego);
                tx.commit();
                System.out.println("Género desvinculado.");
            } else {
                System.out.println("Ese juego no tenía ese género asignado.");
            }
        }
        session.close();
    }

    private static void agregarPlataformaAJuego() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        System.out.print("ID del Juego: ");
        games juego = session.get(games.class, Integer.parseInt(sc.nextLine()));
        System.out.print("ID de la Plataforma: ");
        platforms plat = session.get(platforms.class, Integer.parseInt(sc.nextLine()));

        if (juego != null && plat != null) {
            // 1. COMPROBACIÓN: ¿El juego ya tiene esta plataforma?
            boolean yaLaTiene = false;
            for (platforms p : juego.getPlatforms()) {
                if (p.getId() == plat.getId()) {
                    yaLaTiene = true;
                    break;
                }
            }

            // 2. LÓGICA: Solo añadimos si NO la tiene
            if (yaLaTiene) {
                System.out.println("⚠️ ¡Cuidado! El juego YA tiene esa plataforma asignada.");
            } else {
                juego.getPlatforms().add(plat);
                session.update(juego);
                tx.commit();
                System.out.println("✅ Plataforma añadida correctamente.");
            }
        } else {
            System.out.println("❌ ID de juego o plataforma incorrecto.");
        }
        session.close();
    }

    private static void quitarPlataformaDeJuego() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        System.out.print("ID del Juego: ");
        games juego = session.get(games.class, Integer.parseInt(sc.nextLine()));
        System.out.print("ID de la Plataforma a desvincular: ");
        int idPlat = Integer.parseInt(sc.nextLine());

        if (juego != null) {
            boolean removed = juego.getPlatforms().removeIf(p -> p.getId() == idPlat);
            if (removed) {
                session.update(juego);
                tx.commit();
                System.out.println("Plataforma desvinculada del juego.");
            } else {
                System.out.println("El juego no estaba en esa plataforma.");
            }
        }
        session.close();
    }

    private static void borrarPlataformaYJuegos() {
        System.out.println("ATENCIÓN: Esto borrará la plataforma y TODOS los juegos asociados.");
        System.out.print("Introduce ID de Plataforma para confirmar: ");
        int idPlat = Integer.parseInt(sc.nextLine());

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        platforms plat = session.get(platforms.class, idPlat);
        if (plat != null) {
            System.out.println("Vas a borrar: " + plat.getNombre());
            // Requiere cascade=CascadeType.REMOVE en platforms.java
            session.delete(plat);

            tx.commit();
            System.out.println("Plataforma y sus juegos eliminados.");
        } else {
            System.out.println("Plataforma no existe.");
        }
        session.close();
    }

    private static void borrarJuegoYLogros() {
        System.out.print("ID del Juego a borrar: ");
        int idGame = Integer.parseInt(sc.nextLine());

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        games juego = session.get(games.class, idGame);
        if (juego != null) {
            // Requiere cascade=ALL en achievements dentro de games.java
            session.delete(juego);
            tx.commit();
            System.out.println("Juego y sus logros eliminados.");
        }
        session.close();
    }

    private static void hqlLogrosPorNombreJuego() {
        System.out.print("Introduce inicio del nombre del juego (ej. Mario): ");
        String nombre = sc.nextLine();

        Session session = sessionFactory.openSession();
        // HQL sensible a mayúsculas/minúsculas en nombres de clase
        String hql = "FROM achievements a WHERE a.gameId.nombre LIKE :patron";

        Query<achievements> q = session.createQuery(hql, achievements.class);
        q.setParameter("patron", nombre + "%");

        List<achievements> lista = q.list();
        System.out.println("--- LOGROS ENCONTRADOS ---");
        for (achievements a : lista) {
            System.out.println("Juego: " + a.getGameId().getNombre() + " | Logro: " + a.getNombre());
        }
        session.close();
    }

    private static void hqlJuegosAntesDeAnio() {
        System.out.print("Introduce el año límite (ej. 2000): ");

        int anio = Integer.parseInt(sc.nextLine());

        Session session = sessionFactory.openSession();

        String hql = "FROM games g WHERE cast(year(g.fechaSalida) as int) < :anio";

        Query<games> q = session.createQuery(hql, games.class);
        q.setParameter("anio", anio);

        List<games> lista = q.list();
        System.out.println("--- JUEGOS ENCONTRADOS ---");
        if(lista.isEmpty()) {
            System.out.println("No se encontraron juegos anteriores a " + anio);
        }
        for (games g : lista) {
            System.out.println(g.getNombre() + " (" + g.getFechaSalida() + ")");
        }
        session.close();
    }

    private static void hqlJuegosPorPlataforma() {
        System.out.print("Nombre exacto de la plataforma (ej. PlayStation): ");
        String platNombre = sc.nextLine();

        Session session = sessionFactory.openSession();
        // 'games' en minúscula
        String hql = "SELECT g FROM games g JOIN g.platforms p WHERE p.nombre = :plat";

        Query<games> q = session.createQuery(hql, games.class);
        q.setParameter("plat", platNombre);

        List<games> lista = q.list();
        if(lista.isEmpty()) System.out.println("No se encontraron juegos o la plataforma no existe.");

        for (games g : lista) {
            System.out.println("- " + g.getNombre());
        }
        session.close();
    }

    private static void hqlGenerosDel96() {
        Session session = sessionFactory.openSession();

        System.out.println("--- Géneros distintos en 1996 ---");
        // CORRECCIÓN 1: Añadimos cast(... as int)
        String hql1 = "SELECT DISTINCT gen.name FROM genres gen JOIN gen.games g WHERE cast(year(g.fechaSalida) as int) = 1996";

        List<String> generos = session.createQuery(hql1, String.class).list();
        if (generos.isEmpty()) {
            System.out.println("No se encontraron géneros para 1996.");
        } else {
            generos.forEach(System.out::println);
        }

        System.out.println("\n--- Conteo de juegos por género en 1996 ---");
        // CORRECCIÓN 2: Añadimos cast(... as int) aquí también
        String hql2 = "SELECT gen.name, count(g) FROM genres gen JOIN gen.games g WHERE cast(year(g.fechaSalida) as int) = 1996 GROUP BY gen.name";

        List<Object[]> resultados = session.createQuery(hql2, Object[].class).list();

        for (Object[] fila : resultados) {
            System.out.println("Género: " + fila[0] + " | Cantidad: " + fila[1]);
        }
        session.close();
    }
}