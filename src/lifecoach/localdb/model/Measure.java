package lifecoach.localdb.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import lifecoach.localdb.dao.PeopleDao;
import lifecoach.localdb.model.Person;
import lifecoach.localdb.model.MeasureType;

@Entity  // indicates that this class is an entity to persist in DB
@Table(name="Measure") // to whate table must be persisted
@NamedQuery(name="Measure.findAll", query="SELECT p FROM Measure p")
@XmlRootElement
public class Measure implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    @Id // defines this attributed as the one that identifies the entity
    @GeneratedValue(generator="sqlite_measure")
	@TableGenerator(name="sqlite_measure", table="sqlite_sequence",
				    pkColumnName="name", valueColumnName="seq",
				    pkColumnValue="Measure")
    @Column(name="idMeasure") // maps the following attribute to a column
    private int idMeasure;
    
    @Column(name="value")
    private float value;
    
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
    
    
    // add below all the getters and setters of all the private attributes   
    public int getIdMeasure() {
		return idMeasure;
	}

	public void setIdMeasure(int id) {
		this.idMeasure = id;
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
	
	public static List<Measure> getLastMeasure(int id)
	{
		EntityManager em = PeopleDao.instance.createEntityManager();

		List<Measure> measure = null;
		List<Measure> tmp = null;
		List<MeasureType> types = em.createQuery("SELECT m FROM MeasureType m", MeasureType.class).getResultList();
		
		for(MeasureType type : types)
		{
			int idT = type.getIdMeasureType();
				
			String query = "SELECT mA FROM Measure mA WHERE mA.person.idPerson = " + id + " AND mA.measureType.idMeasureType = " + idT
					+" AND mA.date = (SELECT MAX(mB.date) FROM Measure mB WHERE mB.person.idPerson = " + id + " AND mB.measureType.idMeasureType = " + idT + ")";
			
			// System.out.println(query);
			
			tmp = em.createQuery(query, Measure.class).getResultList();
			
			for(int i = 0; i < tmp.size(); i++)
			{
				if(measure == null)
				{
					measure = tmp;
				}
				else
				{
					measure.add(tmp.get(i));					
				}
			}
		}
		
		PeopleDao.instance.closeConnections(em);
		return measure;
	}
	
	public static Measure getLastMeasureByType(int id, String type)
	{
		EntityManager em = PeopleDao.instance.createEntityManager();

		List<Measure> measure = null;
		Measure m = null;
		
		int idT = MeasureType.getIdMeasureTypeByType(type);
				
		String query = "SELECT mA FROM Measure mA WHERE mA.person.idPerson = " + id + " AND mA.measureType.idMeasureType = " + idT
				+" AND mA.date = (SELECT MAX(mB.date) FROM Measure mB WHERE mB.person.idPerson = " + id + " AND mB.measureType.idMeasureType = " + idT + ")";
		
		// System.out.println(query);
		
		measure = em.createQuery(query, Measure.class).getResultList();	
		
		PeopleDao.instance.closeConnections(em);
		
		if(measure != null)
		{
			m = measure.get(0);
		}
		
		return m;
	}

	// Database operations
	public static Measure getMeasureById(int personId) {
        EntityManager em = PeopleDao.instance.createEntityManager();
        Measure p = em.find(Measure.class, personId);
        PeopleDao.instance.closeConnections(em);
        return p;
    }

	public static List<Measure> getAll() {
        EntityManager em = PeopleDao.instance.createEntityManager();
        List<Measure> list = em.createNamedQuery("Measure.findAll", Measure.class)
            .getResultList();
        PeopleDao.instance.closeConnections(em);
        return list;
    }

    public static Measure saveMeasure(Measure p) {
        EntityManager em = PeopleDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(p);
        tx.commit();
        PeopleDao.instance.closeConnections(em);
        return p;
    } 

    public static Measure updateMeasure(Measure p) {
        EntityManager em = PeopleDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        tx.commit();
        PeopleDao.instance.closeConnections(em);
        return p;
    }

    public static void removeMeasure(Measure p) {
        EntityManager em = PeopleDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        em.remove(p);
        tx.commit();
        PeopleDao.instance.closeConnections(em);
    }
    
    // Special methods
    public static List<Measure> getMeasureByPidAndType(int pId, String type) {
		EntityManager em = PeopleDao.instance.createEntityManager();
				
		String query = "SELECT m FROM Measure m WHERE m.person.idPerson = " + pId 
				+ " AND m.measureType.idMeasureType = (SELECT mT.idMeasureType FROM MeasureType mT WHERE mT.type = \"" + type + "\")";
				
		// System.out.println(query);
		
		List<Measure> measure = em.createQuery(query, Measure.class).getResultList();
		
		PeopleDao.instance.closeConnections(em);
		return measure;
	}
    
    public static List<Measure> getSomeOrderedMeasureByPidAndType(int pId, String type, int nMeasure) {
		EntityManager em = PeopleDao.instance.createEntityManager();
				
		String query = "SELECT m FROM Measure m WHERE m.person.idPerson = " + pId 
				+ " AND m.measureType.idMeasureType = (SELECT mT.idMeasureType FROM MeasureType mT WHERE mT.type = \"" + type + "\") ORDER BY m.date DESC";
				
		// System.out.println(query);
		
		List<Measure> measure = em.createQuery(query, Measure.class).setMaxResults(nMeasure).getResultList();
		
		PeopleDao.instance.closeConnections(em);
		return measure;
	}

	public static List<Measure> getMeasureByMidAndType(int pId, int mId, String type)
	{
		EntityManager em = PeopleDao.instance.createEntityManager();
		
		String query = "SELECT m FROM Measure m WHERE m.person.idPerson = " + pId + " AND m.idMeasure = " + mId
				+ " AND m.measureType.idMeasureType = (SELECT mT.idMeasureType FROM MeasureType mT WHERE mT.type = \"" + type + "\")";
				
		// System.out.println(query);
		
		List<Measure> measure = em.createQuery(query, Measure.class).getResultList();

		PeopleDao.instance.closeConnections(em);
		return measure;
	}
    
}