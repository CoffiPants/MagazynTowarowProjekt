package org.example.magazyntowarowprojekt;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class ProduktDAO {

    private SessionFactory sessionFactory;

    public ProduktDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void dodajProdukt(Produkt produkt) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(produkt);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Produkt> pobierzWszystkieProdukty() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Produkt", Produkt.class).list();
        }
    }

    // Usunięcie produktu
    public void usunProdukt(int id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Produkt produkt = session.get(Produkt.class, id);
            if (produkt != null) {
                session.delete(produkt);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
