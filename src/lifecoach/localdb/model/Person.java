package lifecoach.localdb.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import lifecoach.localdb.dao.PeopleDao;
import lifecoach.localdb.model.Measure;

@Entity  // indicates that this class is an entity to persist in DB
@Table(name="Person") // to whate table must be persisted
@NamedQuery(name="Person.findAll", query="SELECT p FROM Person p")
@XmlRootElement
public class Person implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    @Id // defines this attributed as the one that identifies the entity
    @GeneratedValue(generator="sqlite_person")
    @TableGenerator(name="sqlite_person", table="sqlite_sequence",
			        pkColumnName="name", valueColumnName="seq",
			        pkColumnValue="Person")
    
    @Column(name="idPerson") // maps the following attribute to a column
    private int idPerson;
    
    @Column(name="lastname")
    private String lastname;
    
    @Column(name="firstname")
    private String firstname;
    
    // TODO: Use type Date
    // @Temporal(TemporalType.DATE)
    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name="birthdate")
    //private Date birthdate; 
    private String birthdate;
    
    @Column(name="sex")
    private String sex;
    
    @Column(name="waist")
    private float waist;
    
    @Column(name="hip")
    private float hip;
    
    // MappedBy must be equal to the name of the attribute in Measure that maps this relation
    @OneToMany(mappedBy="person", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private List<Measure> measure;
       
    public int getIdPerson() {
		return idPerson;
	}

	public void setIdPerson(int id) {
		this.idPerson = id;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

//	public String getBirthdate() {
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		return df.format(this.birthdate);
//	}
	
	public String getBirthdate() {
		return birthdate;
	}

//	public void setBirthdate(String birthdate) throws ParseException {
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
//        this.birthdate = format.parse(birthdate);
//	}
	
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	
	public String getSex() {
		return firstname;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public float getWaist() {
		return waist;
	}

	public void setWaist(float waist) {
		this.waist = waist;
	}
	
	public float getHip() {
		return hip;
	}

	public void setHip(float hip) {
		this.hip = hip;
	}
	
	// Inherit elements
	@XmlElementWrapper(name = "healthProfile")
    public List<Measure> getMeasure() {
        return Measure.getLastMeasure(this.idPerson);
		//return measure;
    }
	
    public void setMeasure(List<Measure> measure) {
        this.measure = measure;
    }
    
    @XmlTransient
    public List<Measure> getAllMeasure() {
    	return measure;
    }
	
	// Database operations
	public static Person getPersonById(int personId) {
        EntityManager em = PeopleDao.instance.createEntityManager();
        Person p = em.find(Person.class, personId);
        PeopleDao.instance.closeConnections(em);
        return p;
    }

	public static List<Person> getAll() {
        EntityManager em = PeopleDao.instance.createEntityManager();
        List<Person> list = em.createNamedQuery("Person.findAll", Person.class)
            .getResultList();
        PeopleDao.instance.closeConnections(em);
        return list;
    }

    public static Person savePerson(Person p) {
        // Add idP to measures
    	List<Measure> mList = p.getAllMeasure();
    	
    	if(mList != null)
    	{
	    	for(Measure m: mList)
	    	{
	    		m.setPerson(p);
	    	}
    	}
    	
    	EntityManager em = PeopleDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(p);
        tx.commit();
        PeopleDao.instance.closeConnections(em);
        return p;
    } 

    public static Person updatePerson(Person p) {
        EntityManager em = PeopleDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        tx.commit();
        PeopleDao.instance.closeConnections(em);
        return p;
    }

    public static void removePerson(Person p) {
        EntityManager em = PeopleDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        em.remove(p);
        tx.commit();
        PeopleDao.instance.closeConnections(em);
    }
    
}