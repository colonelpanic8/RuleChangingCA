public class Cell {
    Cell prev;
    Cell next;
    int state, lstate, numberOfStates, id;
    float account;
    static float stealingCoeffecient;

    public Cell(Cell p, int n, int i) {
	numberOfStates = n;
	lstate = (int)(Math.random()*numberOfStates);
	account = 10;
	id = i;
	prev = p;
    }

    public void updateAccount(int[][] payoffStructure, int[] selfPayoff) {
	int var;

	var = payoffStructure[state][prev.state]; 
	if(var < 0) {
	    if (prev.account < Math.abs(var)) {
		account += prev.account;
		prev.account = 0;
	    } else {
		account += Math.abs(var)*stealingCoeffecient;
		prev.account += var;
	    }
	} else {
	    prev.account +=var;
	}
	
	var = payoffStructure[state][next.state];
	if(var < 0) {
	    if (next.account < Math.abs(var)) {
		account += next.account;
		next.account = 0;
	    } else {
		account += Math.abs(var)*stealingCoeffecient;
		next.account += var;
	    }
	} else {
	    next.account +=var;
	}

	var = selfPayoff[state]; 
	if(var < 0 && account < Math.abs(var)) {
	    account = 0;
	} else {
	    account += var;
	}
	
    }

    public int optimize(int[] selfPayoff, int[][] payoffStructure) {
	int[] maxs = new int[numberOfStates];
	int var, nmaxs, chosen;
	float temp, max;
	max = -1;
	nmaxs = 0;
	TextIO.put("Optimization of ");
	TextIO.putln(Integer.toString(id));
	for(int i = 0; i < numberOfStates; i++) {       
	    temp = account;

	    var = payoffStructure[prev.state][i]; 
	    if(var < 0 && account < Math.abs(var)) {
		temp = 0;
	    } else {
		temp += var;
	    }
	    
	    var = payoffStructure[i][prev.state]; 
	    if(var < 0) {
		if (prev.account < Math.abs(var)) {
		    temp += prev.account;
		} else {
		    temp += Math.abs(var)*stealingCoeffecient;
		}
	    }
	    
	    var = selfPayoff[i]; 
	    if(var < 0 && prev.account < Math.abs(var)) {
		temp += prev.account;
	    } else {
		temp += var;
	    }

	    var = payoffStructure[i][next.state];
	    if(var < 0) {
		if (next.account < Math.abs(var)) {
		    temp += next.account;
		} else {
		    temp += Math.abs(var)*stealingCoeffecient;
		}
	    }

	    var = payoffStructure[next.state][i]; 
	    if(var < 0 && account < Math.abs(var)) {
		temp = 0;
	    } else {
		temp += var;
	    }

	    if(temp > max) {
		max = temp;
		maxs[0] = i; 
		nmaxs = 1;
	    } else if(temp == max) {
		maxs[nmaxs] = i;
		nmaxs++;
	    }
	    TextIO.put(Integer.toString(i));
	    TextIO.put(" Gives ");
	    TextIO.putln(Float.toString(temp));
	}
	chosen = maxs[(int)(Math.random()*nmaxs)];
	return chosen;
    }

    public int updateState(int[] rule) {
	int temp = 0;
	temp += next.lstate;
	temp += lstate*numberOfStates;
	temp += prev.lstate*numberOfStates*numberOfStates;
        state = rule[temp];
	return state;
    }
    
    public void finalize() {
	lstate = state;
    }
}