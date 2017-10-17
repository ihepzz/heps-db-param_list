/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package heps.db.param_list.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Persistence;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C.M.P
 */
@Entity
@Table(name = "data")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Data.findAll", query = "SELECT d FROM Data d")
    , @NamedQuery(name = "Data.findById", query = "SELECT d FROM Data d WHERE d.id  = :id")
    , @NamedQuery(name = "Data.findByTeamid", query = "SELECT d FROM Data d WHERE d.team = :team")
    , @NamedQuery(name = "Data.findBySystemid", query = "SELECT d FROM Data d WHERE d.system = :system")
    , @NamedQuery(name = "Data.findByAttributeid", query = "SELECT d FROM Data d WHERE d.attribute = :attribute")
    , @NamedQuery(name = "Data.findByParameterid", query = "SELECT d FROM Data d WHERE d.parameter = :parameter")
    , @NamedQuery(name = "Data.findByValue", query = "SELECT d FROM Data d WHERE d.value = :value")
    , @NamedQuery(name = "Data.findByStatus", query = "SELECT d FROM Data d WHERE d.status = :status")
    , @NamedQuery(name = "Data.findByDatemodified", query = "SELECT d FROM Data d WHERE d.datemodified = :datemodified")
    , @NamedQuery(name = "Data.findByComment", query = "SELECT d FROM Data d WHERE d.comment = :comment")})
public class Data implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Column(name = "Value")
    private String value;
    @Column(name = "Status")
    private String status;
    @Column(name = "Date_modified")
    @Temporal(TemporalType.DATE)
    private Date datemodified;
    @Column(name = "Comment")
    private String comment;
    @JoinColumn(name = "Attribute_id", referencedColumnName = "Id")
    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    private Attribute attribute;
    @JoinColumn(name = "Parameter_id", referencedColumnName = "Id")
    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    private Parameter parameter;
    @JoinColumn(name = "System_id", referencedColumnName = "Id")
    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    private System system;
    @JoinColumn(name = "Team_id", referencedColumnName = "Id")
    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    private Team team;

    public Data() {
    }

   

    public Data(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

   

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDatemodified() {
        return datemodified;
    }

    public void setDatemodified(Date datemodified) {
        this.datemodified = datemodified;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public System getSystem() {
        return system;
    }

    public void setSystem(System system) {
        this.system = system;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
    
    
 /*
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Data other = (Data) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Data{" + "id=" + id + ", value=" + value + ", status=" + status + ", datemodified=" + datemodified + ", comment=" + comment + ", attribute=" + attribute + ", parameter=" + parameter + ", system=" + system + ", team=" + team + '}';
    }

   
    public void persist(Object object) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("heps-db-param_listPU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
**/

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Data other = (Data) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Data{" + "id=" + id + ", value=" + value + ", status=" + status + ", datemodified=" + datemodified + ", comment=" + comment + ", attribute=" + attribute + ", parameter=" + parameter + ", system=" + system + ", team=" + team + '}';
    }
    
}
