package tddc17;


import aima.core.environment.liuvacuum.*;
import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.*;

import java.util.ArrayList;
import java.util.Random;

class MyAgentState
{
	public int[][] world = new int[30][30];
	public int initialized = 0;
	final int UNKNOWN 	= 0;
	final int WALL 		= 1;
	final int CLEAR 	= 2;
	final int DIRT		= 3;
	final int HOME		= 4;
	final int ACTION_NONE 			= 0;
	final int ACTION_MOVE_FORWARD 	= 1;
	final int ACTION_TURN_RIGHT 	= 2;
	final int ACTION_TURN_LEFT 		= 3;
	final int ACTION_SUCK	 		= 4;
	
	public boolean turnAround = false;
	public boolean gohome = false;
	
	public int nbStepOnKnownPath =0;
	public boolean start=false;
	public boolean RightTurn=false;
	
	public boolean comeback=false;
	public int agent_x_position = 1;
	public int agent_y_position = 1;
	public int agent_last_action = ACTION_NONE;
	
	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	public int agent_direction = EAST;
	
	MyAgentState() {
		for (int i=0; i < world.length; i++)
			for (int j=0; j < world[i].length ; j++)
				world[i][j] = UNKNOWN;
		world[1][1] = HOME;
		agent_last_action = ACTION_NONE;
	}
	// Based on the last action and the received percept updates the x & y agent position
	public void updatePosition(DynamicPercept p)
	{
		Boolean bump = (Boolean)p.getAttribute("bump");

		if (agent_last_action==ACTION_MOVE_FORWARD && !bump)   {
			switch (agent_direction) {
			case MyAgentState.NORTH:
				agent_y_position--;
				break;
			case MyAgentState.EAST:
				agent_x_position++;
				break;
			case MyAgentState.SOUTH:
				agent_y_position++;
				break;
			case MyAgentState.WEST:
				agent_x_position--;
				break;
			}
	    }
	}
	
	public void updateWorld(int x_position, int y_position, int info)
	{
		world[x_position][y_position] = info;
	}
	
	public void printWorldDebug()
	{
		for (int i=0; i < world.length; i++) {
			for (int j=0; j < world[i].length ; j++)	{
				if (world[j][i]==UNKNOWN)
					System.out.print(" ? ");
				if (world[j][i]==WALL)
					System.out.print(" # ");
				if (world[j][i]==CLEAR)
					System.out.print(" . ");
				if (world[j][i]==DIRT)
					System.out.print(" D ");
				if (world[j][i]==HOME)
					System.out.print(" H ");
			}
			System.out.println("");
		}
	}
	

	public boolean isKnownPosition(int xpos, int ypos) {
		return world[xpos][ypos]!=UNKNOWN;
	}
	
	public boolean isPositionOK(int xpos, int ypos) {
		return world[xpos][ypos]!=WALL;
	}
	
	public boolean isPositionClear(int xpos, int ypos) {
		return world[xpos][ypos]==CLEAR;
	}
	
	public int getMiniNumberofMovementFromHome() {
		return this.agent_x_position -1 + this.agent_y_position -1;
	}
	
	public boolean frontIsOK() {
		switch (agent_direction) {
		case MyAgentState.NORTH:
			return isPositionOK(agent_x_position, agent_y_position-1);
		case MyAgentState.EAST:
			return isPositionOK(agent_x_position+1, agent_y_position);
		case MyAgentState.SOUTH:
			return isPositionOK(agent_x_position, agent_y_position+1);
		case MyAgentState.WEST:
			return isPositionOK(agent_x_position-1, agent_y_position);
		default:
			return false;
		}
	}
	
	public boolean rightIsOK() {
		switch (agent_direction) {
		case MyAgentState.NORTH:
			return isPositionOK(agent_x_position+1, agent_y_position);
		case MyAgentState.EAST:
			return isPositionOK(agent_x_position, agent_y_position+1);
		case MyAgentState.SOUTH:
			return isPositionOK(agent_x_position-1, agent_y_position);
		case MyAgentState.WEST:
			return isPositionOK(agent_x_position, agent_y_position-1);
		default:
			return false;
		}
	}
	
	public boolean leftIsOK() {
		switch (agent_direction) {
		case MyAgentState.NORTH:
			return isPositionOK(agent_x_position-1, agent_y_position);
		case MyAgentState.EAST:
			return isPositionOK(agent_x_position, agent_y_position-1);
		case MyAgentState.SOUTH:
			return isPositionOK(agent_x_position+1, agent_y_position);
		case MyAgentState.WEST:
			return isPositionOK(agent_x_position, agent_y_position+1);
		default:
			return false;
		}
	}
	
	public boolean frontIsClear() {
		return isPositionClear(getFront().getXpos(), getFront().getYpos());
	}
	
	public Coordinates getFront() {
		switch (agent_direction) {
		case MyAgentState.NORTH:
			return new Coordinates(agent_x_position, agent_y_position-1);
		case MyAgentState.EAST:
			return new Coordinates(agent_x_position+1, agent_y_position);
		case MyAgentState.SOUTH:
			return new Coordinates(agent_x_position, agent_y_position+1);
		case MyAgentState.WEST:
			return new Coordinates(agent_x_position-1, agent_y_position);
		default:
			return new Coordinates(0,0);
		}
	}
	
	public Coordinates getLeft() {
		switch (agent_direction) {
		case MyAgentState.NORTH:
			return new Coordinates(agent_x_position-1, agent_y_position);
		case MyAgentState.EAST:
			return new Coordinates(agent_x_position, agent_y_position-1);
		case MyAgentState.SOUTH:
			return new Coordinates(agent_x_position+1, agent_y_position);
		case MyAgentState.WEST:
			return new Coordinates(agent_x_position, agent_y_position+1);
		default:
			return new Coordinates(0,0);
		}
	}

	public Coordinates getRight() {
		switch (agent_direction) {
		case MyAgentState.NORTH:
			return  new Coordinates(agent_x_position+1, agent_y_position);
		case MyAgentState.EAST:
			return  new Coordinates(agent_x_position, agent_y_position+1);
		case MyAgentState.SOUTH:
			return  new Coordinates(agent_x_position-1, agent_y_position);
		case MyAgentState.WEST:
			return  new Coordinates(agent_x_position, agent_y_position-1);
		default:
			return new Coordinates(0,0);
		}
	}
	
	public boolean rightIsClear() {
		return isPositionClear(getRight().getXpos(), getRight().getYpos());
	}
	
	public boolean leftIsClear() {
		return isPositionClear(getLeft().getXpos(), getLeft().getYpos());
	}
	
	public boolean rightIsAlreadyKnown() {
		switch (agent_direction) {
		case MyAgentState.NORTH:
			return isKnownPosition(agent_x_position+1, agent_y_position);
		case MyAgentState.EAST:
			return isKnownPosition(agent_x_position, agent_y_position+1);
		case MyAgentState.SOUTH:
			return isKnownPosition(agent_x_position-1, agent_y_position);
		case MyAgentState.WEST:
			return isKnownPosition(agent_x_position, agent_y_position-1);
		default:
			return false;
		}
	}
	
	public boolean leftIsAlreadyKnown() {
		switch (agent_direction) {
		case MyAgentState.NORTH:
			return isKnownPosition(agent_x_position-1, agent_y_position);
		case MyAgentState.EAST:
			return isKnownPosition(agent_x_position, agent_y_position-1);
		case MyAgentState.SOUTH:
			return isKnownPosition(agent_x_position+1, agent_y_position);
		case MyAgentState.WEST:
			return isKnownPosition(agent_x_position, agent_y_position+1);
		default:
			return false;
		}
	}
}

class MyAgentProgram implements AgentProgram {

	private int initnialRandomActions = 10;
	private Random random_generator = new Random();
	
	// Here you can define your variables!
	public int iterationCounter = 200;
	public MyAgentState state = new MyAgentState();
	
	// moves the Agent to a random start position
	// uses percepts to update the Agent position - only the position, other percepts are ignored
	// returns a random action
	private Action moveToRandomStartPosition(DynamicPercept percept) {
		int action = random_generator.nextInt(6);
		initnialRandomActions--;
		state.updatePosition(percept);
	
		if(action==0) {
		    state.agent_direction = ((state.agent_direction-1) % 4);
		    if (state.agent_direction<0) 
		    	state.agent_direction +=4;
		    state.agent_last_action = state.ACTION_TURN_LEFT;
			return LIUVacuumEnvironment.ACTION_TURN_LEFT;
		} else if (action==1) {
			state.agent_direction = ((state.agent_direction+1) % 4);
		    state.agent_last_action = state.ACTION_TURN_RIGHT;
		    return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
		} 
		state.agent_last_action=state.ACTION_MOVE_FORWARD;
		return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	}
	
	private void var_dump() {
		System.out.println("x=" + state.agent_x_position);
    	System.out.println("y=" + state.agent_y_position);
    	System.out.println("dir=" + state.agent_direction);
	}
	
	@Override
	public Action execute(Percept percept) {
		
		// DO NOT REMOVE this if condition!!!
    	if (initnialRandomActions>0) {
    		return moveToRandomStartPosition((DynamicPercept) percept);
    	} else if (initnialRandomActions==0) {
    		// process percept for the last step of the initial random actions
    		initnialRandomActions--;
    		state.updatePosition((DynamicPercept) percept);
			System.out.println("Processing percepts after the last execution of moveToRandomStartPosition()");
			state.agent_last_action=state.ACTION_SUCK;
	    	return LIUVacuumEnvironment.ACTION_SUCK;
    	}
		
    	// This example agent program will update the internal agent state while only moving forward.
    	// START HERE - code below should be modified!
    	
    	var_dump();
		
	    iterationCounter--;
	    
	    if (iterationCounter==0)
	    	return NoOpAction.NO_OP;

	    DynamicPercept p = (DynamicPercept) percept;
	    Boolean bump = (Boolean)p.getAttribute("bump");
	    Boolean dirt = (Boolean)p.getAttribute("dirt");
	    Boolean home = (Boolean)p.getAttribute("home");
	    System.out.println("percept: " + p);
	    
	    // State update based on the percept value and the last action
	    state.updatePosition((DynamicPercept)percept);
	    
	    if(bump && !state.frontIsOK()) {
	    	if(!state.comeback && state.agent_direction==1 && state.rightIsOK()) {
	    		state.RightTurn = true;
	    		return turnRight();
	    	}
	    	else if(!state.comeback && state.agent_direction==3 && state.leftIsOK()) {
	    		state.RightTurn = false;
	    		return turnLeft();
	    	}
	    	else{
	    		state.comeback = true;
	    	}
	    };
	    
	    updateWorldfromExecute(bump, dirt);
	    
	    state.printWorldDebug();
	    
	    // Next action selection based on the percept value
	    if (dirt)  {
	    	System.out.println("DIRT -> choosing SUCK action!");
	    	state.agent_last_action=state.ACTION_SUCK;
	    	return LIUVacuumEnvironment.ACTION_SUCK;
	    } else	{
	    	return deliberativePart(bump);
	    }
	}
	
	private Action deliberativePart(boolean bump) {
		if(!state.gohome && state.getMiniNumberofMovementFromHome() +15 > iterationCounter) {
			state.gohome = true;
		}
		
		if(state.gohome) {
			return moveTorwardHome();
		}
		
		if(state.comeback) {
			if(state.agent_x_position==1 && state.agent_y_position==1) {
				return NoOpAction.NO_OP;
			}
			
			if(!bump) {
				if(state.agent_direction==0) {			
					return turnAround();
				}
				return moveForward();
			} else if(state.agent_direction==3) {
				state.RightTurn = true;
				return turnRight();
			} else if(state.agent_direction==1) {
				state.RightTurn = false;
				return turnLeft();
			} else {
				return manageBump();
			}
		}

		if(state.agent_direction!=1 && state.start==false) {
			return turnRight();
		} else {
			state.start=true;
		}
		
		if(!bump) {
			if(state.agent_direction==2) {			
				return turnAround();
			}
			return moveForward();
		} else if(state.agent_direction==1) {
			state.RightTurn = true;
			return turnRight();
		} else if(state.agent_direction==3) {
			state.RightTurn = false;
			return turnLeft();
		} else {
			return manageBump();
		}
		
	}

	private Action turnAround() {
		if(state.agent_last_action==state.ACTION_MOVE_FORWARD || state.agent_last_action==state.ACTION_SUCK) {
			if(state.RightTurn) {
				return turnRight();
			} else {
				return turnLeft();
			}									
		} else {
			return moveForward();
		}
	}

	private Action manageBump() {
		if(!state.rightIsAlreadyKnown() || state.rightIsOK()) {
			return turnRight();
		} else if(!state.leftIsAlreadyKnown() || state.leftIsOK()) {
			return turnLeft();
		} else if(state.turnAround) {
			state.turnAround = false;
			return turnRight();
		} else {
			state.turnAround = true;
			return turnRight();
		}
	}
	
	private Action moveTorwardHome() {
		if(state.agent_x_position==1 && state.agent_y_position==1) {
			state.gohome=false;
			return NoOpAction.NO_OP;
		}
		
		Coordinates home = new Coordinates(1, 1);
		Coordinates current = new Coordinates(state.agent_x_position, state.agent_y_position);
		
		
		if(state.frontIsClear() && state.getFront().distance(home)< current.distance(home)) {
			return moveForward();
		} else if(state.leftIsClear() && state.getLeft().distance(home)< current.distance(home)) {
			return turnLeft();
		} else if(state.rightIsClear() && state.getRight().distance(home)< current.distance(home)) {
			return turnRight();
		} else if(state.frontIsClear()) {
			return moveForward();
		} else if(state.leftIsClear()){
			return turnLeft();
		} else if(state.rightIsClear()) {
			return turnRight();
		} else {
			return turnLeft();
		}

	}
	
	private Action moveForward()  {
		state.agent_last_action=state.ACTION_MOVE_FORWARD;
		return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	}
	
	private Action turnRight() {
		state.agent_last_action=state.ACTION_TURN_RIGHT;
		state.agent_direction = state.agent_direction +1;
		if (state.agent_direction==4) {
			state.agent_direction=0;
		}
    	return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
	}
	
	private Action turnLeft() {
		state.agent_last_action=state.ACTION_TURN_LEFT;
		state.agent_direction = state.agent_direction -1;
		if (state.agent_direction==-1) {
			state.agent_direction=3;
		}
    	return LIUVacuumEnvironment.ACTION_TURN_LEFT;
	}
	
	// refactor of the update function
	private void updateWorldfromExecute(boolean bump, boolean dirt) {
		if (bump) {
			switch (state.agent_direction) {
			case MyAgentState.NORTH:
				state.updateWorld(state.agent_x_position,state.agent_y_position-1,state.WALL);
				break;
			case MyAgentState.EAST:
				state.updateWorld(state.agent_x_position+1,state.agent_y_position,state.WALL);
				break;
			case MyAgentState.SOUTH:
				state.updateWorld(state.agent_x_position,state.agent_y_position+1,state.WALL);
				break;
			case MyAgentState.WEST:
				state.updateWorld(state.agent_x_position-1,state.agent_y_position,state.WALL);
				break;
			}
		}
		if (dirt)
			state.updateWorld(state.agent_x_position,state.agent_y_position,state.DIRT);
		else
			state.updateWorld(state.agent_x_position,state.agent_y_position,state.CLEAR);
	}

}


public class MyVacuumAgent extends AbstractAgent {
    public MyVacuumAgent() {
    	super(new MyAgentProgram());
	}
}
