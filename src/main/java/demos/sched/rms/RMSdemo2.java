package demos.sched.rms;

import algo.sched.Essence;
import algo.sched.RMS;
import ds.*;

public class RMSdemo2 {
	public static void main(String[] args) {
		PeriodicTaskSet pts = new PeriodicTaskSet();
		try {
			pts.addPTask(new PeriodicTask("1", 1, 0, 4, 0, 6, 1));
			pts.addPTask(new PeriodicTask("2", 6, 0, 12, 0, 10, 2));
			pts.addPTask(new PeriodicTask("3", 10, 0, 48, 0, 10, 3));
		} catch (Exception e) {		
		}
		
		Schedule rmsSchedule = new Schedule();
		try {
			rmsSchedule = Essence.schedule(pts, 0, 60, true, jobList -> RMS.hasHighestPriority(jobList));
		} catch (Exception e) {
		}
		
		System.out.println(rmsSchedule);
		System.out.println(Essence.normalizeSchedule(rmsSchedule));
				
	}

}