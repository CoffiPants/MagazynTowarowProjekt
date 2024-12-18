// src/main/java/org/example/magazyntowarowprojekt/ProduktRepository.java
package org.example.magazyntowarowprojekt;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class ProduktRepository {

    private EntityManagerFactory emf;
    private EntityManager em;

    public ProduktRepository() {
        emf = Persistence.createEntityManagerFactory("magazynPU");
        em = emf.createEntityManager();
    }

    public void addProdukt(Produkt produkt) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(produkt);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void close() {
        em.close();
        emf.close();
    }
}