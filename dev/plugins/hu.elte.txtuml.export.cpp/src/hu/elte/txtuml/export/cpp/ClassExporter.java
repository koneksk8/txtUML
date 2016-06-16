package hu.elte.txtuml.export.cpp;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Event;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.SignalEvent;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Trigger;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.Vertex;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.export.cpp.templates.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

public class ClassExporter {
	private static final int _UMLMany = -1;
	private static String _unknownGuardName = "guard";
	private static String _unknownEntryName = "entry";
	private static String _unknownExitName = "exit";

	private Map<String, String> _guardMap;// <guardConstraint,guardName>
	private Map<String, Pair<String, String>> _entryMap;// <name,<state,func>>
	private Map<String, Pair<String, String>> _exitMap;// <name,<state,func>>
	private Map<String, Pair<String, Region>> _submachineMap;// <stateName,<machinename,behavior>>
	private List<String> _subSubMachines;

	private enum FuncTypeEnum {
		Entry, Exit
	}

	private Integer poolId;

	public ClassExporter() {
		reiniIialize();
	}

	public void reiniIialize() {
		_guardMap = new HashMap<String, String>();
		_entryMap = null;
		_exitMap = null;
		_submachineMap = null;
		_subSubMachines = new LinkedList<String>();
	}

	public void createSource(Class class_, String dest_) throws FileNotFoundException, UnsupportedEncodingException {
		String source;
		List<StateMachine> smList = new ArrayList<StateMachine>();
		Shared.getTypedElements(smList, class_.allOwnedElements(), UMLPackage.Literals.STATE_MACHINE);
		if (ownStates(class_, smList)) {
			Region region = smList.get(0).getRegions().get(0);
			_submachineMap = getSubMachines(region);
			createFuncTypeMap(region, FuncTypeEnum.Entry, true);
			createFuncTypeMap(region, FuncTypeEnum.Exit, true);

			for (Map.Entry<String, Pair<String, Region>> entry : _submachineMap.entrySet()) {
				ClassExporter classExporter = new ClassExporter();
				classExporter.createSubSmSource(entry.getValue().getFirst(), class_.getName(),
						entry.getValue().getSecond(), dest_);
				_subSubMachines.addAll(classExporter.getSubmachines());
			}
		}

		source = createClassHeaderSource(class_);
		Shared.writeOutSource(dest_, GenerationTemplates.headerName(class_.getName()),
				GenerationTemplates.headerGuard(source.toString(), class_.getName()));
		source = (createClassCppSource(class_)).toString();
		Shared.writeOutSource(dest_, GenerationTemplates.sourceName(class_.getName()),
				GenerationTemplates.cppInclude(class_.getName()) + getAllDependency(class_, false) + source);
	}

	public List<String> getSubmachines() {
		List<String> ret = new LinkedList<String>();
		if (_submachineMap != null) {
			for (Map.Entry<String, Pair<String, Region>> entry : _submachineMap.entrySet()) {
				ret.add(entry.getValue().getFirst());
			}
			ret.addAll(_subSubMachines);
		}
		return ret;
	}

	public void setConfiguratedPoolId(Integer poolId) {
		this.poolId = poolId;

	}

	private void createSubSmSource(String className_, String parentClass_, Region region_, String dest_)
			throws FileNotFoundException, UnsupportedEncodingException {
		String source = "";
		_submachineMap = getSubMachines(region_);
		createFuncTypeMap(region_, FuncTypeEnum.Entry, false);
		createFuncTypeMap(region_, FuncTypeEnum.Exit, false);

		for (Map.Entry<String, Pair<String, Region>> entry : _submachineMap.entrySet()) {
			createSubSmSource(entry.getValue().getFirst(), parentClass_, entry.getValue().getSecond(), dest_);
		}

		source = createSubSmClassHeaderSource(className_, parentClass_, region_);
		Shared.writeOutSource(dest_, GenerationTemplates.headerName(className_),
				GenerationTemplates.headerGuard(source, className_));
		source = createSubSmClassCppSource(className_, parentClass_, region_).toString();

		String dependencyIncludes = GenerationTemplates.cppInclude(className_);
		dependencyIncludes = GenerationTemplates.debugOnlyCodeBlock(GenerationTemplates.StandardIOinclude)
				+ dependencyIncludes;

		Shared.writeOutSource(dest_, GenerationTemplates.sourceName(className_), dependencyIncludes + "\n" + source);
	}

	private String createClassHeaderSource(Class class_) {
		String source = "";
		List<StateMachine> smList = new ArrayList<StateMachine>();
		Shared.getTypedElements(smList, class_.allOwnedElements(), UMLPackage.Literals.STATE_MACHINE);
		StringBuilder dependency = getAllDependency(class_, true);
		StringBuilder privateParts = createParts(class_, "private");
		StringBuilder protectedParts = createParts(class_, "protected");
		StringBuilder publicParts = createParts(class_, "public");

		List<String> constructorParams = new ArrayList<String>();

		if (ownStates(class_, smList)) {
			Region region = smList.get(0).getRegions().get(0);
			privateParts.append(createEntryFunctionsDecl(region));
			privateParts.append(createExitFunctionsDecl(region));
			privateParts.append(createGuardFunctions(region));
			privateParts.append(createTransitionFunctionDecl(region));
			publicParts.append(GenerationTemplates.stateEnum(getStateList(region), getInitState(region)));
			publicParts.append(GenerationTemplates.eventEnum(getEventList(region)));

			if (_submachineMap.isEmpty()) {
				source = GenerationTemplates.simpleStateMachineClassHeader(dependency.toString(), class_.getName(),
						getBaseClass(class_), constructorParams, publicParts.toString(), protectedParts.toString(),
						privateParts.toString(), true);
			} else {
				source = GenerationTemplates.hierarchicalStateMachineClassHeader(dependency.toString(),
						class_.getName(), getBaseClass(class_), constructorParams, getSubmachines(),
						publicParts.toString(), protectedParts.toString(), privateParts.toString(), true);
			}
		} else {
			source = GenerationTemplates.classHeader(dependency.toString(), class_.getName(), getBaseClass(class_),
					constructorParams, publicParts.toString(), protectedParts.toString(), privateParts.toString(),
					true);
		}
		return source;
	}

	private String createSubSmClassHeaderSource(String className_, String parentclass_, Region region_) {
		String source = "";
		String dependency = GenerationTemplates.cppInclude(parentclass_) + "\n";

		StringBuilder privateParts = createEntryFunctionsDecl(region_);
		privateParts.append(createExitFunctionsDecl(region_));
		privateParts.append(GenerationTemplates.formatSubSmFunctions(createGuardFunctions(region_).toString()));
		privateParts.append(createTransitionFunctionDecl(region_));
		String protectedParts = "";
		String publicParts = GenerationTemplates.stateEnum(getStateList(region_), getInitState(region_));

		if (_submachineMap.isEmpty()) {
			source = GenerationTemplates.simpleSubStateMachineClassHeader(dependency, className_, parentclass_,
					publicParts, protectedParts, privateParts.toString());
		} else {
			source = GenerationTemplates.hierarchicalSubStateMachineClassHeader(dependency, className_, parentclass_,
					getSubmachines(), publicParts, protectedParts, privateParts.toString());
		}
		return source;
	}

	private StringBuilder createClassCppSource(Class class_) {
		StringBuilder source = new StringBuilder("");
		List<StateMachine> smList = new ArrayList<StateMachine>();
		Shared.getTypedElements(smList, class_.allOwnedElements(), UMLPackage.Literals.STATE_MACHINE);

		if (ownStates(class_, smList)) {
			Region region = smList.get(0).getRegions().get(0);
			Multimap<Pair<String, String>, Pair<String, String>> smMap = createMachine(region);
			if (_submachineMap.isEmpty()) {
				source.append(GenerationTemplates.simpleStateMachineClassConstructor(class_.getName(),
						getBaseClass(class_), smMap, getInitialState(region), true, poolId));
				
			} else {
				source.append(GenerationTemplates.hierarchicalStateMachineClassConstructor(class_.getName(),
						getBaseClass(class_), smMap, getEventSubmachineNameMap(), getInitialState(region), true));
				
			}
			source.append(GenerationTemplates.destructorDef(class_.getName(), true));
			source.append(createEntryFunctionsDef(class_.getName(), region));
			source.append(createExitFunctionsDef(class_.getName(), region));
			source.append(createTransitionFunctionsDef(class_.getName(), region, true));

			source.append(GenerationTemplates.entry(class_.getName(), createStateActionMap(_entryMap, region)) + "\n");
			source.append(GenerationTemplates.exit(class_.getName(), createStateActionMap(_exitMap, region)) + "\n");
		} else {
			source.append(GenerationTemplates.constructorDef(class_.getName(), getBaseClass(class_)));
			source.append(GenerationTemplates.destructorDef(class_.getName(), false));
		}

		for (Operation item : class_.getOwnedOperations()) {
			if (!isConstructor(class_, item)) {

				String returnType = getReturnType(item.getReturnResult());

				/*
				 * Behavior behavior=item.getMethods().get(0); String
				 * funcBody="";
				 * if(behavior.eClass().equals(UMLPackage.Literals.ACTIVITY)) {
				 * funcBody=ActivityExport.createfunctionBody((Activity)behavior
				 * ,true); } else { //TODO exception, unknown for me, need the
				 * model }
				 */

				/*
				 * source+=GenerationTemplates.FunctionDef(class_.getName(),
				 * returnType, item.getName(),getOperationParams(item),
				 * funcBody);
				 */

				source.append(GenerationTemplates.functionDef(class_.getName(), returnType, item.getName(),
						getOperationParams(item), GenerationTemplates.getDefaultReturn(returnType)));
			} else {
				// TODO generate constructors
			}
		}
		return source;
	}

	private StringBuilder createSubSmClassCppSource(String className_, String parentClass_, Region region_) {
		StringBuilder source = new StringBuilder("");
		Multimap<Pair<String, String>, Pair<String, String>> smMap = createMachine(region_);
		if (_submachineMap.isEmpty()) {
			source.append(GenerationTemplates.simpleSubStateMachineClassConstructor(className_, parentClass_, smMap,
					getInitialState(region_)));
		} else {
			source.append(GenerationTemplates.hierarchicalSubStateMachineClassConstructor(className_, parentClass_,
					smMap, getEventSubmachineNameMap(), getInitialState(region_)));
		}
		source.append(GenerationTemplates.destructorDef(className_,false));
		StringBuilder subSmSpec = createEntryFunctionsDef(className_, region_);
		subSmSpec.append(createExitFunctionsDef(className_, region_));
		subSmSpec.append(createTransitionFunctionsDef(className_, region_, false));
		subSmSpec.append(GenerationTemplates.entry(className_, createStateActionMap(_entryMap, region_)) + "\n");
		subSmSpec.append(GenerationTemplates.exit(className_, createStateActionMap(_exitMap, region_)) + "\n");

		source.append(GenerationTemplates.formatSubSmFunctions(subSmSpec.toString()));
		return source;
	}

	private Map<String, Pair<String, Region>> getSubMachines(Region region_) {
		Map<String, Pair<String, Region>> submachineMap = new HashMap<String, Pair<String, Region>>();
		for (State state : getStateList(region_)) {
			// either got a submachine or a region, both is not permitted
			StateMachine m = state.getSubmachine();
			if (m != null) {
				submachineMap.put(state.getName(), new Pair<String, Region>(m.getName(), m.getRegions().get(0)));
			} else {
				List<Region> r = state.getRegions();
				if (!r.isEmpty()) {
					submachineMap.put(state.getName(), new Pair<String, Region>(state.getName() + "_subSM", r.get(0)));
				}
			}
		}
		return submachineMap;
	}

	private void createFuncTypeMap(Region region, FuncTypeEnum funcType_, Boolean rt_) {
		Map<String, Pair<String, String>> map = new HashMap<String, Pair<String, String>>();
		String source = "";
		String name = "";
		for (State item : getStateList(region)) {
			Behavior behavior = null;
			String unknownName = null;
			switch (funcType_) {
			case Entry: {
				behavior = item.getEntry();
				unknownName = _unknownEntryName;
				break;
			}
			case Exit: {
				behavior = item.getExit();
				unknownName = _unknownExitName;
				break;
			}
			}

			if (behavior != null) {
				if (behavior.eClass().equals(UMLPackage.Literals.ACTIVITY)) {
					// source=ActivityExport.createfunctionBody((Activity)behavior,rt_);
					source = "";					
					name = item.getName() + "_" + unknownName;
					map.put(name, new Pair<String, String>(item.getName(), source));
				}
			}
		}

		if (funcType_ == FuncTypeEnum.Entry) {
			_entryMap = map;
		} else if (funcType_ == FuncTypeEnum.Exit) {
			_exitMap = map;
		}

	}

	private StringBuilder createEntryFunctionsDecl(Region region_) {
		StringBuilder source = new StringBuilder("");
		for (Map.Entry<String, Pair<String, String>> entry : _entryMap.entrySet()) {
			source.append(GenerationTemplates.functionDecl(entry.getKey()));
		}
		return source;
	}

	private StringBuilder createExitFunctionsDecl(Region region_) {
		StringBuilder source = new StringBuilder("");
		for (Map.Entry<String, Pair<String, String>> entry : _exitMap.entrySet()) {
			source.append(GenerationTemplates.functionDecl(entry.getKey()));
		}
		return source;
	}

	private StringBuilder createGuardFunctions(Region region_) {
		StringBuilder source = new StringBuilder("");
		Integer unknownGuardCount = 0;
		for (Transition item : region_.getTransitions()) {
			Constraint constraint = item.getGuard();
			if (constraint != null) {
				String guardName = _unknownGuardName + unknownGuardCount.toString();
				unknownGuardCount++;
				String guard = Shared.getGuard(constraint);
				if (guard.equals("else")) {
					guard = Shared.calculateSmElseGuard(item);

				}

				_guardMap.put(guard, guardName);
				source.append(GenerationTemplates.guardFunction(guardName, guard, parameterisedEventTrigger(item)));
			}
		}
		source.append("\n");
		return source;
	}

	// TODO string dependency as special case ....
	private StringBuilder getAllDependency(Class class_, Boolean isHeader) {
		StringBuilder source = new StringBuilder("");
		List<String> types = new ArrayList<String>();

		// collecting each item type for dependency analysis
		for (Operation item : class_.getAllOperations()) {
			if (item.getReturnResult() != null) {
				types.add(item.getReturnResult().getType().getName());
			}
			types.addAll(getOperationParamTypes(item));
		}

		Boolean multip = false;
		for (Property item : Shared.getProperties(class_)) {
			if (item.getType() != null) {

				Type attr = item.getType();
				types.add(attr.getName());
				if ((item.getUpper() > 1 || item.getUpper() == _UMLMany) && !multip) {
					multip = true;
					source.append(GenerationTemplates.manyMultiplicityDependecy());
				}
			}
			// TODO else throw except, if we want to terminate the compile

		}

		if (_submachineMap != null) {
			for (Map.Entry<String, Pair<String, Region>> entry : _submachineMap.entrySet()) {
				types.add(entry.getValue().getFirst());
			}
		}

		// dependency analysis
		String header = "";
		for (String t : types) {
			if (!Shared.isBasicType(t) && t != class_.getName()) {
				if (isHeader) {
					header = GenerationTemplates.forwardDeclaration(t);
				} else {
					if (!t.equals("String")) {
						header = GenerationTemplates.cppInclude(t);
					}
				}

				// TODO this is suboptimal
				if (!source.toString().contains(header)) {
					source.append(header);
				}
			}
		}

		if (getBaseClass(class_) != null) {
			source.append(GenerationTemplates.cppInclude(getBaseClass(class_)));
		}

		if (!isHeader) {
			source.append(GenerationTemplates.cppInclude(GenerationTemplates.DeploymentHeader));
			source.append(GenerationTemplates.debugOnlyCodeBlock(GenerationTemplates.StandardIOinclude));
		}

		source.append("\n");
		return source;
	}

	private StringBuilder createTransitionFunctionDecl(Region region_) {
		StringBuilder source = new StringBuilder("");
		for (Transition item : region_.getTransitions()) {
			source.append(GenerationTemplates.transitionActionDecl(item.getName()));
		}
		source.append("\n");
		return source;
	}

	private StringBuilder createTransitionFunctionsDef(String className_, Region region_, Boolean rt_) {
		StringBuilder source = new StringBuilder("");
		for (Transition item : region_.getTransitions()) {
			String body = "";
			/*
			 * String eventName=parameterisedEventTrigger(item);
			 * 
			 * Behavior b=item.getEffect(); if(b != null &&
			 * b.eClass().equals(UMLPackage.Literals.ACTIVITY)) { Activity
			 * a=(Activity)b; body+=ActivityExport.createfunctionBody(a,rt_); }
			 * if(!eventName.isEmpty() && !body.isEmpty()) {
			 * body=GenerationTemplates.GetRealEvent(eventName)+
			 * GenerationTemplates.EventParamUsage(eventName,body); }
			 */

			source.append(GenerationTemplates.transitionActionDef(className_, item.getName(),
					body + createSetState(item) + "\n"));
		}
		source.append("\n");
		return source;
	}

	/*
	 * handle the choice in the statemachine looks: state -transition-
	 * choiceNode < (guard1/tran1) (guard2/tran2)
	 */
	private String createSetState(Transition transition_) {
		String source = "";
		Vertex targetState = transition_.getTarget();
		// choice handling
		if (targetState.eClass().equals(UMLPackage.Literals.PSEUDOSTATE)) {
			List<Pair<String, String>> branches = new LinkedList<Pair<String, String>>();
			Pair<String, String> elseBranch = null;
			for (Transition trans : targetState.getOutgoings()) {
				String guard = Shared.getGuard(trans.getGuard());
				String body = ActivityTemplates.transitionActionCall(trans.getName());

				if (guard.isEmpty() || guard.equals("else")) {
					elseBranch = new Pair<String, String>(guard, body);
				} else {
					branches.add(new Pair<String, String>(guard, body));
				}
			}
			if (elseBranch != null) {
				branches.add(elseBranch);
			}
			source = ActivityTemplates.elseIf(branches).toString();
		} else if (targetState.eClass().equals(UMLPackage.Literals.STATE)) {
			source = GenerationTemplates.setState(targetState.getName());
		} else {
			source = GenerationTemplates.setState("UNKNOWN_TRANSITION_TARGET");
		}
		return source;
	}

	private String parameterisedEventTrigger(Transition transition_) {
		for (Trigger tri : transition_.getTriggers()) {
			Event e = tri.getEvent();
			if (e != null && e.eClass().equals(UMLPackage.Literals.SIGNAL_EVENT)) {
				SignalEvent se = (SignalEvent) e;
				Signal sig = se.getSignal();
				if (se.getSignal().getAllAttributes() != null && !se.getSignal().getAllAttributes().isEmpty()) {
					return sig.getName();
				}
			}
		}
		return "";
	}

	private StringBuilder createParts(Class class_, String modifyer_) {
		StringBuilder source = new StringBuilder("");
		for (Operation item : class_.getOwnedOperations()) {
			if (item.getVisibility().toString().equals(modifyer_)) {
				if (isConstructor(class_, item)) {
					// TODO export constructor
				} else {
					String returnType = getReturnType(item.getReturnResult());
					source.append(
							GenerationTemplates.functionDecl(returnType, item.getName(), getOperationParamTypes(item)));
				}
			}
		}

		for (Property item : Shared.getProperties(class_)) {
			if (item.getVisibility().toString().equals(modifyer_)) {
				String type = "!!!UNKNOWNTYPE!!!";
				if (item.getType() != null) {
					type = item.getType().getName();
				}

				int multip = item.getUpper();
				if (item.getUpper() == _UMLMany) {
					multip = 2;
				}

				String tmp = GenerationTemplates.variableDecl(type, item.getName(), multip);
				// TODO suboptimal code
				if (!source.toString().contains(item.getName())) {
					source.append(tmp);
				}
			}
			// TODO else exception if we want to stop the compile
		}

		return source;
	}

	private boolean isConstructor(Class cls, Operation operation) {

		if (cls.getName().equals(operation.getName())) {
			return true;
		} else {
			return false;
		}
	}

	private List<String> getOperationParamTypes(Operation op_) {
		List<String> ret = new ArrayList<String>();
		for (Parameter param : op_.getOwnedParameters()) {
			if (param != op_.getReturnResult()) {
				if (param.getType() != null) {
					ret.add(param.getType().getName());
				}
			}
		}
		return ret;
	}

	private List<Pair<String, String>> getOperationParams(Operation op_) {
		List<Pair<String, String>> ret = new ArrayList<Pair<String, String>>();
		for (Parameter param : op_.getOwnedParameters()) {
			if (param != op_.getReturnResult()) {
				if (param.getType() != null) {
					ret.add(new Pair<String, String>(param.getType().getName(), param.getName()));
				} else {
					// TODO exception if we want to stop the compile (missing
					// operation, seems fatal error)
					ret.add(new Pair<String, String>("UNKNOWN_TYPE", param.getName()));
				}
			}
		}
		return ret;
	}

	private Map<String, String> getEventSubmachineNameMap() {
		Map<String, String> ret = new HashMap<String, String>();
		for (Map.Entry<String, Pair<String, Region>> entry : _submachineMap.entrySet()) {
			ret.put(entry.getKey(), entry.getValue().getFirst());
		}
		return ret;
	}

	private Map<String, String> createStateActionMap(Map<String, Pair<String, String>> map_, Region region_) {
		Map<String, String> ret = new HashMap<String, String>();
		for (Map.Entry<String, Pair<String, String>> entry : map_.entrySet()) {
			ret.put(entry.getValue().getFirst(), entry.getKey());
		}
		return ret;
	}

	private StringBuilder createEntryFunctionsDef(String className_, Region region_) {
		StringBuilder source = new StringBuilder("");
		for (Map.Entry<String, Pair<String, String>> entry : _entryMap.entrySet()) {
			source.append(GenerationTemplates.functionDef(className_, entry.getKey(), entry.getValue().getSecond()));
		}
		return source;
	}

	private StringBuilder createExitFunctionsDef(String className_, Region region_) {
		StringBuilder source = new StringBuilder("");
		for (Map.Entry<String, Pair<String, String>> entry : _exitMap.entrySet()) {
			source.append(GenerationTemplates.functionDef(className_, entry.getKey(), entry.getValue().getSecond()));
		}
		return source;
	}

	private String getReturnType(Parameter returnResult_) {
		String returnType = null;

		if (returnResult_ != null) {
			returnType = returnResult_.getType().getName();
		}
		return returnType;
	}

	private String getInitialState(Region region_) {
		String source = "NO_INITIAL_STATE";
		for (Vertex item : region_.getSubvertices()) {
			if (item.eClass().equals(UMLPackage.Literals.PSEUDOSTATE)) {
				source = item.getName();
			}
		}
		/*
		 * for(Transition item:region_.getTransitions()) {
		 * if(item.getSource().eClass().equals(UMLPackage.Literals.PSEUDOSTATE))
		 * { source=item.getTarget().getName();//TODO only works if the end is a
		 * state (not choice,etc..)!!!! break; } }
		 */
		return source;
	}

	private String getInitState(Region region) {
		String ret = "";
		for (Vertex item : region.getSubvertices()) {
			if (item.eClass().equals(UMLPackage.Literals.PSEUDOSTATE)) {
				ret = item.getName();
			}
		}
		return ret;
	}

	/*
	 * Map<Pair<String, String>,<String,String> <event,
	 * state>,<guard,handlerName>
	 */
	private Multimap<Pair<String, String>, Pair<String, String>> createMachine(Region region_) {
		Multimap<Pair<String, String>, Pair<String, String>> smMap = HashMultimap.create();
		for (Transition item : region_.getTransitions()) {
			Pair<String, String> eventSignalPair = null;

			if (item.getTriggers().isEmpty()) {
				eventSignalPair = new Pair<String, String>(GenerationTemplates.InitSignal, item.getSource().getName());
			}

			for (Trigger tri : item.getTriggers()) {
				Event e = tri.getEvent();
				if (e != null && e.eClass().equals(UMLPackage.Literals.SIGNAL_EVENT)) {
					SignalEvent se = (SignalEvent) e;
					if (se != null) {
						eventSignalPair = new Pair<String, String>(se.getSignal().getName(),
								item.getSource().getName());
					}
				}
			}
			if (eventSignalPair != null) {
				Pair<String, String> guardTransitionPair = null;
				if (item.getGuard() != null) {
					guardTransitionPair = new Pair<String, String>(_guardMap.get(Shared.getGuard(item.getGuard())),
							item.getName());

				} else {
					guardTransitionPair = new Pair<String, String>(null, item.getName());
				}
				smMap.put(eventSignalPair, guardTransitionPair);
			}
		}
		return smMap;
	}

	private List<State> getStateList(Region region_) {
		List<State> stateList = new ArrayList<State>();
		for (Vertex item : region_.getSubvertices()) {
			if (item.eClass().equals(UMLPackage.Literals.STATE)) {
				stateList.add((State) item);
			}
		}
		return stateList;
	}

	private boolean ownStates(Class class_, List<StateMachine> smList) {

		if (smList.isEmpty() || getStateList(smList.get(0).getRegions().get(0)).size() == 0) {
			return false;
		} else {
			return true;
		}

	}

	private Set<SignalEvent> getEventList(Region region_) {
		Set<SignalEvent> eventList = new HashSet<SignalEvent>();
		for (Transition item : region_.getTransitions()) {
			for (Trigger tri : item.getTriggers()) {
				Event e = tri.getEvent();
				if (e != null && e.eClass().equals(UMLPackage.Literals.SIGNAL_EVENT)) {
					SignalEvent se = (SignalEvent) e;
					if (se != null && !eventList.contains(se)) {
						eventList.add(se);
					}
				}
			}
		}

		// get the submachine events
		Map<String, Pair<String, Region>> submachineMap = getSubMachines(region_);
		for (Map.Entry<String, Pair<String, Region>> entry : submachineMap.entrySet()) {
			eventList.addAll(getEventList(entry.getValue().getSecond()));
		}

		return eventList;
	}

	private String getBaseClass(Class class_) {
		class_.getGenerals();

		if (!class_.getGeneralizations().isEmpty()) {
			return class_.getGeneralizations().get(0).getGeneral().getName();
		} else {
			return null;
		}
	}

}
