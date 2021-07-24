
start "Node" %JAVA_8_HOME%\bin\java -Xmx256m ^
	-Dlog4j.configurationFile=log4j2.xml ^
	-Dlog4j.shutdownHookEnabled=false ^
	-Dproperties=service.properties ^
	-Dreactor.netty.ioWorkerCount=4 ^
	-Dreactor.schedulers.defaultPoolSize=4 ^
	-jar service.jar
