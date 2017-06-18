package lifecoach.localdb.webservice;

import lifecoach.localdb.model.Person;
import lifecoach.localdb.model.Measure;
import lifecoach.localdb.model.MeasureType;
import lifecoach.localdb.model.Goal;
import lifecoach.localdb.model.GoalType;

import java.util.List;

import javax.jws.WebService;

//Service Implementation

@WebService(endpointInterface = "lifecoach.localdb.webservice.People",
    serviceName="PeopleService")
public class PeopleImplementation implements People 
{
	/* Manage Person*/
	
    @Override
    public Person readPerson(int id) {
        System.out.println("Reading Person by id = "+id);
        Person p = Person.getPersonById(id);
        if (p!=null) {
            System.out.println("---> Found Person by id = "+id+" => "+p.getFirstname());
        } else {
            System.out.println("---> Didn't find any Person with  id = "+id);
        }
        return p;
    }

    @Override
    public List<Person> getPeople() {
    	System.out.println("Read person List");
    	return Person.getAll();
    }

    @Override
    public int addPerson(Person person) {
    	System.out.println("Save Person with id = " + person.getIdPerson());
    	person = Person.savePerson(person);
        return person.getIdPerson();
    }

    @Override
    public int updatePerson(Person person) {
    	if(person == null)
    	{
    		System.out.println("Zio billy");
    		return 1;
    	}
    	
    	System.out.println("Update Person with id = " + person.getIdPerson());
    	person = Person.updatePerson(person);
        return person.getIdPerson();
    }

    @Override
    public int deletePerson(int id) {
    	System.out.println("Delete Person with id = " + id);
    	Person p = Person.getPersonById(id);
        if (p!=null) {
            Person.removePerson(p);
            return 0;
        } else {
            return -1;
        }
    }
    
    
    /* Manage Measure */
    
    @Override
    public Measure readMeasure(int pId, String measureType, int mId) {
        System.out.println("Reading Measure by pId = " + pId + ", mId = " + mId + ", type = " + measureType);
        List<Measure> p = Measure.getMeasureByMidAndType(pId, mId, measureType);
        if (p!=null) {
            System.out.println("---> Found Measure");
            for(Measure m : p)
            {
            	System.out.println(m.getIdMeasure() + " " + m.getValue());
            }	
        } else {
            System.out.println("---> Didn't find any Measure");
        }
        return p.get(0);
    }
    
    @Override
    public List<Measure> readLastMeasure(int pId) {
    	System.out.println("Reading LastMeasure by pId = " + pId);
        List<Measure> p = Measure.getLastMeasure(pId);
        if (p!=null) {
            System.out.println("---> Found Measure");
            for(Measure m : p)
            {
            	System.out.println(m.getIdMeasure() + " " + m.getValue());
            }
        } else {
            System.out.println("---> Didn't find any Measure");
        }
        return p;
    }

    @Override
    public List<Measure> getMeasure(int pId, String measureType) {
    	System.out.println("Reading Measure by pId = " + pId + ", type = " + measureType);
        List<Measure> p = Measure.getMeasureByPidAndType(pId, measureType);
        if (p!=null) {
            System.out.println("---> Found Measure");
            for(Measure m : p)
            {
            	System.out.println(m.getIdMeasure() + " " + m.getValue());
            }
        } else {
            System.out.println("---> Didn't find any Measure");
        }
        return p;
    }
    
    @Override
    public List<Measure> getSomeMeasure(int pId, String measureType, int nMeasure) {
    	System.out.println("Reading Measure by pId = " + pId + ", type = " + measureType + ", max = " + nMeasure);
        List<Measure> p = Measure.getSomeMeasureByPidAndTypeOrdered(pId, measureType, nMeasure);
        if (p!=null) {
            System.out.println("---> Found Measure");
            for(Measure m : p)
            {
            	System.out.println(m.getIdMeasure() + " " + m.getValue());
            }
        } else {
            System.out.println("---> Didn't find any Measure");
        }
        return p;
    }

    @Override
    public int addMeasure(int pId, Measure measure) {
    	System.out.println("Save Measure with id = " + measure.getIdMeasure());
        
    	if(measure.getMeasureType()==null)
    	{
    		System.out.println("QUi");
    	}
    	else
    	{
    		System.out.println("QUa " + measure.getMeasureType().getType());
    	}
    	
    	measure.setPerson(Person.getPersonById(pId));
		Measure.saveMeasure(measure);
        return measure.getIdMeasure();
    }

    @Override
    public int updateMeasure(int pId, Measure measure) {
    	System.out.println("Update Measure with id = " + measure.getIdMeasure());
    	measure.setPerson(Person.getPersonById(pId));
    	Measure.updateMeasure(measure);
    	return measure.getIdMeasure();
    }
    
    @Override
    public int deleteMeasure(int id) {
    	System.out.println("Delete Measure with id = " + id);
    	Measure p = Measure.getMeasureById(id);
        if (p!=null) {
            Measure.removeMeasure(p);
            return 0;
        } else {
            return -1;
        }
    }
    
    
    /* Manage MeasureType */
    
    @Override
    public MeasureType readMeasureType(int id) {
        System.out.println("Reading MeasureType by id = " + id);
        MeasureType m = MeasureType.getMeasureTypeById(id);
        if (m!=null) {
            System.out.println("---> Found MeasureType by id = " + id + " => " + m.getType());
        } else {
            System.out.println("---> Didn't find any MeasureType with  id = " + id);
        }
        return m;
    }

    @Override
    public List<MeasureType> getMeasureType() {
    	System.out.println("Read MeasureType List");
        return MeasureType.getAll();
    }
    
    
    /* Manage Goal */
    
    @Override
    public Goal readGoal(int pId, String measureType, int gId) {
        System.out.println("Reading Goal by pId = " + pId + ", mId = " + gId + ", type = " + measureType);
        List<Goal> p = Goal.getGoalByGidAndType(pId, gId, measureType);
        if (p!=null) {
            System.out.println("---> Found Goal");
            for(Goal m : p)
            {
            	System.out.println(m.getIdGoal() + " " + m.getValue());
            }	
        } else {
            System.out.println("---> Didn't find any Goal");
        }
        return p.get(0);
    }

    @Override
    public List<Goal> getGoal(int pId, String measureType) {
    	System.out.println("Reading Goal by pId = " + pId + ", type = " + measureType);
        List<Goal> p = Goal.getGoalByPidAndType(pId, measureType);
        if (p!=null) {
            System.out.println("---> Found Goal");
            for(Goal m : p)
            {
            	System.out.println(m.getIdGoal() + " " + m.getValue());
            }
        } else {
            System.out.println("---> Didn't find any Goal");
        }
        return p;
    }

    @Override
    public int addGoal(int pId, Goal goal) {
    	System.out.println("Save Goal with id = " + goal.getIdGoal());
        
    	if(goal.getMeasureType()==null)
    	{
    		System.out.println("QUi");
    	}
    	else
    	{
    		System.out.println("QUa " + goal.getMeasureType().getType());
    	}
    	
    	goal.setPerson(Person.getPersonById(pId));
		Goal.saveGoal(goal);
        return goal.getIdGoal();
    }

    @Override
    public int updateGoal(int pId, Goal goal) {
    	System.out.println("Update Goal with id = " + goal.getIdGoal());
    	goal.setPerson(Person.getPersonById(pId));
    	Goal.updateGoal(goal);
    	return goal.getIdGoal();
    }
    
    @Override
    public int deleteGoal(int id) {
    	System.out.println("Delete Goal with id = " + id);
    	Goal p = Goal.getGoalById(id);
        if (p!=null) {
            Goal.removeGoal(p);
            return 0;
        } else {
            return -1;
        }
    }
    
    
    /* Manage GoalType */
    
    @Override
    public GoalType readGoalType(int id) {
        System.out.println("Reading GoalType by id = " + id);
        GoalType m = GoalType.getGoalTypeById(id);
        if (m!=null) {
            System.out.println("---> Found GoalType by id = " + id + " => " + m.getType());
        } else {
            System.out.println("---> Didn't find any GoalType with  id = " + id);
        }
        return m;
    }

    @Override
    public List<GoalType> getGoalType() {
    	System.out.println("Read GoalType List");
        return GoalType.getAll();
    }
}