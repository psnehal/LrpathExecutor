To setup and build this server you need to do the following:

1. Copy ws-ncibi-executor.properties.template to ws-ncibi-executor.properties
2. Fill in status.dir and default.db.username and default.db.password in 
   ws-ncibi-executor.properties
3. Fill in task.db.username and task.db.password.
4. If on windows set tmp.dir to an existing directory.
5. Set queue.name to the queue to pull from.
6. Make sure queue.name, status.dir, and task.db.url are configured exactly 
   as they are in ws-ncibi.
7. run bap.sh at the top level of this project. It will build and package
   up the files needed to setup and install the executor.

If bap.sh ran successfully there will be an executor.tar.gz one directory
up. Transfer this file over, unpackage it and run:
sudo sh install.sh

This will setup and install the executor.

If you wish to customize where install.sh installs to you will need to set
the NCIBI_HOME environment variable.

To start/stop/check on the server run /etc/rc4.d/S80ncibiexecutor. This
script takes the following parameters:

Note: NCIBI_EXECUTOR_INSTANCES defaults to 5. To override set this
      variable in your environment.

start: starts $NCIBI_EXECUTOR_INSTANCES of the executor. If there
       are already instances running it starts up additional ones.
stop: stops all instances of the executor.
restart: stops all instances, and then starts $NCIBI_EXECUTOR_INSTANCES
         of the executor.
status: Shows all running instances of the executor.
config: Displays the configuration settings for the executor including
        NCIBI_HOME and NCIBI_EXECUTOR_INSTANCES.
