import java.lang.Math;
import java.util.Arrays;

public class RuleChangingCA {
    static int numberOfStates = 2;
    static int numberOfCells;
    static Cell cells;
    static int[] rule;
    static int[] newRule;
    static int[] selfPayoff;
    static int[][] payoffStructure;

    public static void main(String[] argv) {
	
	TextIO.putln("Enter the number of Agents:");
	numberOfCells = TextIO.getlnInt();
	initCells();
	
	TextIO.putln("Enter the rule string");
	parseRule(TextIO.getlnInt());
	TextIO.putln("Your Rule is:");
	for(int i=0; i< rule.length; i++) {
	    TextIO.put(rule[i]);
	}
	TextIO.putln();
	
	parsePayoff();
	
	TextIO.putln("How Many Generations");
	runSimulation(TextIO.getlnInt());
    }

    public static void runSimulation(int generations) {
	Cell[] maxs = new Cell[numberOfCells];
	Cell runner, chosen;
	int maxcounter;
	for(int i = 0; i < generations; i++) {
	    runner = cells;
	    maxcounter = 0;
	    do {
		if(maxcounter == 0 || runner.account > maxs[0].account ) {
		    maxs[0] = runner;
		    maxcounter = 1;
		} else if(runner.account == maxs[0].account) {
		    maxs[maxcounter] = runner;
		    maxcounter++;
		}
		runner = runner.next;
	    } 	    while(runner != cells); 
	    newRule = Arrays.copyOf(rule, rule.length);
	    chosen = maxs[(int)(Math.random()*maxcounter)];
	    
	    int temp = 0;
	    temp += chosen.next.lstate;
	    temp += chosen.lstate*numberOfStates;
	    temp += chosen.prev.lstate*numberOfStates*numberOfStates;
	    newRule[temp] = chosen.optimize(selfPayoff, payoffStructure);
	    
	    runner = cells;
	    do {
		if(runner == chosen) {
		    runner.updateState(newRule);
		} else {
		    runner.updateState(rule);
		}
		runner = runner.next;
	    } while(runner != cells);
	    
	    runner = cells;
	    do {
		runner.finalize();
		runner.updateAccount(payoffStructure, selfPayoff);
		runner = runner.next;
	    } while(runner != cells);
	    rule = newRule;
	    printCells();
	    TextIO.putln("Your Rule is:");
	    for(int j=0; j < rule.length; j++) {
		TextIO.put(rule[rule.length-j-1]);
	    }
	    TextIO.putln();
	}
	
    }
    
    public static void printCells() {
	Cell runner = cells;
	do {
	    TextIO.put(runner.state);
	    TextIO.put("|");
	    runner = runner.next;
	} while(runner != cells);
	TextIO.putln("");
	runner = cells;
	do {
	    TextIO.put(runner.account);
	    TextIO.put("|");
	    runner = runner.next;
	} while(runner != cells);
	TextIO.putln("");
    }

    public static void initCells() {
	Cell runner, prev;
	prev = null;
	runner =  new Cell(prev, numberOfStates, 0);
	cells = runner;
	prev = runner;
	for(int i = 1; i < numberOfCells; i++) {
	    runner =  new Cell(prev, numberOfStates, i);
	    prev.next = runner;
	    prev = runner;
	}
	runner.next = cells;
	cells.prev = runner;
    }

    public static void parseRule(int n) {
	int size = (int)Math.pow(numberOfStates, 3);
	rule = new int[size];
	for(int i = 0; i < size; i++) {
	    rule[i] = n % numberOfStates;
	    n = n/numberOfStates;
	}
    }

    public static void parsePayoff() {
	payoffStructure = new int[numberOfStates][numberOfStates];
	
	for(int i = 0; i < numberOfStates; i++) {
	    for(int j = 0; j < numberOfStates; j++) {
		TextIO.put("Input payoff for cell state: ");
		TextIO.put(Integer.toString(i));
		TextIO.put(", neighbor state: ");
		TextIO.putln(Integer.toString(j));
		payoffStructure[i][j] = TextIO.getlnInt();
	    }
	}
	selfPayoff = new int[numberOfStates];
	for(int i = 0; i < numberOfStates; i++) {
	    TextIO.put("Input payoff for cell state:");
	    TextIO.putln(Integer.toString(i));
	    selfPayoff[i] = TextIO.getlnInt();
	}
	TextIO.putln("Enter the stealing Coefficient:");
	Cell.stealingCoeffecient = TextIO.getlnFloat();
    }

}