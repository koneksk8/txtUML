package txtuml.importer;


import txtuml.api.BlockBody;
import txtuml.api.Condition;
import txtuml.api.ModelClass;
import txtuml.api.ModelInt;
import txtuml.api.ParameterizedBlockBody;
import txtuml.api.Signal;
import txtuml.api.Action;
import txtuml.importer.ActionImporter.LinkTypes;

public privileged aspect ActionImporterAspect extends AbstractImporterAspect {

	void around():call(void Action.For(ModelInt, ModelInt, ParameterizedBlockBody<ModelInt>) ) && isActive()
	{
		ModelInt from=(ModelInt)(thisJoinPoint.getArgs()[0]);
		ModelInt to=(ModelInt)(thisJoinPoint.getArgs()[1]);
		@SuppressWarnings("unchecked")
		ParameterizedBlockBody<ModelInt> body=(ParameterizedBlockBody<ModelInt>)(thisJoinPoint.getArgs()[2]);
		ActionImporter.importForStatement(from, to,body);
	}
	
	void around():call(void Action.While(Condition,BlockBody)) && isActive()
	{
		Condition cond=(Condition)(thisJoinPoint.getArgs()[0]);
		BlockBody body=(BlockBody)(thisJoinPoint.getArgs()[1]);
		ActionImporter.importWhileStatement(cond,body);
	}
	
	void around():call(void Action.If(Condition,BlockBody)) && isActive()
	{
		Condition cond=(Condition)(thisJoinPoint.getArgs()[0]);
		BlockBody thenBody=(BlockBody)(thisJoinPoint.getArgs()[1]);
		ActionImporter.importIfStatement(cond,thenBody);
	}
	
	void around():call(void Action.If(Condition,BlockBody , BlockBody)) && isActive()
	{
		Condition cond=(Condition)(thisJoinPoint.getArgs()[0]);
		BlockBody thenBody=(BlockBody)(thisJoinPoint.getArgs()[1]);
		BlockBody elseBody=(BlockBody)(thisJoinPoint.getArgs()[2]);
		ActionImporter.importIfStatement(cond,thenBody,elseBody);
	}
	
	void around(): call(void Action.link(..)) && isActive()
	{
		Class<?> leftEnd=(Class<?>)(thisJoinPoint.getArgs()[0]);
		ModelClass leftObj=(ModelClass)(thisJoinPoint.getArgs()[1]);
		Class<?> rightEnd=(Class<?>)(thisJoinPoint.getArgs()[2]);
		ModelClass rightObj=(ModelClass)(thisJoinPoint.getArgs()[3]);
		ActionImporter.importLinkAction(leftEnd,leftObj,rightEnd,rightObj,LinkTypes.CREATE_LINK_LITERAL);	
	}
    		
	/*void around(): call(void Action.unlink(..)) && isActive()
	{
		Class<?> leftEnd=(Class<?>)(thisJoinPoint.getArgs()[0]);
		ModelClass leftObj=(ModelClass)(thisJoinPoint.getArgs()[1]);
		Class<?> rightEnd=(Class<?>)(thisJoinPoint.getArgs()[2]);
		ModelClass rightObj=(ModelClass)(thisJoinPoint.getArgs()[3]);
		ActionImporter.importLinkAction(leftEnd,leftObj,rightEnd,rightObj,LinkTypes.DESTROY_LINK_LITERAL);	
	}*/
    	

	void around(): call(void Action.send(ModelClass, Signal)) && isActive()
	{
	
		ModelClass receiverObj=(ModelClass)(thisJoinPoint.getArgs()[0]);
		Signal event=(Signal)(thisJoinPoint.getArgs()[1]);
		
		ActionImporter.importSignalSend(receiverObj, event);	
			
	}
	
	void around(): 
		(
			call(void Action.log(String)) || 
			call(void Action.logError(String)) ||
			call(void Action.executorLog(String)) ||
			call(void Action.executorFormattedLog(String, Object...)) ||
			call(void Action.executorErrorLog(String))
		)
		&& isActive()
	{
		//do nothing
	}
}