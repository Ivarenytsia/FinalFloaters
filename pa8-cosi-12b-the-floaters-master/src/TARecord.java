import java.util.*;

/**
 * @author nathnaelaschale
 * nathnaelaschale@brandeis.edu
 * @author ivarenytsia
 * ivarenytsia@brandeis.edu
 */
public class TARecord {
	String name;
	Integer ID;
	Integer line;
	Boolean hasJob;
	Integer startLine;

	/**
	 * This is a constructor for the TARecord. It takes in string that represents
	 * the name of the new TA.
	 *
	 * @param name name of the TA to be created.
	 */
	public TARecord(String name) {
		this.name = name;
		this.ID = null;
		this.line = null;
	}

	/**
	 * This method adds an integer that represents the line where the job was
	 * started to the fields of the TARecord.
	 *
	 * @param startLine integer that represents the line where the job was started.
	 */
	public void addStart(Integer startLine) {
		this.startLine = startLine;
	}

	/**
	 * This method takes in two parameters: line and jobID. This method saves those
	 * numbers to the appropriate fields.
	 *
	 * @param line  number of the line where the job was completed to be added.
	 * @param jobID ID of the job that was competed.
	 */
	public void addJob(Integer line, Integer jobID) {
		this.ID = jobID;
		this.line = line;
	}

	/**
	 * This method returns the name of the TA.
	 * @return returns the name of the TA.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * This name returns the job ID that the TA was assigned to.
	 * @return return the job ID of the job TA was working on.
	 */
	public Integer getID() {
		return this.ID;
	}

	/**
	 * this will return string of the name of the TA and what the ID was
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return (this.name + ": worked on: " + this.ID);
	}

	/**
	 * the person line we getting the data
	 *
	 * @return return a integer of the end recorded line
	 */
	public Integer getLine() {
		return this.line;
	}

	/**
	 * where the line the person started the job
	 *
	 * @return return a integer of the start job line
	 */
	public Integer getStart() {
		return this.startLine;
	}
}
