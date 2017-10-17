/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package heps.db.param_list.exl2db;

import heps.db.param_list.api.ParameterAPI;
import heps.db.param_list.entity.Attribute;
import heps.db.param_list.entity.Data;

import heps.db.param_list.entity.Manager;
import heps.db.param_list.entity.Parameter;
import heps.db.param_list.entity.Reference;
import heps.db.param_list.entity.Team;

import heps.db.param_list.entity.Unit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author C.M.P
 */
public class Param2DBT {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("heps-db-param_listPU");
    EntityManager em = emf.createEntityManager();
    SimpleDateFormat formatter = new SimpleDateFormat("M-dd-yy", Locale.ENGLISH);
    Date date = new Date();

    public void instDB(Workbook wb, String sheetName, String created_by, Date create_date) throws ParseException {
        ReadSheet readSheet = new ReadSheet();

        ArrayList paramList = readSheet.getParamList(wb, sheetName);

        for (int i = 0; i < paramList.size(); i++) {
            ParameterAPI p = (ParameterAPI) paramList.get(i);

            //System.out.println(p);
            //System.out.println("i:" + i);
        }

        em.getTransaction().begin();
        em.createQuery("DELETE FROM Data d").executeUpdate();
        em.createQuery("DELETE FROM Team t").executeUpdate();
        em.createQuery("DELETE FROM Manager m").executeUpdate();
        em.createQuery("DELETE FROM System s").executeUpdate();
        em.createQuery("DELETE FROM Attribute a").executeUpdate();
        em.createQuery("DELETE FROM Parameter p").executeUpdate();
        em.createQuery("DELETE FROM Unit u").executeUpdate();
        em.createQuery("DELETE FROM Reference r").executeUpdate();
        em.getTransaction().commit();

        em.getTransaction().begin();

        for (int i = 0; i < paramList.size(); i++) {

            ParameterAPI p = (ParameterAPI) paramList.get(i);

            Data data = new Data();
            data.setValue(p.getData());
            data.setDatemodified(p.getDate());
            data.setComment(p.getDef());
            data.setStatus("");

            try {
                Attribute att = em.createQuery("SELECT a FROM Attribute a WHERE a.name =:name", Attribute.class).setParameter("name", p.getAtt().replaceAll("\\s*", "")).getSingleResult();

                data.setAttribute(att);
            } catch (NoResultException e1) {
                Attribute att = new Attribute();
                att.setName(p.getAtt());
                data.setAttribute(att);
            }

            try {
                heps.db.param_list.entity.System sys = em.createQuery("SELECT s FROM heps.db.param_list.entity.System s WHERE s.name = :name", heps.db.param_list.entity.System.class).setParameter("name", p.getSys()).getSingleResult();
                data.setSystem(sys);
            } catch (NoResultException e2) {
                heps.db.param_list.entity.System sys = new heps.db.param_list.entity.System();
                sys.setName(p.getSys());
                try {
                    heps.db.param_list.entity.System parent_sys = em.createQuery("SELECT s FROM heps.db.param_list.entity.System s WHERE s.name = :name", heps.db.param_list.entity.System.class).setParameter("name", p.getParent_sys()).getSingleResult();
                    sys.setParentid(parent_sys);
                } catch (NoResultException e21) {
                    heps.db.param_list.entity.System parent_sys = new heps.db.param_list.entity.System();
                    parent_sys.setName(p.getParent_sys());
                    sys.setParentid(parent_sys);
                }
                data.setSystem(sys);
            }

            try {
                Team team = em.createQuery("SELECT t FROM Team t WHERE t.name = :name", Team.class).setParameter("name", p.getTeam()).getSingleResult();
                data.setTeam(team);

            } catch (NoResultException e3) {
                Team team = new Team();
                team.setName(p.getTeam());
                try {
                    Team parent_team = em.createQuery("SELECT t FROM Team t WHERE t.name =:name", Team.class).setParameter("name", p.getParent_team()).getSingleResult();
                    team.setParentid(parent_team);

                } catch (NoResultException e31) {
                    Team parent_team = new Team();
                    parent_team.setName(p.getParent_team());
                    team.setParentid(parent_team);
                }
                try {
                    Manager manager = em.createQuery("SELECT m FROM Manager m WHERE m.name = :name", Manager.class).setParameter("name", p.getTeam_manager()).getSingleResult();
                    team.setManagerid(manager);
                } catch (NoResultException e32) {
                    Manager manager = new Manager();
                    manager.setName(p.getTeam_manager());
                    team.setManagerid(manager);
                }
                data.setTeam(team);
            }

            try {
                Parameter param = em.createQuery("SELECT p FROM Parameter p WHERE p.name = :name", Parameter.class).setParameter("name", p.getParam_name().replaceAll("\\s*", "")).getSingleResult();
                //System.out.println("parameter name"+parameter.getName());
                data.setParameter(param);
            } catch (NoResultException e4) {
               // System.out.println("主键参数名查找不到，将重新创建");
                Parameter param = new Parameter();
                param.setName(p.getParam_name());
                param.setDatemodified(p.getDate());
                param.setDefinition(p.getDef());
                try {
                    Unit unit = em.createQuery("SELECT u FROM Unit u WHERE u.name = :name", Unit.class).setParameter("name", p.getUnit()).getSingleResult();
                    param.setUnitid(unit);
                    //y++;
                    //System.out.println("重复Unit："+unit.getName()+"y:"+y);

                } catch (NoResultException e41) {
                    // System.out.println("Unit:"+e1.getMessage());
                    Unit unit = new Unit();
                    unit.setName(p.getUnit());
                    param.setUnitid(unit);
                    //System.out.println("unit success!" + unit.getName() + unit.getId());
                }

                try {
                    Reference ref = em.createQuery("SELECT r FROM Reference r WHERE r.title = :title", Reference.class).setParameter("title", p.getRef_title()).getSingleResult();
                    param.setReferenceid(ref);
                    //z++;
                } catch (NoResultException e42) {
                    Reference ref = new Reference();
                    ref.setTitle(p.getRef_title());
                    ref.setAuthor(p.getRef_author());
                    ref.setPublication(p.getRef_publication());
                    ref.setUrl(p.getRef_url());
                    ref.setKeywords(p.getKeyword());
                    param.setReferenceid(ref);
                }
                data.setParameter(param);

            }

            em.persist(data);

        }
        em.getTransaction().commit();
        em.close();
        emf.close();

    }

}
