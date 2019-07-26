import java.io.*;
import java.util.*;

/**
 * @author nathnaelaschale nathnaelaschale@brandeis.edu
 * @author ivarenytsia ivarenytsia@brandeis.edu
 */
public class TAChecker {
	public static ArrayList<TARecord> TAs = new ArrayList<TARecord>();

	/**
	 * This main method orchestrates the work of the sortWorkOrder and checkValidity
	 * methods. Main prompts for the user's file name and then sends that name to
	 * the sortWorkOrder method that reads that file and populates the TAs arraylist
	 * with TARecord objects that directly correspond to the jobs retrieved from the
	 * user's file.
	 *
	 * @param args
	 * @throws FileNotFoundException takes care of the case if the user's file is
	 *                               not found.
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter a work log:");// prompts for user input
		String nameOfFile = scan.next();// gets user's file name
		sortWorkLog(nameOfFile, scan);// calls this method to read and get the data from the user's file

		ArrayList<String> errorString = checkValidity();
		for (String x : errorString) {
			System.out.println(x);
		}
	}

	/**
	 * This method reads the passed file and either adds a new TA to the arrayList
	 * of TARecords, or passes on parameters to the finishedJob method that assigns
	 * a finished job ID and line number to the correct TA from the arrayList of
	 * TARecords. If the user does not type the right name of the file from their
	 * first try, they will be prompted to try again.
	 *
	 * @param fileName name of the file that is to be read.
	 * @param scan     scanner used to read the users input in the case of the
	 *                 submission of the wrong file name.
	 * @throws FileNotFoundException takes care of the case if the file is not
	 *                               found.
	 */
	public static void sortWorkLog(String fileName, Scanner scan) throws FileNotFoundException {
		int line = 0;
		File file = new File(fileName);
		if (file.canRead()) {
			Scanner consol = new Scanner(file);
			while (consol.hasNext()) {
				String temp = consol.next();
				line += 1;
				if (temp.contains("START")) {
					TARecord ta = new TARecord(temp.replaceAll(";START", ""));
					ta.addStart(line);
					TAs.add(ta);
				} else if (!temp.contains("START")) {
					finishedJob(temp, line);
				}
			}
		} else {
			System.out.println("Enter the correct file name:");
			fileName = scan.next();
			sortWorkLog(fileName, scan);
		}
	}

	/**
	 * This method assigns finished jobs to the matching TAs from the arrayList of
	 * TARecords. If the job is unstarted, then this method will add a new TA entry
	 * to the arrayList, and set the job ID to negative 1, which will be later used
	 * to track and report unstarted jobs.
	 *
	 * @param temp string that contains the whole line of the TAs log.
	 * @param line the line number of the given finished job ID or batch of jobs.
	 */
	public static void finishedJob(String temp, int line) {
		String numbs = temp.substring(temp.indexOf(';') + 1);
		String[] IDs = numbs.split(",");
		String name = temp.substring(0, temp.indexOf(';'));
		int x = 0;
		int i = 0;
		boolean a = false;
		/*
		 * This loop will run for the size of the array of the finished job IDs. Finds a
		 * matching TA without finished job assigned to them and assigns a finished job
		 * to the selected TA.
		 */
		while (!a && i != TAs.size()) {
			if (TAs.get(i).getName().equals(name) && TAs.get(i).line == null) {
				if (x != IDs.length) {
					TAs.get(i).addJob(line, Integer.parseInt(IDs[x]));
				}
				IDs[x] = null;
				if (x + 1 == IDs.length) {
					a = true;
				}
				x += 1;
			}
			i++;
		}
		/*
		 * This enchanted for loop checks if every entry from the array of job IDs was
		 * added to the TA. If a specific ID is still inside the array, then a new TA
		 * will be added to the arrayList with job ID set to negative 1.
		 */
		for (String id : IDs) {//
			if (id != null) {
				TAs.add(new TARecord(name));
				TAs.get(TAs.size() - 1).addJob(line, -1);
			}
		}
	}

	/**
	 * This method checks all the TA jobs for various mistakes, such as unstarted
	 * and shortened jobs, and suspicious batches. The general rules are as
	 * following: a mistake in the non batch job is recorded as either shortened or
	 * unstarted job, while a mistake in the batched job can be recorded either as a
	 * suspicious batch or as a number of unstarted and shortened jobs if the number
	 * of mistakes is greater than 1. It was our design choice to mark a batch of
	 * jobs with one shortened or one unstarted job as a suspicious job; and to
	 * report a batch of jobs with more than one of shortened+unstarted as a mistake
	 * message separately for each error.
	 *
	 * @return errorList an arrayList with all the errors saved as strings.
	 */
	public static ArrayList<String> checkValidity() {
		ArrayList<String> errorList = new ArrayList<String>();// ArrayList of errors.
		int lasCorrJob = -1;
		int lasCorrStart = -1;
		int str = -1;
		for (int i = 0; i < TAs.size(); i++) {
			ArrayList<TARecord> batch = new ArrayList<TARecord>();
			ArrayList<String> unstrd = new ArrayList<>();
			ArrayList<String> localError = new ArrayList<>();
			boolean end = false;
			int sym = 0;
			if (lasCorrJob == -1) {
				if (TAs.get(i).getID() != -1) {
					lasCorrJob = TAs.get(i).getID();
					lasCorrStart = TAs.get(i).getStart();
					str = TAs.get(i).getStart();
				} else {
					end = true;
					unstrd.add(TAs.get(i).getLine() + ";" + TAs.get(i).getName() + ";UNSTARTED_JOB");
					i++;
				}
			}			
			while (!end) {
				if (TAs.get(i).getStart() != null) {
					batch.add(TAs.get(i));
				}
				if (TAs.get(i).getStart() == null) {
					unstrd.add(TAs.get(i).getLine() + ";" + TAs.get(i).getName() + ";UNSTARTED_JOB");
				}
				if (sym == 0) {
					sym = i;
				}
				i++;
				if (i == TAs.size()) {
					end = true;
				} else if (TAs.get(i).getLine() != TAs.get(sym).getLine()) {
					end = true;
				}
			}
			i--;
			for (int a = 0; a < batch.size(); a++) {// count shortened jobs here
				if (str != batch.get(a).getStart()) {
					if (lasCorrJob >= batch.get(a).getID()  && lasCorrStart != batch.get(a).getStart()-1) {
						localError.add(batch.get(a).getStart() + ";" + batch.get(a).getName() + ";SHORTENED_JOB");
					}
					if (lasCorrJob < batch.get(a).getID() && lasCorrStart < batch.get(a).getStart()) {
						lasCorrJob = batch.get(a).getID();
						lasCorrStart = batch.get(a).getStart();
					}
				}
			}
			// merge it with the global error list
			if (localError.size() + unstrd.size() == 1) {
				if (batch.size() > 1) {
					errorList.add(batch.get(0).getLine() + ";" + batch.get(0).getName() + ";SUSPISIOUS_BATCH");
				} else if (localError.size() == 1) {
					errorList.addAll(localError);
				} else if (unstrd.size() == 1) {
					errorList.addAll(unstrd);
				}
			}
			if (localError.size() + unstrd.size() > 1) {
				errorList.addAll(localError);
				errorList.addAll(unstrd);
			}
		}
		return errorList;
	}
}
