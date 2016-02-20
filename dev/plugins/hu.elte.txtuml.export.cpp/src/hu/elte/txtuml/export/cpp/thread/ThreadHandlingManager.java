package hu.elte.txtuml.export.cpp.thread;

import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.templates.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.thread.ThreadPoolConfiguration.LinearFunction;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ThreadHandlingManager {

	private Map<String, ThreadPoolConfiguration> threadDescription;
	private Set<ThreadPoolConfiguration> pools;

	private static final String ThreadConfigurationClassName = "ThreadConfiguration";
	private static final String InsertConfigurationOperationName = "insertConfiguration";
	private static final String ConfigurationStructName = "Configuration";
	private static final String ConfigurationObjectVariableName = "conf";
	private static final String ConfigurationFile = "deployment";
	private static final String ThreadPoolClassName = "StateMachineThreadPool";
	private static final String FunctionName = "LinearFunction";
	private static final String NamespaceName = "deployment";
	// private static final String ThreadedRuntimeName =
	// "ConfiguratedThreadedRT";
	private static final String SingleRuntimeName = "SingleThreadRT";
	private static final String SetConfigurationMethod = "configure";
	private static final String CreatorFunction = "createThrededRuntime";
	private static final String CreateRTMethod = "createRuntime";

	int numberOfThreads;

	public ThreadHandlingManager(Map<String, ThreadPoolConfiguration> description) {

		this.threadDescription = description;
		numberOfThreads = threadDescription.size();

		Collection<ThreadPoolConfiguration> poolsCollection = threadDescription.values();
		pools = new LinkedHashSet<ThreadPoolConfiguration>();
		pools.addAll(poolsCollection);
	}

	public Map<String, ThreadPoolConfiguration> getDescription() {
		return threadDescription;
	}

	public void createConfigurationSource(String dest) throws FileNotFoundException, UnsupportedEncodingException {

		StringBuilder source = new StringBuilder("");
		source.append(GenerationTemplates.cppInclude(ThreadConfigurationClassName.toLowerCase()));
		source.append(GenerationTemplates.cppInclude(GenerationTemplates.RuntimeHeader));
		source.append("\n\n");

		List<String> templateParams = new ArrayList<String>();
		templateParams.add(SingleRuntimeName);
		source.append(GenerationTemplates.usingTemplateType(GenerationTemplates.MyRuntimeName,
				GenerationTemplates.RuntimeName, templateParams));
		source.append("\n\n");

		source.append(GenerationTemplates
				.putNamespace(GenerationTemplates.simpleFunctionDecl(GenerationTemplates.MyRuntimeName, CreatorFunction) + ";",
						NamespaceName));

		Shared.writeOutSource(dest, GenerationTemplates.headerName(ConfigurationFile),
				GenerationTemplates.headerGuard(source.toString(), ConfigurationFile));
		
		source = createDeplyomentFunctionDefinition();
		Shared.writeOutSource(dest, GenerationTemplates.sourceName(ConfigurationFile), source.toString());

	}

	private StringBuilder createDeplyomentFunctionDefinition() {
		StringBuilder source  = new StringBuilder("");
		source.append(GenerationTemplates.cppInclude(ConfigurationFile));
		source.append(GenerationTemplates
				.putNamespace(GenerationTemplates.simpleFunctionDef(GenerationTemplates.MyRuntimeName, CreatorFunction,
						(createConfiguration().append(createThreadedRuntime()).toString()),
						GenerationTemplates.RuntimeParamaterName), NamespaceName));
		
		return source;
	}

	private StringBuilder createThreadedRuntime() {
		StringBuilder source = new StringBuilder("");
		source.append(GenerationTemplates.staticCreate(GenerationTemplates.MyRuntimeName,
				GenerationTemplates.RuntimeParamaterName, CreateRTMethod));
		List<String> params = new ArrayList<String>();
		params.add(ConfigurationObjectVariableName);
		source.append(ActivityTemplates.operationCallOnPointerVariable(GenerationTemplates.RuntimeParamaterName,
				SetConfigurationMethod, params));
		return source;
	}

	private StringBuilder createConfiguration() {
		StringBuilder source = new StringBuilder("");
		List<String> parameters = new ArrayList<String>();
		parameters.add(new Integer(pools.size()).toString());
		source.append(GenerationTemplates.createObject(ThreadConfigurationClassName, ConfigurationObjectVariableName,
				parameters));

		for (ThreadPoolConfiguration pool : pools) {
			parameters.clear();
			parameters.add(allocatePoolObject(pool));
			parameters.add(allocateFunctionObject(pool.getFunction()));
			parameters.add(new Integer(pool.getMaxThread()).toString());

			source.append(insertToConfiguration(pool.getId(),
					GenerationTemplates.allocateObject(ConfigurationStructName, parameters)));
		}

		return source;
	}

	private String insertToConfiguration(Integer id, String configuration) {
		List<String> params = new ArrayList<String>();
		params.add(id.toString());
		params.add(configuration);

		return ActivityTemplates.operationCallOnPointerVariable(ConfigurationObjectVariableName,
				InsertConfigurationOperationName, params);
	}

	private String allocateFunctionObject(LinearFunction function) {
		List<String> params = new ArrayList<String>();
		params.add(new Integer(function.getConstant()).toString());
		params.add(new Double(function.getGradient()).toString());

		return GenerationTemplates.allocateObject(FunctionName, params);
	}

	private String allocatePoolObject(ThreadPoolConfiguration pool) {
		List<String> params = new ArrayList<String>();
		params.add(new Integer(pool.getFunction().getConstant()).toString());
		return GenerationTemplates.allocateObject(ThreadPoolClassName, params);
	}

}
