/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package heps.db.param_list.db2exl;

import heps.db.param_list.entity.Data;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

/**
 *
 * @author Administrator
 */
public class DataJpaController {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("heps-db-param_listPU");
    EntityManager em = emf.createEntityManager();

    public List<Data> findDataEntities() {

        try {
            return em.createQuery("select d from Data d join d.parameter p join d.team t join d.system s join d.attribute a order by p").getResultList();

        } finally {
            em.close();
        }
    }

}
