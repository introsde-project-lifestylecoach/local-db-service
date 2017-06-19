package lifecoach.localdb.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import lifecoach.localdb.dao.PeopleDao;
import lifecoach.localdb.model.Person;
import lifecoach.localdb.model.MeasureType;
import lifecoach.localdb.model.GoalType;

@Entity  // indicates that this class is an entity to persist in DB
@Table(name="Goal") // to whate table must be persisted
@NamedQuery(name="Goal.findAll", query="SELECT p FROM Goal p")
@XmlRootElement
public class Goal implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    @Id // defines this attributed as the one that identifies the entity
    @GeneratedValue(generator="sqlite_goal")
	@TableGenerator(name="sqlite_goal", table="sqlite_sequence",
				    pkColumnName="name", valueColumnName="seq",
				    pkColumnValue="Goal")
    @Column(name="idGoal") // maps the following attribute to a column
    private int idGoal;
    
    @Column(name="value")
    private float value;
    
    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    // TODO: Use type Date
    // @Temporal(TemporalType.DATE)
	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name="date")
    // private Date date;
    private String date;
      
    @ManyToOne(fetch=FetchType.LAZY)
   	@JoinColumn(name="idPerson")
   	private Person person;
    
    @ManyToOne(fetch=FetchType.LAZY)
   	@JoinColumn(name="idMeasureType", insertable = true, updatable = true)
   	private MeasureType measureType;
    
    @ManyToOne(fetch=FetchType.LAZY)
   	@JoinColumn(name="idGoalType", insertable = true, updatable = true)
   	private GoalType goalType;
    
    
    // add below all the getters and setters of all the private attributes   
    public int getIdGoal() {
		return idGoal;
	}

	public void setIdGoal(int id) {
		this.idGoal = id;
	}

	public void setTitle(String title){
		this.title = title;	
	}

	public void setDescription(String description){
		this.description = description;	
	}

	public String getTitle(){
		return title;	
	}

	public String getDescription(){
		return description;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

//	public String getDate() {
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		return df.format(this.date);
//	}
	
	public String getDate() {
		return date;
	}

//	public void setDate(String date) throws ParseException {
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//        this.date = format.parse(date);
//	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	// Transient for JAXB to avoid and infinite loop on serialization
	@XmlTransient
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	public MeasureType getMeasureType() {
		return measureType;
	}

	public void setMeasureType(MeasureType measureType) {
		this.measureType = measureType;
	}
	
	public GoalType getGoalType() {
		return goalType;
	}

	public void setGoalType(GoalType goalType) {
		this.goalType = goalType;
	}
	
	// Database operations
	public static Goal getGoalById(int personId) {
        EntityManager em = PeopleDao.instance.createEntityManager();
        Goal p = em.find(Goal.class, personId);
        PeopleDao.instance.closeConnections(em);
        return p;
    }

	public static List<Goal> getAll() {
        EntityManager em = PeopleDao.instance.createEntityManager();
        List<Goal> list = em.createNamedQuery("Goal.findAll", Goal.class)
            .getResultList();
        PeopleDao.instance.closeConnections(em);
        return list;
    }

    public static Goal saveGoal(Goal p) {
        EntityManager em = PeopleDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(p);
        tx.commit();
        PeopleDao.instance.closeConnections(em);
        return p;
    } 

    public static Goal updateGoal(Goal p) {
        EntityManager em = PeopleDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        tx.commit();
        PeopleDao.instance.closeConnections(em);
        return p;
    }

    public static void removeGoal(Goal p) {
        EntityManager em = PeopleDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        em.remove(p);
        tx.commit();
        PeopleDao.instance.closeConnections(em);
    }
    
    // Special methods
    public static List<Goal> getGoalByPidAndType(int pId, String type) {
		EntityManager em = PeopleDao.instance.createEntityManager();
				
		String query = "SELECT g FROM Goal g WHERE g.person.idPerson = " + pId 
				+ " AND g.measureType.idMeasureType = (SELECT mT.idMeasureType FROM MeasureType mT WHERE mT.type = \"" + type + "\")";
				
		// System.out.println(query);
		
		List<Goal> goal = em.createQuery(query, Goal.class).getResultList();
		
		PeopleDao.instance.closeConnections(em);
		return goal;
	}
    
    public static List<Goal> getGoalByGidAndType(int pId, int gId, String type)
	{
		EntityManager em = PeopleDao.instance.createEntityManager();
		
		String query = "SELECT g FROM Goal g WHERE g.person.idPerson = " + pId + " AND g.idGoal = " + gId
				+ " AND g.measureType.idMeasureType = (SELECT mT.idMeasureType FROM MeasureType mT WHERE mT.type = \"" + type + "\")";
				
		// System.out.println(query);
		
		List<Goal> goal = em.createQuery(query, Goal.class).getResultList();

		PeopleDao.instance.closeConnections(em);
		return goal;
	}

     public static List<Goal> getGoalByTitle(String title){

		EntityManager em = PeopleDao.instance.createEntityManager();
		
		String query = "SELECT g FROM Goal g WHERE g.person.title = \"" + title + "\")";
				
		// System.out.println(query);
		
		List<Goal> goal = em.createQuery(query, Goal.class).getResultList();

		PeopleDao.instance.closeConnections(em);
		return goal;
	}

    
}
