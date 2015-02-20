package txtuml.examples.example1;

import txtuml.api.*;
import txtuml.external.*;

class Model1 extends Model {
	
	class Machine extends ModelClass {
		ModelInt tasksTodo = new ModelInt(2);

		class Init extends InitialState {}
		
		class Off extends State {
			@Override public void entry() {
	        	Action.log("Enters state: 'off'");
            }
            @Override public void exit() {
	        	Action.log("Exits state: 'off'");
            }
		}
				
		class On extends CompositeState {
			@Override public void entry() {
	        	Action.log("Enters state: 'on'");
            }
			@Override public void exit() {
	        	Action.log("Exits state: 'on'");
            }
			
			class Init extends InitialState {}

			class Active extends State {
				@Override public void entry() {
		            Action.log("Enters state: 'active'");
					Action.log("tasks todo: " + Machine.this.tasksTodo); // not valid: when importing, the actual value of tasksTodo will be burnt into the code (this was only written for the testing)
				}
	            @Override public void exit() {
		        	Action.log("Exits state: 'active'");
	            }
			}

			@From(Init.class) @To(Active.class)
			class Initialize extends Transition {}
			
			@From(Active.class) @To(Active.class) @Trigger(DoTasks.class)
			class DoActivity extends Transition {
				@Override public void effect() {
					DoTasks dTE = getSignal();
					Machine.this.tasksTodo = Machine.this.tasksTodo.subtract(dTE.count);	
					Action.log("\tBecoming active...");
				}
			}
		}

		@From(Init.class) @To(Off.class)
		class Initialize extends Transition {
			@Override public void effect() {
				Action.log("\tInitializing...");
			}
		} 
		
		@From(Off.class) @To(On.class) @Trigger(ButtonPress.class)
        class SwitchOn extends Transition {
			@Override public void effect() {
				Action.log("\tSwitch on...");
			}
			
		}
		
		@From(On.class) @To(Off.class) @Trigger(ButtonPress.class)
		class SwitchOff extends Transition {
			@Override public void effect() {
				Action.log("\tSwitch off...");
			}
			@Override public ModelBool guard() {
				return Machine.this.tasksTodo.isLessEqual(new ModelInt(0));
			}
		}
	}
	
	class User extends ModelClass {
		Machine doWork(User param) {
			Action.log("User: starting to work...");
			//Machine myMachine = Action.selectOne(this, Usage.class, "usedMachine");
			Object o = this.assoc(Usage.usedMachine.class);
			System.out.println(o.getClass().getSimpleName());
			Machine myMachine = this.assoc(Usage.usedMachine.class).selectOne();
			
			Action.send(param,new ButtonPress());
			Action.send(myMachine, new ButtonPress()); // switches the machine on
			Action.send(myMachine, new ButtonPress()); // tries to switch it off, but fails because of the guard
			Action.send(myMachine, new DoTasks(new ModelInt(1))); // the machine becomes active and decreases its tasks-to-do count by 1 

			Action.send(myMachine, new ButtonPress()); // tries to switch it off, but fails again
			Action.send(myMachine, new DoTasks(new ModelInt(1))); // the machine becomes active again and decreases its tasks-to-do count by 1
			
			Timer.Handle t1 = Timer.start(myMachine, new ButtonPress(), new ModelInt(2000));
			t1.add(new ModelInt(3000));
			
			If(() -> {
				return new ModelBool(true);
			}, () -> {
				Action.log(""+t1.query());
			});
			
			//if (true) Action.log(""+ t1.query());
			
			Action.send(myMachine, new DoTasks(new ModelInt(1))); // this event has no effect, the machine is switched off

			Action.log("User: work finished...");
			return myMachine;
		}
	}
    
	class Usage extends Association {
		//@One Machine usedMachine;
		//@Many User userOfMachine;
		class usedMachine extends One<Machine> {}
		class userOfMachine extends Many<User> {}
	}

	class ButtonPress extends Signal {
		ModelString name = new ModelString("ButtonPress");
	}

	class DoTasks extends Signal {
		DoTasks(ModelInt count) {
			this.count = count; 
		}
		ModelInt count;
	}
	
	class SpecialDoTasks extends DoTasks{

		SpecialDoTasks(ModelInt count) {
			super(count);
		
		}
	}
	
	public void test() {
		ModelExecutor.Settings.setExecutorLog(true);
		Machine m = new Machine();
		start(m);
		
		User u1 = new User();
		User u2 = Action.create(User.class); //almost equivalent to 'new User()'
												//not exactly: with current implementation the object created by Action.create() will have no enclosing Model1 object
		
		
        Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u1);
        Action.link(Usage.usedMachine.class, m, Usage.userOfMachine.class, u2);
                
        u1.assoc(Usage.usedMachine.class);
        
        u1.doWork(u2);
        
   	}
}

public class Example1 {
	public static void main(String[] args) {
		new Model1().test();
	}
}