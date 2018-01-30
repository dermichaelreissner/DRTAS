package demos.sched;

import algo.sched.*;
import ds.*;

public class generateDifferentDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		long time = System.nanoTime();
		PeriodicTaskSet p = Essence.generateDifferentEDFtoRMS(3, 3, 15);
		time = System.nanoTime() - time;
		
		System.out.println("Elapsed time is " + (double) time / 1000000000.0 + "s");
		
		System.out.println(p);
		System.out.println("Total Utilization is " + p.utilizaton());
		
		try {
			System.out.println("Hyperperiod of the task set is " + Essence.hyperPeriod(p));
			System.out.println("RMS Schedule:");
			System.out.println(Essence.normalizeSchedule(Essence.schedule(p, 0, 50, true, jobList -> RMS.hasHighestPriority(jobList))));
			System.out.println("EDF Schedule:");
			System.out.println(Essence.normalizeSchedule(Essence.schedule(p, 0, 50, true, jobList -> EDF.hasHighestPriority(jobList))));
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}