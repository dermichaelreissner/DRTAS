package ds;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import exceptions.*;

public class PeriodicTaskSet {
	private static MathContext mc = new MathContext(3, RoundingMode.HALF_UP);
	private HashMap<Long, PeriodicTask> pTaskSet;

	public PeriodicTaskSet(){
		pTaskSet = new HashMap<Long, PeriodicTask>();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (PeriodicTask p : pTaskSet.values())
			sb.append(p.toString() + "\n");
		
		return sb.toString();
	}
	
	public HashMap<Long, PeriodicTask> getpTaskSet() {
		return pTaskSet;
	}
	
	public void addPTask(PeriodicTask t) throws TaskSetDuplicateException {
		if (pTaskSet.containsKey(t.getId()))
			throw new TaskSetDuplicateException("Task set already has a task with the id " + t.getId());
			
		pTaskSet.put(t.getId(), t);
	}
	
	public void removeTask(PeriodicTask t) throws TaskSetTaskNonexistent {
		if (!pTaskSet.containsKey(t.getId()))
			throw new TaskSetTaskNonexistent("There is no task with the id " + t.getId());
			
		pTaskSet.remove(t.getId());
	}
	
	public ArrayList<Job> generateJobs(long fromTime, long toTime){
		ArrayList<Job> jobList = new ArrayList<Job>();
		
		for (PeriodicTask t : this.getpTaskSet().values()) {
			if (fromTime < t.getStartingTime())
				jobList.addAll(t.generateJobs(t.getStartingTime(), toTime));
			else jobList.addAll(t.generateJobs(fromTime, toTime));
		}
		
		return jobList;
	}
	
	public ArrayList<Job> generateJobsWithInheritedFixedPriorities(long fromTime, long toTime){
		ArrayList<Job> jobList = new ArrayList<Job>();
		
		for (PeriodicTask t : this.getpTaskSet().values()) {
			if (fromTime < t.getStartingTime())
				jobList.addAll(t.generateJobs(t.getStartingTime(), toTime));
			else jobList.addAll(t.generateJobs(fromTime, toTime));
		}
		
		return jobList;
	}
	
	public ArrayList<Job> generateJobsWithRMSPriorities(long fromTime, long toTime){
		/*
		 * This function generates all of the jobs of the given task set from fromTime to toTime
		 * The priorities are computed based on the periods of the task set which is therefore first sorted by periods
		 * Higher priority value implies higher priority
		 * The lowest priority value is 1, 0 is reserved for unassigned priority and idle tasks (if any exist)
		 */
		ArrayList<Job> jobList = new ArrayList<Job>();
		
		ArrayList<PeriodicTask> sortedTasks = new ArrayList<PeriodicTask>(pTaskSet.values());
		sortedTasks.sort((o1, o2) -> Long.compare(o1.getPeriod(), o2.getPeriod()));
		//System.out.println(sortedTasks);
		
		int prio = sortedTasks.size();
		
		for (PeriodicTask t : sortedTasks) {
			t.setPrio(prio);

			if (fromTime < t.getStartingTime())
				jobList.addAll(t.generateJobs(t.getStartingTime(), toTime));
			else jobList.addAll(t.generateJobs(fromTime, toTime));
						
			prio--;
		}			
		
		return jobList;
	}
	
	public ArrayList<Long> allReleases(long fromTime, long toTime) {
		/*
		 * This function generates all of the time points when jobs of the given task set are released
		 * Those can then be used as the time points of preemption and scheduler decisions
		 * The time points are sorted in ascending order
		 */
		Set<Long> releaseTimes = new HashSet<Long>();
		
		ArrayList<Job> jobList = generateJobs(fromTime, toTime);
		
		for (Job j : jobList)
			releaseTimes.add(j.getReleaseTime());
		
		ArrayList<Long> sortedTimes = new ArrayList<Long>(releaseTimes);
		Collections.sort(sortedTimes);
		
		return sortedTimes;
	}
	
	public BigDecimal utilizaton() {
		BigDecimal totalUtilization = BigDecimal.ZERO;
		
		try {
			for (PeriodicTask t : this.getpTaskSet().values()) {
				totalUtilization = totalUtilization.add(t.utilization(), mc);
			} 
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return totalUtilization;
	}
	
}
