package lifecoach.localdb.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import lifecoach.localdb.dao.LifeCoachDao;
import lifecoach.localdb.model.Goal;

@Entity  // indicates that this class is an entity to persist in DB
@Table(name="GoalType") // to whate table must be persisted
@NamedQuery(name="GoalType.findAll", query="SELECT p FROM GoalType p")
@XmlRootElement
public class GoalType implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    @Id // defines this attributed as the one that identifies the entity
    @GeneratedValue(generator="sqlite_goaltype")
	@TableGenerator(name="sqlite_goaltype", table="sqlite_sequence",
				    pkColumnName="name", valueColumnName="seq",
				    pkColumnValue="GoalType")
    
    @Column(name="idGoalType") // maps the following attribute to a column
    private int idGoalType;
    
    @Column(name="type")
    private String type;
    
    // MappedBy must be equal to the name of the attribute in Measure that maps this relation
    @OneToMany(mappedBy="goalType")
    private List<Goal> goal;
       
    public int getIdGoalType() {
		return idGoalType;
	}

	public void setIdGoalType(int id) {
		this.idGoalType = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	// Transient for JAXB to avoid and infinite loop on serialization
	@XmlTransient
    public List<Goal> getGoal() {
        return goal;
    }
    public void setGoal(List<Goal> goal) {
        this.goal = goal;
    }
	
	// Database operations
	public static GoalType getGoalTypeById(int goalId) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        GoalType p = em.find(GoalType.class, goalId);
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

	public static List<GoalType> getAll() {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        List<GoalType> list = em.createNamedQuery("GoalType.findAll", GoalType.class)
            .getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }

    public static GoalType saveGoalType(GoalType p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    } 

    public static GoalType updateGoalType(GoalType p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }

    public static void removeGoalType(GoalType p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        em.remove(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
    }
    
}
