package lifecoach.localdb.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import lifecoach.localdb.dao.PeopleDao;
import lifecoach.localdb.model.Measure;

@Entity  // indicates that this class is an entity to persist in DB
@Table(name="MeasureType") // to whate table must be persisted
@NamedQuery(name="MeasureType.findAll", query="SELECT p FROM MeasureType p")
@XmlRootElement
public class MeasureType implements Serializable 
{
    private static final long serialVersionUID = 1L;
    
    @Id // defines this attributed as the one that identifies the entity
    @GeneratedValue(generator="sqlite_measuretype")
	@TableGenerator(name="sqlite_measuretype", table="sqlite_sequence",
				    pkColumnName="name", valueColumnName="seq",
				    pkColumnValue="MeasureType")
    
    @Column(name="idMeasureType") // maps the following attribute to a column
    private int idMeasureType;
    
    @Column(name="type")
    private String type;
    
    // MappedBy must be equal to the name of the attribute in Measure that maps this relation
    @OneToMany(mappedBy="measureType")
    private List<Measure> measure;
       
    public int getIdMeasureType() {
		return idMeasureType;
	}

	public void setIdMeasureType(int id) {
		this.idMeasureType = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		
		switch(type)
		{
			case "height":
				this.idMeasureType = 1;
				break;
			case "weight":
				this.idMeasureType = 2;
				break;
			case "step":
				this.idMeasureType = 3;
				break;
		}
	}
	
	// Transient for JAXB to avoid and infinite loop on serialization
	@XmlTransient
    public List<Measure> getMeasure() {
        return measure;
    }
    public void setMeasure(List<Measure> measure) {
        this.measure = measure;
    }
	
	// Database operations
	public static MeasureType getMeasureTypeById(int personId) {
        EntityManager em = PeopleDao.instance.createEntityManager();
        MeasureType p = em.find(MeasureType.class, personId);
        PeopleDao.instance.closeConnections(em);
        return p;
    }
	
	public static int getIdMeasureTypeByType(String type) {
		int id = 0;
		EntityManager em = PeopleDao.instance.createEntityManager();
		
		String query = "SELECT m.idMeasureType FROM MeasureType m WHERE m.type = \"" + type	+ "\"";
		// System.out.println(query);
		
		List<Integer> measureType = em.createQuery(query, Integer.class).getResultList();
				
		PeopleDao.instance.closeConnections(em);

		if(measureType != null)
		{
			id = measureType.get(0);
		}
		
		return id; 
    }

	public static List<MeasureType> getAll() {
        EntityManager em = PeopleDao.instance.createEntityManager();
        List<MeasureType> list = em.createNamedQuery("MeasureType.findAll", MeasureType.class)
            .getResultList();
        PeopleDao.instance.closeConnections(em);
        return list;
    }

    public static MeasureType saveMeasureType(MeasureType p) {
        EntityManager em = PeopleDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(p);
        tx.commit();
        PeopleDao.instance.closeConnections(em);
        return p;
    } 

    public static MeasureType updateMeasureType(MeasureType p) {
        EntityManager em = PeopleDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        tx.commit();
        PeopleDao.instance.closeConnections(em);
        return p;
    }

    public static void removeMeasureType(MeasureType p) {
        EntityManager em = PeopleDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        em.remove(p);
        tx.commit();
        PeopleDao.instance.closeConnections(em);
    }
    
}